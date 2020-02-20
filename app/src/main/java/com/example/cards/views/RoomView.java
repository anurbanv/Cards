package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.cards.App;
import com.example.cards.R;
import com.example.cards.domain.Room;
import com.example.cards.service.Preferences;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomView extends LinearLayout {

    @BindView(R.id.llPlayers) LinearLayout llPlayers;
    @BindView(R.id.tvCount) TextView tvCount;

    @Inject Preferences preferences;

    public RoomView(Context context) {
        super(context);
        init(context);
    }

    public RoomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RoomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.room_view, this, true);
        ButterKnife.bind(this, root);
        App.get().getAppComponent().inject(this);
    }

    public void update(Room room) {
        if (room == null || room.getPlayers().isEmpty()) {
            return;
        }

        String hostName = room.getHostName();
        String playerName = preferences.getPlayerName();
        List<String> players = room.getPlayers();

        llPlayers.removeAllViews();
        for (String name : players) {
            PlayerView playerView = new PlayerView(getContext());
            boolean isHost = hostName.equals(name);
            boolean isYou = playerName.equals(name);
            playerView.setInfo(name, isHost, isYou);
            llPlayers.addView(playerView);
        }

        int playerCount = room.getPlayers().size();
        String count = "[" + playerCount + "/6]";
        tvCount.setText(count);
    }
}
