package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.andrius.logutil.LogUtil;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.service.Preferences;
import com.example.cards.viewmodel.BattleFieldViewModel;
import com.example.cards.viewmodel.PlayersViewModel;
import com.example.cards.viewmodel.RoomViewModel;

import java.util.List;

public class HomeButton extends AppCompatButton {

    private GameView.GameOverListener listener;
    private BattleFieldViewModel battleFieldViewModel;
    private PlayersViewModel playersViewModel;
    private RoomViewModel roomViewModel;
    private Preferences preferences;

    public HomeButton(Context context) {
        super(context);
        init(context);
    }

    public HomeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HomeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        preferences = new Preferences(context);
        setOnClickListener(v -> {
            if (preferences.isMultiPlayerMode()) {
                String playerName = preferences.getPlayerName();
                Player defendingPlayer = playersViewModel.getDefendingPlayer();
                if (!defendingPlayer.getName().equals(playerName)) {
                    LogUtil.w("Current player is not defending");
                    return;
                }
            }
            List<Card> cards = battleFieldViewModel.removeAllCardsFromField();
            Player player = playersViewModel.getDefendingPlayer();
            for (Card card : cards) {
                playersViewModel.addCardToPlayersHand(player, card);
            }

            playersViewModel.playerTookHome();

            boolean gameFinished = playersViewModel.isGameFinished();
            if (gameFinished && listener != null) {
                listener.onGameOver();
            }
            roomViewModel.postGameState();
        });
    }

    public void setViewModels(BattleFieldViewModel battleFieldViewModel, PlayersViewModel playersViewModel,
                              RoomViewModel roomViewModel) {
        this.battleFieldViewModel = battleFieldViewModel;
        this.playersViewModel = playersViewModel;
        this.roomViewModel = roomViewModel;
    }

    public void update(List<Card> defending, List<Card> attacking) {
        if (defending.size() == attacking.size()) {
            setEnabled(false);
            return;
        }
        setEnabled(true);
    }

    public void setListener(GameView.GameOverListener listener) {
        this.listener = listener;
    }
}
