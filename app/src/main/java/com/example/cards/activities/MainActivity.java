package com.example.cards.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cards.R;
import com.example.cards.service.Preferences;
import com.google.firebase.FirebaseApp;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn2Players) Button btn2Players;
    @BindView(R.id.btn4Players) Button btn4Players;
    @BindView(R.id.btn6Players) Button btn6Players;

    @BindView(R.id.btnMultiPlayer) Button btnMultiPlayer;

    public static File latestSave;
    public static Preferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FirebaseApp.initializeApp(this);

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

        btnMultiPlayer.setOnClickListener(v -> startActivity(new Intent(this, JoinRoomActivity.class)));
    }

    private void startGameActivity(int playerCount) {
        Intent intent = new Intent(this, GameActivity.class);
        prefs.setMultiPlayerMode(false);
        intent.putExtra("playerCount", playerCount);
        startActivity(intent);
    }
}
