package com.example.cards.views.card_view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.cards.R;

public class CardHiddenVerView extends CardHiddenView {

    public CardHiddenVerView(Context context) {
        super(context);
    }

    public CardHiddenVerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardHiddenVerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getResId() {
        return R.layout.item_card_hidden_ver;
    }
}
