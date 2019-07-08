package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.DeckOfCards;
import com.example.cards.domain.Player;
import com.example.cards.domain.PlayerState;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.cards.MainActivity.deckViewModel;
import static com.example.cards.MainActivity.gameFieldViewModel;
import static com.example.cards.MainActivity.playersViewModel;

public class GameView extends LinearLayout {

    public void setGameOverListener(GameOverListener gameOverListener) {
        btnHome.setListener(gameOverListener);
        btnDone.setListener(gameOverListener);
    }

    public interface GameOverListener {
        void onGameOver();
    }

    @BindView(R.id.player1) HandView player1;
    @BindView(R.id.player2) HandView player2;
    @BindView(R.id.player3) HandView player3;
    @BindView(R.id.player4) HandView player4;
    @BindView(R.id.player5) HandView player5;
    @BindView(R.id.player6) HandView player6;

    @BindView(R.id.deckView) DeckView deckView;

    @BindView(R.id.btnHome) HomeButton btnHome;
    @BindView(R.id.btnDone) DoneButton btnDone;

    @BindView(R.id.gameField) GameFieldView gameField;

    private List<HandView> playerHands;

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.game_view, this, true);
        ButterKnife.bind(this, root);
    }

    public void updateDeck(DeckOfCards deck) {
        deckView.update(deck);
    }

    public void updateDeck(List<Card> outCards) {
        deckView.update(outCards);
    }

    public void setLastCard(Card card) {
        deckView.setLastCard(card);
    }

    public void updateAttackCards(Card[] cards) {
        btnHome.update(gameFieldViewModel.getDefendingCardList(),
                gameFieldViewModel.getAttackingCardList());
        btnDone.update(gameFieldViewModel.getDefendingCardList(),
                gameFieldViewModel.getAttackingCardList());
        gameField.updateAttackCells(cards);
    }

    public void updateDefendCards(Card[] cards) {
        btnDone.update(gameFieldViewModel.getDefendingCardList(),
                gameFieldViewModel.getAttackingCardList());
        btnHome.update(gameFieldViewModel.getDefendingCardList(),
                gameFieldViewModel.getAttackingCardList());
        gameField.updateDefendCells(cards);
    }

    public void updatePlayers(List<Player> players) {
        for (Player player : players) {
            HandView handView = playerHands.get(player.getId());
            handView.update(player);
        }
    }

    public void startGame(int count) {
        initPlayerViews(count);

        for (int i = 0; i < count; i++) {
            Player player = new Player(i);
            playersViewModel.addPlayer(player);
        }

        List<Player> playersList = playersViewModel.getPlayersInGame();

        for (int i = 0; i < 6; i++) {
            for (Player player : playersList) {
                if (deckViewModel.hasCards()) {
                    Card card = deckViewModel.takeCard();
                    player.addCardToHand(card);
                }
            }
        }

        Player firstPlayer = playersList.get(0);

        Card lowestStrongCard = firstPlayer.lookAtCard(0);
        Player cardOwner = firstPlayer;

        for (Player player : playersViewModel.getPlayersInGame()) {
            for (Card card : player.getHand()) {
                if (!lowestStrongCard.isStrong() && card.isStrong()) {
                    lowestStrongCard = card;
                    cardOwner = player;
                } else if (card.isStrong() && card.getNumber() < lowestStrongCard.getNumber()) {
                    lowestStrongCard = card;
                    cardOwner = player;
                }
            }
        }

        Player nextPlayer = playersViewModel.getNextPlayer(cardOwner);

        cardOwner.setState(PlayerState.ATTACK);
        nextPlayer.setState(PlayerState.DEFEND);
    }

    private void initPlayerViews(int count) {
        playerHands = new ArrayList<>();
        if (count >= 2) {
            if (count == 2) {
                playerHands.add(player1);
                player2.setVisibility(GONE);
                player3.setVisibility(GONE);
                playerHands.add(player4);
                player5.setVisibility(GONE);
                player6.setVisibility(GONE);
            } else if (count < 5) {
                playerHands.add(player1);
                playerHands.add(player2);
                player3.setVisibility(GONE);
                playerHands.add(player4);
                playerHands.add(player5);
                player6.setVisibility(GONE);
            } else if (count < 7) {
                playerHands.add(player1);
                playerHands.add(player2);
                playerHands.add(player3);
                playerHands.add(player4);
                playerHands.add(player5);
                playerHands.add(player6);
            }
        }
    }
}
