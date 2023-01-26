package de.etgramli.battlebros.model;

import java.util.List;

public class ResolvableAbility {
	
	private int cardId;
	private Player actor = null;
	private boolean isOptional = false;
	
	private boolean canChooseFromOwnHand = false;
	private List<Integer> fromOwnHandExcept;
	
	private boolean canChooseFromOwnField = false;
	private List<Integer> fromOwnFieldExcept;
	
	private boolean canChooseFromOpponentField = false;
	private List<Integer> fromOpponentFieldExcept;
	
	private boolean canChooseFromOwnDiscard = false;
	private List<Integer> fromOwnDiscardExcept;
	
	private boolean canChooseFromOpponentDiscard = false;
	private List<Integer> fromOpponentDiscardExcept;
	
	
	public ResolvableAbility(int cardId, Player activator, int gameFieldPosition){
		this.cardId = cardId;
		switch (cardId){
			case 2: //Ausbrecher
				actor = activator;
				isOptional = true;
				canChooseFromOwnField = true;
				fromOwnFieldExcept = List.of(gameFieldPosition); //todo falsch: beliebigen "offenen bro", nicht "anderen bro" verdecken
				canChooseFromOpponentField = true;
				break;
			case 3: //Flammenwerfer
				//todo
				break;
			case 5: //Verascher
				actor = activator.getOpponent();
				canChooseFromOwnField = true;
				//todo alle in except aufnehmen außer die mit dem höchsten wert
				canChooseFromOpponentField = true;
				//todo alle in except aufnehmen außer die mit dem höchsten wert
		}
	}
	
	public int getCardId(){
		return cardId;
	}
	
	public Player getActor(){
		return actor;
	}
	
	public boolean isOptional(){
		return isOptional;
	}
	
	public boolean canChooseFromOwnHand(){
		return canChooseFromOwnHand;
	}
	public List<Integer> fromOwnHandExcept(){
		return fromOwnHandExcept;
	}
	
	public boolean canChooseFromOwnField(){
		return canChooseFromOwnField;
	}
	public List<Integer> fromOwnFieldExcept(){
		return fromOwnFieldExcept;
	}
	
	public boolean canChooseFromOpponentField(){
		return canChooseFromOpponentField;
	}
	public List<Integer> fromOpponentFieldExcept(){
		return fromOpponentFieldExcept;
	}
}