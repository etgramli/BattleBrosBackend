package de.etgramli.battlebros.model;

import java.util.ArrayList;
import java.util.List;

public class Card {

    private final int id;
    private final String name;

    private final int value;
    private final List<Element> elements;

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
        return List.copyOf(elements);
    }

    public static Card getCard(int id){
        return cardCatalogue.get(id - 1);
    }

    private Card(int id,
                 String name,
                 int value,
                 List<Element> elements){
        this.id = id;
        this.name = name;
        this.value = value;
        this.elements = new ArrayList<>(elements);
    }


    private static final List<Card> cardCatalogue = List.of(
        new Card(1, "Feuersalamander", 3, List.of(Element.FIRE)),
        new Card(2, "Ausbrecher", 0, List.of(Element.FIRE)),
        new Card(3, "Flammenwerfer", 0, List.of(Element.FIRE)),
        new Card(4, "Kanonenfutterer", 2, List.of(Element.FIRE)),
        new Card(5, "Verascher", 2, List.of(Element.FIRE)),
        new Card(6, "Fönix", 3, List.of(Element.FIRE)),
        new Card(7, "Potzblitz", 1, List.of(Element.FIRE)),
        new Card(8, "Magmann", 2, List.of(Element.FIRE)),
        new Card(9, "Lavaboy", 4, List.of(Element.FIRE)),
        new Card(10, "Fackeldackel", 4, List.of(Element.FIRE)),
        new Card(11, "Abbrenngolem", 5, List.of(Element.FIRE)),
        new Card(12, "Heißer Feger", 0, List.of(Element.FIRE)),
        new Card(13, "Vulklon", 0, List.of(Element.FIRE)),
        new Card(14, "Streichelholz", 2, List.of(Element.FIRE)),
        new Card(15, "Anfeuerer", 2, List.of(Element.FIRE)),
        new Card(16, "Hitzkopf", 3, List.of(Element.FIRE)),
        new Card(17, "Kohlkopf", 1, List.of(Element.FIRE)),
        new Card(18, "Wasserläufer", 3, List.of(Element.WATER)),
        new Card(19, "Haihammer", 1, List.of(Element.WATER)),
        new Card(20, "Senkschlange", 2, List.of(Element.WATER)),
        new Card(21, "Aquak", 0, List.of(Element.WATER)),
        new Card(22, "Seemannsgarnele", 1, List.of(Element.WATER)),
        new Card(23, "Nagellachs", 1, List.of(Element.WATER)),
        new Card(24, "Orakel von Delfin", 2, List.of(Element.WATER)),
        new Card(25, "Unterweltfährmann", 0, List.of(Element.WATER)),
        new Card(26, "U.B.O.", 4, List.of(Element.WATER)),
        new Card(27, "Welsbrocken", 5, List.of(Element.WATER)),
        new Card(28, "Fesslerkraken", 1, List.of(Element.WATER)),
        new Card(29, "Flebbe und Ut", 2, List.of(Element.WATER)),
        new Card(30, "Meeresfrüchte", 1, List.of(Element.WATER)),
        new Card(31, "Heilqualle", 1, List.of(Element.WATER)),
        new Card(32, "Toller Hecht", 1, List.of(Element.WATER)),
        new Card(33, "Walnuss", 2, List.of(Element.WATER)),
        new Card(34, "Schildfisch", 2, List.of(Element.WATER)),
        new Card(35, "Erdwurm", 3, List.of(Element.EARTH)),
        new Card(36, "Katerpult", 1, List.of(Element.EARTH)),
        new Card(37, "Rammbock", 2, List.of(Element.EARTH)),
        new Card(38, "Zombiene", 3, List.of(Element.EARTH)),
        new Card(39, "Fleischwolf", 3, List.of(Element.EARTH)),
        new Card(40, "Wucherer", 5, List.of(Element.EARTH)),
        new Card(41, "Geröllakämpfer", 6, List.of(Element.EARTH)),
        new Card(42, "Goldgolem", 5, List.of(Element.EARTH)),
        new Card(43, "Blumenstrauß", 0, List.of(Element.EARTH)),
        new Card(44, "Buddelwurf", 2, List.of(Element.EARTH)),
        new Card(45, "Extrablatt", 3, List.of(Element.EARTH)),
        new Card(46, "Felsenfest", 3, List.of(Element.EARTH)),
        new Card(47, "Holzkopf", 3, List.of(Element.EARTH)),
        new Card(48, "Gärtnerzwerg", 3, List.of(Element.EARTH)),
        new Card(49, "Drahtesel", 3, List.of(Element.EARTH)),
        new Card(50, "Blätterdach", 2, List.of(Element.EARTH)),
        new Card(51, "Baumkrone", 3, List.of(Element.EARTH)),
        new Card(52, "Luftschlange", 3, List.of(Element.AIR)),
        new Card(53, "Gittermastkranich", 0, List.of(Element.AIR))
    );
}
