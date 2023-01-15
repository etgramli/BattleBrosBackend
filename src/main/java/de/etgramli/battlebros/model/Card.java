package de.etgramli.battlebros.model;

import java.util.ArrayList;
import java.util.List;

public class Card {

    private static List<Card> cardCatalogue = null;
    private int id;
    private String name;

    private int value;
    private List<Element> elements = new ArrayList<>();

    public int getValue(){
        return value;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public List<Element> getElements(){
        return elements;
    }

    public static Card getCard(int id){
        initializeCardCatalogue();
        return cardCatalogue.get(id - 1);
    }

    private Card(int id,
                 String name,
                 int value,
                 List<Element> elements){
        this.id = id;
        this.name = name;
        this.value = value;
        this.elements.addAll(elements);
    }

    private static void initializeCardCatalogue(){
        if (cardCatalogue != null)
            return;
        cardCatalogue = new ArrayList<Card>();
        cardCatalogue.add(new Card(1, "Feuersalamander", 3, new ArrayList<>(List.of(Element.FIRE))));
        cardCatalogue.add(new Card(2, "Ausbrecher", 0, new ArrayList<>(List.of(Element.FIRE))));
        cardCatalogue.add(new Card(3, "Flammenwerfer", 0, new ArrayList<>(List.of(Element.FIRE))));
        cardCatalogue.add(new Card(4, "Kanonenfutterer", 2, new ArrayList<>(List.of(Element.FIRE))));
        cardCatalogue.add(new Card(5, "Verascher", 2, new ArrayList<>(List.of(Element.FIRE))));
        cardCatalogue.add(new Card(6, "Fönix", 3, new ArrayList<>(List.of(Element.FIRE))));
        cardCatalogue.add(new Card(7, "Potzblitz", 1, new ArrayList<>(List.of(Element.FIRE))));
        cardCatalogue.add(new Card(8, "Magmann", 2, new ArrayList<>(List.of(Element.FIRE))));
        cardCatalogue.add(new Card(9, "Lavaboy", 4, new ArrayList<>(List.of(Element.FIRE))));
        cardCatalogue.add(new Card(10, "Fackeldackel", 4, new ArrayList<>(List.of(Element.FIRE))));
        cardCatalogue.add(new Card(11, "Abbrenngolem", 5, new ArrayList<>(List.of(Element.FIRE))));
    }

}


