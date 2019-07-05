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

    public void shiftDefendingPlayer() {
        Player defendingPlayer = getDefendingPlayer();

        List<Player> playersList = getPlayersInGame();
        for (Player player : playersList) {
            player.setState(PlayerState.NONE);
        }

        Player nextPlayer = MainActivity.playersViewModel.getNextPlayer(defendingPlayer);

        defendingPlayer.setState(PlayerState.ATTACK);
        nextPlayer.setState(PlayerState.DEFEND);
    }

    public boolean isGameFinished() {
        if (MainActivity.deckViewModel.hasCards()) {
            return false;
        }
        List<Player> playersList = getPlayersInGame();
        int count = 0;
        for (Player player : playersList) {
            if (!player.getHand().isEmpty()) {
                count++;
            }
        }
        return count < 2;
    }
}
