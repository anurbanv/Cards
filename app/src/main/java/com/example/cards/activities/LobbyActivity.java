package com.example.cards.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.cards.App;
import com.example.cards.R;
import com.example.cards.service.Preferences;
import com.example.cards.service.RoomService;
import com.example.cards.viewmodel.RoomViewModel;
import com.example.cards.views.RoomView;
import com.example.cards.views.StartButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LobbyActivity extends AppCompatActivity {

    @BindView(R.id.btnLeave) Button btnLeave;
    @BindView(R.id.btnStart) StartButton btnStart;
    @BindView(R.id.roomView) RoomView roomView;

    private RoomViewModel roomViewModel;
    @Inject Preferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        ButterKnife.bind(this);

        App.get().getAppComponent().inject(this);

        RoomService roomService = new RoomService();

        roomViewModel = ViewModelProviders.of(this).get(RoomViewModel.class);

        btnStart.setViewModels(roomViewModel);

        String roomId = preferences.getRoomId();

        roomViewModel.initCloudObserver(roomId);

        roomViewModel.getRoom().observe(this, room -> {
            roomView.update(room);
            btnStart.update(room);
            if (room.isStarted()) {
                Intent intent = new Intent(this, GameActivity.class);
                preferences.setMultiPlayerMode(true);
                intent.putExtra("playerCount", room.getPlayers().size());
                startActivity(intent);
            }
        });

        btnLeave.setOnClickListener(v -> roomService.leaveRoom(success -> finish()));
    }

    @Override
    protected void onDestroy() {
        roomViewModel.removeListener();
        super.onDestroy();
    }
}
