package de.etgramli.battlebros.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void createNewCards() {
        Card card = Card.getCard(1);
        Assertions.assertEquals(card.getValue(), 3);
        Assertions.assertEquals(card.getName(), "Feuersalamander");

        card = Card.getCard(11);
        Assertions.assertEquals(card.getValue(), 5);
        Assertions.assertEquals(card.getName(), "Abbrenngolem");
    }

}