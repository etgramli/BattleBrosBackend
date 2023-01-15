package de.etgramli.battlebros.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void name() {
        Player player = new Player("Johnny", null);
        Assertions.assertTrue(player.getName().equals("Johnny"));
        Assertions.assertFalse(player.getName().equals("Johhhhhhhjnny"));
    }
}