package com.example.cards.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.domain.PlayerState;

import androidx.annotation.Nullable;

public class HandViewV extends HandView {

    public HandViewV(Context context) {
        super(context);
    }

    public HandViewV(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HandViewV(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getLayoutId() {
        return R.layout.hand_view_vertical;
    }

    @Override
    public void update(Player player) {
        llCards.removeAllViews();
        for (Card card : player.getHand()) {
            CardViewRotated cardView = new CardViewRotated(getContext(), card, player);
            llCards.addView(cardView);
        }
        if (player.getState() == PlayerState.ATTACK) {
            setBackgroundColor(Color.RED);
        } else if (player.getState() == PlayerState.DEFEND) {
            setBackgroundColor(Color.BLUE);
        } else {
            setBackgroundColor(getResources().getColor(R.color.gray));
        }
    }
}
