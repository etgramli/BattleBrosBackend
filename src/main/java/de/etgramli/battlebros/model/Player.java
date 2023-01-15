package de.etgramli.battlebros.model;

import java.util.List;
import java.util.Map;

public class Player {

    private String name;
    private Deck deck;

    private Player opponent;

    private boolean hasPassed;
    private GameZone gameZoneDeck = new GameZone(false, false);
    private GameZone gameZoneLife = new GameZone(false, false);
    private GameZone gameZoneHand = new GameZone(true, false);
    private GameZone gameZoneDiscard = new GameZone(true, true);

    private GameField gameField = new GameField();

    public Player(String name, Deck deck){
        this.name = name;
        this.deck = deck;
    }

    public void setOpponent(Player opponent){
        this.opponent = opponent;
    }

    public int getTotalValue(){
        int total = 0;
        for(Card card : gameField.getAllCards()){
            total += card.getValue();
        }
        return total;
    }

    public int getAmountOfLifeCards(){
        return gameZoneLife.getAmountOfCards();
    }

    public String getName() {
        return name;
    }

    public Map<Integer, Card> getCardsInPlay(){
        return gameField.getCards();
    }

    public List<Card> getCardsInHand(){
        return gameZoneHand.getCards();
    }

    public boolean hasPassed(){
        return hasPassed;
    }

    public void pass(){
        hasPassed = true;
    }

    public void setUpGame(){
        gameZoneDeck.addCards(deck.getCards());
        addLifeCards(3);
        drawCards(6);
        hasPassed = false;
    }

    public void cleanUpForNewBattle(){
        hasPassed = false;
        removeAllCardsFromField();
        drawCards(3);
    }

    public void removeAllCardsFromField(){
        for(Integer position : gameField.getAllTakenPositions()){
            gameZoneDiscard.addCard(gameField.removeCard(position));
        }
    }

    public int drawCards(int amount){ //returns the amount of cards actually drawn
        int cardsDrawn = 0;
        for (int i=0; i<= amount; i++) {
            if (drawACard())
                cardsDrawn++;
            else
                break;
        }
        return cardsDrawn;
    }

    public boolean drawACard(){
        if (gameZoneDeck.getAmountOfCards() <= 0)
            return false;
        gameZoneHand.addCard(gameZoneDeck.removeCard(0));
        return true;
    }

    public int addLifeCards(int amount){ //returns the amount of cards actually added
        int cardsAdded = 0;
        for (int i=0; i<= amount; i++) {
            if (addALifeCard())
                cardsAdded++;
            else
                break;
        }
        return cardsAdded;
    }

    public boolean addALifeCard(){
        if (gameZoneDeck.getAmountOfCards() <= 0)
            return false;
        gameZoneLife.addCard(gameZoneDeck.removeCard(0));
        return true;
    }

    public boolean removeALifeCard(){ //return true, if player then loses the game
        if (gameZoneLife.getAmountOfCards() <= 0)
            return true;
        gameZoneDeck.addCard(gameZoneLife.removeCard(0));
        return gameZoneLife.getAmountOfCards() <= 0;
    }

    public boolean playCard(int handIndex, int gameFieldPosition){
        if (gameZoneHand.getCard(handIndex)==null || !cardPlayableAt(gameFieldPosition))
            return false;

        gameField.addCard(gameZoneHand.removeCard(handIndex), gameFieldPosition);
        return true;
    }

    public int getAmountOfCardsOnField(){
        return gameField.getAmountOfCards();
    }

    public Card getCardOnFieldAt(int position){
        return gameField.getCard(position);
    }

    private boolean cardPlayableAt(int gameFieldPosition){
        if (getCardOnFieldAt(gameFieldPosition) != null)
            return false;

        if (getAmountOfCardsOnField()==0 && opponent.getAmountOfCardsOnField()==0 && gameFieldPosition!=0)
            return false;

        if (opponent.getCardOnFieldAt(gameFieldPosition)==null
                && getCardOnFieldAt(gameFieldPosition-1)==null
                && getCardOnFieldAt(gameFieldPosition+1)==null)
            return false;

        return true;
    }
}
