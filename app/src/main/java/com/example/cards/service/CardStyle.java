package com.example.cards.service;

import com.example.cards.R;

public enum CardStyle {

    DEFAULT(0, R.drawable.card_back_ver, R.drawable.card_back_hor),

    DARK(1, R.drawable.card_back_black_ver, R.drawable.card_back_black_hor),

    GOLD(2, R.drawable.card_back_gold_ver, R.drawable.card_back_gold_hor);

    private int id;
    private int drawableId;
    private int drawableIdHor;

    CardStyle(int id, int drawableId, int drawableIdHor) {
        this.id = id;
        this.drawableId = drawableId;
        this.drawableIdHor = drawableIdHor;
    }

    public int getId() {
        return id;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public int getDrawableIdHor() {
        return drawableIdHor;
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
