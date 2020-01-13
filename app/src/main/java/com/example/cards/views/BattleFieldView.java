package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.cards.R;
import com.example.cards.domain.Cell;
import com.example.cards.service.CardDropEventHandler;
import com.example.cards.viewmodel.BattleFieldViewModel;
import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.viewmodel.PlayersViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BattleFieldView extends LinearLayout {

    @BindView(R.id.container) LinearLayout container;

    private CardDropEventHandler handler;

    public BattleFieldView(Context context) {
        super(context);
        init(context);
    }

    public BattleFieldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BattleFieldView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.battle_field, this, true);
        ButterKnife.bind(this, root);
        initCells(container);
    }

    public void setViewModels(CurrentDragViewModel currentDragViewModel,
                              BattleFieldViewModel battleFieldViewModel,
                              PlayersViewModel playersViewModel) {
        handler = new CardDropEventHandler(currentDragViewModel, battleFieldViewModel, playersViewModel);
    }

    private void initCells(LinearLayout container) {
        for (int i = 0; i < 6; i++) {
            CellViewNew cellView = new CellViewNew(getContext(), i);
            cellView.setOnDragListener(new FieldDropListener(i));
            container.addView(cellView);
        }
    }

    public void update(List<Cell> cells) {
        for (int i = 0; i < 6; i++) {
            CellViewNew cellView = (CellViewNew) container.getChildAt(i);
            Cell cell = cells.get(i);
            cellView.update(cell);
        }
    }

    class FieldDropListener implements View.OnDragListener {

        private int cell;

        FieldDropListener(int cell) {
            this.cell = cell;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            if (event.getAction() == DragEvent.ACTION_DROP) {
                CellViewNew cellView = (CellViewNew) container.getChildAt(cell);
                handler.initEvent(cellView.isEmpty(), cell);
            }
            return true;
        }
    }
}
