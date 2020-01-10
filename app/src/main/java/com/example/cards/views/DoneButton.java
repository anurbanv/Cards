package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.example.cards.activities.MainActivity;
import com.example.cards.domain.Card;

import java.util.List;

public class DoneButton extends AppCompatButton {

    private GameView.GameOverListener listener;

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
        setOnClickListener(v -> {
            List<Card> cards = MainActivity.gameFieldViewModel.removeAllCardsFromField();
            for (Card card : cards) {
                MainActivity.deckViewModel.placeCardToOutDeck(card);
            }

            MainActivity.playersViewModel.playerDefended();

            boolean gameFinished = MainActivity.playersViewModel.isGameFinished();
            if (gameFinished && listener != null) {
                listener.onGameOver();
            }
        });
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
        setEnabled(true);
    }

    public void setListener(GameView.GameOverListener listener) {
        this.listener = listener;
    }
}
