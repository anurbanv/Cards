package com.example.cards.viewmodel;

import android.app.Application;

import com.andrius.logutil.LogUtil;
import com.example.cards.MainActivity;
import com.example.cards.domain.Player;
import com.example.cards.domain.PlayerState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PlayersViewModel extends AndroidViewModel {

    private MutableLiveData<List<Player>> players = new MutableLiveData<>();

    public PlayersViewModel(@NonNull Application application) {
        super(application);
        players.setValue(new ArrayList<>());
    }

    public LiveData<List<Player>> getPlayers() {
        return players;
    }

    public List<Player> getPlayersList() {
        return players.getValue();
    }

    public void addPlayer(Player player) {
        List<Player> value = players.getValue();
        value.add(player);
        players.postValue(value);
    }

    public void updatePlayer(Player updatePlayer) {
        List<Player> value = players.getValue();
        for (Player player : value) {
            if (player.getId() == updatePlayer.getId()) {
                value.set(value.indexOf(player), updatePlayer);
                break;
            }
        }
        players.postValue(value);
    }

    public Player getNextPlayer(Player player) {
        List<Player> value = players.getValue();
        int index = value.indexOf(player);
        if (value.size() == index + 1) {
            return value.get(0);
        } else {
            return value.get(index + 1);
        }
    }

    public Player getDefendingPlayer() {
        List<Player> value = players.getValue();
        for (Player player : value) {
            if (player.getState() == PlayerState.DEFEND) {
                return player;
            }
        }
        throw new IllegalStateException("Defending player not found");
    }

    public void shiftDefendingPlayer() {
        Player defendingPlayer = getDefendingPlayer();
        Player nextPlayer = getNextPlayer(defendingPlayer);

        List<Player> playersList = getPlayersList();
        for (Player player : playersList) {
            player.setState(PlayerState.NONE);
        }

        defendingPlayer.setState(PlayerState.ATTACK);
        nextPlayer.setState(PlayerState.DEFEND);
    }

    public boolean isGameFinished() {
        if (MainActivity.deckViewModel.hasCards()) {
            return false;
        }
        List<Player> playersList = getPlayersList();
        int count = 0;
        for (Player player : playersList) {
            if (!player.getHand().isEmpty()) {
                count++;
            }
        }
        return count < 2;
    }
}
