package com.example.cards.views.card_view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.cards.R;

public class CardHiddenVerView extends CardHiddenView {

    private int id = 0;

    public CardHiddenVerView(Context context, int id) {
        super(context);
        this.id = id;
        init();
    }

    public CardHiddenVerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardHiddenVerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    View getMainView(LayoutInflater inflater, ViewGroup root) {
        return inflater.inflate(R.layout.item_card_hidden_ver, root, true);
    }

    private void init() {
        int resId = R.drawable.card_back_ver;
        switch (id) {
            case 1:
                resId = R.drawable.card_back_black_ver;
                break;
            case 2:
                resId = R.drawable.card_back_gold_ver;
                break;
        }
        Drawable drawable = getResources().getDrawable(resId);
        getRoot().findViewById(R.id.background).setBackground(drawable);
    }
}
