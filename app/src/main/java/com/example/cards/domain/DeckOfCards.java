package com.example.cards.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class DeckOfCards {

    private Stack<Card> cardStack;
    private Card lastCard;

    public DeckOfCards() {
        cardStack = new Stack<>();
        List<Card> cardList = new ArrayList<>();
        for (Suite value : Suite.values()) {
            for (int i = 6; i < 10; i++) {
                Card card = new Card(value, i);
                cardList.add(card);
            }
        }
        Collections.shuffle(cardList);
        lastCard = cardList.get(0);
        for (Card card : cardList) {
            if (card.getSuite() == lastCard.getSuite()) {
                card.setStrong(true);
            }
            cardStack.push(card);
        }
    }

    public int cardCount() {
        return cardStack.size();
    }

    public Card takeCard() {
        return cardStack.isEmpty() ? null : cardStack.pop();
    }

    public Card getLastCard() {
        return lastCard;
    }

    public boolean isEmpty() {
        return cardStack.isEmpty();
    }
}
