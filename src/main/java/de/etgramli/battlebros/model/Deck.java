package de.etgramli.battlebros.model;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<Card> cards = new ArrayList<>();

    public Deck(){}
    public Deck(List<Card> cards){
        this.cards.addAll(cards);
    }

    public boolean addCard(Card card){
        if (cards.contains(card))
            return false;
        cards.add(card);
        return true;
    }

    public boolean addCards(List<Card> cards){
        boolean atLeastOneSuccessful = false;
        for (Card card : cards) {
            if (addCard(card))
                atLeastOneSuccessful = true;
        }
        return atLeastOneSuccessful;
    }

    public boolean removeCard(int index){
        if (index < 0 || index >= cards.size())
            return false;
        cards.remove(index);
        return true;
    }

    public boolean removeCard(Card card){
        if (!cards.contains(card))
            return false;
        cards.remove(card);
        return true;
    }

    public List<Card> getCards(){
        return cards;
    }

    public boolean checkIfLegal(){
        return cards.size() == 20;
    }
}
