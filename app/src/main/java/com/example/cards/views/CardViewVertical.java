package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.viewmodel.CurrentDragViewModel;

public class CardViewVertical extends CardView {

    public CardViewVertical(Context context, Card card, Player owner, CurrentDragViewModel currentDragViewModel) {
        super(context, card, owner, currentDragViewModel);
    }

    public CardViewVertical(Context context, Card card, CurrentDragViewModel currentDragViewModel) {
        super(context, card, currentDragViewModel);
    }

    public CardViewVertical(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    int getResId() {
        return R.layout.item_card;
    }
}
