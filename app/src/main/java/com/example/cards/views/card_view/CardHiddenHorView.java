package com.example.cards.views.card_view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.cards.App;
import com.example.cards.R;
import com.example.cards.service.CardStyle;
import com.example.cards.service.Preferences;

import javax.inject.Inject;

public class CardHiddenHorView extends CardHiddenView {

    @Inject Preferences preferences;

    public CardHiddenHorView(Context context) {
        super(context);
        init();
    }

    public CardHiddenHorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardHiddenHorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    View getMainView(LayoutInflater inflater, ViewGroup root) {
        return inflater.inflate(R.layout.item_card_hidden, root, true);
    }

    private void init() {
        App.get().getAppComponent().inject(this);
        CardStyle cardBackStyle = preferences.getCardBackStyle();
        Drawable drawable = getResources().getDrawable(cardBackStyle.getDrawableIdHor());
        getRoot().findViewById(R.id.background).setBackground(drawable);
    }
}
