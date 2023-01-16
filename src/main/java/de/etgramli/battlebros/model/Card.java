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
        cardCatalogue.add(new Card(12, "Heißer Feger", 0, new ArrayList<>(List.of(Element.FIRE))));
        cardCatalogue.add(new Card(13, "Vulklon", 0, new ArrayList<>(List.of(Element.FIRE))));
        cardCatalogue.add(new Card(14, "Streichelholz", 2, new ArrayList<>(List.of(Element.FIRE))));
        cardCatalogue.add(new Card(15, "Anfeuerer", 2, new ArrayList<>(List.of(Element.FIRE))));
        cardCatalogue.add(new Card(16, "Hitzkopf", 3, new ArrayList<>(List.of(Element.FIRE))));
        cardCatalogue.add(new Card(17, "Kohlkopf", 1, new ArrayList<>(List.of(Element.FIRE))));
        cardCatalogue.add(new Card(18, "Wasserläufer", 3, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(19, "Haihammer", 1, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(20, "Senkschlange", 2, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(21, "Aquak", 0, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(22, "Seemannsgarnele", 1, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(23, "Nagellachs", 1, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(24, "Orakel von Delfin", 2, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(25, "Unterweltfährmann", 0, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(26, "U.B.O.", 4, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(27, "Welsbrocken", 5, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(28, "Fesslerkraken", 1, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(29, "Flebbe und Ut", 2, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(30, "Meeresfrüchte", 1, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(31, "Heilqualle", 1, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(32, "Toller Hecht", 1, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(33, "Walnuss", 2, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(34, "Schildfisch", 2, new ArrayList<>(List.of(Element.WATER))));
        cardCatalogue.add(new Card(35, "Erdwurm", 3, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(36, "Katerpult", 1, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(37, "Rammbock", 2, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(38, "Zombiene", 3, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(39, "Fleischwolf", 3, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(40, "Wucherer", 5, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(41, "Geröllakämpfer", 6, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(42, "Goldgolem", 5, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(43, "Blumenstrauß", 0, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(44, "Buddelwurf", 2, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(45, "Extrablatt", 3, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(46, "Felsenfest", 3, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(47, "Holzkopf", 3, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(48, "Gärtnerzwerg", 3, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(49, "Drahtesel", 3, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(50, "Blätterdach", 2, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(51, "Baumkrone", 3, new ArrayList<>(List.of(Element.EARTH))));
        cardCatalogue.add(new Card(52, "Luftschlange", 3, new ArrayList<>(List.of(Element.AIR))));
        cardCatalogue.add(new Card(53, "Gittermastkranich", 0, new ArrayList<>(List.of(Element.AIR))));

    }

}


