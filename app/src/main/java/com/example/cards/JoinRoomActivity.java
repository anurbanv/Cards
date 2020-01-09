package com.example.cards;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

        RoomService roomService = new RoomService();

        btnJoin.setOnClickListener(v -> {
            setInputEnabled(false);

            String playerName = etPlayerName.getText().toString();
            String roomId = etRoomId.getText().toString();

            roomService.joinRoom(playerName, roomId, success -> {
                if (success) {
                    new Handler().postDelayed(() -> roomService.leaveRoom(success1 -> {
                        if (success1) {
                            setInputEnabled(true);
                        }
                    }), 3000);
                } else {
                    setInputEnabled(true);
                    Toast.makeText(this, "Input invalid", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setInputEnabled(boolean enabled) {
        etPlayerName.setEnabled(enabled);
        etRoomId.setEnabled(enabled);
        btnJoin.setEnabled(enabled);
    }
}
