package com.example.cards.views.card_view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.viewmodel.CurrentDragViewModel;

public abstract class CardDragView extends CardOpenView {

    private Player owner;
    private CurrentDragViewModel currentDragViewModel;

    public CardDragView(Context context, Card card, Player owner, CurrentDragViewModel currentDragViewModel) {
        super(context, card);
        this.owner = owner;
        this.currentDragViewModel = currentDragViewModel;
        init();
    }

    public CardDragView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardDragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
