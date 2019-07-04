package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cards.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CellView extends RelativeLayout {

    @BindView(R.id.container) LinearLayout container;
    @BindView(R.id.tvNumber) TextView tvNumber;

    private int index;

    public CellView(Context context, int index) {
        super(context);
        this.index = index;
        init(context);
    }

    public CellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.item_cell, this, true);
        ButterKnife.bind(this, root);
        tvNumber.setText(String.valueOf(index + 1));
    }

    public void setCard(CardView view) {
        container.removeAllViews();
        container.addView(view);
    }

    public void removeCard() {
        container.removeAllViews();
    }
}
