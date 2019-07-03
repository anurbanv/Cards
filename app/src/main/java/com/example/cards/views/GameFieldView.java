package com.example.cards.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.service.CardDropEventHandler;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GameFieldView extends LinearLayout {

    @BindView(R.id.llAttackField) LinearLayout llAttackField;
    @BindView(R.id.llDefendField) LinearLayout llDefendField;

    private CardDropEventHandler handler;

    public GameFieldView(Context context) {
        super(context);
        init(context);
    }

    public GameFieldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameFieldView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.game_field, this, true);
        ButterKnife.bind(this, root);

        handler = new CardDropEventHandler();

        initCells(llAttackField);
        initCells(llDefendField);
    }

    private void initCells(LinearLayout container) {
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        params.setMargins(10, 10, 10, 10);

        for (int i = 0; i < 6; i++) {
            LinearLayout cell = new LinearLayout(getContext());
            cell.setLayoutParams(params);
            cell.setOnDragListener(new FieldDropListener(i, container.equals(llAttackField)));
            cell.setBackgroundColor(Color.DKGRAY);
            cell.setGravity(Gravity.CENTER);
            container.addView(cell);
        }
    }

    public void updateAttackCells(Card[] cards) {
        for (int i = 0; i < 6; i++) {
            LinearLayout cell = (LinearLayout) llAttackField.getChildAt(i);
            cell.removeAllViews();
            Card card = cards[i];
            if (card != null) {
                CardView cardView = new CardView(getContext(), card);
                cell.addView(cardView);
            }
        }
    }

    public void updateDefendCells(Card[] cards) {
        for (int i = 0; i < 6; i++) {
            LinearLayout cell = (LinearLayout) llDefendField.getChildAt(i);
            cell.removeAllViews();
            Card card = cards[i];
            if (card != null) {
                CardView cardView = new CardView(getContext(), card);
                cell.addView(cardView);
            }
        }
    }

    class FieldDropListener implements View.OnDragListener {

        private int cell;
        private boolean attacking;

        FieldDropListener(int cell, boolean attacking) {
            this.cell = cell;
            this.attacking = attacking;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            if (event.getAction() == DragEvent.ACTION_DROP) {
                handler.initEvent(attacking, cell);
            }
            return true;
        }
    }
}
