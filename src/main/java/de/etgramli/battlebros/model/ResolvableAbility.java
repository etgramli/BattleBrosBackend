package de.etgramli.battlebros.model;

import java.util.ArrayList;
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
	private boolean canChooseMultiple = false;
	
	private boolean canChooseFromOwnHand = false;
	private List<Integer> fromOwnHandAllowed;
	
	private boolean canChooseFromOwnField = false;
	private List<Integer> fromOwnFieldAllowed;
	
	private boolean canChooseFromOpponentField = false;
	private List<Integer> fromOpponentFieldAllowed;
	
	private boolean canChooseFromOwnDiscard = false;
	private List<Integer> fromOwnDiscardAllowed;
	
	
	
	public ResolvableAbility(int cardId, Player activator, int gameFieldPosition){
		this.cardId = cardId;
		this.abilityText = "platzhalter fähigkeitstext"; //TODO get from Card class
		this.gameFieldPosition = gameFieldPosition;
		actor = activator;
		switch (cardId){
			case 2: //Ausbrecher
				canChooseFromOwnField = true;
				fromOwnFieldAllowed = actor.getPositionsOfAllFaceUpBros();
				canChooseFromOpponentField = true;
				fromOpponentFieldAllowed = actor.getOpponent().getPositionsOfAllFaceUpBros();
				break;
				
			case 3: //Flammenwerfer
				canChooseFromOwnField = true;
				fromOwnFieldAllowed = actor.getPositionsOfAllFaceUpBros();
				canChooseFromOpponentField = true;
				fromOpponentFieldAllowed = actor.getOpponent().getPositionsOfAllFaceUpBros();
				canChooseMultiple = true;
				isOptional = true;
				break;
				
			case 4: //Kanonenfutterer
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
				
			case 10: //Fackeldackel
				isAutomatic = true;
				break;
				
			case 11: //Abbrenngolem
				isAutomatic = true;
				break;
				
			case 21: //Aquak
				isAutomatic = true;
				break;
				
			case 22: //Seemannsgarnele
				isAutomatic = true;
				break;
			
			case 30: //Meeresfrüchte
				canChooseFromOwnDiscard = true;
				canChooseMultiple = true;
				isOptional = true; //TODO check
				break;
				
			case 31: //Heilqualle
				canChooseFromOwnField = true; //todo check ob evtl man nur facedown bros wählen darf
				canChooseFromOpponentField = true; //todo check ob evtl man nur facedown bros wählen darf
				//TODO könnte optional sein (who knows?)
				break;
				
			case 32: //Toller Hecht
				isAutomatic = true;
				break;
			
			case 33: //Walnuss
				canChooseFromOwnField = true;
				fromOwnFieldAllowed = actor.getPositionsOfAllBrosWithElement(Element.WATER);
				canChooseFromOpponentField = true;
				fromOpponentFieldAllowed = actor.getOpponent().getPositionsOfAllBrosWithElement(Element.WATER);
				isOptional = true; //TODO CHECK (auch checken ob fromXYZallowed hier stimmt
				break;
				
			case 34: //Schildfisch
				isAutomatic = true;
				break;
			
			case 36: //Katerpult
				canChooseFromOpponentField = true;
				fromOpponentFieldAllowed = new ArrayList<>();
				for (int i=gameFieldPosition-1; i<=gameFieldPosition+1; i++){
					if (actor.getOpponent().getCardOnFieldAt(i) != null)
						fromOpponentFieldAllowed.add(i);
				}
				//todo check ob diese ffähigkeit optional ist
				break;
			
			case 37: //Rammbock
				canChooseFromOwnField = true;
				fromOwnFieldAllowed = new ArrayList<>();
				if (actor.getCardOnFieldAt(gameFieldPosition-1) != null)
					fromOwnFieldAllowed.add(gameFieldPosition-1);
				if (actor.getCardOnFieldAt(gameFieldPosition+1) != null)
					fromOwnFieldAllowed.add(gameFieldPosition+1);
				canChooseFromOpponentField = true;
				fromOpponentFieldAllowed = new ArrayList<>();
				if (actor.getOpponent().getCardOnFieldAt(gameFieldPosition) != null)
					fromOpponentFieldAllowed.add(gameFieldPosition);
				isOptional = true; //todo sichergehen dass wirklich optional ist
				break;
				
			case 39: //Fleischwolf
				canChooseFromOwnField = true;
				isOptional = true;
				break;
				
			case 41: //Geröllakämpfer
				isAutomatic = true;
				break;
			
			case 49: //Drahtesel
				isAutomatic = true;
				break;
				
			case 53: //Gittermastkranich
				canChooseFromOwnField = true;
				fromOwnFieldAllowed = actor.getPositionsOfAllBrosExceptOnPosition(gameFieldPosition);
				canChooseFromOpponentField = true;
				//todo check ob optional
				break;
				
			case 65: //Wirbelkind
				canChooseFromOwnField = true;
				canChooseFromOpponentField = true;
				canChooseMultiple = true;
				isOptional = true; //TODO check if this is optional
				break;
				
			case 54: //Fliegende Klatsche
				isAcceptable = true; //todo pretty sure this is right but better check
				break;
				
			default:
				actor = null;
		}
		
		setOptionalIfNeeded();
	}
	
	
	public void advanceProgress(){
		progress++;
		switch (cardId){
			//not needed yet (Here attributes can be changed from one step to the next)
		}
		setOptionalIfNeeded();
	}
	
	
	
	
	private void setOptionalIfNeeded(){
		if (!isOptional && !isAutomatic && !validOptionAvailable())
			isOptional = true;
	}
	
	private boolean validOptionAvailable(){
		if ((canChooseFromOwnHand && (fromOwnHandAllowed==null || !fromOwnHandAllowed.isEmpty()) && !actor.isHandEmpty())
			|| (!canChooseMultiple && canChooseFromOwnField && (fromOwnFieldAllowed==null || !fromOwnFieldAllowed.isEmpty()) && !actor.isFieldEmpty())
			|| (!canChooseMultiple && canChooseFromOpponentField && (fromOpponentFieldAllowed==null || !fromOpponentFieldAllowed.isEmpty()) && !actor.getOpponent().isFieldEmpty())
			|| (canChooseMultiple 
				&& (!canChooseFromOwnField ||(canChooseFromOwnField && (fromOwnFieldAllowed==null || !fromOwnFieldAllowed.isEmpty()) && !actor.isFieldEmpty()))
				&& (!canChooseFromOpponentField || (canChooseFromOpponentField && (fromOpponentFieldAllowed==null || !fromOpponentFieldAllowed.isEmpty()) && !actor.getOpponent().isFieldEmpty()))
				)
			|| (!canChooseMultiple && canChooseFromOwnDiscard && (fromOwnDiscardAllowed==null || !fromOwnDiscardAllowed.isEmpty()) && !actor.isDiscardEmpty())
			|| (canChooseMultiple && canChooseFromOwnDiscard && (fromOwnDiscardAllowed==null || !fromOwnDiscardAllowed.isEmpty()) && !actor.isDiscardEmpty())
		)
			return true;
		else
			return false;
	}
	
	public int getProgress(){
		return progress;
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
	
	public boolean isAcceptable(){
		return isAcceptable;
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
	
	public boolean canChooseFromOwnDiscard(){
		return canChooseFromOwnDiscard;
	}
	
	public List<Integer> fromOwnDiscardAllowed(){
		return fromOwnDiscardAllowed;
	}
	
	public boolean canChooseMultiple(){
		return canChooseMultiple;
	}
}