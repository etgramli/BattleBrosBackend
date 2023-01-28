package de.etgramli.battlebros.model;

import java.util.ArrayList;
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
		for (Map.Entry<Integer, Card> entry : gameField.getAllCards().entrySet())
			total += getValueOfCardOnFieldAt(entry.getKey());
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
	
	public boolean discardCard(int handIndex){
		if (handIndex<0 || handIndex>=gameZoneHand.getAmountOfCards())
            return false;
		
		Card card = gameZoneHand.removeCard(handIndex);
		gameZoneDiscard.addCard(card);
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
	
	public int getAmountOfCardsInHand(){
		return gameZoneHand.getAmountOfCards();
	}

    public Card getCardOnFieldAt(int position){
        return gameField.getCard(position);
    }
	
	public int getValueOfCardOnFieldAt(int position){
		if (gameField.isCardFaceDown(position))
			return 0;
		
		int result = gameField.getCard(position).getValue();
        if (isCardFaceUp(position-1))
		    result += checkValueModifyingAbilities(gameField.getCard(position - 1));

        if (isCardFaceUp(position+1))
            result += checkValueModifyingAbilities(gameField.getCard(position + 1));

		return result;
	}
	
	private int checkValueModifyingAbilities(Card card){
		if (card == null)
			return 0;
		
		int result = 0;
		switch(card.getId()){
			case 15: //Anfeuerer
				result++;
				break;
		}
		return result;
	}
	
	public int getHighestValueOnField(){
		int result = 0;
		for (Map.Entry<Integer, Card> entry : gameField.getAllCards().entrySet()){
			if (gameField.isCardFaceUp(entry.getKey())){
				int value = getValueOfCardOnFieldAt(entry.getKey());
				if (value > result)
					result = value;
			}
		}
        return result;
	}
	
	public List<Integer> getPositionsOfAllFaceUpBrosWithValue(int value){
		List<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, Card> entry : gameField.getAllCards().entrySet()){
			int key = entry.getKey();
            if (isCardFaceUp(key) && getValueOfCardOnFieldAt(key) == value)
				result.add(key);
		}
        return result;
	}

	public boolean isCardFaceUp(int position){
		return gameField.isCardFaceUp(position);
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
	
	public List<Integer> getPositionsOfAllFaceUpBros(){
		return gameField.getAllFaceUpPositions();
	}
	
	public List<Integer> getPositionsOfAllFaceDownBros(){
		return gameField.getAllFaceDownPositions();
	}
}
