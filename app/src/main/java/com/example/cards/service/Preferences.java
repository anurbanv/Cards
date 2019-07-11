package com.example.cards.service;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private SharedPreferences prefs;

    public Preferences(Context context) {
        String PREFERENCES_FILE_NAME = "cards";
        prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void saveRoomSession(String roomId, String playerName) {
        prefs.edit().putString("roomId", roomId).putString("playerName", playerName).apply();
    }

    public void removeStoredSession() {
        prefs.edit().putString("roomId", "").putString("playerName", "").apply();
    }

    public String getRoomId() {
        return prefs.getString("roomId", "");
    }

    public String getPlayerName() {
        return prefs.getString("playerName", "");
    }
}
