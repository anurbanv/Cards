package com.example.cards.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.cards.R;
import com.example.cards.service.Preferences;
import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.viewmodel.DeckViewModel;
import com.example.cards.viewmodel.GameFieldViewModel;
import com.example.cards.viewmodel.NewRoomViewModel;
import com.example.cards.viewmodel.PlayersViewModel;
import com.example.cards.views.GameView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewGameActivity extends AppCompatActivity {

    @BindView(R.id.gameView) GameView gameView;

    private NewRoomViewModel newRoomViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_new);
        ButterKnife.bind(this);

        ViewModelProvider provider = ViewModelProviders.of(this);
        DeckViewModel deckViewModel = provider.get(DeckViewModel.class);
        GameFieldViewModel gameFieldViewModel = provider.get(GameFieldViewModel.class);
        PlayersViewModel playersViewModel = provider.get(PlayersViewModel.class);
        CurrentDragViewModel currentDragViewModel = provider.get(CurrentDragViewModel.class);

        gameView.setViewModels(deckViewModel, gameFieldViewModel, playersViewModel, currentDragViewModel);

        deckViewModel.getDeck().observe(this, deckOfCards -> gameView.updateDeck(deckOfCards));
        deckViewModel.getOutCards().observe(this, cards -> gameView.updateDeck(cards));
        playersViewModel.getPlayers().observe(this, players -> gameView.updatePlayers(players));
        gameFieldViewModel.getAttackingCards().observe(this, cards -> gameView.updateAttackCards(cards));
        gameFieldViewModel.getDefendingCards().observe(this, cards -> gameView.updateDefendCards(cards));

        gameView.setGameOverListener(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Title")
                    .setCancelable(false)
                    .setMessage("message")
                    .setPositiveButton("button", (dialog, which) -> {
                        dialog.dismiss();
                        finish();
                    });
            builder.create().show();
        });

        newRoomViewModel = ViewModelProviders.of(this).get(NewRoomViewModel.class);

        Preferences prefs = new Preferences(this);

        newRoomViewModel.initCloudObserver(prefs.getRoomId());

        newRoomViewModel.getRoom().observe(this, room -> {
            if (!room.isStarted()) {
                finish();
            }
        });

        gameView.postDelayed(() -> gameView.startGame(2), 1000);
    }

    @Override
    protected void onDestroy() {
        newRoomViewModel.setGameStarted(false);
        super.onDestroy();
    }
}
