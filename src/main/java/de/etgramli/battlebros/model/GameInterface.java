package de.etgramli.battlebros.model;

import de.etgramli.battlebros.util.IObservable;

import java.util.List;
import java.util.Set;

public interface GameInterface extends IObservable {

    void startGame();

    int getOtherPlayerNum(final int currentPlayerNum);
    // Getters
    int getTurnPlayerIndex();

    String getPlayerName(int playerIndex);

    List<Card> getCardsInHand(int playerIndex);
    List<Integer> getCardIDsInHand(int playerIndex);

    List<List<Card>> getCardsInPlay();
    List<List<Integer>> getCardIDsInPlay();

    int getTotalValue(int playerIndex);

    int getAmountOfLifeCards(int playerIndex);


    // Gameplay methods
    boolean playCard(int playerIndex, int cardHandIndex, int position);

    boolean discardCard(int playerIndex, int cardHandIndex);

    boolean chooseCardInPlay(int playerRow, int xPosition);

    void chooseYesOrNo(boolean accept);

    void pass();


}
