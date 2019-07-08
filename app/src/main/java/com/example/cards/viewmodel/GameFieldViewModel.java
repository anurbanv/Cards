package com.example.cards.viewmodel;

import android.app.Application;

import com.example.cards.domain.Card;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class GameFieldViewModel extends AndroidViewModel {

    private MutableLiveData<Card[]> attackingCards = new MutableLiveData<>();
    private MutableLiveData<Card[]> defendingCards = new MutableLiveData<>();

    public GameFieldViewModel(@NonNull Application application) {
        super(application);
        reset();
    }

    public MutableLiveData<Card[]> getAttackingCards() {
        return attackingCards;
    }

    public MutableLiveData<Card[]> getDefendingCards() {
        return defendingCards;
    }

    public void reset() {
        attackingCards.setValue(new Card[6]);
        defendingCards.setValue(new Card[6]);
    }

    public Card getAttackCardAtIndex(int index) {
        Card[] value = attackingCards.getValue();
        return value[index];
    }

    public List<Card> getAttackingCardList() {
        List<Card> cards = new ArrayList<>();
        Card[] value = attackingCards.getValue();
        for (Card card : value) {
            if (card != null) {
                cards.add(card);
            }
        }
        return cards;
    }

    public List<Card> getDefendingCardList() {
        List<Card> cards = new ArrayList<>();
        Card[] value = defendingCards.getValue();
        for (Card card : value) {
            if (card != null) {
                cards.add(card);
            }
        }
        return cards;
    }

    public void setAttackingCard(Card card, int index) {
        Card[] value = attackingCards.getValue();
        value[index] = card;
        attackingCards.postValue(value);
    }

    public void setDefendingCard(Card card, int index) {
        Card[] value = defendingCards.getValue();
        value[index] = card;
        defendingCards.postValue(value);
    }

    public List<Card> removeAllCardsFromField() {
        List<Card> list = new ArrayList<>();
        list.addAll(getAttackingCardList());
        list.addAll(getDefendingCardList());
        attackingCards.postValue(new Card[6]);
        defendingCards.postValue(new Card[6]);
        return list;
    }
}
