package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;

import androidx.annotation.Nullable;

public class CardViewHorizontal extends CardView {

    public CardViewHorizontal(Context context, Card card, Player owner) {
        super(context, card, owner);
    }

    public CardViewHorizontal(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardViewHorizontal(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getResId() {
        return R.layout.item_card_rotated;
    }
}
