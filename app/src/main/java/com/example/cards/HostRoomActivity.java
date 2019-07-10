package com.example.cards;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andrius.logutil.LogUtil;
import com.example.cards.service.Preferences;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HostRoomActivity extends AppCompatActivity {

    @BindView(R.id.etRoomId) EditText etRoomId;
    @BindView(R.id.btnMultiPlayer) Button btnMultiPlayer;
    @BindView(R.id.btnJoin) Button btnJoin;
    @BindView(R.id.etPlayerName) EditText etPlayerName;
    @BindView(R.id.tvPlayers) TextView tvPlayers;

    private FirebaseFirestore db;
    private Preferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_room);
        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();
        prefs = MainActivity.prefs;

        String hostedRoomId = prefs.getHostedRoomId();

        if (!hostedRoomId.isEmpty()) {
            setInputEnabled(false);

            etRoomId.setText(hostedRoomId);
            etPlayerName.setText(prefs.getPlayerName());

            DocumentReference room = db.collection("games").document(hostedRoomId);
            room.addSnapshotListener((documentSnapshot, e) -> updateText(documentSnapshot));
            room.get().addOnSuccessListener(this::updateText);
        }

        String joinRoomId = prefs.getJoinRoomId();

        if (!joinRoomId.isEmpty()) {
            setInputEnabled(false);
            etRoomId.setText(joinRoomId);
            etPlayerName.setText(prefs.getPlayerName());

            DocumentReference room = db.collection("games").document(joinRoomId);
            room.addSnapshotListener((documentSnapshot, e) -> updateText(documentSnapshot));
            room.get().addOnSuccessListener(this::updateText);
        }

        btnMultiPlayer.setOnClickListener(v -> {
            setInputEnabled(false);
            hostRoom();
        });

        btnJoin.setOnClickListener(v -> {
            setInputEnabled(false);
            joinRoom();
        });
    }

    private void hostRoom() {
        String roomId = etRoomId.getText().toString().trim();
        if (roomId.length() < 3) {
            Toast.makeText(this, "id min length is 3", Toast.LENGTH_SHORT).show();
            return;
        }
        String playerName = etPlayerName.getText().toString().trim();
        if (playerName.isEmpty()) {
            Toast.makeText(this, "enter player name", Toast.LENGTH_SHORT).show();
        }

        DocumentReference room = db.collection("games").document(roomId);
        room.addSnapshotListener((documentSnapshot, e) -> updateText(documentSnapshot));

        List<String> playerNames = new ArrayList<>();
        playerNames.add(playerName);
        Map<String, Object> game = new HashMap<>();
        game.put("players", playerNames);

        room.set(game).addOnSuccessListener(aVoid -> {
            LogUtil.d("success");

            prefs.setHostedRoomId(roomId);
            prefs.setPlayerName(playerName);

        }).addOnFailureListener(e -> setInputEnabled(true));
    }

    private void joinRoom() {
        String roomId = etRoomId.getText().toString().trim();
        if (roomId.length() < 3) {
            Toast.makeText(this, "id min length is 3", Toast.LENGTH_SHORT).show();
            return;
        }
        String playerName = etPlayerName.getText().toString().trim();
        if (playerName.isEmpty()) {
            Toast.makeText(this, "enter player name", Toast.LENGTH_SHORT).show();
        }

        DocumentReference room = db.collection("games").document(roomId);
        room.addSnapshotListener((documentSnapshot, e) -> updateText(documentSnapshot));

        room.get().addOnSuccessListener(documentSnapshot -> {
            List<String> players = (List<String>) documentSnapshot.get("players");
            players.add(playerName);
            Map<String, Object> game = new HashMap<>();
            game.put("players", players);
            room.set(game);

            prefs.setJoinRoomId(roomId);
            prefs.setPlayerName(playerName);

        }).addOnFailureListener(e -> {
            setInputEnabled(true);
        });
    }

    private void updateText(DocumentSnapshot documentSnapshot) {
        List<String> players = (List<String>) documentSnapshot.get("players");
        if (players != null) {
            String text = "";
            for (String player : players) {
                text += player + "\n";
            }
            tvPlayers.setText(text);
        }
    }

    private void setInputEnabled(boolean enabled) {
        etRoomId.setEnabled(enabled);
        etPlayerName.setEnabled(enabled);
        btnMultiPlayer.setEnabled(enabled);
        btnJoin.setEnabled(enabled);
    }

    @Override
    protected void onDestroy() {
        String hostRoomId = prefs.getHostedRoomId();
        String joinRoomId = prefs.getJoinRoomId();

        if (!hostRoomId.isEmpty()) {
            DocumentReference room = db.collection("games").document(hostRoomId);
            room.delete();
            prefs.setHostedRoomId("");
            prefs.setPlayerName("");
        }

        if (!joinRoomId.isEmpty()) {
            DocumentReference room = db.collection("games").document(joinRoomId);
            room.get().addOnSuccessListener(documentSnapshot -> {
                List<String> players = (List<String>) documentSnapshot.get("players");
                players.remove(prefs.getPlayerName());

                Map<String, Object> game = new HashMap<>();
                game.put("players", players);

                room.set(game).addOnSuccessListener(aVoid -> {
                    prefs.setJoinRoomId("");
                    prefs.setPlayerName("");
                });
            });
        }

        super.onDestroy();
    }
}
