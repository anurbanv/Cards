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

public class CardHiddenVerView extends CardHiddenView {

    @Inject Preferences preferences;

    public CardHiddenVerView(Context context) {
        super(context);
        init();
    }

    public CardHiddenVerView(Context context, CardStyle cardStyle) {
        super(context);
        initWithStyle(cardStyle);
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

    private void initWithStyle(CardStyle cardStyle) {
        Drawable drawable = getResources().getDrawable(cardStyle.getDrawableId());
        getRoot().findViewById(R.id.background).setBackground(drawable);
    }

    private void init() {
        App.get().getAppComponent().inject(this);
        CardStyle cardBackStyle = preferences.getCardBackStyle();
        Drawable drawable = getResources().getDrawable(cardBackStyle.getDrawableId());
        getRoot().findViewById(R.id.background).setBackground(drawable);
    }
}
