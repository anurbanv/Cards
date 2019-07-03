package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

import com.example.cards.MainActivity;
import com.example.cards.domain.Card;

import java.util.List;

import androidx.appcompat.widget.AppCompatButton;

public class DoneButton extends AppCompatButton {

    public DoneButton(Context context) {
        super(context);
        init(context);
    }

    public DoneButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DoneButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOnClickListener(v -> {
            List<Card> cards = MainActivity.gameFieldViewModel.removeAllCardsFromField();
            for (Card card : cards) {
                MainActivity.deckViewModel.placeCardToOutDeck(card);
            }

            boolean gameFinished = MainActivity.playersViewModel.isGameFinished();
            if (gameFinished) {
                Toast.makeText(context, "GAME FINISHED", Toast.LENGTH_LONG).show();
            } else {
                MainActivity.playersViewModel.shiftDefendingPlayer();
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
}
