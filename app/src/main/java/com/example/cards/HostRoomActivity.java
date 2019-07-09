package com.example.cards;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andrius.logutil.LogUtil;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HostRoomActivity extends AppCompatActivity {

    @BindView(R.id.etRoomId) EditText etRoomId;
    @BindView(R.id.btnHost) Button btnHost;

    private FirebaseFirestore db;
    private String roomId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_room);
        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();

        btnHost.setOnClickListener(v -> hostRoom());
    }

    private void hostRoom() {
        roomId = etRoomId.getText().toString().trim();
        if (roomId.length() < 5) {
            Toast.makeText(this, "id min length is 5", Toast.LENGTH_SHORT).show();
            return;
        }

        etRoomId.setEnabled(false);
        btnHost.setEnabled(false);

        Map<String, Object> game = new HashMap<>();
        game.put("test", "test");

        DocumentReference games = db.collection("games").document(roomId);
        games.set(game)
                .addOnSuccessListener(aVoid -> {
                    LogUtil.d("success");
                }).addOnFailureListener(e -> {
            LogUtil.d("fail");
        });

    }

    @Override
    protected void onDestroy() {
        if (!roomId.isEmpty()) {
            DocumentReference room = db.collection("games").document(roomId);
            room.delete()
                    .addOnSuccessListener(aVoid -> {
                        LogUtil.d("success");
                    })
                    .addOnFailureListener(e -> {
                        LogUtil.d("failure");
                    });
        }
        super.onDestroy();
    }
}
