package de.etgramli.battlebros.model;

import de.etgramli.battlebros.util.IObserver;

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
    private Player player1; //index is 0
    private Player player2; //index is 1
    private Player turnPlayer;


	// C O N S T R U C T O R S
    public Game(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
    }
	
	
	// G E T T E R S
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
        if (getPlayer(playerIndex) != turnPlayer)
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
    public boolean chooseCardInPlay(int playerIndex, int playerRow, Integer xPosition) {
		if (noCurrentAbility())
			return false;
		
		Player actor = currentAbility.getActor();
		if (actor==null || actor!=getPlayer(playerIndex) || playerIndex<0 || playerIndex>1 || playerRow<0 || playerRow>1)
			return false;
		
		if (getPlayer(playerRow).getCardInPlay(xPosition) == null)
			return false;
		
		List<Integer> exceptions;
        boolean ownSideOfField;
		if (playerIndex==playerRow){
			if (!currentAbility.canChooseFromOwnField())
				return false;
			ownSideOfField = true;
			exceptions = currentAbility.fromOwnFieldExcept();
		} else {
			if (!currentAbility.canChooseFromOpponentField())
				return false;
			ownSideOfField = false;
			exceptions = currentAbility.fromOpponentFieldExcept();
		}
		
		if (exceptions!=null && exceptions.contains(xPosition))
				return false;
		
		resolveAbilityWithChosenCardInPlay(actor, ownSideOfField, xPosition);
        return true;
    }

    @Override
    public boolean chooseYesOrNo(int playerIndex, boolean accept) { //TODO add playerIndex
		if (noCurrentAbility() || !currentAbility.isOptional()
                || currentAbility.getActor()==null
                || currentAbility.getActor()!=getPlayer(playerIndex))
			return false;
		else if (!accept)
			advanceFromAbility();
		else
			resolveAbilityWithAccepting();
        return true;
    }

    @Override
    public boolean pass(int playerIndex) { //TODO add playerIndex
		if (currentlyResolvingAnAbility() || getPlayer(playerIndex)!=turnPlayer)
			return false;
		
        getTurnPlayer().pass();
        if (getNonTurnPlayer().hasPassed())
            endTheBattle();
		else
			advanceToNextTurn();

        notifyObservers();
		return true;
    }
	
	
	// M E T H O D S (OTHERS)
	private boolean currentlyResolvingAnAbility(){
		return !noCurrentAbility();
	}
	private boolean noCurrentAbility(){
		return currentAbility == null;
	}
	
	public void setCurrentAbility(ResolvableAbility ability){
		currentAbility = ability;
	}
	
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
				//todo
				break;
			//todo don't implement Fönix here but in Unterweltfährmann's ability
			case 7: //Potzblitz
				//todo
				break;
		}
	}
	
	private void resolveAbilityWithAccepting(){
		//todo
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
		}
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
        if (getNonTurnPlayer().hasPassed())
            return;
        turnPlayer = getNonTurnPlayer();
    }

    private void advanceToNextBattle(){
        turnPlayer.cleanUpForNewBattle();
        getNonTurnPlayer().cleanUpForNewBattle();
        turn++;
        notifyObservers();
    }

    private void endTheBattle(){
        /*boolean endOfGameReached;
		if (getNonTurnPlayer().getTotalValue() >= turnPlayer.getTotalValue()) {
			turnPlayer.removeALifeCard();
            endOfGameReached = turnPlayer.hasNoLifeLeft();
            changeTurnPlayer();
        } else {
			getNonTurnPlayer().removeALifeCard();
            endOfGameReached = turnPlayer.hasNoLifeLeft();
        }*/
		
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
            //todo GAME ENDED HERE
        } else {
			advanceToNextBattle();
		}
    }

    @Override
    public boolean discardCard(int playerIndex, int cardHandIndex) {
        return false;
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
