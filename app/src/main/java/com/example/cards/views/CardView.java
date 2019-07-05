package com.example.cards.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cards.MainActivity;
import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

abstract class CardView extends LinearLayout {

    @BindView(R.id.tvNumber) TextView tvNumber;
    @BindView(R.id.tvNumberBottom) TextView tvNumberBottom;
    @BindView(R.id.icon) View icon;
    @BindView(R.id.background) View background;

    private Card card;
    private Player owner;

    public CardView(Context context, Card card, Player owner) {
        super(context);
        this.card = card;
        this.owner = owner;
        init(context);
    }

    public CardView(Context context, Card card) {
        super(context);
        this.card = card;
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

    abstract int getResId();

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(getResId(), this, true);
        ButterKnife.bind(this, root);

        if (owner != null) {
            setOnLongClickListener(v -> {
                DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);
                v.startDrag(null, shadowBuilder, v, 0);
                MainActivity.currentDragViewModel.setCurrentDrag(card, owner);
                return true;
            });
        }

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

        int textColor = color ? getResources().getColor(R.color.red) : Color.BLACK;
        tvNumber.setTextColor(textColor);
        tvNumberBottom.setTextColor(textColor);

        if (card.isStrong()) {
            ViewCompat.setBackgroundTintList(background,
                    getResources().getColorStateList(R.color.cyan));
        }
    }
}
