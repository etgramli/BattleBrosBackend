package de.etgramli.battlebros.model;

import de.etgramli.battlebros.util.IObserver;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class Game implements GameInterface {

	// A T T R I B U T E S
    private List<IObserver> observers = new ArrayList<>();

    private int turn;
	private ResolvableAbility currentAbility = null;
	private List<ResolvableAbility> abilityQueue = new ArrayList<>();
    int NUMBER_OF_PLAYERS = 2;
    private final Player player1; //index is 0
    private final Player player2; //index is 1
    private Player turnPlayer;


	// C O N S T R U C T O R S
    public Game(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
    }
	
	
	// G E T T E R S
	public int getTurnNumber(){
		return turn;
	}
	
    public Player getPlayer(int playerIndex){
        if (playerIndex == 0)
            return player1;
        if (playerIndex == 1)
            return player2;
        return null;
    }
	
	public int getPlayerIndex(Player player){
		if (player == player1)
			return 0;
		else if (player == player2)
			return 1;
		else
			return -1;
	}

	@Override
	public List<Boolean> hasPassed() {
		return List.of(player1.hasPassed(), player2.hasPassed());
	}

    public Player getTurnPlayer(){
        return turnPlayer;
    }

    private Player getNonTurnPlayer(){
        if (turnPlayer == player1)
            return player2;
        return player1;
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
	
	@Override
	public List<ResolvableAbility> getAbilityQueue(){
		return abilityQueue;
	}
	
	@Override
	public ResolvableAbility getCurrentlyResolvingAbility(){
		return currentAbility;
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
	
	
	// M E T H O D S (PLAYER ACTIONS)
	@Override
    public void startGame() {
		player1.setGame(this);
        player1.setOpponent(player2);
        player1.setUpGameWithShuffling();

		player2.setGame(this);
        player2.setOpponent(player1);
        player2.setUpGameWithShuffling();

        turn = 1;
        turnPlayer = player1;

        notifyObservers();
    }
	
	public void startGameWithoutShuffling(){
		player1.setGame(this);
        player1.setOpponent(player2);
        player1.setUpGameWithOutShuffling();

		player2.setGame(this);
        player2.setOpponent(player1);
        player2.setUpGameWithOutShuffling();

        turn = 1;
        turnPlayer = player1;

        notifyObservers();
	}
	
	@Override
    public boolean playCard(int playerIndex, int cardHandIndex, int position) {
        if (notAbleToPlayDiscardOrPass(playerIndex))
			return false;

        boolean result = getPlayer(playerIndex).playCard(cardHandIndex, position);
        if (result)
			notifyObservers();
        return result;
    }
	
	@Override
    public boolean discardCard(int playerIndex, int cardHandIndex) {
		if (notAbleToPlayDiscardOrPass(playerIndex))
			return false;
		
		boolean result = getPlayer(playerIndex).discardCard(cardHandIndex);
		if (result) {
			notifyObservers();
			advanceToNextTurn();
        }
        return result;
    }
	
	@Override
    public boolean pass(int playerIndex) {
		if (notAbleToPlayDiscardOrPass(playerIndex))
			return false;
		
        getTurnPlayer().pass();
        if (getNonTurnPlayer().hasPassed())
            endTheBattle();
		else
			advanceToNextTurn();

        notifyObservers();
		return true;
    }
	
	@Override
	public boolean chooseAbilityToResolve(int playerIndex, int abilityIndex){
		if (currentlyResolvingAnAbility()
			|| getPlayer(playerIndex) != turnPlayer
			//|| turnPlayer.hasPassed()
			|| !abilitiesInQueue()
			|| abilityIndex < 0
			|| abilityIndex >= abilityQueue.size())
			return false;
		
		moveAbilityFromQueueToCurrent(abilityIndex);
		notifyObservers();
		return true;
	}
	
	@Override
    public boolean chooseCardInPlay(int playerIndex, int playerRow, Integer xPosition) {
		if (notAbleToResolveAbility(playerIndex)
			|| playerRow<0
			|| playerRow>1
			|| currentAbility.canChooseMultiple()
			|| getPlayer(playerRow).getCardInPlay(xPosition) == null)
			return false;
		
		List<Integer> allowed;
        boolean ownSideOfField;
		if (playerIndex==playerRow){
			if (!currentAbility.canChooseFromOwnField())
				return false;
			ownSideOfField = true;
			allowed = currentAbility.fromOwnFieldAllowed();
		} else {
			if (!currentAbility.canChooseFromOpponentField())
				return false;
			ownSideOfField = false;
			allowed = currentAbility.fromOpponentFieldAllowed();
		}
		
		if (allowed!=null && !allowed.contains(xPosition))
				return false;
		
		resolveAbilityWithChosenCardInPlay(currentAbility.getActor(), ownSideOfField, xPosition);
		notifyObservers();
        return true;
    }
	
	@Override
	public boolean chooseCardsInPlay(int playerIndex, List<Pair<Integer,Integer>> selections){
		if (notAbleToResolveAbility(playerIndex) || selections==null || selections.isEmpty() || !currentAbility.canChooseMultiple())
			return false;
		
		for (Pair<Integer,Integer> entry : selections){
			int playerRow = entry.getKey();
			int xPosition = entry.getValue();
			
			if (getPlayer(playerRow).getCardInPlay(xPosition) == null)
				return false;
		
			List<Integer> allowed;
			boolean ownSideOfField;
			if (playerIndex==playerRow){
				if (!currentAbility.canChooseFromOwnField())
					return false;
				ownSideOfField = true;
				allowed = currentAbility.fromOwnFieldAllowed();
			} else {
				if (!currentAbility.canChooseFromOpponentField())
					return false;
				ownSideOfField = false;
				allowed = currentAbility.fromOpponentFieldAllowed();
			}
			
			if (allowed!=null && !allowed.contains(xPosition))
					return false;
		}
		
		boolean result = resolveAbilityWithChosenCardsInPlay(currentAbility.getActor(), selections);
		if (result)
			notifyObservers();
		return result;
	}
	
	@Override
	public boolean chooseCardInDiscard(int playerIndex, int discardIndex){
		if (notAbleToResolveAbility(playerIndex)
			|| !currentAbility.canChooseFromOwnDiscard()
			|| currentAbility.canChooseMultiple()
			|| discardIndex < 0
			|| discardIndex >= getPlayer(playerIndex).getAmountOfCardsInDiscard()
			|| (currentAbility.fromOwnDiscardAllowed()!=null && !currentAbility.fromOwnDiscardAllowed().contains(discardIndex))
		)
			return false;
		
		resolveAbilityWithChosenCardInDiscard(currentAbility.getActor(), discardIndex);
		notifyObservers();
		return true;
	}
	
	@Override
	public boolean chooseCardsInDiscard(int playerIndex, List<Integer> selections){
		if (notAbleToResolveAbility(playerIndex)
			|| selections==null
			|| selections.isEmpty()
			|| !currentAbility.canChooseMultiple()
			|| !currentAbility.canChooseFromOwnDiscard()
		)
			return false;
		
		for (Integer selection : selections){
			if ((currentAbility.fromOwnDiscardAllowed()!=null && !currentAbility.fromOwnDiscardAllowed().contains(selection))
					|| selection < 0
					|| selection >= getPlayer(playerIndex).getAmountOfCardsInDiscard())
				return false;
		}
		
		resolveAbilityWithChosenCardsInDiscard(currentAbility.getActor(), selections);
		notifyObservers();
		return true;
	}
	
	@Override
	public boolean chooseCardInHand(int playerIndex, int handIndex){
		if (notAbleToResolveAbility(playerIndex)
			|| !currentAbility.canChooseFromOwnHand()
			|| handIndex < 0
			|| handIndex >= getPlayer(playerIndex).getAmountOfCardsInHand()
			|| currentAbility.canChooseMultiple()
			|| (currentAbility.fromOwnHandAllowed()!=null && !currentAbility.fromOwnHandAllowed().contains(handIndex)))
			return false;
		
		resolveAbilityWithChosenCardInHand(currentAbility.getActor(), handIndex);
		notifyObservers();
		return true;
	}
	
	@Override
	public boolean chooseCardsInHand(int playerIndex, List<Integer> selections){
		if (notAbleToResolveAbility(playerIndex)
			|| selections==null
			|| selections.isEmpty()
			|| !currentAbility.canChooseMultiple()
			|| !currentAbility.canChooseFromOwnHand()
		)
			return false;
		
		for (Integer selection : selections){
			if ((currentAbility.fromOwnHandAllowed()!=null && !currentAbility.fromOwnHandAllowed().contains(selection))
					|| selection < 0
					|| selection >= getPlayer(playerIndex).getAmountOfCardsInHand())
				return false;
		}
		
		resolveAbilityWithChosenCardsInHand(currentAbility.getActor(), selections);
		notifyObservers();
		return true;
	}

    @Override
    public boolean chooseAccept(int playerIndex) {
		if (notAbleToResolveAbility(playerIndex) || !currentAbility.isAcceptable())
			return false;
		
		resolveAbilityWithAccepting(currentAbility.getActor());
		notifyObservers();
		return true;
    }

	@Override
	public boolean chooseCancel(int playerIndex){
		if (notAbleToResolveAbility(playerIndex) || (!currentAbility.isOptional() && !currentAbility.isAcceptable()))
			return false;

		advanceFromAbility();
		notifyObservers();
		return true;
	}
	
	
	// M E T H O D S (Activate ComesIntoPlay-ABILITIES)
	public void activateComesIntoPlayAbility(Player player, int gameFieldPosition){
		if (isCardAbilityNegated(player, gameFieldPosition)) {
			advanceFromAbility();
			return;
		}
		
		int cardId = getIdOfCardInPlay(player, gameFieldPosition);
		switch (cardId){
			case 2: //Ausbrecher;
			case 3: //Flammenwerfer
			case 4: //Kanonenfutterer
			case 5: //Verascher
			case 7: //Potzblitz
			case 9: //Lavaboy
			case 11: //Abbrenngolem
			case 21: //Aquak
			case 22: //Seemannsgarnele
			case 31: //Heilqualle
			case 34: //Schildfisch
			case 36: //Katerpult
			case 37: //Rammbock
			case 39: //Fleischwolf
			case 41: //Geröllakämpfer
			case 53: //Gittermastkranich
			case 54: //Fliegende Klatsche
			case 65: //Wirbelkind
				addAbilityToQueue(new ResolvableAbility(cardId, player, gameFieldPosition));
				break;	
			
			//todo don't implement Fönix here, but instead in Unterweltfährmann's ability
			
			case 32: //Toller Hecht
				int toTheLeft = gameFieldPosition - 1;
				int toTheRight = gameFieldPosition + 1;
				if (
					(player.isCardFaceUp(toTheLeft) && player.getElementsOfCardAt(toTheLeft).contains(Element.WATER))
					|| (player.isCardFaceUp(toTheRight) && player.getElementsOfCardAt(toTheRight).contains(Element.WATER))
				)
					addAbilityToQueue(new ResolvableAbility(cardId, player, gameFieldPosition));
				break;
			
			default:
				break;
		}
		
		if (player.getElementsOfCardAt(gameFieldPosition).contains(Element.WATER)){ //Fackeldackel
			int abilityId = 10; //Fackeldackel
			for (int i = 0; i < countFaceUpUnnegatedOnAnySide(abilityId); i++)
				addAbilityToQueue(new ResolvableAbility(abilityId, player, gameFieldPosition));
		}
		
		advanceFromAbility();
	}
	
	
	// M E T H O D S (Resolve Resolvable-ABILITIES)
	private void resolveAutomaticAbility(Player actor, int gameFieldPosition){
		switch(currentAbility.getCardId()){
			case 4: // Kanonenfutterer
				actor.flipOpponentCardFaceDown(gameFieldPosition);
				advanceFromAbility();
				break;
			case 9: //Lavaboy
			case 21: //Aquak
			case 32: //Toller Hecht
				actor.drawCards(1);
				advanceFromAbility();
				break;
				
			case 10: //Fackeldackel
				int abilityId = 10; //Fackeldackel
				for (int xPosition : getPositionsOfAllFaceUpUnnegatedOnSideOf(actor, abilityId))
					actor.flipOwnCardFaceDown(xPosition);
				for (int xPosition : getPositionsOfAllFaceUpUnnegatedOnSideOf(actor.getOpponent(), abilityId))
					actor.flipOpponentCardFaceDown(xPosition);
				advanceFromAbility();
				break;
				
			case 11: //Abbrenngolem
				actor.flipOwnCardFaceDown(gameFieldPosition);
				advanceFromAbility();
				break;
				
			case 22: //Seemannsgarnele
				actor.drawCards(1);
				actor.getOpponent().drawCards(1);
				advanceFromAbility();
				break;
				
			case 34: //Schildfisch
				actor.flipOwnCardFaceUp(gameFieldPosition - 1);
				actor.flipOwnCardFaceUp(gameFieldPosition + 1);
				advanceFromAbility();
				break;
				
			case 41: //Geröllakämpfer
				actor.discardOwnCardFromField(gameFieldPosition);
				break;
			
			default:
				advanceFromAbility();
		}
	}
	
	private void resolveAbilityWithAccepting(Player actor){
		switch(currentAbility.getCardId()){
			case 54: //Fliegende Klatsche
				actor.returnOpponentCardFromFieldToTopOfDeck(currentAbility.getGameFieldPosition());
				advanceFromAbility();
				break;
				
			default:
				advanceFromAbility();
		}
	}
	
	private void resolveAbilityWithChosenCardInPlay(Player actor, boolean targetingOwnRow, int xPosition){
		switch(currentAbility.getCardId()){
			case 2: // Ausbrecher
			case 5: // Verascher
				if (targetingOwnRow)
					actor.flipOwnCardFaceDown(xPosition);
				else
					actor.flipOpponentCardFaceDown(xPosition);
				advanceFromAbility();
				break;
				
			case 31: //Heilqualle
				if (targetingOwnRow)
					actor.flipOwnCardFaceUp(xPosition);
				else
					actor.flipOpponentCardFaceUp(xPosition);
				advanceFromAbility();
			
			case 36: //Katerpult
			case 37: //Rammbock
			case 39: //Fleischwolf
				if (targetingOwnRow)
					actor.discardOwnCardFromField(xPosition);
				else
					actor.discardOpponentCardFromField(xPosition);
				advanceFromAbility();
				break;
			
			case 53: //Gittermastkranich
				if (targetingOwnRow)
					actor.returnOwnCardFromFieldToHand(xPosition);
				else
					actor.returnOpponentCardFromFieldToHand(xPosition);
				advanceFromAbility();
				break;
				
			default:
				advanceFromAbility();
		}
	}
	
	private boolean resolveAbilityWithChosenCardsInPlay(Player actor, List<Pair<Integer,Integer>> selections){
		switch(currentAbility.getCardId()){
			case 3: // Flammenwerfer
				//check if total value of selected bros is 2 or less
				int totalValue = 0;
				for (Pair<Integer, Integer> selection : selections){
					Player player = getPlayer(selection.getKey());
					if (player == null)
						continue;
					totalValue += player.getValueOfCardOnFieldAt(selection.getValue());
					if (totalValue > 2)
						return false;
				}
				//turn selected cards face down
				for (Pair<Integer, Integer> selection : selections){
					Player player = getPlayer(selection.getKey());
					if (player == null)
						continue;
					else if (player == actor)
						actor.flipOwnCardFaceDown(selection.getValue());
					else if (player == actor.getOpponent())
						actor.flipOpponentCardFaceDown(selection.getValue());
				}
				advanceFromAbility();
				return true;
				
			case 65: //Wirbelkind
				//check if exactly 2 bros are selected, and these bros are different positions on the same side
				if (selections.size() != 2)
					return false;
				int rowIndex1 = selections.get(0).getKey();
				int rowIndex2 = selections.get(1).getKey();
				int xPosition1 = selections.get(0).getValue();
				int xPosition2 = selections.get(1).getValue();
				if (rowIndex1 != rowIndex2 || xPosition1 == xPosition2)
					return false;
				//swap places for these two bros
				if (!getPlayer(rowIndex1).swapPositionsOfTwoCardsOnField(xPosition1, xPosition2))
					return false;
				advanceFromAbility();
				return true;
				
			default:
				advanceFromAbility();
		}
		return false;
	}
	
	private void resolveAbilityWithChosenCardInHand(Player actor, int handIndex){
		switch(currentAbility.getCardId()){
			case 7: //Potzblitz
				actor.discardCard(handIndex);
				if (actor.getAmountOfCardsInHand() > actor.getOpponent().getAmountOfCardsInHand()
					&& currentAbility.getProgress() == 0)
					currentAbility.advanceProgress();
				else
					advanceFromAbility();
				break;
				
			default:
				advanceFromAbility();
		}
	}
	
	private void resolveAbilityWithChosenCardsInHand(Player actor, List<Integer> selections){
		switch(currentAbility.getCardId()){
			//TODO not needed yet
			default:
				advanceFromAbility();
		}
	}
	
	private void resolveAbilityWithChosenCardInDiscard(Player actor, int discardIndex){
		switch(currentAbility.getCardId()){
			//TODO not needed yet
			default:
				advanceFromAbility();
		}
	}
	
	private void resolveAbilityWithChosenCardsInDiscard(Player actor, List<Integer> selections){
		switch(currentAbility.getCardId()){
			//TODO not needed yet
			default:
				advanceFromAbility();
		}
	}
	
	
	// M E T H O D S (OTHERS)
	public boolean notAllowedToDrawCards(Player player){
		return isThereAFaceUpUnnegatedOnSideOf(player.getOpponent(), 8); //Magmann
	}

	private boolean isThereAFaceUpUnnegatedOnAnySide(int cardId){
		return (isThereAFaceUpUnnegatedOnSideOf(player1, cardId)
			|| isThereAFaceUpUnnegatedOnSideOf(player2, cardId));
	}
	
	public boolean isThereAFaceUpUnnegatedOnSideOf(Player player, int cardId){
		for (Integer position : player.getPositionsOfAllFaceUpBros()){
			if (getIdOfCardInPlay(player, position)==cardId && !isCardAbilityNegated(player, position))
				return true;
		}
		return false;
	}
	
	public boolean isThereAFaceUpUnnegatedOppositeToTheseElemensThatsNotOppositeToTheseFaceUpUnnegatedOnAnySide(int cardId, List<Element> elements, List<Integer> oppositeCardIds){
		return (isThereAFaceUpUnnegatedOppositeToTheseElemensThatsNotOppositeToTheseFaceUpUnnegatedOnSideOf(player1, cardId, elements, oppositeCardIds)
			|| isThereAFaceUpUnnegatedOppositeToTheseElemensThatsNotOppositeToTheseFaceUpUnnegatedOnSideOf(player2, cardId, elements, oppositeCardIds));
	}
	
	public boolean isThereAFaceUpUnnegatedOppositeToTheseElemensThatsNotOppositeToTheseFaceUpUnnegatedOnSideOf(Player player, int cardId, List<Element> elements, List<Integer> oppositeCardIds){
		for (Integer position : getPositionsOfAllFaceUpUnnegatedOnSideOf(player, cardId)){
			if (
				!oppositeCardIds.contains(getIdOfCardInPlay(player.getOpponent(), position)) // is NOT opposite to either of oppositeCardIds
				&& !Collections.disjoint(player.getOpponent().getElementsOfCardAt(position), elements) // IS opposite to any of elements
			)
				return true;
		}
		return false;
	}
	
	public int countFaceUpUnnegatedOnSideOfButNotAt(Player player, int cardId, int exceptPosition){
		int result = 0;
		for (Integer position : player.getPositionsOfAllFaceUpBros()){
			if (position!=exceptPosition && getIdOfCardInPlay(player, position)==cardId && !isCardAbilityNegated(player, position))
				result++;
		}
		return result;
	}
	
	public int countFaceUpUnnegatedOnAnySide(int cardId){
		return (countFaceUpUnnegatedOnSideOf(player1, cardId)
			  + countFaceUpUnnegatedOnSideOf(player2, cardId));
	}
	
	public int countFaceUpUnnegatedOnSideOf(Player player, int cardId){
		int result = 0;
		for (Integer position : player.getPositionsOfAllFaceUpBros()){
			if (getIdOfCardInPlay(player, position)==cardId && !isCardAbilityNegated(player, position))
				result++;
		}
		return result;
	}
	
	public List<Integer> getPositionsOfAllFaceUpUnnegatedOnSideOf(Player player, int cardId){
		List<Integer> result = new ArrayList<>();
		for (Integer position : player.getPositionsOfAllFaceUpBros()){
			if (getIdOfCardInPlay(player, position)==cardId && !isCardAbilityNegated(player, position))
				result.add(position);
		}
		return result;
	}
	
	public boolean isCardAbilityNegated(Player player, int xPosition){
		//return false;
		
		//done:
		// > Holzkopf neben Holzkopf DONE
		// > Senkschlange ggüber von Senkschlange DONE
		// > Haihammer ggüber von Senkschlange DONE
		// > Haihammer im bereich von Haihammer DONE
		// > Haihammer annulliert Senkschlange, die Holzkopf neben Haihammer annulliert. -> Resultat: Haihammer & Senkschlange bleiben aktiv, Holzkopf bleibt annulliert
		// > Verstummer
		// > Verstummer ggüber Bro vom selben Element wie Verstummer (muss nur ein element teilen)
		// > Verstummer ggüber Senkschlange / Haihammer
		// > Verstummer gegenüber von Senkschlange / Haihammer
		// > Verstummer neben Holzkopf while their element is being negated by verstummer
		// > Verstummer neben Holzkopf while their element is being negated by verstummer
		
		
		
		int cardId = getIdOfCardInPlay(player, xPosition);
		
		/*{ //Verstummer
			int abilityId = 56; //Verstummer
			
			if (cardId!=abilityId && isThereAFaceUpUnnegatedOnAnySide(abilityId)) {
				List<Element> elementsAffectedByVerstummer = new ArrayList<>();
				for (Integer position : getPositionsOfAllFaceUpUnnegatedOnSideOf(player, abilityId)){
					if (player.getOpponent().isCardFaceUp(position)){
						for (Element element : player.getOpponent().getElementsOfCardAt(position)){
							if (!elementsAffectedByVerstummer.contains(element)){
								elementsAffectedByVerstummer.add(element);
							}
						}
					}
				}
				for (Integer position : getPositionsOfAllFaceUpUnnegatedOnSideOf(player.getOpponent(), abilityId)){
					if (player.isCardFaceUp(position)){
						for (Element element : player.getElementsOfCardAt(position)){
							if (!elementsAffectedByVerstummer.contains(element)){
								elementsAffectedByVerstummer.add(element);
							}
						}
					}
				}

				if (
					(cardId==19||cardId==20) && !isThereAFaceUpUnnegatedOppositeToTheseElemensThatsNotOppositeToTheseFaceUpUnnegatedOnAnySide(abilityId, player.getElementsOfCardAt(xPosition), List.of(19, 20)) //Haihammer o. Senkschlange opposite to Verstummer won't be negated (unless another verstummer negates all water)
				){
					//do nothing
				} else if (!Collections.disjoint(player.getElementsOfCardAt(xPosition), elementsAffectedByVerstummer)){
						return true;
				}
			}
		}*/
		
		{ //Holzkopf
			int abilityId = 47; //Holzkopf
			int toTheLeft = xPosition - 1;
			int toTheRight = xPosition + 1;
			if (cardId == abilityId){ //Holzkopf next to another Holzkopf
				//do nothing
			} else if (
				(
					getIdOfCardInPlay(player, toTheLeft) == abilityId
					&& player.isCardFaceUp(toTheLeft)
					&& !isCardAbilityNegated(player, toTheLeft)
					&& !(cardId==56 && player.getOpponent().isCardFaceUp(xPosition) && !Collections.disjoint(player.getOpponent().getElementsOfCardAt(xPosition), player.getElementsOfCardAt(toTheLeft))) //Verstummer, that's in next to Holzkopf, but negates an element of that Holzkopf!!!
				) || (
					getIdOfCardInPlay(player, toTheRight) == abilityId
					&& player.isCardFaceUp(toTheRight)
					&& !isCardAbilityNegated(player, toTheRight)
					&& !(cardId==56 && player.getOpponent().isCardFaceUp(xPosition) && !Collections.disjoint(player.getOpponent().getElementsOfCardAt(xPosition), player.getElementsOfCardAt(toTheRight))) //Verstummer, that's in next to Holzkopf, but negates an element of that Holzkopf!!!
				)
			){
				return true;
			}
		}
		
		{ //Haihammer
			int abilityId = 19; //Haihammer
			int toTheLeft = xPosition - 1;
			int toTheRight = xPosition + 1;
			if (cardId == abilityId){ //Haihammer in range of another Haihammer
				//do nothing
			} else {
				if (cardId == 20 || cardId == 56){ //Senkschlange or Verstummer opposite to Haihammer
					//do nothing
				} else if (getIdOfCardInPlay(player.getOpponent(), xPosition) == abilityId
						&& player.getOpponent().isCardFaceUp(xPosition)
						&& !isCardAbilityNegated(player.getOpponent(), xPosition)){
					return true;
				}

				if (cardId==20 && getIdOfCardInPlay(player.getOpponent(), xPosition)==47 && player.getOpponent().isCardFaceUp(xPosition)){ //Senkschlange opposite to Holzkopf that's next to Haihammer TODO check this in AbilityTest019
					//do nothing
				} else { //check left and right to this xPosition for Haihammer on the opposite side of the field
					if (getIdOfCardInPlay(player.getOpponent(), toTheLeft) == abilityId
							&& player.getOpponent().isCardFaceUp(toTheLeft)
							&& !isCardAbilityNegated(player.getOpponent(), toTheLeft)
							&& !(cardId==56 && player.getOpponent().isCardFaceUp(xPosition) && !Collections.disjoint(player.getOpponent().getElementsOfCardAt(xPosition), player.getOpponent().getElementsOfCardAt(toTheLeft))) //Verstummer, that's in range of Haihammer, but negates an element of that Haihammer!!!
					)
							return true;
					if (getIdOfCardInPlay(player.getOpponent(), toTheRight) == abilityId
							&& player.getOpponent().isCardFaceUp(toTheRight)
							&& !isCardAbilityNegated(player.getOpponent(), toTheRight)
							&& !(cardId==56 && player.getOpponent().isCardFaceUp(xPosition) && !Collections.disjoint(player.getOpponent().getElementsOfCardAt(xPosition), player.getOpponent().getElementsOfCardAt(toTheRight))) //Verstummer, that's in range of Haihammer, but negates an element of that Haihammer!!!
					)
							return true;
				}
			}
		}
		
		{ //Senkschlange
			int abilityId = 20; //Senkschlange
			if (
				cardId == abilityId //Senkschlange opposite to another Senkschlange
				|| cardId == 19 //Haihammer opposite to Senkschlange
				|| cardId == 56 //Verstummer opposite to Senkschlange
			) {
				//do nothing
			} else if (
				getIdOfCardInPlay(player.getOpponent(), xPosition) == abilityId
				&& player.getOpponent().isCardFaceUp(xPosition)
				&& !isCardAbilityNegated(player.getOpponent(), xPosition)
			){
				return true;
			}
		}
		
		return false;
	}
	
	public int getIdOfCardInPlay(Player player, int xPosition){
		//todo wärmeleiter
		if (player.getCardInPlay(xPosition) == null)
			return -1;
		return player.getCardInPlay(xPosition).getId();
	}
	
	public List<Integer> getPositionsOfFaceDownCards(int playerIndex){
		return getPlayer(playerIndex).getPositionsOfAllFaceDownBros();
	}

	private boolean notAbleToPlayDiscardOrPass(int playerIndex){
		return currentlyResolvingAnAbility() || abilitiesInQueue() || getPlayer(playerIndex)!=turnPlayer || turnPlayer.hasPassed();
	}
	
	private boolean notAbleToResolveAbility(int playerIndex){
		return noCurrentAbility()
				|| currentAbility.getActor() == null
				|| currentAbility.getActor() != getPlayer(playerIndex);
	}
	
	public boolean currentlyResolvingAnAbility(){
		return !noCurrentAbility() && currentAbility.getActor()!=null;
	}
	private boolean noCurrentAbility(){
		return currentAbility == null;
	}
	
	public boolean currentlyWaitingForAbilityToBeChosen(){
		return noCurrentAbility() && abilityQueue!=null && abilityQueue.size()>=2;
	}
	
	private boolean abilitiesInQueue(){
		return (abilityQueue!=null && !abilityQueue.isEmpty());
	}
	
	public void addAbilityToQueue(ResolvableAbility ability){
		abilityQueue.add(ability);
	}
	
	private void advanceFromAbility(){
		currentAbility = null;
		
		if (abilityQueue==null || abilityQueue.isEmpty())
			advanceToNextTurn();
		else if (abilityQueue.size() == 1){
			moveAbilityFromQueueToCurrent(0);
		} else
			askObserversToChooseAbility(getPlayerIndex(turnPlayer));
	}
	
	private void moveAbilityFromQueueToCurrent(int abilityIndex){
		currentAbility = abilityQueue.remove(abilityIndex);
			if (currentAbility.isAutomatic())
				resolveAutomaticAbility(currentAbility.getActor(), currentAbility.getGameFieldPosition());
			else
				askObserversToResolveAbility();
	}
	
    private void advanceToNextTurn(){
        turn++;

		if (!getNonTurnPlayer().hasPassed())
			changeTurnPlayer();

		notifyObservers();
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
		
		{ // Nagellachs
			int abilityId = 23; //Nagellachs
			player1.drawCards(countFaceUpUnnegatedOnSideOf(player1, abilityId));
			player2.drawCards(countFaceUpUnnegatedOnSideOf(player2, abilityId));
		}
		
		Player loser;
		if (getNonTurnPlayer().getTotalValue() >= turnPlayer.getTotalValue()){
			loser = turnPlayer;
			changeTurnPlayer();
		} else {
			loser = getNonTurnPlayer();
		}
		
		loser.removeALifeCard();
		notifyObservers();

        if (loser.hasNoLifeLeft()){
            //todo GAME ENDS HERE
        } else {
			advanceToNextBattle();
		}
    }

    @Override
    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObservers() {
        observers.clear();
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer : observers)
            observer.update();
    }
	
	public void askObserversToChooseAbility(int playerIndex){
		for (IObserver observer : observers)
			observer.selectNextAbilityToResolve(playerIndex);
	}
	
	public void askObserversToResolveAbility(){
		int playerIndex = getPlayerIndex(currentAbility.getActor());
		for (IObserver observer : observers){
			if (currentAbility.canChooseMultiple()){
				 if (currentAbility.canChooseFromOwnField() || currentAbility.canChooseFromOpponentField())
					 observer.selectAnyPlayedCards(playerIndex);
				 if (currentAbility.canChooseFromOwnDiscard())
					 observer.selectDiscardedCards(playerIndex);
			} else {
				if (currentAbility.canChooseFromOwnField() && currentAbility.canChooseFromOpponentField())
					observer.selectAnyPlayedCard(playerIndex);
				else if (currentAbility.canChooseFromOwnField())
					observer.selectMyPlayedCard(playerIndex);
				else if (currentAbility.canChooseFromOpponentField())
					observer.selectOpponentPlayedCard(playerIndex);
				else if (currentAbility.canChooseFromOwnDiscard())
					observer.selectDiscardedCard(playerIndex);
				else if (currentAbility.canChooseFromOwnHand())
					observer.selectMyHandCard(playerIndex);
				else if (currentAbility.isAcceptable())
					observer.selectAcceptAbility(playerIndex);
			}
		}
	}
	
	
}
