package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.example.cards.App;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.service.Preferences;
import com.example.cards.viewmodel.BattleFieldViewModel;
import com.example.cards.viewmodel.DeckViewModel;
import com.example.cards.viewmodel.PlayersViewModel;
import com.example.cards.viewmodel.RoomViewModel;

import java.util.List;

import javax.inject.Inject;

public class DoneButton extends AppCompatButton {

    private GameView.GameOverListener listener;
    private BattleFieldViewModel battleFieldViewModel;
    private PlayersViewModel playersViewModel;
    private DeckViewModel deckViewModel;
    private RoomViewModel roomViewModel;
    @Inject Preferences preferences;

    public DoneButton(Context context) {
        super(context);
        init();
    }

    public DoneButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DoneButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        App.get().getAppComponent().inject(this);
        setOnClickListener(v -> {
            List<Card> cards = battleFieldViewModel.removeAllCardsFromField();
            for (Card card : cards) {
                deckViewModel.placeCardToOutDeck(card);
            }

            playersViewModel.playerDefended();

            boolean gameFinished = playersViewModel.isGameFinished();
            if (gameFinished && listener != null) {
                listener.onGameOver();
            }
            roomViewModel.postGameState();
        });
    }

    public void setViewModels(BattleFieldViewModel battleFieldViewModel,
                              PlayersViewModel playersViewModel,
                              DeckViewModel deckViewModel, RoomViewModel roomViewModel) {
        this.battleFieldViewModel = battleFieldViewModel;
        this.playersViewModel = playersViewModel;
        this.deckViewModel = deckViewModel;
        this.roomViewModel = roomViewModel;
    }

    public void update(List<Card> defending, List<Card> attacking) {
        if (defending.size() != attacking.size()) {
            setEnabled(false);
            return;
        }
        if (defending.isEmpty()) {
            setEnabled(false);
            return;
        }
        if (preferences.isMultiPlayerMode()) {
            String playerName = preferences.getPlayerName();
            Player attackingPlayer = playersViewModel.getAttackingPlayer();
            if (!playerName.equals(attackingPlayer.getName())) {
                setEnabled(false);
                return;
            }
        }
        setEnabled(true);
    }

    public void setListener(GameView.GameOverListener listener) {
        this.listener = listener;
    }
}
