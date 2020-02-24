package com.example.cards.views.card_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public abstract class CardView extends LinearLayout {

    private View root;

    public CardView(Context context) {
        super(context);
        init(context);
    }

    public CardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    abstract View getMainView(LayoutInflater inflater, ViewGroup root);

    private void init(Context context) {
        this.root = getMainView(LayoutInflater.from(context), this);
    }

    public View getRoot() {
        return root;
    }
}
