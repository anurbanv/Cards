package com.example.cards.viewmodel;

import android.app.Application;

import com.example.cards.domain.Card;
import com.example.cards.domain.DeckOfCards;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DeckViewModel extends AndroidViewModel {

    private MutableLiveData<DeckOfCards> deck = new MutableLiveData<>();
    private MutableLiveData<List<Card>> outCards = new MutableLiveData<>();

    public DeckViewModel(@NonNull Application application) {
        super(application);
        deck.setValue(new DeckOfCards());
        outCards.setValue(new ArrayList<>());
    }

    public LiveData<DeckOfCards> getDeck() {
        return deck;
    }

    public Card getLastCard() {
        DeckOfCards value = deck.getValue();
        return value.getLastCard();
    }

    public LiveData<List<Card>> getOutCards() {
        return outCards;
    }

    public Card takeCard() {
        DeckOfCards value = deck.getValue();
        Card card = value.takeCard();
        deck.postValue(value);
        return card;
    }

    public void placeCardToOutDeck(Card card) {
        List<Card> value = outCards.getValue();
        value.add(card);
        outCards.postValue(value);
    }

    public boolean hasCards() {
        return !deck.getValue().isEmpty();
    }
}
