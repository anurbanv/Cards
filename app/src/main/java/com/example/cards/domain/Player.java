package com.example.cards.domain;

import com.example.cards.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private List<Card> hand;
    private int id;
    private PlayerState state = PlayerState.NONE;
    private boolean isOut = false;

    public Player(int id) {
        this.id = id;
        hand = new ArrayList<>();
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getId() {
        return id;
    }

    public Card lookAtCard(int index) {
        return hand.get(index);
    }

    public void addCardToHand(Card card) {
        hand.add(card);
        MainActivity.playersViewModel.updatePlayer(this);
    }

    public void removeCard(Card card) {
        hand.remove(card);
        MainActivity.playersViewModel.updatePlayer(this);
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
        MainActivity.playersViewModel.updatePlayer(this);
    }

    public void checkIfOut() {
        if (hand.isEmpty() && !MainActivity.deckViewModel.hasCards()) {
            isOut = true;
            MainActivity.playersViewModel.updatePlayer(this);
        }
    }

    public boolean isOut() {
        return isOut;
    }
}
