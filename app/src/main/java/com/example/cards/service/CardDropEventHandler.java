package com.example.cards.service;

import com.andrius.logutil.LogUtil;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.viewmodel.GameFieldViewModel;
import com.example.cards.viewmodel.PlayersViewModel;

import java.util.List;

public class CardDropEventHandler {

    private int cell;
    private Card card;
    private Player cardOwner;
    private CurrentDragViewModel currentDragViewModel;
    private GameFieldViewModel gameFieldViewModel;
    private PlayersViewModel playersViewModel;

    public CardDropEventHandler(CurrentDragViewModel currentDragViewModel,
                                GameFieldViewModel gameFieldViewModel,
                                PlayersViewModel playersViewModel) {
        this.currentDragViewModel = currentDragViewModel;
        this.gameFieldViewModel = gameFieldViewModel;
        this.playersViewModel = playersViewModel;
    }

    public void initEvent(boolean attacking, int cell) {
        this.cell = cell;

        card = currentDragViewModel.getCurrentDrag();
        cardOwner = currentDragViewModel.getCardOwner();

        if (attacking) {
            attackEvent();
        } else {
            defendEvent();
        }
    }

    private void attackEvent() {
        List<Card> attackCards = gameFieldViewModel.getAttackingCardList();
        List<Card> defendCards = gameFieldViewModel.getDefendingCardList();
        boolean success = false;

        Player defendingPlayer = playersViewModel.getDefendingPlayer();

        if (cardOwner.getAction() == Player.Action.ATTACK) {
            int cardsToDefend = attackCards.size() - defendCards.size();
            int handSize = defendingPlayer.getHand().size();
            if (handSize - cardsToDefend > 0) {
                if (attackCards.isEmpty()) {
                    success = true;
                } else {
                    if (containsSameNumber(attackCards, card) || containsSameNumber(defendCards, card)) {
                        success = true;
                    }
                }
            } else {
                LogUtil.w("Defending player has no cards");
            }
        } else if (cardOwner.getAction() == Player.Action.DEFEND) {

            if (defendCards.isEmpty() && !attackCards.isEmpty()) {

                Player nextPlayer = playersViewModel.getNextPlayerInGame(defendingPlayer);

                if (nextPlayer.getHand().size() >= attackCards.size() + 1) {
                    boolean allSameNumber = true;
                    for (Card attackCard : attackCards) {
                        if (card.getNumber() != attackCard.getNumber()) {
                            allSameNumber = false;
                            break;
                        }
                    }
                    if (allSameNumber) {
                        cardOwner.removeCard(card);
                        gameFieldViewModel.setAttackingCard(card, cell);
                        playersViewModel.shiftDefendingPlayer();
                    } else {
                        LogUtil.w("Not all cards are number " + card.getNumber());
                    }
                } else {
                    LogUtil.w("Next player does not have enough cards");
                }
            } else {
                LogUtil.w("Cannot transfer defended cards");
            }
        } else {
            LogUtil.w("Player is not defending nor attacking");
        }

        if (success) {
            cardOwner.removeCard(card);
            gameFieldViewModel.setAttackingCard(card, cell);
        } else {
            LogUtil.w("Cannot place this card " + card.toString());
        }
    }

    private void defendEvent() {
        boolean success = false;
        if (cardOwner.getAction() == Player.Action.DEFEND) {
            Card cardToDefend = gameFieldViewModel.getAttackCardAtIndex(cell);
            if (cardToDefend != null) {
                if (cardToDefend.getSuite() == card.getSuite()) {
                    if (card.getNumber() > cardToDefend.getNumber()) {
                        success = true;
                    } else {
                        LogUtil.w("Number " + card.getNumber() + " too low");
                    }
                } else if (!cardToDefend.isStrong() && card.isStrong()) {
                    success = true;
                } else {
                    LogUtil.w("Wrong suite");
                }
            } else {
                LogUtil.w("Attack cell " + cell + " is empty");
            }
        } else {
            LogUtil.w("Player " + cardOwner.getId() + " is not defending");
        }

        if (success) {
            cardOwner.removeCard(card);
            gameFieldViewModel.setDefendingCard(card, cell);
        }
    }

    private boolean containsSameNumber(List<Card> cards, Card card) {
        for (Card card1 : cards) {
            if (card1.getNumber() == card.getNumber()) {
                return true;
            }
        }
        return false;
    }
}
