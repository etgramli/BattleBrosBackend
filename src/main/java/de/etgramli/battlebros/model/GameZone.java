package de.etgramli.battlebros.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameZone {

    private final List<Card> cards  = new ArrayList<>();
    private final boolean isVisibleToOwner;
    private final boolean isVisibleToOpponent;

    public GameZone(boolean isVisibleToOwner, boolean isVisibleToOpponent){
        this.isVisibleToOwner = isVisibleToOwner;
        this.isVisibleToOpponent = isVisibleToOpponent;
    }

    public int getAmountOfCards(){
        return cards.size();
    }
	
	public int getAmountOfCardsWithElement(Element element){
		int result = 0;
		for (Card card : cards){
			if (card.getElements().contains(element))
				result++;
		}
		return result;
	}

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public void addCard(Card card){
        cards.add(card);
    }
	
	public void addCardToTop(Card card){
		cards.add(0, card);
	}

    public void addCards(List<Card> cards){
        this.cards.addAll(cards);
    }

    public Card removeCard(int position){
        return cards.remove(position);
    }

    public Card getCard(int position){
        return cards.get(position);
    }

    public List<Card> getCards(){
        return cards;
    }

}
