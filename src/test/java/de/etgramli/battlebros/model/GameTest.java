package de.etgramli.battlebros.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void gameTest() {
        Deck deck1 = new Deck();
        deck1.addCard(Card.getCard(1));
        deck1.addCard(Card.getCard(2));
        deck1.addCard(Card.getCard(3));
        deck1.addCard(Card.getCard(4));
        deck1.addCard(Card.getCard(5));
        deck1.addCard(Card.getCard(6));
        deck1.addCard(Card.getCard(7));
        deck1.addCard(Card.getCard(8));
        deck1.addCard(Card.getCard(9));
        deck1.addCard(Card.getCard(10));
        deck1.addCard(Card.getCard(11));
        deck1.addCard(Card.getCard(12));
        deck1.addCard(Card.getCard(13));
        deck1.addCard(Card.getCard(14));
        deck1.addCard(Card.getCard(15));
        deck1.addCard(Card.getCard(16));
        deck1.addCard(Card.getCard(17));
        deck1.addCard(Card.getCard(18));
        deck1.addCard(Card.getCard(19));
        deck1.addCard(Card.getCard(20));
        assertTrue(deck1.checkIfLegal());

        List<Card> listOfCardsForDeck2 = new ArrayList<>();
        listOfCardsForDeck2.add(Card.getCard(21));
        listOfCardsForDeck2.add(Card.getCard(22));
        listOfCardsForDeck2.add(Card.getCard(23));
        listOfCardsForDeck2.add(Card.getCard(24));
        listOfCardsForDeck2.add(Card.getCard(25));
        listOfCardsForDeck2.add(Card.getCard(26));
        listOfCardsForDeck2.add(Card.getCard(27));
        listOfCardsForDeck2.add(Card.getCard(28));
        listOfCardsForDeck2.add(Card.getCard(29));
        listOfCardsForDeck2.add(Card.getCard(30));
        listOfCardsForDeck2.add(Card.getCard(31));
        listOfCardsForDeck2.add(Card.getCard(32));
        listOfCardsForDeck2.add(Card.getCard(33));
        listOfCardsForDeck2.add(Card.getCard(34));
        listOfCardsForDeck2.add(Card.getCard(35));
        listOfCardsForDeck2.add(Card.getCard(36));
        listOfCardsForDeck2.add(Card.getCard(37));
        listOfCardsForDeck2.add(Card.getCard(38));
        listOfCardsForDeck2.add(Card.getCard(39));
        listOfCardsForDeck2.add(Card.getCard(40));
        Deck deck2 = new Deck(listOfCardsForDeck2);
        assertTrue(deck2.checkIfLegal());

        Player player1 = new Player("Joshman, Epic G8mer", deck1);
        Player player2 = new Player("Java_Jim, L33tH4xx0r", deck2);

        Game game = new Game(player1, player2);

        game.startGame();
        drawGameState(player1, player2);

        assertTrue(game.playCard(game.getTurnPlayerIndex(), 0, 0));
        drawGameState(player1, player2);
        
        assertTrue(game.playCard(game.getTurnPlayerIndex(), 0, 0));
        drawGameState(player1, player2);

        assertTrue(game.playCard(game.getTurnPlayerIndex(), 0, 1));
        drawGameState(player1, player2);

        game.pass();
        drawGameState(player1, player2);

        assertTrue(game.playCard(game.getTurnPlayerIndex(), 0, -1));
        drawGameState(player1, player2);

        game.pass();

    }

    void drawGameState(Player player1, Player player2) {
        drawPlayerState(player1);
        drawPlayerState(player2);
        System.out.println();
    }

    void drawPlayerState(Player player){
        System.out.println(player.getName());
        System.out.printf("Hand: ");
        for (Card card : player.getCardsInHand())
            System.out.printf(card.getName() + "  ");
        System.out.printf("\nCards on Field: ");
        for (Map.Entry<Integer, Card> entry : player.getCardsOnField().entrySet())
            System.out.printf("[" + entry.getKey() + ": "+ entry.getValue().getName() + "]  ");
        System.out.println("\nTotalValue: " + player.getTotalValue());
        System.out.println("Life: " + player.getAmountOfLifeCards());
    }

}