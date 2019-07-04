package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
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

        for (int i = 0; i < 6; i++) {
            CellView cellView = new CellView(getContext(), i);
            cellView.setOnDragListener(new FieldDropListener(i, container.equals(llAttackField)));
            container.addView(cellView);
        }
    }

    public void updateAttackCells(Card[] cards) {
        for (int i = 0; i < 6; i++) {
            CellView cell = (CellView) llAttackField.getChildAt(i);
            cell.removeCard();
            Card card = cards[i];
            if (card != null) {
                CardView cardView = new CardView(getContext(), card);
                cell.setCard(cardView);
            }
        }
    }

    public void updateDefendCells(Card[] cards) {
        for (int i = 0; i < 6; i++) {
            CellView cell = (CellView) llDefendField.getChildAt(i);
            cell.removeCard();
            Card card = cards[i];
            if (card != null) {
                CardView cardView = new CardView(getContext(), card);
                cell.setCard(cardView);
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
