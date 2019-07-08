package com.example.cards.viewmodel;

import android.app.Application;

import com.example.cards.MainActivity;
import com.example.cards.domain.Player;
import com.example.cards.domain.PlayerState;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PlayersViewModel extends AndroidViewModel {

    private MutableLiveData<List<Player>> players = new MutableLiveData<>();

    public PlayersViewModel(@NonNull Application application) {
        super(application);
        reset();
    }

    public LiveData<List<Player>> getPlayers() {
        return players;
    }

    public List<Player> getAllPlayers() {
        return players.getValue();
    }

    public List<Player> getPlayersInGame() {
        List<Player> playing = new ArrayList<>();
        List<Player> value = players.getValue();
        for (Player player : value) {
            if (!player.isOut()) {
                playing.add(player);
            }
        }
        return playing;
    }

    public void reset() {
        players.setValue(new ArrayList<>());
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
        List<Player> value = getPlayersInGame();
        int index = value.indexOf(player);
        if (value.size() == index + 1) {
            return value.get(0);
        } else {
            return value.get(index + 1);
        }
    }

    public Player getDefendingPlayer() {
        List<Player> value = getPlayersInGame();
        for (Player player : value) {
            if (player.getState() == PlayerState.DEFEND) {
                return player;
            }
        }
        throw new IllegalStateException("Defending player not found");
    }

    public boolean finishRound(boolean tookHome) {
        Player defendingPlayer = getDefendingPlayer();

        List<Player> allPlayers = getPlayersInGame();

        for (Player player : allPlayers) {
            player.setState(PlayerState.NONE);
            player.checkIfOut();
        }

        allPlayers = getPlayersInGame();

        if (allPlayers.size() > 1) {
            if (defendingPlayer.isOut() || tookHome) {
                Player nextPlayer = getNextPlayer(defendingPlayer);
                nextPlayer.setState(PlayerState.ATTACK);
                Player nextPlayer1 = getNextPlayer(nextPlayer);
                nextPlayer1.setState(PlayerState.DEFEND);
            } else {
                Player nextPlayer = getNextPlayer(defendingPlayer);
                nextPlayer.setState(PlayerState.DEFEND);
                defendingPlayer.setState(PlayerState.ATTACK);
            }
            return false;
        } else {
            return true;
        }
    }

    public void shiftDefendingPlayer() {
        Player defendingPlayer = getDefendingPlayer();

        List<Player> playersList = getAllPlayers();
        for (Player player : playersList) {
            player.setState(PlayerState.NONE);
        }

        Player nextPlayer = MainActivity.playersViewModel.getNextPlayer(defendingPlayer);

        defendingPlayer.setState(PlayerState.ATTACK);
        nextPlayer.setState(PlayerState.DEFEND);
    }
}
