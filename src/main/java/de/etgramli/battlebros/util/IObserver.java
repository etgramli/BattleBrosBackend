package de.etgramli.battlebros.util;

/**
 * To make UIs the observers of the game.
 */
public interface IObserver {
    /**
     * Call to make the observers query the game's state and update the frontend.
     */
    void update();

    /**
     * Make a player select a card from his hand.
     * @param playerIndex The index of the player who has to select a card.
     */
    void selectMyHandCard(final int playerIndex);

    /**
     * Make a player select one of his played cards.
     * @param playerIndex Index of the player to choose a card.
     */
    void selectMyPlayedCard(final int playerIndex);

    /**
     * Make a player choose a card from the opponent's played cards.
     * @param playerIndex Index of the player to choose a card.
     */
    void selectOpponentPlayedCard(final int playerIndex);

    /**
     * Make a player choose a discarded card.
     * @param playerIndex Index of the player to choose a card.
     */
    void selectDiscardedCard(final int playerIndex);
}
