package de.etgramli.battlebros.model;

import java.util.List;

public class ResolvableAbility {
	
	private final int cardId;
	private String abilityText = "";
	private Player actor = null;
	private boolean isOptional = false;

	private boolean isAcceptable = false; //TODO
	private boolean isAutomatic = false;
	private int gameFieldPosition;
	private int progress = 0; //counts how many steps are already finished (most abilities consist of just 1 step)
	private boolean canChooseMultiple = false; //TODO make use of (Flammenwerfer)
	
	private boolean canChooseFromOwnHand = false;
	private List<Integer> fromOwnHandAllowed;
	
	private boolean canChooseFromOwnField = false;
	private List<Integer> fromOwnFieldAllowed;
	
	private boolean canChooseFromOpponentField = false;
	private List<Integer> fromOpponentFieldAllowed;
	
	private boolean canChooseFromOwnDiscard = false;
	private List<Integer> fromOwnDiscardAllowed;
	
	private boolean canChooseFromOpponentDiscard = false;
	private List<Integer> fromOpponentDiscardAllowed;
	
	
	public ResolvableAbility(int cardId, Player activator, int gameFieldPosition){
		this.cardId = cardId;
		this.abilityText = "platzhalter fähigkeitstext"; //TODO get from Card class
		this.gameFieldPosition = gameFieldPosition;
		switch (cardId){
			case 2: //Ausbrecher
				actor = activator;
				canChooseFromOwnField = true;
				fromOwnFieldAllowed = actor.getPositionsOfAllFaceUpBros();
				canChooseFromOpponentField = true;
				fromOpponentFieldAllowed = actor.getOpponent().getPositionsOfAllFaceUpBros();
				break;
			case 3: //Flammenwerfer
				//TODO
				break;
			case 4: //Kanonenfutterer
				actor = activator;
				isAutomatic = true;
				break;
			case 5: //Verascher
				actor = activator.getOpponent();
				int highestValue = actor.getHighestValueOnField();
				int opponentsHighestValue = actor.getOpponent().getHighestValueOnField();
				if (opponentsHighestValue > highestValue)
					highestValue = opponentsHighestValue;
				canChooseFromOwnField = true;
				fromOwnFieldAllowed = actor.getPositionsOfAllFaceUpBrosWithValue(highestValue);
				canChooseFromOpponentField = true;
				fromOpponentFieldAllowed = actor.getOpponent().getPositionsOfAllFaceUpBrosWithValue(highestValue);
				break;
			case 7: //Potzblitz
				actor = activator.getOpponent();
				canChooseFromOwnHand = true;
				break;
			case 9: //Lavaboy
				actor = activator.getOpponent();
				isAutomatic = true;
				break;
			case 11: //Abbrenngolem
				actor = activator;
				isAutomatic = true;
				break;
		}
		
		if (!isAutomatic && !isOptional && !validOptionAvailable())
			isOptional = true;
	}
	
	private boolean validOptionAvailable(){
		if ((canChooseFromOwnHand && (fromOwnHandAllowed==null || !fromOwnHandAllowed.isEmpty()))
			|| (canChooseFromOwnField && (fromOwnFieldAllowed==null || !fromOwnFieldAllowed.isEmpty()))
			|| (canChooseFromOpponentField && (fromOpponentFieldAllowed==null || !fromOpponentFieldAllowed.isEmpty()))
			|| (canChooseFromOwnDiscard && (fromOwnDiscardAllowed==null || !fromOwnDiscardAllowed.isEmpty()))
			|| (canChooseFromOpponentDiscard && (fromOpponentDiscardAllowed==null || !fromOpponentDiscardAllowed.isEmpty())))
			return true;
		else
			return false;
	}
	
	public int getProgress(){
		return progress;
	}
	
	public void advanceProgress(){
		progress++;
		switch (cardId){
			//not needed yet (Here attributes can be changed from one step to the next)
		}
		
	}
	
	public int getCardId(){
		return cardId;
	}
	
	public String getAbilityText(){
		return abilityText;
	}
	
	public Player getActor(){
		return actor;
	}
	
	public Card getCard(){
		return Card.getCard(cardId);
	}
	
	public int getGameFieldPosition(){
		return gameFieldPosition;
	}
	
	public boolean isOptional(){
		return isOptional;
	}
	
	public boolean isAutomatic(){
		return isAutomatic;
	}
	
	public boolean canChooseFromOwnHand(){
		return canChooseFromOwnHand;
	}
	public List<Integer> fromOwnHandAllowed(){
		return fromOwnHandAllowed;
	}
	
	public boolean canChooseFromOwnField(){
		return canChooseFromOwnField;
	}
	public List<Integer> fromOwnFieldAllowed(){
		return fromOwnFieldAllowed;
	}
	
	public boolean canChooseFromOpponentField(){
		return canChooseFromOpponentField;
	}
	public List<Integer> fromOpponentFieldAllowed(){
		return fromOpponentFieldAllowed;
	}
}