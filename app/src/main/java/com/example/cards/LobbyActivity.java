package com.example.cards;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cards.service.RoomService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LobbyActivity extends AppCompatActivity {

    @BindView(R.id.btnLeave) Button btnLeave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        ButterKnife.bind(this);

        RoomService roomService = new RoomService(this);

        btnLeave.setOnClickListener(v -> roomService.leaveRoom(success -> finish()));
    }
}
