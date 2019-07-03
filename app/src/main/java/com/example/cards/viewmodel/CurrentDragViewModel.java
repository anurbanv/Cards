package com.example.cards.viewmodel;

import android.app.Application;

import com.example.cards.domain.Card;
import com.example.cards.domain.Player;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class CurrentDragViewModel extends AndroidViewModel {

    private Card currentDrag;
    private Player owner;

    public CurrentDragViewModel(@NonNull Application application) {
        super(application);
    }

    public void setCurrentDrag(Card card, Player owner) {
        currentDrag = card;
        this.owner = owner;
    }

    public Card getCurrentDrag() {
        return currentDrag;
    }

    public Player getCardOwner() {
        return owner;
    }
}
