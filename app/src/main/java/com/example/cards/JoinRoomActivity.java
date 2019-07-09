package com.example.cards;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andrius.logutil.LogUtil;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class JoinRoomActivity extends AppCompatActivity {

    @BindView(R.id.etRoomId) EditText etRoomId;
    @BindView(R.id.btnJoin) Button btnJoin;
    @BindView(R.id.etPlayerName) EditText etPlayerName;
    @BindView(R.id.tvPlayers) TextView tvPlayers;

    private FirebaseFirestore db;
    private String roomId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);
        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();

        btnJoin.setOnClickListener(v -> joinRoom());
    }

    private void joinRoom() {
        String roomId = etRoomId.getText().toString().trim();
        if (roomId.length() < 5) {
            Toast.makeText(this, "id min length is 5", Toast.LENGTH_SHORT).show();
            return;
        }
        String playerName = etPlayerName.getText().toString().trim();
        if (playerName.isEmpty()) {
            Toast.makeText(this, "enter player name", Toast.LENGTH_SHORT).show();
        }

        this.roomId = roomId;

        DocumentReference room = db.collection("games").document(roomId);

        room.get().addOnSuccessListener(documentSnapshot -> {
            etRoomId.setEnabled(false);
            etPlayerName.setEnabled(false);
            btnJoin.setEnabled(false);
            List<String> players = (List<String>) documentSnapshot.get("players");
            players.add(playerName);
            Map<String, Object> game = new HashMap<>();
            game.put("players", players);
            room.set(game)
                    .addOnSuccessListener(aVoid -> {
                        String playerString = "";
                        for (String player : players) {
                            playerString += player + "\n";
                        }
                        tvPlayers.setText(playerString);
                    }).addOnFailureListener(e -> LogUtil.d("Fail"));

        }).addOnFailureListener(e -> {
            LogUtil.d("fail");
        });
    }

    @Override
    protected void onDestroy() {
        if (!TextUtils.isEmpty(roomId)) {
            DocumentReference room = db.collection("games").document(roomId);

            String playerName = etPlayerName.getText().toString().trim();

            room.get().addOnSuccessListener(documentSnapshot -> {

                List<String> players = (List<String>) documentSnapshot.get("players");
                players.remove(playerName);
                Map<String, Object> game = new HashMap<>();
                game.put("players", players);
                room.set(game)
                        .addOnSuccessListener(aVoid -> {
                            String playerString = "";
                            for (String player : players) {
                                playerString += player + "\n";
                            }
                            tvPlayers.setText(playerString);
                        }).addOnFailureListener(e -> LogUtil.d("Fail"));
            }).addOnFailureListener(e -> {
                LogUtil.d("fail");
            });
        }

        super.onDestroy();
    }
}
