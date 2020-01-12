package com.example.cards.views.card_view;

import android.content.Context;

import com.example.cards.R;
import com.example.cards.domain.Card;

public class CardViewVer extends CardView {

    public CardViewVer(Context context, Card card) {
        super(context, card);
    }

    @Override
    int getResId() {
        return R.layout.item_card;
    }
}
