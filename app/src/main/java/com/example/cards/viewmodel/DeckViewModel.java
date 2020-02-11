package com.example.cards.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.cards.domain.Card;
import com.example.cards.domain.DeckOfCards;

import java.util.ArrayList;
import java.util.List;

public class DeckViewModel extends AndroidViewModel {

    private MutableLiveData<DeckOfCards> deck = new MutableLiveData<>();
    private MutableLiveData<List<Card>> outCards = new MutableLiveData<>();

    public DeckViewModel(@NonNull Application application) {
        super(application);
        reset();
    }

    public MutableLiveData<DeckOfCards> getDeck() {
        return deck;
    }

    private void reset() {
        deck.setValue(new DeckOfCards());
        outCards.setValue(new ArrayList<>());
    }

    public MutableLiveData<List<Card>> getOutCards() {
        return outCards;
    }

    public Card takeCard() {
        DeckOfCards value = deck.getValue();
        if (value != null) {
            Card card = value.takeCard();
            deck.postValue(value);
            return card;
        }
        return null;
    }

    public void placeCardToOutDeck(Card card) {
        List<Card> value = outCards.getValue();
        if (value != null) {
            value.add(card);
            outCards.postValue(value);
        }
    }

    public boolean hasCards() {
        DeckOfCards value = deck.getValue();
        if (value != null) {
            return !value.isEmpty();
        }
        return false;
    }

    public DeckOfCards getDeckOfCards() {
        return deck.getValue();
    }
}
