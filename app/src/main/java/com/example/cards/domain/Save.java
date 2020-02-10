package com.example.cards.domain;

import com.andrius.fileutil.FileUtil;
import com.example.cards.activities.MainActivity;
import com.example.cards.service.PlayersService;
import com.example.cards.viewmodel.BattleFieldViewModel;
import com.example.cards.viewmodel.DeckViewModel;
import com.example.cards.viewmodel.PlayersViewModel;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Save {

    private static transient Gson gson = new Gson();
    private DeckOfCards deckOfCards;
    private List<Card> outCards;
    private List<Player> players;
    private List<Card> attackCards;
    private List<Card> defendCards;

    public static Save getStoredSave() {
        if (MainActivity.latestSave == null) {
            throw new IllegalArgumentException("no save");
        }
        String jsonString = FileUtil.readFileText(MainActivity.latestSave);
        return gson.fromJson(jsonString, Save.class);
    }

    public static Save getSaveFromJson(String json) {
        return gson.fromJson(json, Save.class);
    }

    public Save(DeckOfCards deckOfCards, List<Card> outCards, List<Player> players,
                List<Card> attackCards, List<Card> defendCards) {
        this.deckOfCards = deckOfCards;
        this.outCards = outCards;
        this.players = players;
        this.attackCards = attackCards;
        this.defendCards = defendCards;
    }

    public Save(DeckViewModel deckViewModel, PlayersViewModel playersViewModel,
                BattleFieldViewModel battleFieldViewModel) {
        this.deckOfCards = deckViewModel.getDeck().getValue();
        this.outCards = deckViewModel.getOutCards().getValue();
        this.players = playersViewModel.getPlayers().getValue();
        this.attackCards = battleFieldViewModel.getAttackCards();
        this.defendCards = battleFieldViewModel.getDefendCards();
    }

    public Save(int playerCount, List<String> playerNames, String playerName) {
        DeckOfCards deckOfCards = new DeckOfCards();
        this.deckOfCards = deckOfCards;
        this.outCards = new ArrayList<>();
        PlayersService playersService = new PlayersService();
        List<Player> players = playersService.getNewPlayersList(playerCount);
        playersService.fillPlayersHands(players, deckOfCards);
        playersService.setPlayerNames(players, playerNames);
        playersService.cyclePlayersToPosition(players, playerName);
        Player attacker = playersService.getPlayerWithLowestStrongCard(players);
        Player defender = playersService.getNextPlayerInGame(players, attacker);
        playersService.setDefendingPlayer(players, defender);
        this.players = players;
        this.attackCards = new ArrayList<>(Collections.nCopies(6, null));
        this.defendCards = new ArrayList<>(Collections.nCopies(6, null));
    }

    public void saveToFileSystem() {
        File file = MainActivity.latestSave;
        FileUtil.deleteFile(file);
        FileUtil.createFile(file);
        FileUtil.writeText(file, getJsonString());
    }

    public String getJsonString() {
        return gson.toJson(this);
    }

    public void restoreState(DeckViewModel deckViewModel, PlayersViewModel playersViewModel,
                             BattleFieldViewModel battleFieldViewModel, String playerName) {
        PlayersService playersService = new PlayersService();
        deckViewModel.getDeck().postValue(deckOfCards);
        deckViewModel.getOutCards().postValue(outCards);
        playersService.cyclePlayersToPosition(players, playerName);
        playersViewModel.getPlayers().postValue(players);
        battleFieldViewModel.postFieldState(attackCards, defendCards);
    }
}
