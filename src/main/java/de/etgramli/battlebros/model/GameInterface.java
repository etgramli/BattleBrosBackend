package de.etgramli.battlebros.model;

import de.etgramli.battlebros.util.IObservable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public interface GameInterface extends IObservable {

    void startGame();

    // Getters
    int getOtherPlayerNum(final int currentPlayerNum);

    int getTurnPlayerIndex();

    String getPlayerName(int playerIndex);

    List<Card> getCardsInHand(int playerIndex);

    List<Integer> getPositionsOfFaceDownCards(int playerIndex);

    List<Integer> getCardIDsInHand(int playerIndex);

    Map<Integer, Card> getCardsInPlay(int playerIndex);
    Map<Integer, Integer> getCardIDsInPlay(int playerIndex);

    int getTotalValue(int playerIndex);

	List<ResolvableAbility> getAbilityQueue();  // ToDo: Change return value to List<String>, add argument player index
	ResolvableAbility getCurrentlyResolvingAbility();   // ToDo: Change return value to String, add argument player index

    int getAmountOfLifeCards(int playerIndex);

    List<Boolean> hasPassed();

    // Gameplay methods
    boolean playCard(int playerIndex, int cardHandIndex, int position);

    boolean discardCard(int playerIndex, int cardHandIndex);
	
	boolean pass(int playerIndex);

	boolean chooseAbilityToResolve(int playerIndex, int abilityIndex);

    /**
     * A player chooses one card in play (due to an effect).
     * @param playerIndex Index of the player choosing a card.
     * @param playerRow Row of the cards, in which the card is.
     * @param xPosition Position of the card in the row.
     * @return  True if the action is successful (was allowed).
     */
	boolean chooseCardInPlay(int playerIndex, int playerRow, Integer xPosition);

    /**
     * A player chooses multiple cards in play.
     * @param playerIndex The index of the player choosing the cards.
     * @param selections A list of pais, consisting of the row index and position of the card in the row.
     * @return  True if the action is successful (was allowed).
     */
	boolean chooseCardsInPlay(int playerIndex, List<Pair<Integer,Integer>> selections);
	
	boolean chooseCardInDiscard(int playerIndex, int discardIndex);
	
	boolean chooseCardsInDiscard(int playerIndex, List<Integer> selections);
	
	boolean chooseCardInHand(int playerIndex, int handIndex);
	
	boolean chooseCardsInHand(int playerIndex, List<Integer> selections);
		
    boolean chooseAccept(int playerIndex);
    boolean chooseCancel(int playerIndex);

}
