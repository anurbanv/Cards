package com.example.cards.domain;

import java.util.List;

public class Room {

    private String roomId;
    private List<String> players;

    public Room(String roomId, List<String> players) {
        this.roomId = roomId;
        this.players = players;
    }

    public List<String> getPlayers() {
        return players;
    }

    public String getRoomId() {
        return roomId;
    }
}
