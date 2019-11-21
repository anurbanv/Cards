package com.example.cards;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cards.service.Preferences;
import com.example.cards.views.RoomView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.cards.MainActivity.roomViewModel;

public class HostRoomActivity extends AppCompatActivity {

    @BindView(R.id.etRoomId) EditText etRoomId;
    @BindView(R.id.btnJoin) Button btnJoin;
    @BindView(R.id.btnLeave) Button btnLeave;
    @BindView(R.id.etPlayerName) EditText etPlayerName;
    @BindView(R.id.roomView) RoomView roomView;
    @BindView(R.id.btnStart) Button btnStart;

    private boolean activityRunning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_room);
        ButterKnife.bind(this);

        Preferences prefs = MainActivity.prefs;

        if (prefs.isSavedSession()) {
            roomViewModel.restoreRoom((created, message) -> {
                if (created) {
                    setInputEnabled(false);
                    etRoomId.setText(prefs.getRoomId());
                    etPlayerName.setText(prefs.getPlayerName());
                } else {
                    prefs.removeStoredSession();
                }
            });
        }

        roomViewModel.getRoom().observe(this, room -> {
            if (room == null) {
                activityRunning = false;
            }
            roomView.update(room);
            if (room != null && room.isStarted() && !activityRunning) {
                activityRunning = true;
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra("count", 2);
                intent.putExtra("multiPlayer", true);
                startActivity(intent);
            }
        });

        btnJoin.setOnClickListener(v -> {
            String roomId = etRoomId.getText().toString().trim();
            String playerName = etPlayerName.getText().toString().trim();

            setInputEnabled(false);

            roomViewModel.joinRoom(roomId, playerName, (created, message) -> {
                if (!created) {
                    etRoomId.setText("");
                    etPlayerName.setText("");
                    setInputEnabled(true);
                } else {
                    prefs.saveRoomSession(roomId, playerName);
                }
            });
        });

        btnLeave.setOnClickListener(v -> {
            String roomId = prefs.getRoomId();
            String playerName = prefs.getPlayerName();
            roomViewModel.leaveRoom(roomId, playerName, (success, msg) -> {
                if (success) {
                    setInputEnabled(true);
                    etRoomId.setText("");
                    etPlayerName.setText("");
                    prefs.removeStoredSession();
                }
            });
        });

        btnStart.setOnClickListener(v -> {
            roomViewModel.startGame((created, message) -> {

            });
        });
    }

    private void setInputEnabled(boolean enabled) {
        etRoomId.setEnabled(enabled);
        etPlayerName.setEnabled(enabled);
        btnJoin.setEnabled(enabled);
    }
}
