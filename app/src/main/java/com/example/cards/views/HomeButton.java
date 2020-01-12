package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.viewmodel.GameFieldViewModel;
import com.example.cards.viewmodel.PlayersViewModel;

import java.util.List;

public class HomeButton extends AppCompatButton {

    private GameView.GameOverListener listener;
    private GameFieldViewModel gameFieldViewModel;
    private PlayersViewModel playersViewModel;

    public HomeButton(Context context) {
        super(context);
        init();
    }

    public HomeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HomeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnClickListener(v -> {
            List<Card> cards = gameFieldViewModel.removeAllCardsFromField();
            Player player = playersViewModel.getDefendingPlayer();
            for (Card card : cards) {
                player.addCardToHand(card);
            }

            playersViewModel.playerTookHome();

            boolean gameFinished = playersViewModel.isGameFinished();
            if (gameFinished && listener != null) {
                listener.onGameOver();
            }
        });
    }

    public void setViewModels(GameFieldViewModel gameFieldViewModel, PlayersViewModel playersViewModel) {
        this.gameFieldViewModel = gameFieldViewModel;
        this.playersViewModel = playersViewModel;
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
