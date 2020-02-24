package com.example.cards.views.card_view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.example.cards.R;
import com.example.cards.domain.Card;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class CardOpenView extends CardView {

    @BindView(R.id.tvNumber) TextView tvNumber;
    @BindView(R.id.tvNumberBottom) TextView tvNumberBottom;
    @BindView(R.id.icon) View icon;
    @BindView(R.id.iconBottom) View iconBottom;
    @BindView(R.id.background) View background;

    private Card card;

    public CardOpenView(Context context, Card card) {
        super(context);
        this.card = card;
        init();
    }

    public CardOpenView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardOpenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ButterKnife.bind(this, getRoot());

        tvNumber.setText(card.getNumberText());
        tvNumberBottom.setText(card.getNumberText());

        int resId = 0;
        boolean color = false;
        switch (card.getSuite()) {
            case DIAMONDS:
                resId = R.drawable.ic_diamond;
                color = true;
                break;
            case CLUBS:
                resId = R.drawable.ic_clubs;
                break;
            case HEARTS:
                resId = R.drawable.ic_heart;
                color = true;
                break;
            case SPADES:
                resId = R.drawable.ic_spades;
                break;
        }

        Drawable drawable = getResources().getDrawable(resId);
        icon.setBackground(drawable);
        iconBottom.setBackground(drawable);

        int textColor = color ? getResources().getColor(R.color.red) : Color.BLACK;
        tvNumber.setTextColor(textColor);
        tvNumberBottom.setTextColor(textColor);

        if (card.isStrong()) {
            ViewCompat.setBackgroundTintList(background,
                    getResources().getColorStateList(R.color.cyan));
        }
    }

    public Card getCard() {
        return card;
    }
}
