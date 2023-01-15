package de.etgramli.battlebros.model;

import de.etgramli.battlebros.model.card.Card;

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

    List<Card> getPlayerHand(int playerIndex);

    List<List<Board.CardTuple>> getPlayedCards();

    // Gameplay methods
    boolean playCard(int cardHandIndex, Board.BoardPosition position);

    Set<Board.BoardPosition> getValidPositions();

    void fold();
}
