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

    public boolean isSavedSession() {
        return !getRoomId().isEmpty() && !getPlayerName().isEmpty();
    }

    public void setMultiPlayerMode(boolean on) {
        prefs.edit().putBoolean("multiPlayer", on).apply();
    }

    public boolean isMultiPlayerMode() {
        return prefs.getBoolean("multiPlayer", false);
    }

    public void setCardBackStyle(CardStyle cardStyle) {
        prefs.edit().putString("cardStyle", cardStyle.name()).apply();
    }

    public CardStyle getCardBackStyle() {
        return CardStyle.valueOf(prefs.getString("cardStyle", CardStyle.DEFAULT.name()));
    }
}
