package com.example.cards.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.cards.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerView extends LinearLayout {

    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.icon) View icon;
    @BindView(R.id.root) View root;

    public PlayerView(Context context) {
        super(context);
        init(context);
    }

    public PlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setInfo(String name, boolean isHost, boolean isYou) {
        tvName.setText(name);
        if (isHost) {
            icon.setVisibility(VISIBLE);
        }
        if (isYou) {
            Drawable background = root.getBackground().mutate();
            DrawableCompat.setTint(background, getResources().getColor(R.color.colorPrimary));
            root.setBackground(background);
            tvName.setTextColor(Color.WHITE);
        }
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.item_player, this, true);
        ButterKnife.bind(root, this);
    }
}
