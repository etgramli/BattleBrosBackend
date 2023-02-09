package de.etgramli.battlebros.model;

import java.util.ArrayList;
import java.util.Collections;
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
		drawCardsIgnoringAbilities(6);
        hasPassed = false;
	}

    private void shuffleDeck(){
        gameZoneDeck.shuffle();
    }

    public void cleanUpForNewBattle(){
        hasPassed = false;
        removeAllCardsFromField();
        drawCardsIgnoringAbilities(3);
    }

    public void removeAllCardsFromField(){
        for(Integer position : gameField.getAllTakenPositions()){
            gameZoneDiscard.addCard(gameField.removeCard(position));
        }
    }
	
	public boolean swapPositionsOfTwoCardsOnField(int xPosition1, int xPosition2){
		return gameField.swapCardsAt(xPosition1, xPosition2);
	}
	
	public boolean shuffleFromDiscardToDeck(List<Integer> selections){
		if (selections==null || selections.size()==0 || selections.size()>2)
			return false;
		//TODO check for erzbengel etc
		if (selections.size() == 2 && selections.get(0) < selections.get(1)){
			int temp = selections.get(0);
			selections.set(0, selections.get(1));
			selections.set(1, temp);
		}
		for (Integer selection : selections)
			gameZoneDeck.addCard(gameZoneDiscard.removeCard(selection));
		gameZoneDeck.shuffle();
		return true;
	}

    public int drawCards(int amount){ //returns the amount of cards actually drawn
		if (game.notAllowedToDrawCards(this))
			return 0;
		else
        	return drawCardsIgnoringAbilities(amount);
    }

	public int drawCardsIgnoringAbilities(int amount){ //returns the amount of cards actually drawn
		int cardsDrawn = 0;
		for (int i=0; i< amount; i++) {
			if (drawACard())
				cardsDrawn++;
			else
				break;
		}
		return cardsDrawn;
	}

    private boolean drawACard(){ //doesn't check for Magmann etc -> only use for game rule based drawing, not for ability based drawing!
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
        if (handIndex<0 || handIndex>=gameZoneHand.getAmountOfCards() || !isCardPlayableAt(gameZoneHand.getCard(handIndex), gameFieldPosition))
			return false;
		
		Card card = gameZoneHand.removeCard(handIndex);
        gameField.addCard(card, gameFieldPosition);
		game.activateComesIntoPlayAbility(this, gameFieldPosition);
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
	
	public boolean discardOwnCardFromField(int gameFieldPosition){
		return discardCardFromField(true, gameFieldPosition);
	}
	
	public boolean discardOpponentCardFromField(int gameFieldPosition){
		return discardCardFromField(false, gameFieldPosition);
	}
	
	private boolean discardCardFromField(boolean mine, int gameFieldPosition){
		//TODO check if discarding, affecting different players cards, etc is allowed:
		//if (game.notAllowedToDiscardCardFromField(this, mine, gameField))
		//	return false;
		GameField field;
		GameZone discard;
		if (mine){
			field = gameField;
			discard = gameZoneDiscard;
		} else {
			field = opponent.getGameField();
			discard = opponent.getGameZoneDiscard();
		}
		Card card = field.removeCard(gameFieldPosition);
		discard.addCard(card);
		return true;
	}
	
	public boolean returnOwnCardFromFieldToHand(int gameFieldPosition){
		return returnCardFromFieldToHand(true, gameFieldPosition);
	}
	
	public boolean returnOpponentCardFromFieldToHand(int gameFieldPosition){
		return returnCardFromFieldToHand(false, gameFieldPosition);
	}
	
	private boolean returnCardFromFieldToHand(boolean mine, int gameFieldPosition){
		//TODO check if flipping, affecting different players cards, etc is allowed:
		//if (game.notAllowedToReturnCardToHand(this, mine, gameField))
		//	return false;
		GameField field;
		GameZone hand;
		if (mine){
			field = gameField;
			hand = gameZoneHand;
		} else {
			field = opponent.getGameField();
			hand = opponent.getGameZoneHand();
		}
		Card card = field.removeCard(gameFieldPosition);
		hand.addCard(card);
		return true;
	}
	
	public boolean returnOwnCardFromFieldToTopOfDeck(int gameFieldPosition){
		return returnCardFromFieldToTopOfDeck(true, gameFieldPosition);
	}
	
	public boolean returnOpponentCardFromFieldToTopOfDeck(int gameFieldPosition){
		return returnCardFromFieldToTopOfDeck(false, gameFieldPosition);
	}
	
	private boolean returnCardFromFieldToTopOfDeck(boolean mine, int gameFieldPosition){
		//TODO check if flipping, affecting different players cards, etc is allowed:
		//if (game.notAllowedToReturnCardToTopOfDeck(this, mine, gameField))
		//	return false;
		GameField field;
		GameZone deck;
		if (mine){
			field = gameField;
			deck = gameZoneDeck;
		} else {
			field = opponent.getGameField();
			deck = opponent.getGameZoneDeck();
		}
		Card card = field.removeCard(gameFieldPosition);
		deck.addCardToTop(card);
		return true;
	}
	
	public GameField getGameField(){
		return gameField;
	}
	
	public GameZone getGameZoneDiscard(){
		return gameZoneDiscard;
	}
	
	public GameZone getGameZoneHand(){
		return gameZoneHand;
	}
	
	public GameZone getGameZoneDeck(){
		return gameZoneDeck;
	}
	
	public Game getGame(){
		return game;
	}

    public int getAmountOfCardsOnField(){
        return gameField.getAmountOfCards();
    }
	
	public int getAmountOfCardsInHand(){
		return gameZoneHand.getAmountOfCards();
	}
	
	public int getAmountOfCardsInDiscard(){
		return gameZoneDiscard.getAmountOfCards();
	}
	
	private int countCardsInDiscardWithElement(Element element){
		return gameZoneDiscard.getAmountOfCardsWithElement(element);
	}
	
	public int getAmountOfCardsInDeck(){
		return gameZoneDeck.getAmountOfCards();
	}
	
	public boolean isHandEmpty(){
		return getAmountOfCardsInHand() == 0;
	}
	
	public boolean isFieldEmpty(){
		return getAmountOfCardsOnField() == 0;
	}
	
	public boolean isDiscardEmpty(){
		return getAmountOfCardsInDiscard() == 0;
	}
	
	public boolean isDeckEmpty(){
		return getAmountOfCardsInDeck() == 0;
	}

    public Card getCardOnFieldAt(int position){
        return gameField.getCard(position);
    }
	
	public List<Element> getElementsOfCardAt(int xPosition){
		//TODO element-changing abilities (Polywicht & Contrawicht) -> aber auch nur bei offenen bros anwenden!
		return gameField.getCard(xPosition).getElements();
	}
	
	public int getValueOfCardOnFieldAt(int position){
		if (gameField.isCardFaceDown(position))
			return 0;
		
		int cardId = game.getIdOfCardInPlay(this, position);
		int abilityId = -1;
		
		int base = gameField.getCard(position).getValue();
        int modifier = 0;
		int multiplier = 1;
		
		{ //Felsenfest
			abilityId = 46; //Felsenfest
			if (abilityId == getIdIfFaceUpUnnegated(position))
				return base;
		}
		
		{ //Vulklon
			abilityId = 13; //Vulklon
			if (abilityId == getIdIfFaceUpUnnegated(position)){
				if (!(game.getIdOfCardInPlay(opponent, position)==abilityId && !game.isCardAbilityNegated(opponent, position))) //TODO check if there's no corner cases where a Vulklon still copies the powval of another Vulklon
					base = opponent.getValueOfCardOnFieldAt(position); //TODO check ability text, if this is right
			}
		}
		
		{ //Welsbrocken
			abilityId = 27; //Welsbrocken
			if (abilityId == getIdIfFaceUpUnnegated(position)) //Welsebrocken
				modifier -= 3;
		}
		
		{ //Heißer Feger
			abilityId = 12; //Heißer Feger
			if (abilityId == getIdIfFaceUpUnnegated(position)) //Heißer Feger
				modifier += (2 * (getAmountOfAllFaceDownBros() + opponent.getAmountOfAllFaceDownBros()));
		}
		
		{ //Blumenstrauß
			abilityId = 43; //Blumenstrauß
			if (abilityId == getIdIfFaceUpUnnegated(position)){ //Blumenstrauß
				int amountOfFaceUpBrosOtherThanThis = getAmountOfAllFaceUpBros() - 1;
				if (amountOfFaceUpBrosOtherThanThis > 0)
					modifier += amountOfFaceUpBrosOtherThanThis;
			}
		}
		
		{ //Schluckspecht
			abilityId = 63; //Schluckspecht
			if (abilityId == getIdIfFaceUpUnnegated(position)){ //Schluckspecht
				modifier += getAmountOfCardsInHand();
			}
		}
		
		{ //Luftikuss
			abilityId = 64; //Luftikuss
			if (abilityId == getIdIfFaceUpUnnegated(position)){ //Luftikuss
				modifier += countCardsInDiscardWithElement(Element.AIR);
			}
		}
		
		{ // Hitzkopf & Kohlkopf
			if (getElementsOfCardAt(position).contains(Element.FIRE)){
				abilityId = 16; //Hitzkopf
				modifier += game.countFaceUpUnnegatedOnSideOfButNotAt(this, abilityId, position); //Hitzkopf (my side of field)
				modifier += game.countFaceUpUnnegatedOnSideOf(opponent, abilityId); //Hitzkopf (opponent's side of field)
				
				abilityId = 17; //Kohlkopf
				//check card to the left
				int positonToTheLeft = position - 1;
				if (getIdIfFaceUpUnnegated(positonToTheLeft) == abilityId) //Kohlkopf
					modifier += 2;
				//check card to the right
				int positionToTheRight = position + 1;
				if (getIdIfFaceUpUnnegated(positionToTheRight) == abilityId) //Kohlkopf
					modifier += 2;
			}
		}
		
		{ //Anfeuerer
			abilityId = 15; //Anfeuerer
			//check card to the left
			int positonToTheLeft = position-1;
			if (getIdIfFaceUpUnnegated(positonToTheLeft) == abilityId) //Anfeuerer
				modifier++;
			//check card to the right
			int positionToTheRight = position+1;
			if (getIdIfFaceUpUnnegated(positionToTheRight) == abilityId) //Anfeuerer
				modifier++;
		}
		
		{ //Wucherer
			abilityId = 40; //Wucherer
			//check card to the left
			int positonToTheLeft = position-1;
			if (getIdIfFaceUpUnnegated(positonToTheLeft) == abilityId) //Wucherer
				modifier -= 2;
			//check card to the right
			int positionToTheRight = position+1;
			if (getIdIfFaceUpUnnegated(positionToTheRight) == abilityId) //Wucherer
				modifier -= 2;
		}
		
		{ //Feiges Huhn
			abilityId = 66; //Feiges Huhn
			if (opponent.getIdIfFaceUpUnnegated(position) == abilityId) //Feiges Huhn
				modifier -= 2;
		}
		
		{ //Streichelholz
			abilityId = 14; //Streichelholz
			if (getIdIfFaceUpUnnegated(position)==abilityId){ //Streichelholz
				multiplier = 2;
			}
		}
		
		{ //Fesslerkraken
			abilityId = 28; //Fesslerkraken
			if (opponent.getIdIfFaceUpUnnegated(position) == abilityId){ //Fesslerkraken
				base = 0;
				modifier = 0;
			}
		}
		
		int result = base + (modifier * multiplier);
		if (result <= 0)
			return 0;
		else
			return result;
	}

    public boolean isCardFaceUp(int xPosition){
        return gameField.isCardFaceUp(xPosition);
    }
	
	public int getHighestValueOnField(){
		int result = 0;
		for (Integer position : gameField.getAllTakenPositions()){
			if (gameField.isCardFaceUp(position)){
				int value = getValueOfCardOnFieldAt(position);
				if (value > result)
					result = value;
			}
		}
        return result;
	}
	
	public List<Integer> getPositionsOfAllFaceUpBrosWithValue(int value){
		List<Integer> result = new ArrayList<>();
        for (Integer position : gameField.getAllTakenPositions()){
            if (gameField.isCardFaceUp(position) && getValueOfCardOnFieldAt(position) == value)
				result.add(position);
		}
        return result;
	}
	
	public List<Integer> getPositionsOfAllBrosWithElement(Element element){
		List<Integer> result = new ArrayList<>();
		for (Integer position : gameField.getAllTakenPositions()){
			if (getElementsOfCardAt(position).contains(element))
				result.add(position);
		}
		return result;
	}

    public Map<Integer, Card> getCardsOnField(){
        return gameField.getAllCards();
    }

    private boolean isCardPlayableAt(Card card, int gameFieldPosition){
        if (getCardOnFieldAt(gameFieldPosition) != null)
            return false;

        if (getAmountOfCardsOnField()==0 && opponent.getAmountOfCardsOnField()==0) {
			if (gameFieldPosition != 0)
				return false;
        } else if (opponent.getCardOnFieldAt(gameFieldPosition)==null
                && getCardOnFieldAt(gameFieldPosition-1)==null
                && getCardOnFieldAt(gameFieldPosition+1)==null) {
            return false;
        }
		
		int abilityId = -1;
		
		{ //Goldgolem
			abilityId = 42; //Goldgolem
			if (game.isThereAFaceUpUnnegatedOnSideOf(this, abilityId)) //Goldgolem
				return false;
		}
		
		{ //U.B.O.
			abilityId = 26; //U.B.O.
			if (card.getId() == abilityId && getTotalValue() >= opponent.getTotalValue()) // U.B.O.
				return false;
		}
		
		{ //Dämond
			abilityId = 61; //Dämond
			if (card.getId() == abilityId && gameField.getAmountOfCards() != 0) // Dämond
				return false;
		}
		
		{ //Verbieter
			abilityId = 55; //Verbieter
			List<Element> forbiddenElements = new ArrayList<>();
			for (int i=0; i<=1 ;i++){
				Player player = game.getPlayer(i);
				List<Integer> positions = game.getPositionsOfAllFaceUpUnnegatedOnSideOf(player, abilityId); //Verbieter
				for (Integer position : positions){
					if (player.getOpponent().isCardFaceUp(position)){
						for (Element element : player.getOpponent().getElementsOfCardAt(position)){
							if (!forbiddenElements.contains(element))
								forbiddenElements.add(element);
						}
					}
				}
			}
			if (!Collections.disjoint(forbiddenElements, card.getElements()))
				return false;
		}
		
		{ //Fliegenpilz
			abilityId = 58; //Fliegenpilz
			if (opponent.getIdIfFaceUpUnnegated(gameFieldPosition) == abilityId)
				return false;
		}
		
		{ //Wolkenkratzer
			abilityId = 62; //Wolkenkratzer
			int positionToTheLeft = gameFieldPosition - 1;
			int positionToTheRight = gameFieldPosition + 1;
			if (getIdIfFaceUpUnnegated(positionToTheLeft) == abilityId
				|| getIdIfFaceUpUnnegated(positionToTheRight) == abilityId)
				return false;
		}
		
        return true;
    }
	
	public int getIdIfFaceUpUnnegated(int xPosition){
		if (isCardFaceUp(xPosition) && !game.isCardAbilityNegated(this, xPosition))
			return game.getIdOfCardInPlay(this, xPosition);
		else
			return -1;
	}
	
	public List<Integer> getPositionsOfAllFaceUpBros(){
		return gameField.getAllFaceUpPositions();
	}
	
	public List<Integer> getPositionsOfAllFaceDownBros(){
		return gameField.getAllFaceDownPositions();
	}
	
	public List<Integer> getPositionsOfAllBrosExceptOnPosition(int exceptPosition){
		return gameField.getAllTakenPositionsExcept(exceptPosition);
	}
	
	private int getAmountOfAllFaceDownBros(){
		return gameField.getAllFaceDownPositions().size();
	}
	
	private int  getAmountOfAllFaceUpBros(){
		return gameField.getAllFaceUpPositions().size();
	}
}
