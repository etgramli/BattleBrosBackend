package de.etgramli.battlebros.model;

import de.etgramli.battlebros.util.IObservable;
import de.etgramli.battlebros.util.IObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Player getPlayer(int playerIndex){
        if (playerIndex == 0)
            return player1;
        if (playerIndex == 1)
            return player2;
        return null;
    }

    private Player getTurnPlayer(){
        return turnPlayer;
    }

    private Player getNonTurnPlayer(){
        if (turnPlayer == player1)
            return player2;
        return player1;
    }

    private void advanceToNextTurn(){
        turn++;
        changeTurnPlayer();
    }

    private void changeTurnPlayer(){
        turnPlayer = getNonTurnPlayer();
    }

    private void advanceToNextBattle(){
        turnPlayer.cleanUpForNewBattle();
        getNonTurnPlayer().cleanUpForNewBattle();
        turn++;
        notifyObservers();
    }

    private void endTheBattle(){
        boolean endOfGameReached;
        if (getNonTurnPlayer().getTotalValue() >= turnPlayer.getTotalValue()) {
            endOfGameReached = turnPlayer.removeALifeCard();
            changeTurnPlayer();
        } else {
            endOfGameReached = getNonTurnPlayer().removeALifeCard();
        }

        if (endOfGameReached){
            //...
        }
        advanceToNextBattle();
    }

    @Override
    public void startGame() {
        player1.setOpponent(player2);
        player1.setUpGame();

        player2.setOpponent(player1);
        player2.setUpGame();

        turn = 1;
        turnPlayer = player1;

        notifyObservers();
    }

    public int getOtherPlayerNum(final int currentPlayerNum){
        return (currentPlayerNum + 1) % NUMBER_OF_PLAYERS;
    }

    @Override
    public int getTurnPlayerIndex() {
        if (turnPlayer == player1)
            return 0;
        return 1;
    }

    @Override
    public String getPlayerName(int playerIndex) {
        return getPlayer(playerIndex).getName();
    }

    @Override
    public List<Card> getCardsInHand(int playerIndex) {
        return getPlayer(playerIndex).getCardsInHand();
    }

    @Override
    public List<Integer> getCardIDsInHand(int playerIndex) {
        List<Integer> result = new ArrayList<>();
        for (Card card : getCardsInHand(playerIndex))
            result.add(card.getId());
        return result;
    }

    public Map<Integer, Card> getCardsInPlay(int playerIndex){
        return getPlayer(playerIndex).getCardsInPlay();
    }
    public Map<Integer, Integer> getCardIDsInPlay(int playerIndex){
        Map<Integer, Card> cardsInPlayer = getCardsInPlay(playerIndex);

        Map<Integer, Integer> result = new HashMap<>();
        for (Map.Entry<Integer, Card> entry : cardsInPlayer.entrySet())
            result.put(entry.getKey(), entry.getValue().getId());
        return result;
    }

    @Override
    public int getTotalValue(int playerIndex) {
        return getPlayer(playerIndex).getTotalValue();
    }

    @Override
    public int getAmountOfLifeCards(int playerIndex) {
        return getPlayer(playerIndex).getAmountOfLifeCards();
    }

    @Override
    public boolean playCard(int playerIndex, int cardHandIndex, int position) {
        boolean result = getPlayer(playerIndex).playCard(cardHandIndex, position);
        if (result)
            notifyObservers();
        return result;
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
        getTurnPlayer().pass();
        if (getNonTurnPlayer().hasPassed()){
            endTheBattle();
        }
        advanceToNextTurn();
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
