package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;

import androidx.annotation.Nullable;

public class HandViewHorizontal extends HandView {

    public HandViewHorizontal(Context context) {
        super(context);
    }

    public HandViewHorizontal(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HandViewHorizontal(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getLayoutId() {
        return R.layout.hand_view;
    }

    @Override
    CardView getCardView(Context context, Card card, Player player) {
        return new CardViewVertical(getContext(), card, player);
    }
}
