package com.example.cards.views.card_view;

import android.content.Context;

import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.viewmodel.CurrentDragViewModel;

public abstract class CardViewDrag extends CardView {

    private Player owner;
    private CurrentDragViewModel currentDragViewModel;

    public CardViewDrag(Context context, Card card, Player owner, CurrentDragViewModel currentDragViewModel) {
        super(context, card);
        this.owner = owner;
        this.currentDragViewModel = currentDragViewModel;
        init();
    }

    private void init() {
        if (owner != null) {
            setOnLongClickListener(v -> {
                DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);
                v.startDrag(null, shadowBuilder, v, 0);
                currentDragViewModel.setCurrentDrag(getCard(), owner);
                return true;
            });
        }
    }
}
