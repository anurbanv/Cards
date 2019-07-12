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
    private Player defendingPlayer;

    public PlayersViewModel(@NonNull Application application) {
        super(application);
        reset();
    }

    public MutableLiveData<List<Player>> getPlayers() {
        return players;
    }

    public List<Player> getPlayerList() {
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

    public void setDefendingPlayer(Player player) {
        for (Player player1 : getPlayerList()) {
            player1.setState(PlayerState.NONE);
        }
        player.setState(PlayerState.DEFEND);
        defendingPlayer = player;
        if (getPlayersInGame().size() > 1) {
            getPreviousPlayerInGame(player).setState(PlayerState.ATTACK);
        }
    }

    public void reset() {
        players.setValue(new ArrayList<>());
    }

    public void addPlayer(Player player) {
        List<Player> value = players.getValue();
        value.add(player);
        players.postValue(value);
    }

    public void updatePlayers() {
        players.postValue(players.getValue());
    }

    public Player getDefendingPlayer() {
        if (defendingPlayer == null) {
            throw new IllegalStateException("defending player not found");
        }
        return defendingPlayer;
    }

    public void playerTookHome() {
        if (getPlayersInGame().size() > 1) {
            Player nextPlayer = getNextPlayerInGame(defendingPlayer);
            setDefendingPlayer(getNextPlayerInGame(nextPlayer));
        }
    }

    public void playerDefended() {
        if (defendingPlayer.cannotPlay()) {
            defendingPlayer.setOut();
        }

        if (getPlayersInGame().size() > 1) {
            if (defendingPlayer.isOut()) {
                Player nextPlayer = getNextPlayerInGame(defendingPlayer);
                setDefendingPlayer(getNextPlayerInGame(nextPlayer));
            } else {
                setDefendingPlayer(getNextPlayerInGame(defendingPlayer));
            }
        }
    }

    public void shiftDefendingPlayer() {
        if (getPlayersInGame().size() > 1) {
            Player nextPlayer = getNextPlayerInGame(defendingPlayer);
            setDefendingPlayer(nextPlayer);
        }
    }

    public void attackingPlayerOut(Player player) {
        if (getPlayersInGame().size() > 2) {
            getPreviousPlayerInGame(player).setState(PlayerState.ATTACK);
        }
    }

    public boolean isGameFinished() {
        return getPlayersInGame().size() <= 1;
    }

    public Player getNextPlayerInGame(Player player) {
        if (getPlayersInGame().size() < 2) {
            throw new IllegalStateException("Cannot find next player (playing size = 1)");
        }

        List<Player> playerList = getPlayerList();

        int index = playerList.indexOf(player);
        if (index == -1) {
            throw new IllegalStateException("Cannot find player in list");
        }

        index = (index + 1) % playerList.size();
        Player nextPlayer = playerList.get(index);

        while (nextPlayer.isOut()) {
            index = (index + 1) % playerList.size();
            nextPlayer = playerList.get(index);
        }
        return nextPlayer;
    }

    public Player getPreviousPlayerInGame(Player player) {
        if (getPlayersInGame().size() < 2) {
            throw new IllegalStateException("Cannot find next player (playing size = 1)");
        }

        List<Player> playerList = getPlayerList();
        int index = playerList.indexOf(player);
        if (index == -1) {
            throw new IllegalStateException("Cannot find player in list");
        }

        index = index == 0 ? playerList.size() - 1 : index - 1;
        Player previousPlayer = playerList.get(index);

        while (previousPlayer.isOut()) {
            index = index == 0 ? playerList.size() - 1 : index - 1;
            previousPlayer = playerList.get(index);
        }
        return previousPlayer;
    }
}
