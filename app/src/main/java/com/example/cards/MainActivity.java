package com.example.cards;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.domain.PlayerState;
import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.viewmodel.DeckViewModel;
import com.example.cards.viewmodel.GameFieldViewModel;
import com.example.cards.viewmodel.PlayersViewModel;
import com.example.cards.views.DeckView;
import com.example.cards.views.DoneButton;
import com.example.cards.views.GameFieldView;
import com.example.cards.views.HandViewH;
import com.example.cards.views.HandViewV;
import com.example.cards.views.HomeButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.player1) HandViewH player1Hand;
    @BindView(R.id.player3) HandViewH player3Hand;
    @BindView(R.id.player2) HandViewV player2Hand;
    @BindView(R.id.player4) HandViewV player4Hand;

    @BindView(R.id.btnHome) HomeButton btnHome;
    @BindView(R.id.btnDone) DoneButton btnDone;

    @BindView(R.id.deckView) DeckView deckView;

    @BindView(R.id.gameField) GameFieldView gameField;

    public static DeckViewModel deckViewModel;
    public static PlayersViewModel playersViewModel;
    public static CurrentDragViewModel currentDragViewModel;
    public static GameFieldViewModel gameFieldViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (deckViewModel == null) {
            deckViewModel = ViewModelProviders.of(this).get(DeckViewModel.class);
        }

        if (playersViewModel == null) {
            playersViewModel = ViewModelProviders.of(this).get(PlayersViewModel.class);
        }

        if (currentDragViewModel == null) {
            currentDragViewModel = ViewModelProviders.of(this).get(CurrentDragViewModel.class);
        }

        if (gameFieldViewModel == null) {
            gameFieldViewModel = ViewModelProviders.of(this).get(GameFieldViewModel.class);
        }

        deckViewModel.getDeck().observe(this, deckOfCards -> deckView.update(deckOfCards));
        deckViewModel.getOutCards().observe(this, cards -> deckView.update(cards));
        deckView.setLastCard(deckViewModel.getLastCard());

        playersViewModel.getPlayers().observe(this, players -> {
            for (Player player : players) {
                switch (player.getId()) {
                    case 1:
                        player1Hand.update(player);
                        break;
                    case 2:
                        player2Hand.update(player);
                        break;
                    case 3:
                        player3Hand.update(player);
                        break;
                    case 4:
                        player4Hand.update(player);
                        break;
                }
            }
        });

        gameFieldViewModel.getAttackingCards().observe(this, cards -> {
            btnHome.update(gameFieldViewModel.getDefendingCardList(),
                    gameFieldViewModel.getAttackingCardList());
            btnDone.update(gameFieldViewModel.getDefendingCardList(),
                    gameFieldViewModel.getAttackingCardList());
            gameField.updateAttackCells(cards);
        });

        gameFieldViewModel.getDefendingCards().observe(this, cards -> {
            btnDone.update(gameFieldViewModel.getDefendingCardList(),
                    gameFieldViewModel.getAttackingCardList());
            btnHome.update(gameFieldViewModel.getDefendingCardList(),
                    gameFieldViewModel.getAttackingCardList());
            gameField.updateDefendCells(cards);
        });

        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Player player3 = new Player(3);
        Player player4 = new Player(4);
        playersViewModel.addPlayer(player1);
        playersViewModel.addPlayer(player2);
        playersViewModel.addPlayer(player3);
        playersViewModel.addPlayer(player4);

        AsyncTask.execute(() -> {
            for (int i = 0; i < 6; i++) {
                Card card = deckViewModel.takeCard();
                player1.addCardToHand(card);
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    break;
                }
                card = deckViewModel.takeCard();
                player2.addCardToHand(card);
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    break;
                }
                card = deckViewModel.takeCard();
                player3.addCardToHand(card);
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    break;
                }
                card = deckViewModel.takeCard();
                player4.addCardToHand(card);
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    break;
                }
            }

            Card lowestStrongCard = player1.lookAtCard(0);
            Player cardOwner = player1;

            for (Player player : playersViewModel.getPlayersList()) {
                for (Card card : player.getHand()) {
                    if (!lowestStrongCard.isStrong() && card.isStrong()) {
                        lowestStrongCard = card;
                        cardOwner = player;
                    } else if (card.isStrong() && card.getNumber() < lowestStrongCard.getNumber()) {
                        lowestStrongCard = card;
                        cardOwner = player;
                    }
                }
            }

            Player nextPlayer = playersViewModel.getNextPlayer(cardOwner);

            cardOwner.setState(PlayerState.ATTACK);
            nextPlayer.setState(PlayerState.DEFEND);
        });
    }
}
