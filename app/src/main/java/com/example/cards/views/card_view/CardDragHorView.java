package com.example.cards.views.card_view;

import android.content.Context;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.viewmodel.CurrentDragViewModel;

public class CardDragHorView extends CardDragView {

    public CardDragHorView(Context context, Card card, Player owner, CurrentDragViewModel currentDragViewModel) {
        super(context, card, owner, currentDragViewModel);
    }

    @Override
    int getResId() {
        return R.layout.item_card_rotated;
    }
}
