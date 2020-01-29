package com.example.cards.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cards.R;
import com.example.cards.service.Preferences;
import com.example.cards.service.RoomService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JoinRoomActivity extends AppCompatActivity {

    @BindView(R.id.etPlayerName) EditText etPlayerName;
    @BindView(R.id.etRoomId) EditText etRoomId;
    @BindView(R.id.btnJoin) Button btnJoin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);
        ButterKnife.bind(this);

        etRoomId.setText("12345");

        etPlayerName.setText(String.valueOf(System.currentTimeMillis()));

        RoomService roomService = new RoomService(this);

        Preferences prefs = new Preferences(this);

        if (prefs.isSavedSession()) {
            setInputEnabled(false);
            etRoomId.setText(prefs.getRoomId());
            etPlayerName.setText(prefs.getPlayerName());
            roomService.reJoinRoom(this::onJoinedRoom);
        }

        btnJoin.setOnClickListener(v -> {
            setInputEnabled(false);

            String playerName = etPlayerName.getText().toString();
            String roomId = etRoomId.getText().toString();

            roomService.joinRoom(roomId, playerName, this::onJoinedRoom);
        });
    }

    private void onJoinedRoom(boolean success) {
        if (success) {
            startActivity(new Intent(this, LobbyActivity.class));
            finish();
        } else {
            setInputEnabled(true);
            Toast.makeText(this, "Failed to join", Toast.LENGTH_SHORT).show();
        }
    }

    private void setInputEnabled(boolean enabled) {
        etPlayerName.setEnabled(enabled);
        etRoomId.setEnabled(enabled);
        btnJoin.setEnabled(enabled);
    }
}
