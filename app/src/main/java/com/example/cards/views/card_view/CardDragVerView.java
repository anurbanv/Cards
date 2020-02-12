package com.example.cards.views.card_view;

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

public class CardDragVerView extends CardDragView {

    public CardDragVerView(Context context, Card card, Player owner,
                           CurrentDragViewModel currentDragViewModel,
                           BattleFieldViewModel battleFieldViewModel, PlayersViewModel playersViewModel,
                           RoomViewModel roomViewModel, DeckViewModel deckViewModel) {
        super(context, card, owner, currentDragViewModel, battleFieldViewModel,
                playersViewModel, roomViewModel, deckViewModel);
    }

    public CardDragVerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardDragVerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getResId() {
        return R.layout.item_card;
    }
}
