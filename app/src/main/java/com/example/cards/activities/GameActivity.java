package com.example.cards.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.cards.App;
import com.example.cards.R;
import com.example.cards.domain.Save;
import com.example.cards.service.Preferences;
import com.example.cards.viewmodel.BattleFieldViewModel;
import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.viewmodel.DeckViewModel;
import com.example.cards.viewmodel.PlayersViewModel;
import com.example.cards.viewmodel.RoomViewModel;
import com.example.cards.views.GameView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity {

    @BindView(R.id.gameView) GameView gameView;
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.btnRestore) Button btnRestore;
    @BindView(R.id.testButtons) View testButtons;

    private RoomViewModel roomViewModel;
    @Inject Preferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        App.get().getAppComponent().inject(this);

        if (preferences.isMultiPlayerMode()) {
            testButtons.setVisibility(View.GONE);
        }

        int playerCount = getIntent().getIntExtra("playerCount", 0);

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

        String playerName = preferences.getPlayerName();

        btnRestore.setOnClickListener(v -> {
            Save storedSave = Save.getStoredSave();
            storedSave.restoreState(deckViewModel, playersViewModel, battleFieldViewModel,
                    playerName, preferences);
        });

        String roomId = preferences.getRoomId();
        boolean multiPlayer = preferences.isMultiPlayerMode();

        if (multiPlayer && roomId != null) {
            roomViewModel.initCloudObserver(roomId);

            roomViewModel.getRoom().observe(this, room -> {
                if (!room.isStarted()) {
                    finish();
                } else {
                    String gameState = room.getGameState();
                    if (!TextUtils.isEmpty(gameState)) {
                        Save save = Save.getSaveFromJson(gameState);
                        save.restoreState(deckViewModel, playersViewModel, battleFieldViewModel,
                                playerName, preferences);
                    }
                }
            });
        }
        gameView.startGame(playerCount);
    }

    @Override
    public void onBackPressed() {
        if (preferences.isMultiPlayerMode()) {
            roomViewModel.setGameStarted(false);
        } else {
            super.onBackPressed();
        }
    }
}
