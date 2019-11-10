package com.example.cards.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.cards.domain.Player;

import java.util.ArrayList;
import java.util.List;

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

    private List<Player> getPlayerList() {
        return players.getValue();
    }

    public List<Player> getPlayersInGame() {
        List<Player> playing = new ArrayList<>();
        List<Player> value = players.getValue();
        if (value != null) {
            for (Player player : value) {
                if (!player.isOut()) {
                    playing.add(player);
                }
            }
        }
        return playing;
    }

    public void setDefendingPlayer(Player player) {
        for (Player player1 : getPlayerList()) {
            player1.setAction(Player.Action.NONE);
        }
        player.setAction(Player.Action.DEFEND);
        defendingPlayer = player;
        if (getPlayersInGame().size() > 1) {
            getPreviousPlayerInGame(player).setAction(Player.Action.ATTACK);
        }
    }

    public void reset() {
        players.setValue(new ArrayList<>());
    }

    public void addPlayer(Player player) {
        List<Player> value = players.getValue();
        if (value != null) {
            value.add(player);
            players.postValue(value);
        }
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
            getPreviousPlayerInGame(player).setAction(Player.Action.ATTACK);
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

        int index = getPlayerIndex(playerList, player);
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

    private int getPlayerIndex(List<Player> players, Player player) {
        for (int i = 0; i < players.size(); i++) {
            Player player1 = players.get(i);
            if (player1.getId() == player.getId()) {
                return i;
            }
        }
        return -1;
    }

    private Player getPreviousPlayerInGame(Player player) {
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
