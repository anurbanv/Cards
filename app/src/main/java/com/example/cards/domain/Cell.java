package com.example.cards.domain;

public class Cell {

    private Card attackCard;
    private Card defendCard;


    public Card getAttackCard() {
        return attackCard;
    }

    public void setAttackCard(Card attackCard) {
        this.attackCard = attackCard;
    }

    public Card getDefendCard() {
        return defendCard;
    }

    public void setDefendCard(Card defendCard) {
        this.defendCard = defendCard;
    }
}
