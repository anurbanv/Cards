package com.example.cards;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.cards.service.Preferences;
import com.example.cards.viewmodel.NewRoomViewModel;

import butterknife.ButterKnife;

public class NewGameActivity extends AppCompatActivity {

    private NewRoomViewModel newRoomViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_new);
        ButterKnife.bind(this);

        newRoomViewModel = ViewModelProviders.of(this).get(NewRoomViewModel.class);

        Preferences prefs = new Preferences(this);

        newRoomViewModel.initCloudObserver(prefs.getRoomId());

        newRoomViewModel.getRoom().observe(this, room -> {
            if (!room.isStarted()) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        newRoomViewModel.setGameStarted(false);
        super.onDestroy();
    }
}
