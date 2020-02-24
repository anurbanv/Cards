package com.example.cards.views.card_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.viewmodel.BattleFieldViewModel;
import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.viewmodel.DeckViewModel;
import com.example.cards.viewmodel.PlayersViewModel;
import com.example.cards.viewmodel.RoomViewModel;

public class CardDragHorView extends CardDragView {

    public CardDragHorView(Context context, Card card, Player owner,
                           CurrentDragViewModel currentDragViewModel,
                           BattleFieldViewModel battleFieldViewModel, PlayersViewModel playersViewModel,
                           RoomViewModel roomViewModel, DeckViewModel deckViewModel) {
        super(context, card, owner, currentDragViewModel, battleFieldViewModel,
                playersViewModel, roomViewModel, deckViewModel);
    }

    public CardDragHorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardDragHorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    View getMainView(LayoutInflater inflater, ViewGroup root) {
        return inflater.inflate(R.layout.item_card_rotated, root, true);
    }
}
