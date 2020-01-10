package com.example.cards.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cards.domain.Room;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class NewRoomViewModel extends AndroidViewModel {

    private final CollectionReference gamesRef;

    private MutableLiveData<Room> room = new MutableLiveData<>();

    private ListenerRegistration listener;

    public NewRoomViewModel(@NonNull Application application) {
        super(application);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        gamesRef = db.collection("games");
    }

    public LiveData<Room> getRoom() {
        return room;
    }

    public void initCloudObserver(String roomId) {
        if (listener != null) {
            listener.remove();
        }
        DocumentReference roomRef = gamesRef.document(roomId);
        listener = roomRef.addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null) this.room.postValue(new Room(documentSnapshot));
        });
    }

    public void removeListener() {
        if (listener != null) {
            listener.remove();
        }
    }
}
