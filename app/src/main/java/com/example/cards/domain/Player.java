package com.example.cards.domain;

import androidx.annotation.NonNull;

import com.example.cards.viewmodel.DeckViewModel;
import com.example.cards.viewmodel.PlayersViewModel;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public enum Action {
        ATTACK, DEFEND, NONE
    }

    private List<Card> hand;
    private int id;
    private PlayersViewModel playersViewModel;
    private DeckViewModel deckViewModel;
    private Action action = Action.NONE;
    private boolean isOut = false;

    public Player(int id, PlayersViewModel playersViewModel, DeckViewModel deckViewModel) {
        this.id = id;
        this.playersViewModel = playersViewModel;
        this.deckViewModel = deckViewModel;
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
        playersViewModel.updatePlayers();
    }

    public void removeCard(Card card) {
        hand.remove(card);
        if (cannotPlay()) {
            playersViewModel.attackingPlayerOut(this);
            setOut();
        }
        playersViewModel.updatePlayers();
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        if (this.action != action) {
            this.action = action;
            playersViewModel.updatePlayers();
        }
    }

    public boolean cannotPlay() {
        return hand.isEmpty() && !deckViewModel.hasCards();
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
