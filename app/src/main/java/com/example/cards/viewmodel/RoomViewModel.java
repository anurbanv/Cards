package com.example.cards.viewmodel;

import android.app.Application;

import com.example.cards.domain.Room;
import com.example.cards.service.Preferences;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class RoomViewModel extends AndroidViewModel {

    public interface Callback {
        void onComplete(boolean created);
    }

    private MutableLiveData<Room> room = new MutableLiveData<>();

    private ListenerRegistration listener;

    private FirebaseFirestore db;
    private Preferences prefs;

    public RoomViewModel(@NonNull Application application) {
        super(application);
        db = FirebaseFirestore.getInstance();
        prefs = new Preferences(application);
    }

    public LiveData<Room> getRoom() {
        return room;
    }

    public void initCloudObserver(String roomId) {
        if (listener != null) {
            listener.remove();
        }
        DocumentReference room = db.collection("games").document(roomId);
        listener = room.addSnapshotListener((documentSnapshot, e) -> {
            List<String> players = (List<String>) documentSnapshot.get("players");
            Boolean started = (Boolean) documentSnapshot.get("started");
            if (players != null) {
                Room room1 = new Room(roomId, players);
                if (started != null && started) {
                    room1.setStarted();
                }
                this.room.postValue(room1);
            } else {
                this.room.postValue(null);
            }
        });
    }

    public void joinRoom(String roomId, String playerName, Callback callback) {
        if (roomId.length() < 4 || playerName.trim().isEmpty()) {
            callback.onComplete(false);
            return;
        }

        DocumentReference room = db.collection("games").document(roomId);
        room.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> players = (List<String>) task.getResult().get("players");

                if (players == null) {
                    players = new ArrayList<>();
                }

                if (players.size() >= 2) {
                    callback.onComplete(false);
                    return;
                }

                players.add(playerName);

                Map<String, Object> game = new HashMap<>();
                game.put("players", players);

                initCloudObserver(roomId);

                room.set(game).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        prefs.saveRoomSession(roomId, playerName);
                        callback.onComplete(true);
                    } else {
                        callback.onComplete(false);
                    }
                });
            } else {
                callback.onComplete(false);
            }
        });
    }

    public void leaveRoom(Callback callback) {
        String roomId = prefs.getRoomId();
        String playerName = prefs.getPlayerName();

        if (roomId.isEmpty() || playerName.isEmpty()) {
            callback.onComplete(false);
            return;
        }

        DocumentReference room = db.collection("games").document(roomId);
        room.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> players = (List<String>) task.getResult().get("players");
                players.remove(playerName);

                if (players.isEmpty()) {
                    room.delete();
                } else {
                    Map<String, Object> game = new HashMap<>();
                    game.put("players", players);

                    room.set(game).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            prefs.saveRoomSession(roomId, playerName);
                        }
                    });
                }
                prefs.removeStoredSession();
                this.room.postValue(null);
                listener.remove();
                callback.onComplete(true);
            } else {
                callback.onComplete(false);
            }
        });
    }

    public boolean restoreState() {
        if (!prefs.getRoomId().isEmpty()) {
            initCloudObserver(prefs.getRoomId());
            return true;
        }
        return false;
    }

    public void startGame() {
        String roomId = prefs.getRoomId();

        if (roomId.isEmpty()) {
            return;
        }

        DocumentReference room = db.collection("games").document(roomId);
        room.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> players = (List<String>) task.getResult().get("players");
                Map<String, Object> game = new HashMap<>();
                game.put("players", players);
                game.put("started", true);
                room.set(game);
            }
        });
    }

    public boolean isStarted() {
        if (room != null) {
            Room value = room.getValue();
            return value.isStarted();
        }
        return false;
    }
}
