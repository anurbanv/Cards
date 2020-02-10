package com.example.cards.service;

import com.example.cards.domain.Card;
import com.example.cards.domain.DeckOfCards;
import com.example.cards.domain.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayersService {

    public PlayersService() {

    }

    public void fillPlayersHands(List<Player> allPlayers, DeckOfCards deckOfCards) {
        while (deckOfCards.hasCards()) {
            boolean allHandsFull = true;
            for (Player player : allPlayers) {
                if (player.getHand().size() < 6) {
                    allHandsFull = false;
                    Card card = deckOfCards.takeCard();
                    player.addCardToHand(card);
                }
            }
            if (allHandsFull) {
                break;
            }
        }
    }

    public void setPlayerNames(List<Player> allPlayers, List<String> playerNames) {
        if (allPlayers.size() != playerNames.size()) {
            throw new IllegalArgumentException("players size invalid");
        }
        for (int i = 0; i < allPlayers.size(); i++) {
            Player player = allPlayers.get(i);
            String name = playerNames.get(i);
            player.setName(name);
        }
    }

    public List<Player> getNewPlayersList(int count) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            players.add(new Player(i));
        }
        return players;
    }

    public Player getPlayerWithLowestStrongCard(List<Player> allPlayers) {
        Player firstPlayer = allPlayers.get(0);
        Card lowestStrongCard = firstPlayer.lookAtCard(0);
        Player cardOwner = firstPlayer;

        for (Player player : getNotOutPlayers(allPlayers)) {
            for (Card card : player.getHand()) {
                if (!lowestStrongCard.isStrong() && card.isStrong()) {
                    lowestStrongCard = card;
                    cardOwner = player;
                } else if (card.isStrong() && card.getNumber() < lowestStrongCard.getNumber()) {
                    lowestStrongCard = card;
                    cardOwner = player;
                }
            }
        }
        return cardOwner;
    }

    public void setDefendingPlayer(List<Player> allPlayers, Player defender) {
        for (Player player : allPlayers) {
            player.setAction(Player.Action.NONE);
        }
        defender.setAction(Player.Action.DEFEND);
        if (getNotOutPlayers(allPlayers).size() > 1) {
            Player previous = getPreviousPlayerInGame(allPlayers, defender);
            previous.setAction(Player.Action.ATTACK);
        }
    }

    public List<Player> getNotOutPlayers(List<Player> allPlayers) {
        List<Player> playing = new ArrayList<>();
        for (Player player : allPlayers) {
            if (!player.isOut()) {
                playing.add(player);
            }
        }
        return playing;
    }

    public Player getNextPlayerInGame(List<Player> allPlayers, Player player) {

        int index = allPlayers.indexOf(player);
        if (index == -1) {
            throw new IllegalStateException("Cannot find player in list");
        }

        index = (index + 1) % allPlayers.size();
        Player nextPlayer = allPlayers.get(index);

        while (nextPlayer.isOut()) {
            index = (index + 1) % allPlayers.size();
            nextPlayer = allPlayers.get(index);
        }
        return nextPlayer;
    }

    public Player getPreviousPlayerInGame(List<Player> allPlayers, Player player) {
        int index = allPlayers.indexOf(player);
        if (index == -1) {
            throw new IllegalStateException("Cannot find player in list");
        }

        index = index == 0 ? allPlayers.size() - 1 : index - 1;
        Player previousPlayer = allPlayers.get(index);

        while (previousPlayer.isOut()) {
            index = index == 0 ? allPlayers.size() - 1 : index - 1;
            previousPlayer = allPlayers.get(index);
        }
        return previousPlayer;
    }
}
