package de.etgramli.battlebros.model;

import java.util.List;

public class ResolvableAbility {
	
	private final int cardId;
	private Player actor = null;
	private boolean isOptional = false;
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
		switch (cardId){
			case 2: //Ausbrecher
				actor = activator;
				isOptional = true;
				canChooseFromOwnField = true;
				fromOwnFieldAllowed = actor.getPositionsOfAllFaceUpBros(); //alt und falsch: fromOwnFieldAllowed = List.of(gameFieldPosition);
				canChooseFromOpponentField = true;
				fromOpponentFieldAllowed = actor.getOpponent().getPositionsOfAllFaceUpBros();
				break;
			case 3: //Flammenwerfer
				//todo
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
		}
		
		if (!isOptional && !validOptionAvailable())
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
	
	public Player getActor(){
		return actor;
	}
	
	public Card getCard(){
		return Card.getCard(cardId);
	}
	
	public boolean isOptional(){
		return isOptional;
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