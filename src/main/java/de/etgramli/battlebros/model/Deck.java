package de.etgramli.battlebros.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Deck {
    public static final Map<String, Deck> DECKS = Map.of(
            "Feurio!", new Deck(Stream.of(1, 2, 3, 4, 5, 7, 8, 9, 10, 12, 13, 14, 15, 16, 17, 19, 25, 36, 53, 62).map(Card::getCard).toList()),
            "The River is flowing", new Deck(Stream.of(18, 19, 20, 21, 22, 23, 24, 25, 26, 28, 29, 30, 31, 32, 33, 34, 43, 48, 53, 63).map(Card::getCard).toList())
    );

    static {
        assert DECKS.values().stream().allMatch(Deck::checkIfLegal);
    }

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
        // ToDo: Hallo Herr Game-Designer. Muss hier auch auf Dupletten geprüft werden? Oder darf man eine Karte auch mehrfach im Deck haben?!?
        // Und man könnte diese Methode im Konstruktor aufrufen und ggf eine IllegalArgumentException werfen
        return cards.size() == 20;
    }
}
