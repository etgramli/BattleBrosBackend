package de.etgramli.battlebros.model;

import de.etgramli.battlebros.util.IObserver;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		return true;
	}
	
	@Override
    public boolean chooseCardInPlay(int playerIndex, int playerRow, Integer xPosition) {
		if (notAbleToResolveAbility(playerIndex) || playerRow<0 || playerRow>1)
			return false;
		
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
		
		resolveAbilityWithChosenCardInPlay(currentAbility.getActor(), ownSideOfField, xPosition);
        return true;
    }
	
	@Override
	public boolean chooseCardsInPlay(int playerIndex, List<Pair<Integer,Integer>> selections){
		if (notAbleToResolveAbility(playerIndex) || selections.isEmpty())
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
		
		return resolveAbilityWithChosenCardsInPlay(currentAbility.getActor(), selections);
	}
	
	@Override
	public boolean chooseCardInDiscard(int playerIndex, int discardIndex){
		return false; //TODO
	}
	
	@Override
	public boolean chooseCardsInDiscard(int playerIndex, List<Pair<Integer,Integer>> selections){
		return false; //TODO
	}
	
	@Override
	public boolean chooseCardInHand(int playerIndex, int handIndex){
		if (notAbleToResolveAbility(playerIndex)
			|| !currentAbility.canChooseFromOwnHand()
			|| handIndex < 0
			|| handIndex >= getPlayer(playerIndex).getAmountOfCardsInHand()
			|| (currentAbility.fromOwnHandAllowed()!=null && !currentAbility.fromOwnHandAllowed().contains(handIndex)))
			return false;
		
		resolveAbilityWithChosenCardInHand(currentAbility.getActor(), handIndex);
		return true;
	}

    @Override
    public boolean chooseAccept(int playerIndex) {
		return false;
		//TODO
		//resolveAbilityWithAccepting();
    }

	@Override
	public boolean chooseCancel(int playerIndex){
		if (notAbleToResolveAbility(playerIndex) || !currentAbility.isOptional())
			return false;

		advanceFromAbility();
		return true;
	}
	
	
	// M E T H O D S (Activate ComesIntoPlay-ABILITIES)
	public void activateComesIntoPlayAbility(Player player, Card card, int gameFieldPosition){
		if (isCardAbilityNegated(player, gameFieldPosition))
			return;
		
		switch (card.getId()){
			case 2: //Ausbrecher
				addAbilityToQueue(new ResolvableAbility(2, player, gameFieldPosition));
				break;
			case 3: //Flammenwerfer
				addAbilityToQueue(new ResolvableAbility(3, player, gameFieldPosition));
				break;
			case 4: //Kanonenfutterer
				addAbilityToQueue(new ResolvableAbility(4, player, gameFieldPosition));
				break;
			case 5: //Verascher
				addAbilityToQueue(new ResolvableAbility(5, player, gameFieldPosition));
				break;
				
			//todo don't implement Fönix here, but instead in Unterweltfährmann's ability
			
			case 7: //Potzblitz
				addAbilityToQueue(new ResolvableAbility(7, player, gameFieldPosition));
				break;
			case 9: //Lavaboy
				addAbilityToQueue(new ResolvableAbility(9, player, gameFieldPosition));
				break;
			case 11: //Abbrenngolem
				addAbilityToQueue(new ResolvableAbility(11, player, gameFieldPosition));
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
			default:
				advanceFromAbility();
		}
	}
	
	private void resolveAbilityWithAccepting(){
		Player actor = currentAbility.getActor();
		switch(currentAbility.getCardId()){
			//TODO not needed yet
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
			if (player.getCardOnFieldAt(position).getId()==cardId && !isCardAbilityNegated(player, position))
				return true;
		}
		return false;
	}
	
	public int countFaceUpUnnegatedOnSideOfButNotAt(Player player, int cardId, int exceptPosition){
		int result = 0;
		for (Integer position : player.getPositionsOfAllFaceUpBros()){
			if (position!=exceptPosition && player.getCardOnFieldAt(position).getId()==cardId && !isCardAbilityNegated(player, position))
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
			if (player.getCardOnFieldAt(position).getId()==cardId && !isCardAbilityNegated(player, position))
				result++;
		}
		return result;
	}
	
	public List<Integer> getPositionsOfAllFaceUpUnnegatedOnSideOf(Player player, int cardId){
		List<Integer> result = new ArrayList<>();
		for (Integer position : player.getPositionsOfAllFaceUpBros()){
			if (player.getCardOnFieldAt(position).getId()==cardId && !isCardAbilityNegated(player, position))
				result.add(position);
		}
		return result;
	}
	
	public boolean isCardAbilityNegated(Player player, int xPosition){
		return false; //TODO
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
		}
	}
	
	private void moveAbilityFromQueueToCurrent(int abilityIndex){
		currentAbility = abilityQueue.remove(abilityIndex);
			if (currentAbility.isAutomatic())
				resolveAutomaticAbility(currentAbility.getActor(), currentAbility.getGameFieldPosition());
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
}
