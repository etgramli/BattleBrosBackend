package de.etgramli.battlebros.model;

import de.etgramli.battlebros.util.IObservable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GameInterface extends IObservable {

    void startGame();

    int getOtherPlayerNum(final int currentPlayerNum);
	
    // Getters
    int getTurnPlayerIndex();

    String getPlayerName(int playerIndex);

    List<Card> getCardsInHand(int playerIndex);
    List<Integer> getCardIDsInHand(int playerIndex);

    Map<Integer, Card> getCardsInPlay(int playerIndex);
    Map<Integer, Integer> getCardIDsInPlay(int playerIndex);

    int getTotalValue(int playerIndex);

    int getAmountOfLifeCards(int playerIndex);

    // Gameplay methods
    boolean playCard(int playerIndex, int cardHandIndex, int position);

    boolean discardCard(int playerIndex, int cardHandIndex);
	
	boolean pass(int playerIndex);

	boolean chooseCardInPlay(int playerIndex, int playerRow, Integer xPosition);
	
	boolean chooseCardsInPlay(int playerIndex, List<Pair<Integer,Integer>> selections);
	
	boolean chooseCardInDiscard(int playerIndex, int discardIndex);
	
	boolean chooseCardsInDiscard(int playerIndex, List<Pair<Integer,Integer>> selections);
	
	boolean chooseCardInHand(int playerIndex, int handIndex);
		
    boolean chooseYesOrNo(int playerIndex, boolean accept);

}
