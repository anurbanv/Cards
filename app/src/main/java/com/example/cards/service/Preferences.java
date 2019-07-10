package com.example.cards.service;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private SharedPreferences prefs;

    public Preferences(Context context) {
        String PREFERENCES_FILE_NAME = "cards";
        prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setHostedRoomId(String roomId) {
        prefs.edit().putString("roomId", roomId).apply();
    }

    public String getHostedRoomId() {
        return prefs.getString("roomId", "");
    }

    public void setPlayerName(String playerName) {
        prefs.edit().putString("playerName", playerName).apply();
    }

    public String getPlayerName() {
        return prefs.getString("playerName", "");
    }
}
