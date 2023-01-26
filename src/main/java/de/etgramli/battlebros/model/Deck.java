package de.etgramli.battlebros.model;

import java.util.*;
import java.util.stream.Stream;

public class Deck {
    public static final Map<String, Deck> DECKS = Map.of(
            "Feurio!", new Deck(Stream.of(1, 2, 3, 4, 5, 7, 8, 9, 10, 12, 13, 14, 15, 16, 17, 19, 25, 36, 53, 62).map(Card::getCard).toList()),
            "The River is flowing", new Deck(Stream.of(18, 19, 20, 21, 22, 23, 24, 25, 26, 28, 29, 30, 31, 32, 33, 34, 43, 48, 53, 63).map(Card::getCard).toList())
    );

    static {
        assert DECKS.values().stream().allMatch(Deck::checkIfLegal);
    }

    private Set<Card> cards = new HashSet<>();

    public Deck(){}
    public Deck(Collection<Card> cards){
        this.cards.addAll(cards);
    }

    public boolean addCard(Card card){
        //if (cards.contains(card))
        //    return false;
        //cards.add(card);
        //return true;
		return cards.add(card);
    }

    public boolean addCards(Collection<Card> cards){
        boolean atLeastOneSuccessful = false;
        for (Card card : cards) {
            if (addCard(card))
                atLeastOneSuccessful = true;
        }
        return atLeastOneSuccessful;
    }

    /*public boolean removeCard(int index){
        if (index < 0 || index >= cards.size())
            return false;
        cards.remove(index);
        return true;
    }*/

    public boolean removeCard(Card card){
        /*if (!cards.contains(card))
            return false;
        cards.remove(card);
        return true;*/
		return cards.remove(card);
    }

    public List<Card> getCards(){
        return new ArrayList<>(cards);
    }

    public boolean checkIfLegal(){
        // ToDo: Hallo Herr Game-Designer. Muss hier auch auf Dupletten geprüft werden? Oder darf man eine Karte auch mehrfach im Deck haben?!?
        // Und man könnte diese Methode im Konstruktor aufrufen und ggf eine IllegalArgumentException werfen
		
		// Ja hoi, dupletten sind verboten, das muss hier gecheckt werden. Dafür gibts doch bestimmt eine java collections methode oder so, oder?
		// Im Konstruktor aufrufen ist evtl blöd, weil ich fände es schöner wenn man im deckeditor auch nicht-legale decks bauen darf, aber bei denen
		// dann einfach ein dicker roter textlabel darauf aufmerksam macht, dass das deck nicht legal ist.
		// LG, Josh der geniale Game Design Virtuoso
		// EDIT: Ich hab die liste mit karten hier jetzt von einer List zu einem Set gemacht, dann kann man keine dupletten haben.
        return cards.size() == 20;
    }
}
