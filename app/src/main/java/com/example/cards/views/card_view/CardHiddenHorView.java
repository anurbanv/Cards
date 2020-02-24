package com.example.cards.views.card_view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.cards.R;

public class CardHiddenHorView extends CardHiddenView {

    public CardHiddenHorView(Context context) {
        super(context);
    }

    public CardHiddenHorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardHiddenHorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    View getMainView(LayoutInflater inflater, ViewGroup root) {
        View view = inflater.inflate(R.layout.item_card_hidden, root, true);
        Drawable drawable = getResources().getDrawable(R.drawable.card_back_hor);
        view.findViewById(R.id.background).setBackground(drawable);
        return view;
    }
}
