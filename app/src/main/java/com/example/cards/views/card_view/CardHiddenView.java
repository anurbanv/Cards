package com.example.cards.views.card_view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public abstract class CardHiddenView extends CardView {

    public CardHiddenView(Context context) {
        super(context);
    }

    public CardHiddenView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardHiddenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
