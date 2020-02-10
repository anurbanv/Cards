package com.example.cards.views.card_view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.cards.R;

public class CardHiddenHorView extends CardHiddenView {

    public CardHiddenHorView(Context context) {
        super(context);
    }

    public CardHiddenHorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardHiddenHorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getResId() {
        return R.layout.item_card_hidden;
    }
}
