package com.example.cards;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.andrius.fileutil.FileUtil;
import com.andrius.logutil.LogUtil;
import com.example.cards.domain.Save;
import com.example.cards.views.GameView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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

    @BindView(R.id.btnSend) Button btnSend;
    @BindView(R.id.btnReceive) Button btnReceive;

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        btnSend.setOnClickListener(v -> {

            String jsonString = FileUtil.readFileText(MainActivity.latestSave);

            if (TextUtils.isEmpty(jsonString)) {
                LogUtil.d("Empty save");
                return;
            }

            Map<String, Object> game = new HashMap<>();
            game.put("state", jsonString);

            DocumentReference games = db.collection("games").document("123");
            games.set(game)
                    .addOnSuccessListener(aVoid -> {
                        LogUtil.d("success");
                    }).addOnFailureListener(e -> {
                LogUtil.d("fail");
            });
        });

        btnReceive.setOnClickListener(v -> {

            DocumentReference games = db.collection("games").document("123");

            games.get().addOnSuccessListener(documentSnapshot -> {
                LogUtil.d(documentSnapshot.get("state").toString());
                String jsonString = documentSnapshot.get("state").toString();
                Save.restoreFromJsonString(jsonString);
            }).addOnFailureListener(e -> {
                LogUtil.d("Failed");
            });
        });

        final CollectionReference docRef = db.collection("games");
        docRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            LogUtil.d("changed");
        });
    }
}
