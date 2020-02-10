package com.example.cards.views.card_view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.cards.R;
import com.example.cards.domain.Card;

public class CardVerView extends CardOpenView {

    public CardVerView(Context context, Card card) {
        super(context, card);
    }

    public CardVerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardVerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getResId() {
        return R.layout.item_card;
    }
}
