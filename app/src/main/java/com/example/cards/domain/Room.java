package com.example.cards.domain;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {

    private List<String> players;
    private boolean started;
    private String gameState;
    private String hostName;

    @SuppressWarnings("unchecked")
    public Room(DocumentSnapshot result) {
        List<String> players = (List<String>) result.get("players");
        if (players == null) players = new ArrayList<>();
        Boolean started = (Boolean) result.get("started");
        if (started == null) started = false;
        String gameState = (String) result.get("gameState");
        if (gameState == null) gameState = "";
        String hostName = (String) result.get("hostName");
        if (hostName == null) hostName = "";
        this.players = players;
        this.started = started;
        this.gameState = gameState;
        this.hostName = hostName;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void addPlayer(String playerName) {
        players.add(playerName);
        if (players.size() == 1) {
            hostName = playerName;
        }
    }

    public void setGameStarted(boolean started) {
        this.started = started;
    }

    public boolean isStarted() {
        return started;
    }

    public String getGameState() {
        return gameState;
    }

    public String getHostName() {
        return hostName;
    }

    public void changeHost() {
        this.hostName = players.get(0);
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
        result.put("hostName", hostName);
        return result;
    }

    public void setGameState(Save save) {
        this.gameState = save.getJsonString();
    }
}
