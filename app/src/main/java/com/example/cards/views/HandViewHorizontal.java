package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.views.card_view.CardDragVerView;
import com.example.cards.views.card_view.CardView;

public class HandViewHorizontal extends HandView {

    public HandViewHorizontal(Context context) {
        super(context);
    }

    public HandViewHorizontal(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HandViewHorizontal(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getLayoutId() {
        return R.layout.hand_view;
    }

    @Override
    CardView getCardView(Context context, Card card, Player player, CurrentDragViewModel currentDragViewModel) {
        return new CardDragVerView(getContext(), card, player, currentDragViewModel);
    }
}
