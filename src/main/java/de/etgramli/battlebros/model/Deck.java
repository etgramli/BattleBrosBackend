package de.etgramli.battlebros.model;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<Card> cards = new ArrayList<>();

    public Deck(){}

    public List<Card> getCards(){
        return cards;
    }

    public boolean checkIfLegal(){
        return true;
    }
}
