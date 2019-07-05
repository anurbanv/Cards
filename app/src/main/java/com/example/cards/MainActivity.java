package com.example.cards;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.viewmodel.DeckViewModel;
import com.example.cards.viewmodel.GameFieldViewModel;
import com.example.cards.viewmodel.PlayersViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn2Players) Button btn2Players;
    @BindView(R.id.btn4Players) Button btn4Players;
    @BindView(R.id.btn6Players) Button btn6Players;

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

        btn2Players.setOnClickListener(v -> startGameActivity(2));
        btn4Players.setOnClickListener(v -> startGameActivity(4));
        btn6Players.setOnClickListener(v -> startGameActivity(6));
    }

    private void startGameActivity(int playerCount) {
        resetModels();
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("count", playerCount);
        startActivity(intent);
    }

    private void resetModels() {
        deckViewModel.reset();
        playersViewModel.reset();
        gameFieldViewModel.reset();
    }
}
