package com.example.cards;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cards.service.Preferences;
import com.example.cards.views.RoomView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HostRoomActivity extends AppCompatActivity {

    @BindView(R.id.etRoomId) EditText etRoomId;
    @BindView(R.id.btnJoin) Button btnJoin;
    @BindView(R.id.btnLeave) Button btnLeave;
    @BindView(R.id.etPlayerName) EditText etPlayerName;
    @BindView(R.id.roomView) RoomView roomView;
    @BindView(R.id.btnStart) Button btnStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_room);
        ButterKnife.bind(this);

        Preferences prefs = MainActivity.prefs;

        MainActivity.roomViewModel.getRoom().observe(this, room -> roomView.update(room));

        boolean restored = MainActivity.roomViewModel.restoreState();
        if (restored) {
            etRoomId.setText(prefs.getRoomId());
            etPlayerName.setText(prefs.getPlayerName());
            setInputEnabled(false);
        }

        btnJoin.setOnClickListener(v -> {
            String roomId = etRoomId.getText().toString().trim();
            String playerName = etPlayerName.getText().toString().trim();

            setInputEnabled(false);

            MainActivity.roomViewModel.joinRoom(roomId, playerName, created -> {
                if (!created) {
                    Toast.makeText(this, "FAILED", Toast.LENGTH_SHORT).show();
                    setInputEnabled(true);
                }
            });
        });

        btnLeave.setOnClickListener(v -> MainActivity.roomViewModel.leaveRoom(success -> {
            if (success) {
                setInputEnabled(true);
                etRoomId.setText("");
                etPlayerName.setText("");
            }
        }));

        btnStart.setOnClickListener(v -> {

        });
    }

    private void setInputEnabled(boolean enabled) {
        etRoomId.setEnabled(enabled);
        etPlayerName.setEnabled(enabled);
        btnJoin.setEnabled(enabled);
    }
}
