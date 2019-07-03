package com.example.cards.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class DeckOfCards {

    private Stack<Card> cardStack;
    private Card strong;

    public DeckOfCards() {
        cardStack = new Stack<>();
        List<Card> cardList = new ArrayList<>();
        for (Suite value : Suite.values()) {
            for (int i = 6; i < 15; i++) {
                Card card = new Card(value, i);
                cardList.add(card);
            }
        }
        Collections.shuffle(cardList);
        strong = cardList.get(0);
        for (Card card : cardList) {
            if (card.getSuite() == strong.getSuite()) {
                card.setStrong(true);
            }
            cardStack.push(card);
        }
    }

    public int cardCount() {
        return cardStack.size();
    }

    public Card takeCard() {
        return cardStack.pop();
    }

    public Suite getStrong() {
        return strong.getSuite();
    }

    public Card getLastCard() {
        return cardStack.get(0);
    }


    public boolean isEmpty() {
        return cardStack.isEmpty();
    }
}
