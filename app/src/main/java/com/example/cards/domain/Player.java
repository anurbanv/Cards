package com.example.cards.domain;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public enum Action {
        ATTACK, DEFEND, NONE
    }

    private List<Card> hand;
    private int id;
    private Action action = Action.NONE;
    private boolean isOut = false;
    private String name;

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
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        if (this.action != action) {
            this.action = action;
        }
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut() {
        isOut = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Player{" +
                ", id=" + id +
                ", action=" + action +
                ", isOut=" + isOut +
                ", name='" + name + '\'' +
                '}';
    }
}
