package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.viewmodel.BattleFieldViewModel;
import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.viewmodel.DeckViewModel;
import com.example.cards.viewmodel.PlayersViewModel;
import com.example.cards.viewmodel.RoomViewModel;
import com.example.cards.views.card_view.CardDragHorView;
import com.example.cards.views.card_view.CardHiddenHorView;
import com.example.cards.views.card_view.CardView;

public class HandViewVertical extends HandView {

    public HandViewVertical(Context context) {
        super(context);
    }

    public HandViewVertical(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HandViewVertical(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getLayoutId() {
        return R.layout.hand_view_vertical;
    }

    @Override
    CardView getCardView(Context context, Card card, Player player,
                         CurrentDragViewModel currentDragViewModel,
                         BattleFieldViewModel battleFieldViewModel,
                         PlayersViewModel playersViewModel,
                         RoomViewModel roomViewModel, DeckViewModel deckViewModel) {
        return new CardDragHorView(getContext(), card, player, currentDragViewModel,
                battleFieldViewModel, playersViewModel, roomViewModel, deckViewModel);
    }

    @Override
    CardView getCardHiddenView(Context context) {
        return new CardHiddenHorView(context);
    }
}
