package com.example.cards.views;


import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.example.cards.App;
import com.example.cards.domain.Room;
import com.example.cards.service.Preferences;
import com.example.cards.viewmodel.RoomViewModel;

import javax.inject.Inject;

public class StartButton extends AppCompatButton {

    @Inject Preferences preferences;
    private RoomViewModel roomViewModel;

    public StartButton(Context context) {
        super(context);
        init();
    }

    public StartButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StartButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        App.get().getAppComponent().inject(this);
        setOnClickListener(v -> roomViewModel.setGameStarted(true));
    }

    public void setViewModels(RoomViewModel roomViewModel) {
        this.roomViewModel = roomViewModel;
    }

    public void update(Room room) {
        String playerName = preferences.getPlayerName();
        String hostName = room.getHostName();
        boolean isHost = playerName.equals(hostName);
        setEnabled(isHost);

        if (room.getPlayers().size() < 2) {
            setEnabled(false);
        }
    }
}
