package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;

import com.example.cards.MainActivity;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;

import java.util.List;

import androidx.appcompat.widget.AppCompatButton;

public class HomeButton extends AppCompatButton {

    private GameView.GameOverListener listener;

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
        setOnClickListener(v -> {
            List<Card> cards = MainActivity.gameFieldViewModel.removeAllCardsFromField();
            Player player = MainActivity.playersViewModel.getDefendingPlayer();
            for (Card card : cards) {
                player.addCardToHand(card);
            }

            boolean gameOver = MainActivity.playersViewModel.finishRound(true);
            if (gameOver && listener != null) {
                listener.onGameOver();
            }
        });
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
