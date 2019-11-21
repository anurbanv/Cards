package com.example.cards.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cards.domain.Room;
import com.example.cards.domain.Save;
import com.example.cards.service.Preferences;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class RoomViewModel extends AndroidViewModel {

    public interface Callback {
        void onComplete(boolean created, String message);
    }

    private MutableLiveData<Room> room = new MutableLiveData<>();

    private ListenerRegistration listener;

    private CollectionReference gamesRef;
    private Preferences prefs;

    public RoomViewModel(@NonNull Application application) {
        super(application);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        gamesRef = db.collection("games");
        prefs = new Preferences(application);
        room.postValue(null);
    }

    public LiveData<Room> getRoom() {
        return room;
    }

    private void initCloudObserver(String roomId) {
        if (listener != null) {
            listener.remove();
        }
        DocumentReference roomRef = gamesRef.document(roomId);
        listener = roomRef.addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null) this.room.postValue(new Room(documentSnapshot));
        });
    }

    public void joinRoom(String roomId, String playerName, Callback callback) {
        if (roomId.length() < 4 ) {
            callback.onComplete(false, "Room id must be 4 or longer");
            return;
        }

        if (playerName.trim().isEmpty()) {
            callback.onComplete(false, "Player name cannot be empty");
            return;
        }

        DocumentReference roomRef = gamesRef.document(roomId);
        roomRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Room room = new Room(task.getResult());

                if (room.getPlayers().size() >= 2) {
                    callback.onComplete(false, "current max players is 2");
                    return;
                }

                if (room.playerExists(playerName)) {
                    callback.onComplete(false, "player name already exists");
                    return;
                }

                room.getPlayers().add(playerName);

                initCloudObserver(roomId);

                roomRef.set(room.getObjectMap()).addOnCompleteListener(task1 -> {
                    callback.onComplete(true, "Room");
                });
            } else {
                callback.onComplete(false, "Failed to retrieve game data");
            }
        });
    }

    public void leaveRoom(String roomId, String playerName, Callback callback) {

        Room value = room.getValue();
        if (value == null) {
            callback.onComplete(false, "ds");
            return;
        }

        DocumentReference roomRef = gamesRef.document(roomId);
        roomRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Room room = new Room(task.getResult());
                room.removePlayer(playerName);

                if (room.getPlayers().isEmpty()) {
                    roomRef.delete();
                } else {
                    roomRef.set(room.getObjectMap());
                }

                if (listener != null) listener.remove();
                this.room.postValue(null);
            }
            callback.onComplete(task.isSuccessful(), "D");
        });
    }

    public void restoreRoom(Callback callback) {
        String roomId = prefs.getRoomId();

        if (roomId.isEmpty()) {
            callback.onComplete(false, "");
            return;
        }

        DocumentReference roomRef = gamesRef.document(roomId);
        roomRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Room room = new Room(task.getResult());
                String playerName = prefs.getPlayerName();
                if (room.playerExists(playerName)) {
                    initCloudObserver(roomId);
                    this.room.postValue(room);
                }
            }
            callback.onComplete(task.isSuccessful(), "d");
        });
    }

    public void startGame(Callback callback) {
        String roomId = prefs.getRoomId();

        if (roomId.isEmpty()) {
            callback.onComplete(false, "no room id");
            return;
        }

        DocumentReference roomRef = gamesRef.document(roomId);
        roomRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Room room = new Room(task.getResult());
                room.setStarted();
                roomRef.set(room.getObjectMap());
            }
        });
    }

    public void postGameState(Callback callback) {
        String roomId = prefs.getRoomId();

        if (roomId.isEmpty()) {
            callback.onComplete(false, "no room id");
            return;
        }

        DocumentReference roomRef = gamesRef.document(roomId);
        roomRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Room room = new Room(task.getResult());
                room.setGameState(Save.getGameState());
                roomRef.set(room.getObjectMap());
            }
        });
    }
}
