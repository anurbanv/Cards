package com.example.cards.views.card_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.service.CardDropEventHandler;
import com.example.cards.viewmodel.BattleFieldViewModel;
import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.viewmodel.PlayersViewModel;
import com.example.cards.viewmodel.RoomViewModel;

public abstract class CardDragView extends CardOpenView {

    private Player owner;
    private CurrentDragViewModel currentDragViewModel;
    private CardDropEventHandler handler;

    public CardDragView(Context context, Card card, Player owner,
                        CurrentDragViewModel currentDragViewModel,
                        BattleFieldViewModel battleFieldViewModel, PlayersViewModel playersViewModel,
                        RoomViewModel roomViewModel) {
        super(context, card);
        this.owner = owner;
        this.currentDragViewModel = currentDragViewModel;

        handler = new CardDropEventHandler(currentDragViewModel, battleFieldViewModel,
                playersViewModel, roomViewModel);
        init();
    }

    public CardDragView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardDragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        if (owner != null) {
            setOnLongClickListener(v -> {
                DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);
                v.startDrag(null, shadowBuilder, v, 0);
                currentDragViewModel.setCurrentDrag(getCard(), owner);
                return true;
            });
        }

        setOnClickListener(v -> {
            ViewGroup parent = (ViewGroup) getParent();
            int index = parent.indexOfChild(this);
            Card card = owner.getHand().get(index);
            handler.initClickEvent(card, owner);
        });
    }
}
