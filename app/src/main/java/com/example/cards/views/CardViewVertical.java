package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;

import androidx.annotation.Nullable;

public class CardViewVertical extends CardView {

    public CardViewVertical(Context context, Card card, Player owner) {
        super(context, card, owner);
    }

    public CardViewVertical(Context context, Card card) {
        super(context, card);
    }

    public CardViewVertical(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    int getResId() {
        return R.layout.item_card;
    }
}
