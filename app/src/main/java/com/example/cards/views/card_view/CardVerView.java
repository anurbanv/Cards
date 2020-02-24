package com.example.cards.views.card_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.cards.R;
import com.example.cards.domain.Card;

public class CardVerView extends CardOpenView {

    public CardVerView(Context context, Card card) {
        super(context, card);
    }

    public CardVerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardVerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    View getMainView(LayoutInflater inflater, ViewGroup root) {
        return inflater.inflate(R.layout.item_card, root, true);
    }
}
