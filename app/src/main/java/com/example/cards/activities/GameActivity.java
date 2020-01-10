package com.example.cards.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cards.R;
import com.example.cards.domain.Save;
import com.example.cards.views.GameView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.cards.activities.MainActivity.deckViewModel;
import static com.example.cards.activities.MainActivity.gameFieldViewModel;
import static com.example.cards.activities.MainActivity.playersViewModel;
import static com.example.cards.activities.MainActivity.roomViewModel;

public class GameActivity extends AppCompatActivity {

    @BindView(R.id.gameView) GameView gameView;
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.btnRestore) Button btnRestore;
    @BindView(R.id.btnPost) Button btnPost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        int count = getIntent().getIntExtra("count", 0);

        if (count == 0) {
            finish();
        }

        boolean multiPlayer = getIntent().getBooleanExtra("multiPlayer", false);

        if (multiPlayer) {
//            roomViewModel.getRoom().observe(this, room -> {
//                String savedState = room.getGameState();
//                if (!TextUtils.isEmpty(savedState)) {
//                    Save.restoreFromString(room.getGameState());
//                }
//            });
        }

        btnPost.setOnClickListener(v -> {
            if (multiPlayer) {
                roomViewModel.postGameState((created, message) -> {

                });
            }
        });

        roomViewModel.getRoom().observe(this, room -> {
            if (multiPlayer && room != null) {
                if (!room.getGameState().isEmpty()) Save.restoreFromString(room.getGameState());
            }
        });

        deckViewModel.getDeck().observe(this, deckOfCards -> gameView.updateDeck(deckOfCards));
        deckViewModel.getOutCards().observe(this, cards -> gameView.updateDeck(cards));

        playersViewModel.getPlayers().observe(this, players -> gameView.updatePlayers(players));

        gameFieldViewModel.getAttackingCards().observe(this, cards ->
                gameView.updateAttackCards(cards));

        gameFieldViewModel.getDefendingCards().observe(this, cards ->
                gameView.updateDefendCards(cards));

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

        btnSave.setOnClickListener(v -> Save.saveToFileSystem());
        btnRestore.setOnClickListener(v -> Save.restoreFromFileSystem());

        gameView.startGame(count);
    }

    @Override
    protected void onDestroy() {
        MainActivity.resetModels();
        super.onDestroy();
    }
}
