package com.example.cards.service;

import android.content.Context;

import com.example.cards.domain.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RoomService {

    private final CollectionReference gamesRef;
    private final Preferences prefs;

    public interface JoinCallback {
        void onComplete(boolean success);
    }

    public interface LeaveCallback {
        void onComplete(boolean success);
    }

    public RoomService(Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        gamesRef = db.collection("games");
        prefs = new Preferences(context);
    }

    public void joinRoom(String roomId, String playerName, JoinCallback callback) {
        if (prefs.isSavedSession()) {
            if (prefs.getRoomId().equals(roomId) && prefs.getPlayerName().equals(playerName)) {
                callback.onComplete(true);
                return;
            }
        }

        if (!isInputValid(playerName, roomId)) {
            callback.onComplete(false);
            return;
        }

        DocumentReference roomRef = gamesRef.document(roomId);
        roomRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Room room = new Room(task.getResult());

                if (room.getPlayers().size() >= 2 || room.playerExists(playerName)) {
                    callback.onComplete(false);
                    return;
                }

                room.addPlayer(playerName);

                roomRef.set(room.getObjectMap()).addOnCompleteListener(setTask -> {
                    if (setTask.isSuccessful()) {
                        prefs.saveRoomSession(roomId, playerName);
                    }
                    callback.onComplete(setTask.isSuccessful());
                });
            } else {
                callback.onComplete(false);
            }
        });
    }

    public void reJoinRoom(JoinCallback callback) {
        if (!prefs.isSavedSession()) {
            callback.onComplete(false);
            return;
        }

        String roomId = prefs.getRoomId();
        String playerName = prefs.getPlayerName();

        DocumentReference roomRef = gamesRef.document(roomId);
        roomRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Room room = new Room(task.getResult());

                if (room.playerExists(playerName)) {
                    callback.onComplete(true);
                } else {
                    prefs.removeStoredSession();
                    callback.onComplete(false);
                }
            } else {
                callback.onComplete(false);
            }
        });
    }

    public void leaveRoom(LeaveCallback callback) {
        if (!prefs.isSavedSession()) {
            callback.onComplete(false);
            return;
        }

        DocumentReference roomRef = gamesRef.document(prefs.getRoomId());
        roomRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Room room = new Room(task.getResult());
                room.removePlayer(prefs.getPlayerName());

                OnCompleteListener<Void> listener = task1 -> {
                    if (task1.isSuccessful()) {
                        prefs.removeStoredSession();
                    }
                    callback.onComplete(task1.isSuccessful());
                };

                if (room.getPlayers().isEmpty()) {
                    roomRef.delete().addOnCompleteListener(listener);
                } else {
                    room.changeHost();
                    roomRef.set(room.getObjectMap()).addOnCompleteListener(listener);
                }
            } else {
                callback.onComplete(false);
            }
        });
    }

    private boolean isInputValid(String playerName, String roomId) {
        playerName = playerName.trim();
        roomId = roomId.trim();

        if (playerName.isEmpty() || roomId.isEmpty()) {
            return false;
        }

        return playerName.length() >= 4 && roomId.length() >= 4;
    }
}
