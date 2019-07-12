package com.example.cards.domain;

import com.example.cards.MainActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class Player {

    public enum Action {
        ATTACK, DEFEND, NONE
    }

    private List<Card> hand;
    private int id;
    private Action action = Action.NONE;
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
        MainActivity.playersViewModel.updatePlayers();
    }

    public void removeCard(Card card) {
        hand.remove(card);
        if (cannotPlay()) {
            MainActivity.playersViewModel.attackingPlayerOut(this);
            setOut();
        }
        MainActivity.playersViewModel.updatePlayers();
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        if (this.action != action) {
            this.action = action;
            MainActivity.playersViewModel.updatePlayers();
        }
    }

    public boolean cannotPlay() {
        return hand.isEmpty() && !MainActivity.deckViewModel.hasCards();
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut() {
        isOut = true;
    }


    @NonNull
    @Override
    public String toString() {
        return "Player{" +
                ", id=" + id +
                ", action=" + action +
                ", isOut=" + isOut +
                '}';
    }
}
