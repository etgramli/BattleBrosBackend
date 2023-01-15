package de.etgramli.battlebros.model;

import java.util.ArrayList;
import java.util.List;

public class GameZone {

    private List<Card> cards  = new ArrayList<>();
    private boolean isVisibleToOwner;
    private boolean isVisibleToOpponent;

    public GameZone(boolean isVisibleToOwner, boolean isVisibleToOpponent){
        this.isVisibleToOwner = isVisibleToOwner;
        this.isVisibleToOpponent = isVisibleToOpponent;
    }

    public int getAmountOfCards(){
        return cards.size();
    }

    public void addCard(Card card){
        cards.add(card);
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

}