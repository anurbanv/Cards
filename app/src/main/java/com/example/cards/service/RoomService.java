package com.example.cards.service;

import com.example.cards.App;
import com.example.cards.domain.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

public class RoomService {

    private final CollectionReference gamesRef;
    @Inject Preferences preferences;

    public interface JoinCallback {
        void onComplete(boolean success);
    }

    public interface LeaveCallback {
        void onComplete(boolean success);
    }

    public RoomService() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        App.get().getAppComponent().inject(this);
        gamesRef = db.collection("games");
    }

    public void joinRoom(String roomId, String playerName, JoinCallback callback) {
        if (preferences.isSavedSession()) {
            if (preferences.getRoomId().equals(roomId) && preferences.getPlayerName().equals(playerName)) {
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
                        preferences.saveRoomSession(roomId, playerName);
                    }
                    callback.onComplete(setTask.isSuccessful());
                });
            } else {
                callback.onComplete(false);
            }
        });
    }

    public void reJoinRoom(JoinCallback callback) {
        if (!preferences.isSavedSession()) {
            callback.onComplete(false);
            return;
        }

        String roomId = preferences.getRoomId();
        String playerName = preferences.getPlayerName();

        DocumentReference roomRef = gamesRef.document(roomId);
        roomRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Room room = new Room(task.getResult());

                if (room.playerExists(playerName)) {
                    callback.onComplete(true);
                } else {
                    preferences.removeStoredSession();
                    callback.onComplete(false);
                }
            } else {
                callback.onComplete(false);
            }
        });
    }

    public void leaveRoom(LeaveCallback callback) {
        if (!preferences.isSavedSession()) {
            callback.onComplete(false);
            return;
        }

        DocumentReference roomRef = gamesRef.document(preferences.getRoomId());
        roomRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Room room = new Room(task.getResult());
                room.removePlayer(preferences.getPlayerName());

                OnCompleteListener<Void> listener = task1 -> {
                    if (task1.isSuccessful()) {
                        preferences.removeStoredSession();
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
