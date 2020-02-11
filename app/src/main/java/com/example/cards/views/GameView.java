package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Cell;
import com.example.cards.domain.DeckOfCards;
import com.example.cards.domain.Player;
import com.example.cards.service.Preferences;
import com.example.cards.viewmodel.BattleFieldViewModel;
import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.viewmodel.DeckViewModel;
import com.example.cards.viewmodel.PlayersViewModel;
import com.example.cards.viewmodel.RoomViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameView extends LinearLayout {

    private DeckViewModel deckViewModel;
    private BattleFieldViewModel battleFieldViewModel;
    private PlayersViewModel playersViewModel;
    private CurrentDragViewModel currentDragViewModel;
    private RoomViewModel roomViewModel;

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

    @BindView(R.id.battleField) BattleFieldView battleField;

    private List<HandView> playerHands;
    private Preferences preferences;

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
        preferences = new Preferences(context);
    }

    public void setViewModels(DeckViewModel deckViewModel, BattleFieldViewModel battleFieldViewModel,
                              PlayersViewModel playersViewModel,
                              CurrentDragViewModel currentDragViewModel,
                              RoomViewModel roomViewModel) {
        this.deckViewModel = deckViewModel;
        this.battleFieldViewModel = battleFieldViewModel;
        this.playersViewModel = playersViewModel;
        this.currentDragViewModel = currentDragViewModel;
        this.roomViewModel = roomViewModel;

        deckView.setViewModels(playersViewModel, deckViewModel, roomViewModel);
        btnHome.setViewModels(battleFieldViewModel, playersViewModel, roomViewModel);
        btnDone.setViewModels(battleFieldViewModel, playersViewModel, deckViewModel, roomViewModel);
        battleField.setViewModels(currentDragViewModel, battleFieldViewModel, playersViewModel, roomViewModel);
    }

    public void updateDeck(DeckOfCards deck) {
        deckView.update(deck);
    }

    public void updateDeck(List<Card> outCards) {
        deckView.update(outCards);
    }

    public void updateCells(List<Cell> cells) {
        btnHome.update(battleFieldViewModel.getDefendingCardList(),
                battleFieldViewModel.getAttackingCardList());
        btnDone.update(battleFieldViewModel.getDefendingCardList(),
                battleFieldViewModel.getAttackingCardList());
        battleField.update(cells);
    }

    public void updatePlayers(List<Player> players) {
        if (players.isEmpty()) {
            return;
        }
        for (int i = 0; i < playerHands.size(); i++) {
            HandView handView = playerHands.get(i);
            Player player = players.get(i);
            handView.update(player);
        }
    }

    public void startGame(int count) {
        initPlayerViews(count);

        for (HandView playerHand : playerHands) {
            playerHand.setViewModel(currentDragViewModel, battleFieldViewModel,
                    playersViewModel, roomViewModel);
        }

        if (preferences.isMultiPlayerMode()) {
            return;
        }

        for (int i = 0; i < count; i++) {
            Player player = new Player(i);
            playersViewModel.addPlayer(player);
        }

        List<Player> playersList = playersViewModel.getPlayersInGame();

        for (int i = 0; i < 6; i++) {
            for (Player player : playersList) {
                if (deckViewModel.hasCards()) {
                    Card card = deckViewModel.takeCard();
                    playersViewModel.addCardToPlayersHand(player, card);
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

        playersViewModel.setDefendingPlayer(playersViewModel.getNextPlayerInGame(cardOwner));
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
                player2.setVisibility(GONE);
                playerHands.add(player3);
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
