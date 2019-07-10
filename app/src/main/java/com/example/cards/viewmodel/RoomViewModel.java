package com.example.cards.viewmodel;

import android.app.Application;

import com.example.cards.MainActivity;
import com.example.cards.domain.Room;
import com.example.cards.service.Preferences;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

    private FirebaseFirestore db;
    private Preferences prefs;

    public RoomViewModel(@NonNull Application application) {
        super(application);
        db = FirebaseFirestore.getInstance();
        prefs = MainActivity.prefs;
    }

    public LiveData<Room> getRoom() {
        return room;
    }

    public void createRoom(String roomId, Callback callback) {
        DocumentReference room = db.collection("games").document(roomId);

        checkIfRoomExists(room, exists -> {
            if (exists) {
                callback.onComplete(false);
            } else {
                List<String> playerNames = new ArrayList<>();
                playerNames.add("testas");
                Map<String, Object> game = new HashMap<>();
                game.put("players", playerNames);

                room.set(game).addOnCompleteListener(task -> {
                    callback.onComplete(task.isSuccessful());
                });
            }
        });

        room.addSnapshotListener((documentSnapshot, e) -> {
            List<String> players = (List<String>) documentSnapshot.get("players");
            if (players != null) {
                this.room.postValue(new Room(roomId, players));
            }
        });
    }

    private void checkIfRoomExists(DocumentReference room, Callback callback) {
        room.get().addOnCompleteListener(task -> {
            Object players = task.getResult().get("players");
            callback.onComplete(players != null);
        });
    }
}
