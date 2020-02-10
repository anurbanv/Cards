package com.example.cards.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.cards.R;
import com.example.cards.domain.Save;
import com.example.cards.service.Preferences;
import com.example.cards.viewmodel.BattleFieldViewModel;
import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.viewmodel.DeckViewModel;
import com.example.cards.viewmodel.PlayersViewModel;
import com.example.cards.viewmodel.RoomViewModel;
import com.example.cards.views.GameView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity {

    @BindView(R.id.gameView) GameView gameView;

    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.btnRestore) Button btnRestore;

    private RoomViewModel roomViewModel;
    private boolean multiPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        int playerCount = getIntent().getIntExtra("playerCount", 0);
        multiPlayer = getIntent().getBooleanExtra("multiPlayer", false);

        if (playerCount == 0) {
            finish();
        }

        ViewModelProvider provider = ViewModelProviders.of(this);
        DeckViewModel deckViewModel = provider.get(DeckViewModel.class);
        PlayersViewModel playersViewModel = provider.get(PlayersViewModel.class);
        playersViewModel.setDeckViewModel(deckViewModel);
        CurrentDragViewModel currentDragViewModel = provider.get(CurrentDragViewModel.class);
        BattleFieldViewModel battleFieldViewModel = provider.get(BattleFieldViewModel.class);
        roomViewModel = ViewModelProviders.of(this).get(RoomViewModel.class);
        roomViewModel.setViewModels(deckViewModel, playersViewModel, battleFieldViewModel);

        gameView.setViewModels(deckViewModel, battleFieldViewModel, playersViewModel,
                currentDragViewModel, roomViewModel);

        deckViewModel.getDeck().observe(this, deckOfCards -> gameView.updateDeck(deckOfCards));
        deckViewModel.getOutCards().observe(this, cards -> gameView.updateDeck(cards));
        playersViewModel.getPlayers().observe(this, players -> gameView.updatePlayers(players));
        battleFieldViewModel.getCells().observe(this, cells -> gameView.updateCells(cells));

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

        btnSave.setOnClickListener(v -> {
            Save save = new Save(deckViewModel.getDeck().getValue(),
                    deckViewModel.getOutCards().getValue(),
                    playersViewModel.getPlayers().getValue(),
                    battleFieldViewModel.getAttackCards(),
                    battleFieldViewModel.getDefendCards());
            save.saveToFileSystem();
        });

        btnRestore.setOnClickListener(v -> {
            Save storedSave = Save.getStoredSave();
            storedSave.restoreState(deckViewModel, playersViewModel, battleFieldViewModel);
        });

        Preferences prefs = new Preferences(this);

        String roomId = prefs.getRoomId();

        if (multiPlayer && roomId != null) {
            roomViewModel.initCloudObserver(roomId);

            roomViewModel.getRoom().observe(this, room -> {
                if (!room.isStarted()) {
                    finish();
                } else {
                    String gameState = room.getGameState();
                    if (!TextUtils.isEmpty(gameState)) {
                        Save save = Save.getSaveFromJson(gameState);
                        save.restoreState(deckViewModel, playersViewModel, battleFieldViewModel);
                    }
                }
            });
        }
        gameView.startGame(playerCount, multiPlayer);
    }

    @Override
    public void onBackPressed() {
        if (multiPlayer) {
            roomViewModel.setGameStarted(false);
        } else {
            super.onBackPressed();
        }
    }
}
