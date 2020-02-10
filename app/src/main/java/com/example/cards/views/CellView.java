package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Cell;
import com.example.cards.views.card_view.CardView;
import com.example.cards.views.card_view.CardViewHor;
import com.example.cards.views.card_view.CardViewVer;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class CellView extends RelativeLayout {

    @BindView(R.id.tvNumber) TextView tvNumber;
    @BindView(R.id.container) FrameLayout container;

    private int index;

    public CellView(Context context, int index) {
        super(context);
        this.index = index;
        init(context);
    }

    public CellView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CellView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.item_cell, this, true);
        ButterKnife.bind(this, root);
        tvNumber.setText(String.valueOf(index + 1));
    }

    private void setAttackCard(Card card) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        CardView cardView = new CardViewVer(getContext(), card);
        cardView.setLayoutParams(params);
        container.addView(cardView);
    }

    private void setDefendCard(Card card) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        CardView cardView = new CardViewHor(getContext(), card);
        cardView.setLayoutParams(params);
        container.addView(cardView);
    }

    public boolean isEmpty() {
        return container.getChildCount() == 0;
    }

    public void update(Cell cell) {
        container.removeAllViews();
        Card attackCard = cell.getAttackCard();
        if (attackCard != null) setAttackCard(attackCard);
        Card defendCard = cell.getDefendCard();
        if (defendCard != null) setDefendCard(defendCard);
    }
}
