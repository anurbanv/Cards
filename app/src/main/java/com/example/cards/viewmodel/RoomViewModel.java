package com.example.cards.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cards.App;
import com.example.cards.domain.Room;
import com.example.cards.domain.Save;
import com.example.cards.service.Preferences;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import javax.inject.Inject;

public class RoomViewModel extends AndroidViewModel {

    private final CollectionReference gamesRef;

    private MutableLiveData<Room> room = new MutableLiveData<>();

    private ListenerRegistration listener;
    private DocumentReference roomRef;

    private DeckViewModel deckViewModel;
    private PlayersViewModel playersViewModel;
    private BattleFieldViewModel battleFieldViewModel;
    @Inject Preferences preferences;

    public RoomViewModel(@NonNull Application application) {
        super(application);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        App.get().getAppComponent().inject(this);
        gamesRef = db.collection("games");
    }

    public LiveData<Room> getRoom() {
        return room;
    }

    public void setViewModels(DeckViewModel deckViewModel, PlayersViewModel playersViewModel,
                              BattleFieldViewModel battleFieldViewModel) {
        this.deckViewModel = deckViewModel;
        this.playersViewModel = playersViewModel;
        this.battleFieldViewModel = battleFieldViewModel;
    }

    public void initCloudObserver(String roomId) {
        if (listener != null) {
            listener.remove();
        }
        roomRef = gamesRef.document(roomId);
        listener = roomRef.addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null) {
                Room room = new Room(documentSnapshot);
                this.room.postValue(room);
            }
        });
    }

    public void removeListener() {
        if (listener != null) {
            listener.remove();
        }
    }

    public void setGameStarted(boolean started) {
        roomRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Room room = new Room(task.getResult());
                room.setGameStarted(started);
                if (started) {
                    String playerName = preferences.getPlayerName();
                    room.setGameState(new Save(2, room.getPlayers(), playerName));
                }
                roomRef.set(room.getObjectMap());
            }
        });
    }

    public void postGameState() {
        if (roomRef == null) {
            return;
        }
        roomRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Room room = new Room(task.getResult());
                Save save = new Save(deckViewModel, playersViewModel, battleFieldViewModel);
                room.setGameState(save);
                roomRef.set(room.getObjectMap());
            }
        });
    }
}
