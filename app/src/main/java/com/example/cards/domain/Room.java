package com.example.cards.domain;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {

    private String roomId;
    private List<String> players;
    private boolean started = false;
    private String gameState;

    public Room(DocumentSnapshot result) {
        List<String> players = (List<String>) result.get("players");
        if (players == null) players = new ArrayList<>();
        Boolean started = (Boolean) result.get("started");
        if (started == null) started = false;
        String gameState = (String) result.get("gameState");
        if (gameState == null) gameState = "";
        this.players = players;
        this.started = started;
        this.gameState = gameState;
    }

    public List<String> getPlayers() {
        return players;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setGameStarted(boolean started) {
        this.started = started;
    }

    public void setStarted() {
        started = true;
    }

    public boolean isStarted() {
        return started;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public boolean playerExists(String playerName) {
        for (String player : players) {
            if (player.equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    public void removePlayer(String playerName) {
        for (String player : players) {
            if (player.equals(playerName)) {
                players.remove(player);
                return;
            }
        }
    }

    public Map<String, Object> getObjectMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("players", players);
        result.put("started", started);
        result.put("gameState", gameState);
        return result;
    }

    public void setGameState(Save save) {
        this.gameState = save.getJsonString();
    }
}
