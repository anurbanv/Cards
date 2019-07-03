package com.example.cards.domain;

public class Card {

    private Suite suite;
    private int number;
    private boolean strong;

    public Card(Suite suite, int number) {
        this.suite = suite;
        this.number = number;
    }

    public Suite getSuite() {
        return suite;
    }

    public int getNumber() {
        return number;
    }

    public String getNumberText() {
        switch (number) {
            case 11:
                return "J";
            case 12:
                return "Q";
            case 13:
                return "K";
            case 14:
                return "A";
            default:
                return String.valueOf(number);
        }
    }

    public boolean isStrong() {
        return strong;
    }

    public void setStrong(boolean strong) {
        this.strong = strong;
    }

    @Override
    public String toString() {
        return "Card{" +
                "suite=" + suite +
                ", number=" + number +
                ", strong=" + strong +
                '}';
    }
}
