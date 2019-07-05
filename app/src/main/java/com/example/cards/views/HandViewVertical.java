package com.example.cards.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.domain.PlayerState;

import androidx.annotation.Nullable;

public class HandViewVertical extends HandView {

    public HandViewVertical(Context context) {
        super(context);
    }

    public HandViewVertical(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HandViewVertical(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
            CardViewHorizontal cardView = new CardViewHorizontal(getContext(), card, player);
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

    @Override
    CardView getCardView(Context context, Card card, Player player) {
        return new CardViewHorizontal(getContext(), card, player);
    }
}
