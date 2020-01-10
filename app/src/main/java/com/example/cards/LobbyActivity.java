package com.example.cards;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.cards.service.Preferences;
import com.example.cards.service.RoomService;
import com.example.cards.viewmodel.NewRoomViewModel;
import com.example.cards.views.RoomView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LobbyActivity extends AppCompatActivity {

    @BindView(R.id.btnLeave) Button btnLeave;
    @BindView(R.id.btnStart) Button btnStart;
    @BindView(R.id.tvStarted) TextView tvStarted;
    @BindView(R.id.roomView) RoomView roomView;

    private NewRoomViewModel newRoomViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        ButterKnife.bind(this);

        RoomService roomService = new RoomService(this);

        Preferences prefs = new Preferences(this);

        newRoomViewModel = ViewModelProviders.of(this).get(NewRoomViewModel.class);

        String roomId = prefs.getRoomId();

        newRoomViewModel.initCloudObserver(roomId);

        newRoomViewModel.getRoom().observe(this, room -> {
            roomView.update(room);
            if (room.isStarted()) {
                startActivity(new Intent(this, NewGameActivity.class));
            }
            tvStarted.setText("Started: " + room.isStarted());
        });

        btnLeave.setOnClickListener(v -> roomService.leaveRoom(success -> finish()));

        btnStart.setOnClickListener(v -> newRoomViewModel.setGameStarted(true));
    }

    @Override
    protected void onDestroy() {
        newRoomViewModel.removeListener();
        super.onDestroy();
    }
}
