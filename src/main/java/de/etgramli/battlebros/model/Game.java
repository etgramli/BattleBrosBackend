package de.etgramli.battlebros.model;

import de.etgramli.battlebros.util.IObservable;
import de.etgramli.battlebros.util.IObserver;

import java.util.ArrayList;
import java.util.List;

public class Game implements GameInterface {

    private List<IObserver> observers = new ArrayList<>();

    private int turn;
    int NUMBER_OF_PLAYERS = 2;
    private Player player1;
    private Player player2;

    private Player turnPlayer;

    public Game(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public void startGame() {
        player1.setUpGame();
        player2.setUpGame();
        turn = 1;
        turnPlayer = player1;
    }

    public int getOtherPlayerNum(final int currentPlayerNum){
        return (currentPlayerNum + 1) % NUMBER_OF_PLAYERS;
    }

    @Override
    public int getTurnPlayerIndex() {
        return 0;
    }

    @Override
    public String getPlayerName(int playerIndex) {
        return null;
    }

    @Override
    public List<Card> getCardsInHand(int playerIndex) {
        return null;
    }

    @Override
    public List<Integer> getCardIDsInHand(int playerIndex) {
        return null;
    }

    @Override
    public List<List<Card>> getCardsInPlay() {
        return null;
    }

    @Override
    public List<List<Integer>> getCardIDsInPlay() {
        return null;
    }

    @Override
    public int getTotalValue(int playerIndex) {
        if (playerIndex == 0)
            return player1.getTotalValue();
        if (playerIndex == 1)
            return player2.getTotalValue();
        return -1;
    }

    @Override
    public int getAmountOfLifeCards(int playerIndex) {
        return 0;
    }

    @Override
    public boolean playCard(int playerIndex, int cardHandIndex, int position) {
        return false;
    }

    @Override
    public boolean discardCard(int playerIndex, int cardHandIndex) {
        return false;
    }

    @Override
    public boolean chooseCardInPlay(int playerRow, int xPosition) {
        return false;
    }

    @Override
    public void chooseYesOrNo(boolean accept) {

    }

    @Override
    public void pass() {

    }

    @Override
    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer : observers)
            observer.update();
    }
}
