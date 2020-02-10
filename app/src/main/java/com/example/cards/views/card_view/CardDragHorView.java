package com.example.cards.views.card_view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.viewmodel.BattleFieldViewModel;
import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.viewmodel.PlayersViewModel;
import com.example.cards.viewmodel.RoomViewModel;

public class CardDragHorView extends CardDragView {

    public CardDragHorView(Context context, Card card, Player owner,
                           CurrentDragViewModel currentDragViewModel,
                           BattleFieldViewModel battleFieldViewModel, PlayersViewModel playersViewModel,
                           RoomViewModel roomViewModel) {
        super(context, card, owner, currentDragViewModel, battleFieldViewModel,
                playersViewModel, roomViewModel);
    }

    public CardDragHorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardDragHorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getResId() {
        return R.layout.item_card_rotated;
    }
}
