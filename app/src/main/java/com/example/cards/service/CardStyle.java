package com.example.cards.service;

import com.example.cards.R;

public enum CardStyle {

    DEFAULT(R.drawable.card_back_ver),

    DARK(R.drawable.card_back_black_ver),

    GOLD(R.drawable.card_back_gold_ver);

    private int drawableId;

    CardStyle(int drawableId) {
        this.drawableId = drawableId;
    }

    public int getDrawableId() {
        return drawableId;
    }
}
