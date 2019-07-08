package com.example.cards.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.domain.PlayerState;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class HandView extends LinearLayout {

    @BindView(R.id.llCards) LinearLayout llCards;

    public HandView(Context context) {
        super(context);
        init(context);
    }

    public HandView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HandView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getLayoutId(), this, true);
        ButterKnife.bind(this, view);
    }

    abstract int getLayoutId();

    public void update(Player player) {
        llCards.removeAllViews();
        for (Card card : player.getHand()) {
            llCards.addView(getCardView(getContext(), card, player));
        }

        if (player.isOut()) {
            DrawableCompat.setTint(getBackground(), getResources().getColor(R.color.colorPrimary));
        } else {
            if (player.getState() == PlayerState.ATTACK) {
                DrawableCompat.setTint(getBackground(), Color.RED);
            } else if (player.getState() == PlayerState.DEFEND) {
                DrawableCompat.setTint(getBackground(), Color.BLUE);
            } else {
                DrawableCompat.setTint(getBackground(), getResources().getColor(R.color.gray));
            }
        }
    }

    abstract CardView getCardView(Context context, Card card, Player player);
}
