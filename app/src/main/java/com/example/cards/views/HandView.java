package com.example.cards.views;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.service.Preferences;
import com.example.cards.viewmodel.BattleFieldViewModel;
import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.viewmodel.DeckViewModel;
import com.example.cards.viewmodel.PlayersViewModel;
import com.example.cards.viewmodel.RoomViewModel;
import com.example.cards.views.card_view.CardView;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class HandView extends LinearLayout {

    @BindView(R.id.llCards) LinearLayout llCards;
    @BindView(R.id.btnInfo) Button btnInfo;

    private CurrentDragViewModel currentDragViewModel;
    private BattleFieldViewModel battleFieldViewModel;
    private PlayersViewModel playersViewModel;
    private RoomViewModel roomViewModel;
    private DeckViewModel deckViewModel;
    private Player player;
    private Preferences preferences;

    public HandView(Context context) {
        super(context);
        init(context);
    }

    public HandView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HandView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getLayoutId(), this, true);
        ButterKnife.bind(this, view);

        preferences = new Preferences(context);

        btnInfo.setOnClickListener(v -> showDialog(context));
    }

    public void setViewModel(CurrentDragViewModel currentDragViewModel,
                             BattleFieldViewModel battleFieldViewModel,
                             PlayersViewModel playersViewModel,
                             RoomViewModel roomViewModel, DeckViewModel deckViewModel) {
        this.currentDragViewModel = currentDragViewModel;
        this.battleFieldViewModel = battleFieldViewModel;
        this.playersViewModel = playersViewModel;
        this.roomViewModel = roomViewModel;
        this.deckViewModel = deckViewModel;
    }

    abstract int getLayoutId();

    public void update(Player player) {
        this.player = player;
        llCards.removeAllViews();
        for (Card card : player.getHand()) {
            llCards.addView(getCardViewToAdd(getContext(), card, player));
        }

        if (player.isOut()) {
            DrawableCompat.setTint(getBackground(), getResources().getColor(R.color.colorPrimary));
        } else {
            if (player.getAction() == Player.Action.ATTACK) {
                DrawableCompat.setTint(getBackground(), Color.RED);
            } else if (player.getAction() == Player.Action.DEFEND) {
                DrawableCompat.setTint(getBackground(), Color.BLUE);
            } else {
                DrawableCompat.setTint(getBackground(), getResources().getColor(R.color.gray));
            }
        }
    }

    abstract CardView getCardView(Context context, Card card, Player player,
                                  CurrentDragViewModel currentDragViewModel,
                                  BattleFieldViewModel battleFieldViewModel,
                                  PlayersViewModel playersViewModel,
                                  RoomViewModel roomViewModel, DeckViewModel deckViewModel);

    abstract CardView getCardHiddenView(Context context);

    private CardView getCardViewToAdd(Context context, Card card, Player player) {
        if (preferences.isMultiPlayerMode()) {
            String playerName = preferences.getPlayerName();
            if (!playerName.equals(player.getName())) {
                return getCardHiddenView(context);
            }
        }
        return getCardView(context, card, player, currentDragViewModel, battleFieldViewModel,
                playersViewModel, roomViewModel, deckViewModel);
    }

    private void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("MSG " + player.getName());
        builder.create().show();
    }
}
