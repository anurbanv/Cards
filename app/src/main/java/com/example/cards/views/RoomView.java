package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.cards.R;
import com.example.cards.domain.Room;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomView extends LinearLayout {

    @BindView(R.id.tvPlayers) TextView tvPlayers;

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
    }

    public void update(Room room) {
        if (room == null || room.getPlayers().isEmpty()) {
            tvPlayers.setText("empty");
            return;
        }

        List<String> players = room.getPlayers();
        String text = "";
        for (String player : players) {
            text += player + "\n";
        }
        String substring = text.substring(0, text.length() - 1);
        tvPlayers.setText(substring);
    }
}
