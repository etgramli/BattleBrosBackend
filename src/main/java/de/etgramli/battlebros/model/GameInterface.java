package de.etgramli.battlebros.model;

import java.util.List;
import java.util.Set;

public interface GameInterface {
    int NUMBER_OF_PLAYERS = 2;

    static int getOtherPlayerNum(final int currentPlayerNum) {
        return (currentPlayerNum + 1) % NUMBER_OF_PLAYERS;
    }
    // Getters
    int getRoundsWon(int playerIndex);

    int getCurrentPlayerIndex();

    String getPlayerName(int playerIndex);

    List<?> getPlayerHand(int playerIndex);

    List<List<?>> getPlayedCards();

    // Gameplay methods
    boolean playCard(int cardHandIndex, Object position);

    Set<?> getValidPositions();

    void fold();
}
