package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.example.cards.domain.Card;
import com.example.cards.viewmodel.BattleFieldViewModel;
import com.example.cards.viewmodel.PlayersViewModel;

import java.util.List;

import static com.example.cards.activities.MainActivity.deckViewModel;

public class DoneButton extends AppCompatButton {

    private GameView.GameOverListener listener;
    private BattleFieldViewModel battleFieldViewModel;
    private PlayersViewModel playersViewModel;

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
            List<Card> cards = battleFieldViewModel.removeAllCardsFromField();
            for (Card card : cards) {
                deckViewModel.placeCardToOutDeck(card);
            }

            playersViewModel.playerDefended();

            boolean gameFinished = playersViewModel.isGameFinished();
            if (gameFinished && listener != null) {
                listener.onGameOver();
            }
        });
    }

    public void setViewModels(BattleFieldViewModel battleFieldViewModel, PlayersViewModel playersViewModel) {
        this.battleFieldViewModel = battleFieldViewModel;
        this.playersViewModel = playersViewModel;
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
