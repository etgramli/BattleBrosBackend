package de.etgramli.battlebros.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player {

    private final String name;
    private final Deck deck;

    private Player opponent;
	private Game game;

    private boolean hasPassed;
    private final GameZone gameZoneDeck = new GameZone(false, false);
    private final GameZone gameZoneLife = new GameZone(false, false);
    private final GameZone gameZoneHand = new GameZone(true, false);
    private final GameZone gameZoneDiscard = new GameZone(true, true);

    private final GameField gameField = new GameField();

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

    public void setUpGameWithShuffling(){
        setUpGame(true);
    }
	
	public void setUpGameWithOutShuffling(){
		setUpGame(false);
    }
	
	private void setUpGame(boolean doShuffle){
		gameZoneDeck.addCards(deck.getCards());
        if (doShuffle)
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
		if (game.notAllowedToDrawCards(this))
			return 0;
	
        int cardsDrawn = 0;
        for (int i=0; i< amount; i++) {
            if (drawACard())
                cardsDrawn++;
            else
                break;
        }
        return cardsDrawn;
    }

    private boolean drawACard(){
		if (game.notAllowedToDrawCards(this))
			return false;
		
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

    private boolean addALifeCard(){
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
		//TODO check if flipping, affecting different players cards, etc is allowed:
		//if (game.notAllowedToFlipCard(this, mine, gameField, faceUp))
		//	return false;
		
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
		
		if (getElementsOfCardAt(position).contains(Element.FIRE)
            && gameField.getCard(position).getId() != 16){ //if this bro is a fire bro, but isn't Hitzkopf TODO darf schon gebufft werden wenn es selbst hitzkopf ist, aber halt nur nicht von sich selbst, UND! es kann theoretisch mehr als 2 hitzköpfe geben, man muss auch kopien zählen können
			if (game.isThereAFaceUpUnnegatedOnSideOf(this, 16)) //Hitzkopf
				result++;
			if (game.isThereAFaceUpUnnegatedOnSideOf(opponent, 16)) //Hitzkopf
				result++;
			//check card to the left
			int positonToTheLeft = position - 1;
			if (gameField.isCardFaceUp(positonToTheLeft) 
				&& !game.isCardAbilityNegated(this, positonToTheLeft)
				&& gameField.getCard(positonToTheLeft).getId()==17) //Kohlkopf
				result += 2;
			//check card to the right
			int positionToTheRight = position + 1;
			if (gameField.isCardFaceUp(positionToTheRight) 
				&& !game.isCardAbilityNegated(this, positionToTheRight)
				&& gameField.getCard(positionToTheRight).getId()==17) //Kohlkopf
				result += 2;
		}
		
		//check card to the left
		int positonToTheLeft = position - 1;
        if (gameField.isCardFaceUp(positonToTheLeft) 
			&& !game.isCardAbilityNegated(this, positonToTheLeft)
			&& gameField.getCard(positonToTheLeft).getId()==15) //Anfeuerer
			result++;

		//check card to the right
		int positionToTheRight = position + 1;
        if (gameField.isCardFaceUp(positionToTheRight) 
			&& !game.isCardAbilityNegated(this, positionToTheRight)
			&& gameField.getCard(positionToTheRight).getId()==15) //Anfeuerer
			result++;
		
		//TODO check opposite card

		if (result <= 0)
			return 0;
		if (gameField.getCard(position).getId()==14 && !game.isCardAbilityNegated(this, position)){ //Streichelholz
			result *= 2;
		}
		return result;
	}

    public boolean isCardFaceUp(int xPosition){
        return gameField.isCardFaceUp(xPosition);
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
            if (gameField.isCardFaceUp(key) && getValueOfCardOnFieldAt(key) == value)
				result.add(key);
		}
        return result;
	}

    public Map<Integer, Card> getCardsOnField(){
        return gameField.getAllCards();
    }

    private boolean isCardPlayableAt(int gameFieldPosition){
        if (getCardOnFieldAt(gameFieldPosition) != null)
            return false;

        if (getAmountOfCardsOnField()==0 && opponent.getAmountOfCardsOnField()==0) {
            return gameFieldPosition == 0;
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
	
	public List<Element> getElementsOfCardAt(int xPosition){
		//TODO element changing abilities
		return gameField.getCard(xPosition).getElements();
	}
}
