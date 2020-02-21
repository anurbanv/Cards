package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.cards.App;
import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Cell;
import com.example.cards.domain.DeckOfCards;
import com.example.cards.domain.Player;
import com.example.cards.service.PlayersService;
import com.example.cards.service.Preferences;
import com.example.cards.viewmodel.BattleFieldViewModel;
import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.viewmodel.DeckViewModel;
import com.example.cards.viewmodel.PlayersViewModel;
import com.example.cards.viewmodel.RoomViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameView extends LinearLayout {

    @BindView(R.id.player1) HandView player1;
    @BindView(R.id.player2) HandView player2;
    @BindView(R.id.player3) HandView player3;
    @BindView(R.id.player4) HandView player4;
    @BindView(R.id.player5) HandView player5;
    @BindView(R.id.player6) HandView player6;
    @BindView(R.id.deckContainer) DeckView deckContainer;
    @BindView(R.id.btnHome) HomeButton btnHome;
    @BindView(R.id.btnDone) DoneButton btnDone;
    @BindView(R.id.battleField) BattleFieldView battleField;

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

    @Inject Preferences preferences;
    private List<HandView> playerHands;
    private PlayersService playersService;

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

        App.get().getAppComponent().inject(this);
        playersService = new PlayersService();
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

        deckContainer.setViewModels(playersViewModel, deckViewModel, roomViewModel);
        btnHome.setViewModels(battleFieldViewModel, playersViewModel, roomViewModel);
        btnDone.setViewModels(battleFieldViewModel, playersViewModel, deckViewModel, roomViewModel);
        battleField.setViewModels(currentDragViewModel, battleFieldViewModel, playersViewModel,
                roomViewModel, deckViewModel);
    }

    public void updateDeck(DeckOfCards deck) {
        deckContainer.update(deck);
    }

    public void updateDeck(List<Card> outCards) {
        deckContainer.update(outCards);
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
                    playersViewModel, roomViewModel, deckViewModel);
        }

        if (preferences.isMultiPlayerMode()) {
            return;
        }

        List<Player> playersList = playersService.getNewPlayersList(count);
        playersViewModel.addPlayers(playersList);
        playersService.fillPlayersHands(playersList, deckViewModel.getDeckOfCards());
        Player cardOwner = playersService.getPlayerWithLowestStrongCard(playersList);
        playersViewModel.setDefendingPlayer(playersViewModel.getNextPlayerInGame(cardOwner));
    }

    private void initPlayerViews(int count) {
        playerHands = new ArrayList<>();

        if (count >= 2) {
            switch (count) {
                case 2:
                    playerHands.addAll(Arrays.asList(player1, player4));
                    break;
                case 3:
                    playerHands.addAll(Arrays.asList(player1, player3, player5));
                    break;
                case 4:
                    playerHands.addAll(Arrays.asList(player1, player3, player4, player5));
                    break;
                case 5:
                    playerHands.addAll(Arrays.asList(player1, player2, player3,
                            player5, player6));
                    break;
                case 6:
                    playerHands.addAll(Arrays.asList(player1, player2, player3,
                            player4, player5, player6));
                    break;
            }
            for (HandView playerHand : playerHands) {
                playerHand.setVisibility(VISIBLE);
            }
        }
    }
}
