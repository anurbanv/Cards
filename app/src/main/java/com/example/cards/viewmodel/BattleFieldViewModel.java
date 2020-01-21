package com.example.cards.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cards.domain.Card;
import com.example.cards.domain.Cell;

import java.util.ArrayList;
import java.util.List;

public class BattleFieldViewModel extends AndroidViewModel {

    private MutableLiveData<List<Cell>> cells = new MutableLiveData<>();

    public BattleFieldViewModel(@NonNull Application application) {
        super(application);
        reset();
    }

    public LiveData<List<Cell>> getCells() {
        return cells;
    }

    private void reset() {
        List<Cell> cellList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            cellList.add(new Cell());
        }
        cells.setValue(cellList);
    }

    public void postFieldState(List<Card> attackCards, List<Card> defendCards) {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Cell cell = new Cell();
            cell.setAttackCard(attackCards.get(i));
            cell.setDefendCard(defendCards.get(i));
            cells.add(cell);
        }
        this.cells.postValue(cells);
    }

    public List<Card> getAttackCards() {
        List<Card> cards = new ArrayList<>();
        List<Cell> value = cells.getValue();
        if (value != null) {
            for (Cell cell : value) {
                cards.add(cell.getAttackCard());
            }
        }
        return cards;
    }

    public List<Card> getDefendCards() {
        List<Card> cards = new ArrayList<>();
        List<Cell> value = cells.getValue();
        if (value != null) {
            for (Cell cell : value) {
                cards.add(cell.getDefendCard());
            }
        }
        return cards;
    }

    public List<Card> getAttackingCardList() {
        List<Card> cards = new ArrayList<>();
        List<Cell> value = cells.getValue();
        if (value != null) {
            for (Cell cell : value) {
                if (cell.getAttackCard() != null) {
                    cards.add(cell.getAttackCard());
                }
            }
        }
        return cards;
    }

    public List<Card> getDefendingCardList() {
        List<Card> cards = new ArrayList<>();
        List<Cell> value = cells.getValue();
        if (value != null) {
            for (Cell cell : value) {
                if (cell.getDefendCard() != null) {
                    cards.add(cell.getDefendCard());
                }
            }
        }
        return cards;
    }

    public void setAttackingCard(Card card, int index) {
        List<Cell> value = cells.getValue();
        if (value != null) {
            value.get(index).setAttackCard(card);
            cells.postValue(value);
        }
    }

    public void setDefendingCard(Card card, int index) {
        List<Cell> value = cells.getValue();
        if (value != null) {
            value.get(index).setDefendCard(card);
            cells.postValue(value);
        }
    }

    public Card getAttackCardAtIndex(int index) {
        List<Cell> value = cells.getValue();
        if (value != null) {
            return value.get(index).getAttackCard();
        }
        return null;
    }

    public List<Card> removeAllCardsFromField() {
        List<Card> list = new ArrayList<>();
        list.addAll(getAttackingCardList());
        list.addAll(getDefendingCardList());
        reset();
        return list;
    }
}
