package com.example.cards.domain;

import com.andrius.fileutil.FileUtil;
import com.example.cards.MainActivity;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import static com.example.cards.MainActivity.deckViewModel;
import static com.example.cards.MainActivity.gameFieldViewModel;
import static com.example.cards.MainActivity.playersViewModel;

public class Save {

    private static Gson gson = new Gson();
    private DeckOfCards deckOfCards;
    private List<Card> outCards;
    private List<Player> players;
    private Card[] attacking;
    private Card[] defending;

    public static void restoreFromFileSystem() {
        String jsonString = FileUtil.readFileText(MainActivity.latestSave);
        Save save = gson.fromJson(jsonString, Save.class);
        save.restoreState();
    }

    public static void saveToFileSystem() {
        Save save = new Save();
        String jsonString = gson.toJson(save);
        File file = MainActivity.latestSave;
        FileUtil.createFile(file);
        FileUtil.writeText(file, jsonString);
    }

    public static void restoreFromJsonString(String json) {
        Save save = gson.fromJson(json, Save.class);
        save.restoreState();
    }

    private Save() {
        deckOfCards = deckViewModel.getDeck().getValue();
        outCards = deckViewModel.getOutCards().getValue();
        players = playersViewModel.getPlayers().getValue();
        attacking = gameFieldViewModel.getAttackingCards().getValue();
        defending = gameFieldViewModel.getDefendingCards().getValue();
    }

    private void restoreState() {
        deckViewModel.getDeck().setValue(deckOfCards);
        deckViewModel.getOutCards().setValue(outCards);
        playersViewModel.getPlayers().setValue(players);
        gameFieldViewModel.getAttackingCards().setValue(attacking);
        gameFieldViewModel.getDefendingCards().setValue(defending);
    }
}
