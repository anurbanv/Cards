package com.example.cards.domain;

import com.andrius.logutil.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private String roomId;
    private List<String> players;

    public Room(String roomId) {
        LogUtil.d("new room");
        this.roomId = roomId;
        this.players = new ArrayList<>();
    }

    public Room(String roomId, List<String> players) {
        LogUtil.d("new room  1");
        this.roomId = roomId;
        this.players = players;
    }

    public List<String> getPlayers() {
        return players;
    }
}
