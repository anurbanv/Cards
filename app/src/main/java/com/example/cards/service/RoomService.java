package com.example.cards.service;

import com.example.cards.domain.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RoomService {

    private final CollectionReference gamesRef;

    public interface JoinCallback {
        void onComplete(boolean success);
    }

    public interface LeaveCallback {
        void onComplete(boolean success);
    }

    private String playerName = null;
    private String roomId = null;

    public RoomService() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        gamesRef = db.collection("games");
    }

    public void joinRoom(String playerName, String roomId, JoinCallback callback) {
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

                room.getPlayers().add(playerName);

                roomRef.set(room.getObjectMap()).addOnCompleteListener(setTask -> {
                    if (setTask.isSuccessful()) {
                        this.playerName = playerName;
                        this.roomId = roomId;
                    }
                    callback.onComplete(setTask.isSuccessful());
                });
            } else {
                callback.onComplete(false);
            }
        });
    }

    public void leaveRoom(LeaveCallback callback) {
        if (playerName == null || roomId == null) {
            callback.onComplete(false);
            return;
        }
        DocumentReference roomRef = gamesRef.document(roomId);
        roomRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Room room = new Room(task.getResult());
                room.removePlayer(playerName);

                OnCompleteListener<Void> listener = task1 -> {
                    if (task1.isSuccessful()) {
                        this.roomId = null;
                    }
                    callback.onComplete(task1.isSuccessful());
                };

                if (room.getPlayers().isEmpty()) {
                    roomRef.delete().addOnCompleteListener(listener);
                } else {
                    roomRef.set(room.getObjectMap()).addOnCompleteListener(listener);
                }
            } else {
                callback.onComplete(false);
            }
        });
    }

    public boolean isInputValid(String playerName, String roomId) {
        playerName = playerName.trim();
        roomId = roomId.trim();

        if (playerName.isEmpty() || roomId.isEmpty()) {
            return false;
        }

        return playerName.length() >= 4 && roomId.length() >= 4;
    }
}
