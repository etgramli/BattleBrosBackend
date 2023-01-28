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
	
	public Card getCardCorrespondingToCurrentlyResolvingAbility(){
		return currentAbility.getCard();
	}
	
    private Player getPlayer(int playerIndex){
        if (playerIndex == 0)
            return player1;
        if (playerIndex == 1)
            return player2;
        return null;
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
	
	public ResolvableAbility getCurrentAbility(){
		return currentAbility;
	}
	
	
	// M E T H O D S (PLAYER ACTIONS)
	@Override
    public void startGame() {
		player1.setGame(this);
        player1.setOpponent(player2);
        player1.setUpGame();

		player2.setGame(this);
        player2.setOpponent(player1);
        player2.setUpGame();

        turn = 1;
        turnPlayer = player1;

        notifyObservers();
    }
	
	@Override
    public boolean playCard(int playerIndex, int cardHandIndex, int position) {
        if (notAbleToPlayDiscardOrPass(playerIndex))
			return false;

        boolean result = getPlayer(playerIndex).playCard(cardHandIndex, position);
        if (result) {
			notifyObservers();
			if (noCurrentAbility())
				advanceToNextTurn();
        }
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
		resolveAbilityWithChosenCardsInPlay(currentAbility.getActor(), selections);
		return true;
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
		if (notAbleToPlayDiscardOrPass(playerIndex)
			|| !currentAbility.canChooseFromOwnHand()
			|| handIndex < 0
			|| handIndex >= getPlayer(playerIndex).getAmountOfCardsInHand()
			|| (currentAbility.fromOwnHandAllowed()!=null && !currentAbility.fromOwnHandAllowed().contains(handIndex)))
			return false;
		
		resolveAbilityWithChosenCardInHand(currentAbility.getActor(), handIndex);
		return true;
	}

    @Override
    public boolean chooseYesOrNo(int playerIndex, boolean accept) {
		if (notAbleToResolveAbility(playerIndex) || !currentAbility.isOptional())
			return false;
		else if (!accept)
			advanceFromAbility();
		else
			resolveAbilityWithAccepting();
        return true;
    }
	
	
	// M E T H O D S (Activate ComesIntoPlay-ABILITIES)
	public void activateComesIntoPlayAbility(Player player, Card card, int gameFieldPosition){
		switch (card.getId()){
			case 2: //Ausbrecher
				setCurrentAbility(new ResolvableAbility(2, player, gameFieldPosition));
				break;
			case 3: //Flammenwerfer
				//todo
				break;
			case 4: //Kanonenfutterer
				player.flipOpponentCardFaceDown(gameFieldPosition);
				break;
			case 5: //Verascher
				setCurrentAbility(new ResolvableAbility(5, player, gameFieldPosition));
				break;
			//todo don't implement Fönix here but in Unterweltfährmann's ability
			case 7: //Potzblitz
				setCurrentAbility(new ResolvableAbility(7, player, gameFieldPosition));
				break;
		}
	}
	
	
	// M E T H O D S (Resolve Resolvable-ABILITIES)
	private void resolveAbilityWithAccepting(){
		//todo
	}
	
	private void resolveAbilityWithChosenCardInPlay(Player actor, boolean targetingOwnRow, int xPosition){
		switch(currentAbility.getCardId()){
			// FLIP CHOSEN CARD FACE DOWN
			case 2: // Ausbrecher
			case 5: // Verascher
				if (targetingOwnRow)
					actor.flipOwnCardFaceDown(xPosition);
				else
					actor.flipOpponentCardFaceDown(xPosition);
				advanceFromAbility();
				break;
		}
	}
	
	private void resolveAbilityWithChosenCardsInPlay(Player actor, List<Pair<Integer,Integer>> selections){
		//TODO
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
		}
	}
	
	// M E T H O D S (OTHERS)

	public List<Integer> getPositionsOfFaceDownCards(int playerIndex){
		return getPlayer(playerIndex).getPositionsOfAllFaceDownBros();
	}

	private boolean notAbleToPlayDiscardOrPass(int playerIndex){
		return currentlyResolvingAnAbility() || getPlayer(playerIndex)!=turnPlayer || turnPlayer.hasPassed();
	}
	
	private boolean notAbleToResolveAbility(int playerIndex){
		return noCurrentAbility()
			|| currentAbility.getActor()==null
			|| currentAbility.getActor()!=getPlayer(playerIndex);
	}
	
	public boolean currentlyResolvingAnAbility(){
		return !noCurrentAbility() && currentAbility.getActor()!=null;
	}
	private boolean noCurrentAbility(){
		return currentAbility == null;
	}
	
	public void setCurrentAbility(ResolvableAbility ability){
		currentAbility = ability;
	}
	
	private void advanceFromAbility(){
		currentAbility =  null;
		advanceToNextTurn();
	}
	
    private void advanceToNextTurn(){
        turn++;
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
