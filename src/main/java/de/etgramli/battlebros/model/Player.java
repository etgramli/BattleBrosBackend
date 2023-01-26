package de.etgramli.battlebros.model;

import java.util.List;
import java.util.Map;

public class Player {

    private String name;
    private Deck deck;

    private Player opponent;
	private Game game;

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
	
	public Player getOpponent(){
		return opponent;
	}
	
	public void setGame(Game game){
		this.game = game;
	}

    public int getTotalValue(){
        int total = 0;
        /*for(Card card : gameField.getListOfAllCards()){
            total += card.getValue();
        }*/
		for (Map.Entry<Integer, Card> entry : gameField.getAllCards().entrySet()){
			if (gameField.isCardFaceUp(entry.getKey()))
				total += entry.getValue().getValue();
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
	
	public Card getCardInPlay(int position){
		return gameField.getCard(position);
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
        shuffleDeck();
        addLifeCards(3);
        drawCards(6);
        hasPassed = false;
    }

    private void shuffleDeck(){
        gameZoneDeck.shuffle();
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
        for (int i=0; i< amount; i++) {
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
        for (int i=0; i< amount; i++) {
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

    public boolean removeALifeCard(){
        if (gameZoneLife.getAmountOfCards() <= 0)
            return false;
        gameZoneDeck.addCard(gameZoneLife.removeCard(0));
        return true;
    }
	
	public boolean hasNoLifeLeft(){
		return gameZoneLife.getAmountOfCards() <= 0;
	}

    public boolean playCard(int handIndex, int gameFieldPosition){
        if (handIndex<0 || handIndex>=gameZoneHand.getAmountOfCards() || !isCardPlayableAt(gameFieldPosition))
            return false;

		Card card = gameZoneHand.removeCard(handIndex);
        gameField.addCard(card, gameFieldPosition);
		game.activateComesIntoPlayAbility(this, card, gameFieldPosition);
        return true;
    }
	
	public boolean flipOwnCardFaceUp(int gameFieldPosition){
		return flipCard(true, true, gameFieldPosition);
	}
    public boolean flipOwnCardFaceDown(int gameFieldPosition){
		return flipCard(false, true, gameFieldPosition);
	}
    public boolean flipOpponentCardFaceUp(int gameFieldPosition){
		return flipCard(true, false, gameFieldPosition);
	}
    public boolean flipOpponentCardFaceDown(int gameFieldPosition){
		return flipCard(false, false, gameFieldPosition);
	}
	private boolean flipCard(boolean faceUp, boolean mine, int gameFieldPosition){
		GameField field;
		if (mine)
			field = gameField;
		else
			field = opponent.getGameField();
		
		return field.turnCard(faceUp, gameFieldPosition);
	}
	
	public GameField getGameField(){
		return gameField;
	}

    public int getAmountOfCardsOnField(){
        return gameField.getAmountOfCards();
    }

    public Card getCardOnFieldAt(int position){
        return gameField.getCard(position);
    }

    public Map<Integer, Card> getCardsOnField(){
        return gameField.getAllCards();
    }

    private boolean isCardPlayableAt(int gameFieldPosition){
        if (getCardOnFieldAt(gameFieldPosition) != null)
            return false;

        if (getAmountOfCardsOnField()==0 && opponent.getAmountOfCardsOnField()==0) {
            if (gameFieldPosition!=0)
                return false;
        } else if (opponent.getCardOnFieldAt(gameFieldPosition)==null
                && getCardOnFieldAt(gameFieldPosition-1)==null
                && getCardOnFieldAt(gameFieldPosition+1)==null) {
            return false;
        }
        return true;
    }
}
