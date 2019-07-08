package com.example.cards;

import android.os.Bundle;
import android.widget.Button;

import com.example.cards.domain.Save;
import com.example.cards.views.GameView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.cards.MainActivity.deckViewModel;
import static com.example.cards.MainActivity.gameFieldViewModel;
import static com.example.cards.MainActivity.playersViewModel;

public class GameActivity extends AppCompatActivity {

    @BindView(R.id.gameView) GameView gameView;
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.btnRestore) Button btnRestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        int count = getIntent().getIntExtra("count", 0);

        if (count == 0) {
            finish();
        }

        deckViewModel.getDeck().observe(this, deckOfCards -> gameView.updateDeck(deckOfCards));
        deckViewModel.getOutCards().observe(this, cards -> gameView.updateDeck(cards));
        gameView.setLastCard(deckViewModel.getLastCard());

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
}
