package com.example.cards;

import android.os.Bundle;

import com.example.cards.domain.Card;
import com.example.cards.domain.Player;
import com.example.cards.domain.PlayerState;
import com.example.cards.views.DeckView;
import com.example.cards.views.DoneButton;
import com.example.cards.views.GameFieldView;
import com.example.cards.views.HandViewH;
import com.example.cards.views.HandViewV;
import com.example.cards.views.HomeButton;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.cards.MainActivity.deckViewModel;
import static com.example.cards.MainActivity.gameFieldViewModel;
import static com.example.cards.MainActivity.playersViewModel;

public class GameActivity extends AppCompatActivity {

    @BindView(R.id.player1) HandViewH player1Hand;
    @BindView(R.id.player3) HandViewH player3Hand;
    @BindView(R.id.player2) HandViewV player2Hand;
    @BindView(R.id.player4) HandViewV player4Hand;

    @BindView(R.id.btnHome) HomeButton btnHome;
    @BindView(R.id.btnDone) DoneButton btnDone;

    @BindView(R.id.deckView) DeckView deckView;

    @BindView(R.id.gameField) GameFieldView gameField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        int count = getIntent().getIntExtra("count", 0);

        if (count == 0) {
            finish();
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

        for (int i = 0; i < count; i++) {
            Player player = new Player(i + 1);
            playersViewModel.addPlayer(player);
        }

        List<Player> playersList = playersViewModel.getPlayersList();

        for (int i = 0; i < 6; i++) {
            for (Player player : playersList) {
                Card card = deckViewModel.takeCard();
                player.addCardToHand(card);
            }
        }

        Player firstPlayer = playersList.get(0);

        Card lowestStrongCard = firstPlayer.lookAtCard(0);
        Player cardOwner = firstPlayer;

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
    }
}
