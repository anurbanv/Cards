package com.example.cards.service;

import com.example.cards.R;

public enum CardStyle {

    DEFAULT(0, R.drawable.card_back_ver),

    DARK(1, R.drawable.card_back_black_ver),

    GOLD(2, R.drawable.card_back_gold_ver);

    private int id;
    private int drawableId;

    CardStyle(int id, int drawableId) {
        this.id = id;
        this.drawableId = drawableId;
    }

    public int getId() {
        return id;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public static CardStyle getById(int id) {
        for (CardStyle value : values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        return DEFAULT;
    }
}
