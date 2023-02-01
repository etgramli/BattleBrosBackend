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
     * Make a player choose a card in play. (on either side of the field)
     * @param playerIndex Index of the player to choose a card.
     */
    void selectAnyPlayedCard(final int playerIndex);
	
	/**
     * Make a player choose multiple cards (can also be zero or one) in play. (on either side of the field)
     * @param playerIndex Index of the player to choose a card.
     */
    void selectAnyPlayedCards(final int playerIndex);

    /**
     * Make a player choose a discarded card.
     * @param playerIndex Index of the player to choose a card.
     */
    void selectDiscardedCard(final int playerIndex);
	
	/**
     * Make a player choose multiple discarded cards.
     * @param playerIndex Index of the player to choose a card.
     */
    void selectDiscardedCards(final int playerIndex);
	
	/**
     * Make a player choose the next ability to resolve.
     * @param playerIndex Index of the player to choose a card.
     */
    void selectNextAbilityToResolve(final int playerIndex);
	
	/**
     * Make a player accept the resolution of an ability. (The player could also cancel)
     * @param playerIndex Index of the player to choose a card.
     */
    void selectAcceptAbility(final int playerIndex);
	
}
