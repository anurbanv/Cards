package com.example.cards.viewmodel;

import android.app.Application;

import com.example.cards.domain.Player;
import com.example.cards.domain.PlayerState;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class PlayersViewModel extends AndroidViewModel {

    private MutableLiveData<List<Player>> players = new MutableLiveData<>();

    public PlayersViewModel(@NonNull Application application) {
        super(application);
        reset();
    }

    public MutableLiveData<List<Player>> getPlayers() {
        return players;
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

    public void updatePlayers(){
        players.postValue(players.getValue());
    }

    public void playerOut(Player player) {
        if (player.getState() == PlayerState.ATTACK) {
            Player previousPlayer = getPreviousPlayer(player);
            previousPlayer.setState(PlayerState.ATTACK);
        }
        player.setPlayerOut();
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

    public Player getPreviousPlayer(Player player) {
        List<Player> value = getPlayersInGame();
        int index = value.indexOf(player);
        if (index == 0) {
            return value.get(value.size() - 1);
        } else {
            return value.get(index - 1);
        }
    }

    public Player getDefendingPlayer() {
        List<Player> value = getPlayers().getValue();
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

        List<Player> playersList = getPlayersInGame();
        for (Player player : playersList) {
            player.setState(PlayerState.NONE);
        }

        playersList = getPlayersInGame();

        if (playersList.size() > 1) {
            Player nextPlayer = getNextPlayer(defendingPlayer);
            Player previousPlayer = getPreviousPlayer(nextPlayer);
            nextPlayer.setState(PlayerState.DEFEND);
            previousPlayer.setState(PlayerState.ATTACK);
        }
    }
}
