package com.example.cards.service;

import com.andrius.logutil.LogUtil;
import com.example.cards.MainActivity;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;

import java.util.List;

import static com.example.cards.MainActivity.currentDragViewModel;

public class CardDropEventHandler {

    private int cell;
    private Card card;
    private Player cardOwner;

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
        List<Card> attackCards = MainActivity.gameFieldViewModel.getAttackingCardList();
        List<Card> defendCards = MainActivity.gameFieldViewModel.getDefendingCardList();
        boolean success = false;

        Player defendingPlayer = MainActivity.playersViewModel.getDefendingPlayer();

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

                Player nextPlayer = MainActivity.playersViewModel.getNextPlayerInGame(defendingPlayer);

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
                        MainActivity.gameFieldViewModel.setAttackingCard(card, cell);
                        MainActivity.playersViewModel.shiftDefendingPlayer();
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
            MainActivity.gameFieldViewModel.setAttackingCard(card, cell);
        } else {
            LogUtil.w("Cannot place this card " + card.toString());
        }
    }

    private void defendEvent() {
        boolean success = false;
        if (cardOwner.getAction() == Player.Action.DEFEND) {
            Card cardToDefend = MainActivity.gameFieldViewModel.getAttackCardAtIndex(cell);
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
            MainActivity.gameFieldViewModel.setDefendingCard(card, cell);
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
