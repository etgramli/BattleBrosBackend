package de.etgramli.battlebros.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbilityTest002 {
	
	//Remember:
	// At the beginning of a game: 3 life cards are added THEN 6 hand cards are drawn
	// Top card of the deck = index 0 of gameZoneDeck
	// so: first 3 cards of deck go to life & next 6 cards after that go to hand (deckIdx 3 == handIndx 0)

    @Test
    void testAusbrecher1() {
		int ausbrecherId = 2;
		int firstPlayerIdx = 0;
		int secondPlayerIdx = 1;
		
		//MAKE PLAYERS + GAME
		Deck deck1 = new Deck();
		deck1.addCard(Card.getCard(18));
        deck1.addCard(Card.getCard(19));
        deck1.addCard(Card.getCard(20));
        deck1.addCard(Card.getCard(ausbrecherId)); //ausbrecher will be index 0 in hand
        deck1.addCard(Card.getCard(1));
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
        assertTrue(deck1.checkIfLegal());

        List<Card> listOfCardsForDeck2 = new ArrayList<>();
		listOfCardsForDeck2.add(Card.getCard(18));
        listOfCardsForDeck2.add(Card.getCard(19));
        listOfCardsForDeck2.add(Card.getCard(20));
        listOfCardsForDeck2.add(Card.getCard(ausbrecherId)); //ausbrecher will be index 0 in hand
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(3));
        listOfCardsForDeck2.add(Card.getCard(4));
        listOfCardsForDeck2.add(Card.getCard(5));
        listOfCardsForDeck2.add(Card.getCard(6));
        listOfCardsForDeck2.add(Card.getCard(7));
        listOfCardsForDeck2.add(Card.getCard(8));
        listOfCardsForDeck2.add(Card.getCard(9));
        listOfCardsForDeck2.add(Card.getCard(10));
        listOfCardsForDeck2.add(Card.getCard(11));
        listOfCardsForDeck2.add(Card.getCard(12));
        listOfCardsForDeck2.add(Card.getCard(13));
        listOfCardsForDeck2.add(Card.getCard(14));
        listOfCardsForDeck2.add(Card.getCard(15));
        listOfCardsForDeck2.add(Card.getCard(16));
        listOfCardsForDeck2.add(Card.getCard(17));
        Deck deck2 = new Deck(listOfCardsForDeck2);
        assertTrue(deck2.checkIfLegal());

        Player player1 = new Player("Josh", deck1);
        Player player2 = new Player("Eti", deck2);
		
		Game game = new Game(player1, player2);

		game.startGameWithoutShuffling();
		System.out.println(game.getCardIDsInHand(firstPlayerIdx));
		System.out.println(game.getCardIDsInHand(secondPlayerIdx));
		Assertions.assertTrue(game.getCardsInHand(firstPlayerIdx).get(0).getId() == ausbrecherId);
		Assertions.assertTrue(game.getCardsInHand(secondPlayerIdx).get(0).getId() == ausbrecherId);

		{ //second player tries to do actions (can't because it't the other player's turn) 
			Assertions.assertFalse(game.playCard(secondPlayerIdx, 0, 0));
			Assertions.assertFalse(game.playCard(secondPlayerIdx, 1, 0));
			Assertions.assertFalse(game.playCard(secondPlayerIdx, 2, 0));
			Assertions.assertFalse(game.playCard(secondPlayerIdx, 2, 1));
			Assertions.assertFalse(game.playCard(secondPlayerIdx, 2, 2));
			Assertions.assertFalse(game.playCard(secondPlayerIdx, 2, -1));
			Assertions.assertFalse(game.playCard(secondPlayerIdx, 2, -2));
			Assertions.assertFalse(game.playCard(secondPlayerIdx, 3, 0));
			Assertions.assertFalse(game.playCard(secondPlayerIdx, 4, 0));
			Assertions.assertFalse(game.playCard(secondPlayerIdx, 5, 0));
			Assertions.assertFalse(game.playCard(secondPlayerIdx, 6, 0));
			Assertions.assertFalse(game.playCard(secondPlayerIdx, 7, 0));
			Assertions.assertFalse(game.playCard(secondPlayerIdx, -1, 0));
			Assertions.assertFalse(game.playCard(secondPlayerIdx, -20, 0));
			Assertions.assertFalse(game.discardCard(secondPlayerIdx, 0));
			Assertions.assertFalse(game.discardCard(secondPlayerIdx, 1));
			Assertions.assertFalse(game.discardCard(secondPlayerIdx, 2));
			Assertions.assertFalse(game.discardCard(secondPlayerIdx, 3));
			Assertions.assertFalse(game.pass(secondPlayerIdx));
			Assertions.assertFalse(game.chooseAccept(secondPlayerIdx));
			Assertions.assertFalse(game.chooseCancel(secondPlayerIdx));
			Assertions.assertFalse(game.pass(secondPlayerIdx));
			Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, 0));
			Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, secondPlayerIdx, 0));
			Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, secondPlayerIdx, 1));
			Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, secondPlayerIdx, 0));
			//Assertions.assertFalse(game.chooseCardsInPlay(secondPlayerIdx, firstPlayerIdx, List.of(0)));
			//Assertions.assertFalse(game.chooseCardsInPlay(secondPlayerIdx, secondPlayerIdx, List.of()));
			//Assertions.assertFalse(game.chooseCardsInPlay(secondPlayerIdx, secondPlayerIdx, List.of(0,1,2,-1)));
			//todo make extensive
		}
		{ //first player tries to do some illegal actions
			Assertions.assertFalse(game.playCard(firstPlayerIdx, 0, 1));
			Assertions.assertFalse(game.playCard(firstPlayerIdx, 0, 2));
			Assertions.assertFalse(game.playCard(firstPlayerIdx, 0, -1));
			Assertions.assertFalse(game.playCard(firstPlayerIdx, 0, -100));
			Assertions.assertFalse(game.playCard(firstPlayerIdx, -1, 0));
			Assertions.assertFalse(game.playCard(firstPlayerIdx, -10, 0));
			Assertions.assertFalse(game.playCard(firstPlayerIdx, 6, 0));
			Assertions.assertFalse(game.playCard(firstPlayerIdx, 15, 0));
			Assertions.assertFalse(game.discardCard(firstPlayerIdx, -1));
			Assertions.assertFalse(game.discardCard(firstPlayerIdx, -4));
			Assertions.assertFalse(game.discardCard(firstPlayerIdx, 6));
			Assertions.assertFalse(game.discardCard(firstPlayerIdx, 8000));
			Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 8000));
			Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, -1));
			Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 0));
			Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 0));
			Assertions.assertFalse(game.chooseAccept(firstPlayerIdx));
			Assertions.assertFalse(game.chooseCancel(firstPlayerIdx));
			//todo make extensive
		}
		{ //first player plays their ausbrecher. and then tries to cancels the ability (it's not optional), then turns the own ausbrecher face down
			assertTrue(game.playCard(firstPlayerIdx, 0, 0));
			Assertions.assertFalse(game.chooseAccept(firstPlayerIdx));
			Assertions.assertFalse(game.chooseCancel(firstPlayerIdx));
			Assertions.assertFalse(game.pass(firstPlayerIdx));
			Assertions.assertFalse(game.discardCard(firstPlayerIdx, 0));
			Assertions.assertFalse(game.discardCard(firstPlayerIdx, 1));
			Assertions.assertFalse(game.discardCard(firstPlayerIdx, 2));
			Assertions.assertFalse(game.discardCard(firstPlayerIdx, -1));
			Assertions.assertFalse(game.discardCard(firstPlayerIdx, 26));
			Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 0));
			Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, -1));
			Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 1));
			Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, -1));
			Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, -2));
			Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, -10));
			Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 1));
			Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 2));
			Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 10));
			Assertions.assertFalse(game.chooseCardInDiscard(firstPlayerIdx, 0));
			Assertions.assertFalse(game.chooseCardInDiscard(firstPlayerIdx, 1));
			Assertions.assertFalse(game.chooseCardInDiscard(firstPlayerIdx, 2));
			Assertions.assertFalse(game.chooseCardInDiscard(firstPlayerIdx, 0));
			Assertions.assertFalse(game.chooseCardInDiscard(firstPlayerIdx, 1));
			Assertions.assertFalse(game.chooseCardInDiscard(firstPlayerIdx, 2));
			Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, 0));
			Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, 1));
			Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, 2));
			Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, -1));
			Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, 50));
			assertFalse(game.chooseCancel(firstPlayerIdx));
			assertTrue(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 0));
			
			//check if ausbrecher is face down
			assertFalse(game.getPositionsOfFaceDownCards(firstPlayerIdx).isEmpty());
		}
    }
	
	@Test
    void testAusbrecher2() {
		int ausbrecherId = 2;
		
		//MAKE PLAYERS + GAME
		Deck deck1 = new Deck();
		deck1.addCard(Card.getCard(18));
        deck1.addCard(Card.getCard(19));
        deck1.addCard(Card.getCard(20));
        deck1.addCard(Card.getCard(ausbrecherId)); //ausbrecher will be index 0 in hand
        deck1.addCard(Card.getCard(1));
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
        assertTrue(deck1.checkIfLegal());

        List<Card> listOfCardsForDeck2 = new ArrayList<>();
		listOfCardsForDeck2.add(Card.getCard(18));
        listOfCardsForDeck2.add(Card.getCard(19));
        listOfCardsForDeck2.add(Card.getCard(20));
        listOfCardsForDeck2.add(Card.getCard(ausbrecherId)); //ausbrecher will be index 0 in hand
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(3));
        listOfCardsForDeck2.add(Card.getCard(4));
        listOfCardsForDeck2.add(Card.getCard(5));
        listOfCardsForDeck2.add(Card.getCard(6));
        listOfCardsForDeck2.add(Card.getCard(7));
        listOfCardsForDeck2.add(Card.getCard(8));
        listOfCardsForDeck2.add(Card.getCard(9));
        listOfCardsForDeck2.add(Card.getCard(10));
        listOfCardsForDeck2.add(Card.getCard(11));
        listOfCardsForDeck2.add(Card.getCard(12));
        listOfCardsForDeck2.add(Card.getCard(13));
        listOfCardsForDeck2.add(Card.getCard(14));
        listOfCardsForDeck2.add(Card.getCard(15));
        listOfCardsForDeck2.add(Card.getCard(16));
        listOfCardsForDeck2.add(Card.getCard(17));
        Deck deck2 = new Deck(listOfCardsForDeck2);
        assertTrue(deck2.checkIfLegal());

        Player player1 = new Player("Josh", deck1);
        Player player2 = new Player("Eti", deck2);

        Game game = new Game(player1, player2);

		game.startGameWithoutShuffling();
		
		int firstPlayerIdx = 0;
		int secondPlayerIdx = 1;
		
		{ //first player plays their ausbrecher. and then turns the own ausbrecher face down
			assertTrue(game.playCard(firstPlayerIdx, 0, 0));
			
			assertFalse(game.chooseAccept(firstPlayerIdx));
			assertFalse(game.pass(firstPlayerIdx));
			assertFalse(game.discardCard(firstPlayerIdx, 0));
			assertFalse(game.discardCard(firstPlayerIdx, 1));
			assertFalse(game.discardCard(firstPlayerIdx, 2));
			assertFalse(game.discardCard(firstPlayerIdx, -1));
			assertFalse(game.discardCard(firstPlayerIdx, 26));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 0));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, -1));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 1));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, -1));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, -2));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, -10));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 1));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 2));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 10));
			assertFalse(game.chooseCardInDiscard(firstPlayerIdx, 0));
			assertFalse(game.chooseCardInDiscard(firstPlayerIdx, 1));
			assertFalse(game.chooseCardInDiscard(firstPlayerIdx, 2));
			assertFalse(game.chooseCardInDiscard(firstPlayerIdx, 0));
			assertFalse(game.chooseCardInDiscard(firstPlayerIdx, 1));
			assertFalse(game.chooseCardInDiscard(firstPlayerIdx, 2));
			assertFalse(game.chooseCardInHand(firstPlayerIdx, 0));
			assertFalse(game.chooseCardInHand(firstPlayerIdx, 1));
			assertFalse(game.chooseCardInHand(firstPlayerIdx, 2));
			assertFalse(game.chooseCardInHand(firstPlayerIdx, -1));
			assertFalse(game.chooseCardInHand(firstPlayerIdx, 50));
			
			assertTrue(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 0));
			
			//check if ausbrecher is now turned face down
			assertTrue(game.getPositionsOfFaceDownCards(firstPlayerIdx).contains(0));
			
			//player 2 plays a card that has no ability
			assertFalse(game.pass(firstPlayerIdx));
			assertFalse(game.playCard(secondPlayerIdx, -1, 0));
			assertFalse(game.playCard(secondPlayerIdx, 6, 0));
			assertFalse(game.playCard(secondPlayerIdx, 1, -1));
			assertFalse(game.playCard(secondPlayerIdx, 1, -2));
			assertFalse(game.playCard(secondPlayerIdx, 1, 1));
			assertFalse(game.playCard(secondPlayerIdx, 1, 2));
			assertFalse(game.playCard(secondPlayerIdx, 1, 4));
			assertTrue(game.playCard(secondPlayerIdx, 1, 0));
			
			//player 1 plays a card that has no ability
			assertFalse(game.pass(secondPlayerIdx));
			assertFalse(game.playCard(firstPlayerIdx, 0, -2));
			assertFalse(game.playCard(firstPlayerIdx, 0, -3));
			assertFalse(game.playCard(firstPlayerIdx, 0, 2));
			assertFalse(game.playCard(firstPlayerIdx, 0, 3));
			assertFalse(game.playCard(firstPlayerIdx, 0, 4));
			assertTrue(game.playCard(firstPlayerIdx, 0, 1));
			
			//player 2 plays their ausbrecher
			assertFalse(game.pass(firstPlayerIdx));
			assertFalse(game.chooseCancel(firstPlayerIdx));
			assertFalse(game.chooseCardInHand(firstPlayerIdx, 0));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 0));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 1));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, -1));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 0));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, -1));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 1));
			assertFalse(game.chooseCardInPlay(secondPlayerIdx, secondPlayerIdx, 0));
			assertFalse(game.chooseCardInPlay(secondPlayerIdx, secondPlayerIdx, 1));
			assertFalse(game.chooseCardInPlay(secondPlayerIdx, secondPlayerIdx, -1));
			assertFalse(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, 0));
			assertFalse(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, 1));
			assertFalse(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, -1));
			assertTrue(game.playCard(secondPlayerIdx, 0, -1));
			
			//player 2 has turns face down their opponent's card
			assertFalse(game.playCard(firstPlayerIdx, 0, -3));
			assertFalse(game.playCard(firstPlayerIdx, 0, -2));
			assertFalse(game.playCard(firstPlayerIdx, 0, -1));
			assertFalse(game.playCard(firstPlayerIdx, 0, 0));
			assertFalse(game.playCard(firstPlayerIdx, 0, 1));
			assertFalse(game.playCard(firstPlayerIdx, 0, 2));
			assertFalse(game.playCard(firstPlayerIdx, 0, 3));
			assertFalse(game.playCard(firstPlayerIdx, 0, 4));
			assertFalse(game.playCard(secondPlayerIdx, 0, -3));
			assertFalse(game.playCard(secondPlayerIdx, 0, -2));
			assertFalse(game.playCard(secondPlayerIdx, 0, -1));
			assertFalse(game.playCard(secondPlayerIdx, 0, 0));
			assertFalse(game.playCard(secondPlayerIdx, 0, 1));
			assertFalse(game.playCard(secondPlayerIdx, 0, 2));
			assertFalse(game.playCard(secondPlayerIdx, 0, 3));
			assertFalse(game.playCard(secondPlayerIdx, 0, 4));
			assertFalse(game.pass(firstPlayerIdx));
			assertFalse(game.pass(secondPlayerIdx));
			assertFalse(game.chooseAccept(firstPlayerIdx));
			assertFalse(game.chooseCancel(firstPlayerIdx));
			assertFalse(game.chooseCardInHand(firstPlayerIdx, 0));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 0));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 1));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, -1));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 0));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, -1));
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 1));
			assertFalse(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, 0)); //should already be face down
			assertFalse(game.chooseCardInPlay(secondPlayerIdx, secondPlayerIdx, -10));
			assertFalse(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, -1));
			
			assertTrue(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, 1));
			
			//check if ausbrecher is now turned face down
			assertTrue(game.getPositionsOfFaceDownCards(firstPlayerIdx).contains(0));
			assertTrue(game.getPositionsOfFaceDownCards(firstPlayerIdx).contains(1));
			assertTrue(game.getPositionsOfFaceDownCards(secondPlayerIdx).isEmpty());
		}
    }

}