package de.etgramli.battlebros.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameField {
    private Map<Integer, Card> cards  = new HashMap<Integer, Card>();
	private Map<Integer, Boolean> cardFacings = new HashMap<Integer, Boolean>();

    public GameField(){
    }

    public List<Card> getListOfAllCards(){
        List<Card> result = new ArrayList<>();
        for (Map.Entry<Integer, Card> entry : cards.entrySet())
            result.add(entry.getValue());
        return result;
    }

    public Map<Integer, Card> getAllCards(){
        return cards;
    }

    public List<Integer> getAllTakenPositions(){
        List<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, Card> entry : cards.entrySet())
            result.add(entry.getKey());
        return result;
    }
	
	 public List<Integer> getAllFaceDownPositions(){
        List<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, Boolean> entry : cardFacings.entrySet()){
            if (!entry.getValue())
				result.add(entry.getKey());
		}
        return result;
    }

    public Map<Integer, Card> getCards(){
        return cards;
    }

    public int getAmountOfCards(){
        return cards.size();
    }

    public void addCard(Card card, int position){
        cards.put(position, card);
		cardFacings.put(position, true);
    }
    public Card getCard(int position){
        return cards.get(position);
    }
	
	public boolean isCardFaceUp(int position){
		return cardFacings.get(position);
	}
	
	public boolean turnCard(boolean faceUp, int position){
		if (cards.get(position) == null)
			return false;
		
		cardFacings.put(position, faceUp);
		return true;
	}

    public Card removeCard(int position){
		cardFacings.remove(position);
        return cards.remove(position);
    }
}
