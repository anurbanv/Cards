package com.example.cards;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.cards.service.Preferences;
import com.example.cards.viewmodel.CurrentDragViewModel;
import com.example.cards.viewmodel.DeckViewModel;
import com.example.cards.viewmodel.GameFieldViewModel;
import com.example.cards.viewmodel.PlayersViewModel;
import com.google.firebase.FirebaseApp;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn2Players) Button btn2Players;
    @BindView(R.id.btn4Players) Button btn4Players;
    @BindView(R.id.btn6Players) Button btn6Players;

    @BindView(R.id.btnMultiPlayer) Button btnHost;

    public static DeckViewModel deckViewModel;
    public static PlayersViewModel playersViewModel;
    public static CurrentDragViewModel currentDragViewModel;
    public static GameFieldViewModel gameFieldViewModel;
    public static File latestSave;
    public static Preferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FirebaseApp.initializeApp(this);

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

        if (latestSave == null) {
            File appDir = getExternalFilesDir(null);
            latestSave = new File(appDir, "latestSave.txt");
        }

        if (prefs == null) {
            prefs = new Preferences(this);
        }

        btn2Players.setOnClickListener(v -> startGameActivity(2));
        btn4Players.setOnClickListener(v -> startGameActivity(4));
        btn6Players.setOnClickListener(v -> startGameActivity(6));

        btnHost.setOnClickListener(v -> startActivity(new Intent(this, HostRoomActivity.class)));
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
