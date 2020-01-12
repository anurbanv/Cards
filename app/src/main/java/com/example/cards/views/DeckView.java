package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrius.logutil.LogUtil;
import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.DeckOfCards;
import com.example.cards.domain.Player;
import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.viewmodel.DeckViewModel;
import com.example.cards.viewmodel.PlayersViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeckView extends LinearLayout {

    @BindView(R.id.tvDeckCount) TextView tvDeckCount;
    @BindView(R.id.tvOutCount) TextView tvOutCount;
    @BindView(R.id.btnDeck) View btnDeck;
    @BindView(R.id.lastCard) RelativeLayout lastCard;
    @BindView(R.id.outCards) View outCards;
    private PlayersViewModel playersViewModel;
    private DeckViewModel deckViewModel;
    private CurrentDragViewModel currentDragViewModel;

    public DeckView(Context context) {
        super(context);
        init(context);
    }

    public DeckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DeckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.deck_view, this, true);
        ButterKnife.bind(this, root);

        btnDeck.setOnClickListener(v -> {
            List<Player> playersList = playersViewModel.getPlayersInGame();
            for (Player player : playersList) {
                while (player.getHand().size() < 6) {
                    if (deckViewModel.hasCards()) {
                        Card card = deckViewModel.takeCard();
                        player.addCardToHand(card);
                    } else {
                        LogUtil.w("Deck ran out of cards");
                        break;
                    }
                }
            }
        });
    }

    public void setViewModels(PlayersViewModel playersViewModel, DeckViewModel deckViewModel,
                              CurrentDragViewModel currentDragViewModel) {
        this.playersViewModel = playersViewModel;
        this.deckViewModel = deckViewModel;
        this.currentDragViewModel = currentDragViewModel;
    }

    public void update(DeckOfCards deck) {
        if (deck.isEmpty()) {
            btnDeck.setEnabled(false);
            btnDeck.setVisibility(INVISIBLE);
            tvDeckCount.setVisibility(GONE);
        }
        tvDeckCount.setText(String.valueOf(deck.cardCount()));
        lastCard.removeAllViews();
        lastCard.addView(new CardViewVertical(getContext(), deck.getLastCard(), currentDragViewModel));
    }

    public void update(List<Card> outCards) {
        tvOutCount.setText(String.valueOf(outCards.size()));
    }
}
