package de.etgramli.battlebros.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbilityTest008_009 {
	
	//Remember:
	// At the beginning of a game: 3 life cards are added THEN 6 hand cards are drawn
	// Top card of the deck = index 0 of gameZoneDeck
	// so: first 3 cards of deck go to life & next 6 cards after that go to hand (deckIdx 3 == handIndx 0)

	int magmannId = 8;
	int lavaboyId = 9;
	int firstPlayerIdx = 0;
	int secondPlayerIdx = 1;

    @Test
	void magmannLavaboyTest1() {
		
		//MAKE PLAYERS + GAME
		Deck deck1 = new Deck();
		deck1.addCard(Card.getCard(18));
        deck1.addCard(Card.getCard(19));
        deck1.addCard(Card.getCard(20));
        deck1.addCard(Card.getCard(magmannId)); //card will be index 0 in hand
        deck1.addCard(Card.getCard(lavaboyId)); //index 1
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

        List<Card> listOfCardsForDeck2 = new ArrayList<>();
		listOfCardsForDeck2.add(Card.getCard(18));
        listOfCardsForDeck2.add(Card.getCard(19));
        listOfCardsForDeck2.add(Card.getCard(20));
        listOfCardsForDeck2.add(Card.getCard(magmannId)); //card will be index 0 in hand
        listOfCardsForDeck2.add(Card.getCard(lavaboyId));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        Deck deck2 = new Deck(listOfCardsForDeck2);

        Player player1 = new Player("Josh-1", deck1);
        Player player2 = new Player("Eti-2", deck2);
		
		Game game = new Game(player1, player2);

		game.startGameWithoutShuffling();
		
		System.out.println(game.getCardIDsInHand(firstPlayerIdx));
		System.out.println(game.getCardIDsInHand(secondPlayerIdx));
		Assertions.assertTrue(game.getCardsInHand(firstPlayerIdx).get(0).getId() == magmannId);
		Assertions.assertTrue(game.getCardsInHand(secondPlayerIdx).get(0).getId() == magmannId);
		
		//both players have 6 cards in hand
		Assertions.assertTrue(game.getPlayer(firstPlayerIdx).getAmountOfCardsInHand() == 6);
		Assertions.assertTrue(game.getPlayer(secondPlayerIdx).getAmountOfCardsInHand() == 6);
		
		//player 1 plays their lavaboy, player 2 draws a card and now has 7 cards in hand
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 1, 0));
		Assertions.assertTrue(game.getPlayer(firstPlayerIdx).getAmountOfCardsInHand() == 5);
		Assertions.assertTrue(game.getPlayer(secondPlayerIdx).getAmountOfCardsInHand() == 7);
		
		//player 2 plays their magmann
		Assertions.assertTrue(game.playCard(secondPlayerIdx, 0, 0));
		
		//player 1 passes
		Assertions.assertTrue(game.pass(firstPlayerIdx));
		
		//player 2 plays their lavaboy, player 1 does not draw any cards because of magmann
		Assertions.assertTrue(game.getPlayer(firstPlayerIdx).getAmountOfCardsInHand() == 5);
		Assertions.assertTrue(game.getPlayer(secondPlayerIdx).getAmountOfCardsInHand() == 6);
		
		Assertions.assertTrue(game.playCard(secondPlayerIdx, 0, 1));
		
		Assertions.assertTrue(game.getPlayer(firstPlayerIdx).getAmountOfCardsInHand() == 5);
		Assertions.assertTrue(game.getPlayer(secondPlayerIdx).getAmountOfCardsInHand() == 5);
		
	}
	
	
	@Test
	void magmannLavaboyTest2() {
		
		//MAKE PLAYERS + GAME
		Deck deck1 = new Deck();
		deck1.addCard(Card.getCard(18));
        deck1.addCard(Card.getCard(19));
        deck1.addCard(Card.getCard(20));
        deck1.addCard(Card.getCard(magmannId)); //card will be index 0 in hand
        deck1.addCard(Card.getCard(lavaboyId)); //index 1
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

        List<Card> listOfCardsForDeck2 = new ArrayList<>();
		listOfCardsForDeck2.add(Card.getCard(18));
        listOfCardsForDeck2.add(Card.getCard(19));
        listOfCardsForDeck2.add(Card.getCard(20));
        listOfCardsForDeck2.add(Card.getCard(magmannId)); //card will be index 0 in hand
        listOfCardsForDeck2.add(Card.getCard(lavaboyId));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        Deck deck2 = new Deck(listOfCardsForDeck2);

        Player player1 = new Player("Josh-1", deck1);
        Player player2 = new Player("Eti-2", deck2);
		
		Game game = new Game(player1, player2);

		game.startGameWithoutShuffling();
		
		System.out.println(game.getCardIDsInHand(firstPlayerIdx));
		System.out.println(game.getCardIDsInHand(secondPlayerIdx));
		Assertions.assertTrue(game.getCardsInHand(firstPlayerIdx).get(0).getId() == magmannId);
		Assertions.assertTrue(game.getCardsInHand(secondPlayerIdx).get(0).getId() == magmannId);
		
		//both players have 6 cards in hand
		Assertions.assertTrue(game.getPlayer(firstPlayerIdx).getAmountOfCardsInHand() == 6);
		Assertions.assertTrue(game.getPlayer(secondPlayerIdx).getAmountOfCardsInHand() == 6);
		
		//player 1 plays their magmann
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 0, 0));
		Assertions.assertTrue(game.getPlayer(firstPlayerIdx).getAmountOfCardsInHand() == 5);
		Assertions.assertTrue(game.getPlayer(secondPlayerIdx).getAmountOfCardsInHand() == 6);
		
		//player 2 plays their lavaboy, player 1 draws a card
		Assertions.assertTrue(game.playCard(secondPlayerIdx, 1, 0));
		
		Assertions.assertTrue(game.getPlayer(firstPlayerIdx).getAmountOfCardsInHand() == 6);
		Assertions.assertTrue(game.getPlayer(secondPlayerIdx).getAmountOfCardsInHand() == 5);
		
		//player 1 plays their lavaboy, player 2 doesn't get to draw
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 0, 1));
		
		Assertions.assertTrue(game.getPlayer(firstPlayerIdx).getAmountOfCardsInHand() == 5);
		Assertions.assertTrue(game.getPlayer(secondPlayerIdx).getAmountOfCardsInHand() == 5);
		
		
		
	}
	
	@Test
	void magmannLavaboyTest3() {
		int ausbrecherId = 2;
		
		//MAKE PLAYERS + GAME
		Deck deck1 = new Deck();
		deck1.addCard(Card.getCard(18));
        deck1.addCard(Card.getCard(19));
        deck1.addCard(Card.getCard(20));
        deck1.addCard(Card.getCard(magmannId)); //card will be index 0 in hand
        deck1.addCard(Card.getCard(lavaboyId)); //index 1
        deck1.addCard(Card.getCard(ausbrecherId));
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

        List<Card> listOfCardsForDeck2 = new ArrayList<>();
		listOfCardsForDeck2.add(Card.getCard(18));
        listOfCardsForDeck2.add(Card.getCard(19));
        listOfCardsForDeck2.add(Card.getCard(20));
        listOfCardsForDeck2.add(Card.getCard(magmannId)); //card will be index 0 in hand
        listOfCardsForDeck2.add(Card.getCard(lavaboyId));
        listOfCardsForDeck2.add(Card.getCard(ausbrecherId));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(1));
        Deck deck2 = new Deck(listOfCardsForDeck2);

        Player player1 = new Player("Josh-1", deck1);
        Player player2 = new Player("Eti-2", deck2);
		
		Game game = new Game(player1, player2);

		game.startGameWithoutShuffling();
		
		System.out.println(game.getCardIDsInHand(firstPlayerIdx));
		System.out.println(game.getCardIDsInHand(secondPlayerIdx));
		Assertions.assertTrue(game.getCardsInHand(firstPlayerIdx).get(0).getId() == magmannId);
		Assertions.assertTrue(game.getCardsInHand(secondPlayerIdx).get(0).getId() == magmannId);
		
		//both players have 6 cards in hand
		Assertions.assertTrue(game.getPlayer(firstPlayerIdx).getAmountOfCardsInHand() == 6);
		Assertions.assertTrue(game.getPlayer(secondPlayerIdx).getAmountOfCardsInHand() == 6);
		
		//player 1 plays their magmann
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 0, 0));
		Assertions.assertTrue(game.getPlayer(firstPlayerIdx).getAmountOfCardsInHand() == 5);
		Assertions.assertTrue(game.getPlayer(secondPlayerIdx).getAmountOfCardsInHand() == 6);
		
		//player 2 plays their ausbrecher in order to flip magmann face down
		Assertions.assertTrue(game.playCard(secondPlayerIdx, 2, 0));
		Assertions.assertTrue(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, 0));
		
		Assertions.assertTrue(game.getPlayer(firstPlayerIdx).getAmountOfCardsInHand() == 5);
		Assertions.assertTrue(game.getPlayer(secondPlayerIdx).getAmountOfCardsInHand() == 5);
		
		//player 1 plays their lavaboy, player 2 gets to draw, because even though player 1 has a magmann on their field, it is face down
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 0, 1));
		
		Assertions.assertTrue(game.getPlayer(firstPlayerIdx).getAmountOfCardsInHand() == 4);
		Assertions.assertTrue(game.getPlayer(secondPlayerIdx).getAmountOfCardsInHand() == 6);
		
		
		
	}
	
	
}