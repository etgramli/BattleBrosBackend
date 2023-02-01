package de.etgramli.battlebros.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbilityTest019 {
	
	//Remember:
	// At the beginning of a game: 3 life cards are added THEN 6 hand cards are drawn
	// Top card of the deck = index 0 of gameZoneDeck
	// so: first 3 cards of deck go to life & next 6 cards after that go to hand (deckIdx 3 == handIndx 0)

    @Test
    void testAnullierer1() {
		int haihammerIdx = 19;
		int senkschlangeIdx = 20;
		int holzkopfIdx = 47;
		int ausbrecherIdx = 2;
		int lavaboyIdx = 9;
		int anfeuererIdx = 15;
		
		int firstPlayerIdx = 0;
		int secondPlayerIdx = 1;
		
		//MAKE PLAYERS + GAME
		Deck deck1 = new Deck();
		deck1.addCard(Card.getCard(18));
        deck1.addCard(Card.getCard(19));
        deck1.addCard(Card.getCard(20));
        deck1.addCard(Card.getCard(haihammerIdx)); //index 0 in hand
        deck1.addCard(Card.getCard(senkschlangeIdx));
        deck1.addCard(Card.getCard(holzkopfIdx));
        deck1.addCard(Card.getCard(ausbrecherIdx));
        deck1.addCard(Card.getCard(lavaboyIdx));
        deck1.addCard(Card.getCard(anfeuererIdx)); //index 5
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

        List<Card> listOfCardsForDeck2 = new ArrayList<>();
		listOfCardsForDeck2.add(Card.getCard(18));
        listOfCardsForDeck2.add(Card.getCard(19));
        listOfCardsForDeck2.add(Card.getCard(20));
        listOfCardsForDeck2.add(Card.getCard(haihammerIdx)); //index 0 in hand
        listOfCardsForDeck2.add(Card.getCard(senkschlangeIdx));
        listOfCardsForDeck2.add(Card.getCard(holzkopfIdx));
        listOfCardsForDeck2.add(Card.getCard(ausbrecherIdx));
        listOfCardsForDeck2.add(Card.getCard(lavaboyIdx));
        listOfCardsForDeck2.add(Card.getCard(anfeuererIdx));
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

        Player player1 = new Player("Josh", deck2);
        Player player2 = new Player("Eti", deck2);
		
		Game game = new Game(player1, player2);

		game.startGameWithoutShuffling();
		System.out.println(game.getCardIDsInHand(firstPlayerIdx));
		System.out.println(game.getCardIDsInHand(secondPlayerIdx));
		Assertions.assertTrue(game.getCardsInHand(firstPlayerIdx).get(0).getId() == haihammerIdx);
		Assertions.assertTrue(game.getCardsInHand(secondPlayerIdx).get(0).getId() == haihammerIdx);

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
		{ //first player plays their senkschlange
			assertTrue(game.playCard(firstPlayerIdx, 1, 0));
			
			//second player plays anfeuerer
			assertTrue(game.playCard(secondPlayerIdx, 5, 0));
			
			//first player passes
			assertTrue(game.pass(firstPlayerIdx));
			
			//second player plays their ausbrecher
			assertTrue(game.playCard(secondPlayerIdx, 3, 1));
			assertTrue(game.getTotalValue(secondPlayerIdx) == 2);
			
			//secondPlayer flips senkschlange face down
			assertTrue(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, 0));
			assertTrue(game.getTotalValue(secondPlayerIdx) == 3);
		}
    }
	
	 @Test
    void testAnullierer2() {
		int haihammerIdx = 19;
		int senkschlangeIdx = 20;
		int holzkopfIdx = 47;
		int ausbrecherIdx = 2;
		int lavaboyIdx = 9;
		int anfeuererIdx = 15;
		
		int firstPlayerIdx = 0;
		int secondPlayerIdx = 1;
		
		//MAKE PLAYERS + GAME
		Deck deck1 = new Deck();
		deck1.addCard(Card.getCard(18));
        deck1.addCard(Card.getCard(19));
        deck1.addCard(Card.getCard(20));
        deck1.addCard(Card.getCard(haihammerIdx)); //index 0 in hand
        deck1.addCard(Card.getCard(senkschlangeIdx));
        deck1.addCard(Card.getCard(holzkopfIdx));
        deck1.addCard(Card.getCard(ausbrecherIdx));
        deck1.addCard(Card.getCard(lavaboyIdx));
        deck1.addCard(Card.getCard(anfeuererIdx)); //index 5
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

        List<Card> listOfCardsForDeck2 = new ArrayList<>();
		listOfCardsForDeck2.add(Card.getCard(18));
        listOfCardsForDeck2.add(Card.getCard(19));
        listOfCardsForDeck2.add(Card.getCard(20));
        listOfCardsForDeck2.add(Card.getCard(haihammerIdx)); //index 0 in hand
        listOfCardsForDeck2.add(Card.getCard(senkschlangeIdx));
        listOfCardsForDeck2.add(Card.getCard(holzkopfIdx));
        listOfCardsForDeck2.add(Card.getCard(ausbrecherIdx));
        listOfCardsForDeck2.add(Card.getCard(lavaboyIdx));
        listOfCardsForDeck2.add(Card.getCard(anfeuererIdx));
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

        Player player1 = new Player("Josh", deck2);
        Player player2 = new Player("Eti", deck2);
		
		Game game = new Game(player1, player2);

		game.startGameWithoutShuffling();
		System.out.println(game.getCardIDsInHand(firstPlayerIdx));
		System.out.println(game.getCardIDsInHand(secondPlayerIdx));
		Assertions.assertTrue(game.getCardsInHand(firstPlayerIdx).get(0).getId() == haihammerIdx);
		Assertions.assertTrue(game.getCardsInHand(secondPlayerIdx).get(0).getId() == haihammerIdx);

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
		{ //first player plays their senkschlange
			assertTrue(game.playCard(firstPlayerIdx, 1, 0));
			
			//second player plays anfeuerer
			assertTrue(game.playCard(secondPlayerIdx, 5, 0));
			
			assertTrue(!game.isCardAbilityNegated(game.getPlayer(firstPlayerIdx), 0));
			assertTrue(game.isCardAbilityNegated(game.getPlayer(secondPlayerIdx), 0));
			
			//first player plays their lavaboy
			assertTrue(game.getPlayer(secondPlayerIdx).getAmountOfCardsInHand() == 5);
			assertTrue(game.playCard(firstPlayerIdx, 3, 1));
			assertTrue(game.getPlayer(secondPlayerIdx).getAmountOfCardsInHand() == 6);
			
			assertTrue(!game.isCardAbilityNegated(game.getPlayer(firstPlayerIdx), 1));
			
			//second player plays their haihammer
			assertTrue(game.playCard(secondPlayerIdx, 0, 1));
			assertFalse(game.getTotalValue(secondPlayerIdx) == 2);
			
			assertTrue(!game.isCardAbilityNegated(game.getPlayer(secondPlayerIdx), 1));
			assertTrue(!game.isCardAbilityNegated(game.getPlayer(firstPlayerIdx), 0));
			assertTrue(game.isCardAbilityNegated(game.getPlayer(firstPlayerIdx), 1));
			
			assertTrue(game.getTotalValue(secondPlayerIdx) == 3);
			
			//firstplay plays ausbrecher
			assertTrue(game.playCard(firstPlayerIdx, 1, 2));
			
			assertTrue(game.isCardAbilityNegated(game.getPlayer(firstPlayerIdx), 2));
			
			//ausbrecher's abilitiy does NOT trigger
			assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 0));
			
			assertFalse(game.pass(firstPlayerIdx));
			assertTrue(game.pass(secondPlayerIdx));
		}
    }
	
	@Test
    void testAnullierer3() {
		int haihammerIdx = 19;
		int senkschlangeIdx = 20;
		int holzkopfIdx = 47;
		int ausbrecherIdx = 2;
		int lavaboyIdx = 9;
		int anfeuererIdx = 15;
		
		int firstPlayerIdx = 0;
		int secondPlayerIdx = 1;
		
		//MAKE PLAYERS + GAME
		Deck deck1 = new Deck();
		deck1.addCard(Card.getCard(18));
        deck1.addCard(Card.getCard(19));
        deck1.addCard(Card.getCard(20));
        deck1.addCard(Card.getCard(haihammerIdx)); //index 0 in hand
        deck1.addCard(Card.getCard(senkschlangeIdx));
        deck1.addCard(Card.getCard(holzkopfIdx));
        deck1.addCard(Card.getCard(ausbrecherIdx));
        deck1.addCard(Card.getCard(lavaboyIdx));
        deck1.addCard(Card.getCard(anfeuererIdx)); //index 5
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

        List<Card> listOfCardsForDeck2 = new ArrayList<>();
		listOfCardsForDeck2.add(Card.getCard(18));
        listOfCardsForDeck2.add(Card.getCard(19));
        listOfCardsForDeck2.add(Card.getCard(20));
        listOfCardsForDeck2.add(Card.getCard(haihammerIdx)); //index 0 in hand
        listOfCardsForDeck2.add(Card.getCard(senkschlangeIdx));
        listOfCardsForDeck2.add(Card.getCard(holzkopfIdx));
        listOfCardsForDeck2.add(Card.getCard(ausbrecherIdx));
        listOfCardsForDeck2.add(Card.getCard(lavaboyIdx));
        listOfCardsForDeck2.add(Card.getCard(anfeuererIdx));
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

        Player player1 = new Player("Josh", deck2);
        Player player2 = new Player("Eti", deck2);
		
		Game game = new Game(player1, player2);

		game.startGameWithoutShuffling();
		System.out.println(game.getCardIDsInHand(firstPlayerIdx));
		System.out.println(game.getCardIDsInHand(secondPlayerIdx));
		Assertions.assertTrue(game.getCardsInHand(firstPlayerIdx).get(0).getId() == haihammerIdx);
		Assertions.assertTrue(game.getCardsInHand(secondPlayerIdx).get(0).getId() == haihammerIdx);

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
		{ //first player plays their holzkopf
			assertTrue(game.playCard(firstPlayerIdx, 2, 0));
			
			//second player discards a card
			assertTrue(game.discardCard(secondPlayerIdx, 5));
			
			//first player plays their anfeuerer, which is negated by their own holzkopf
			assertTrue(game.getTotalValue(firstPlayerIdx) == 3);
			assertTrue(game.playCard(firstPlayerIdx, 4, 1));
			assertTrue(game.getTotalValue(firstPlayerIdx) == 5);
			assertTrue(!game.isCardAbilityNegated(game.getPlayer(firstPlayerIdx), 0));
			assertTrue(game.isCardAbilityNegated(game.getPlayer(firstPlayerIdx), 1));

			//second player plays their senkschlange, negating holzkopf's ability, which restores anfeuerer's abilitiy
			assertTrue(game.playCard(secondPlayerIdx, 1, 0));
			assertTrue(game.isCardAbilityNegated(game.getPlayer(firstPlayerIdx), 0));
			assertTrue(!game.isCardAbilityNegated(game.getPlayer(firstPlayerIdx), 1));
			assertTrue(game.getTotalValue(firstPlayerIdx) == 6);
			
			//first player plays their haihammer, negating senkschlange, which restores holzkopf, which re-negates anfeuerer
			// --> PROBLEM: HAIHAMMER SHOULDN'T BE ABLE TO RESTORE HOLZKOPF; BECAUSE THAT WOULD NEGATE HAIHAMMER
			assertTrue(game.playCard(firstPlayerIdx, 0, -1));

			drawGameState(game, game.getPlayer(0), game.getPlayer(1));
			
			assertTrue(game.getTotalValue(firstPlayerIdx) == 7);
			assertTrue(game.isCardAbilityNegated(game.getPlayer(firstPlayerIdx), 0));
			assertTrue(!game.isCardAbilityNegated(game.getPlayer(firstPlayerIdx), 1));
			assertTrue(!game.isCardAbilityNegated(game.getPlayer(firstPlayerIdx), -1));
			assertTrue(!game.isCardAbilityNegated(game.getPlayer(secondPlayerIdx), 1));
			

			
		}
    }
	
	static void drawGameState(Game game, Player player1, Player player2) {
		System.out.println("\n\n______ Zug=" + game.getTurnNumber() + " AmZug=" + game.getTurnPlayer().getName() + " ______");
		if (game.currentlyResolvingAnAbility())
			System.out.println("!!! Es wird gerade folgende Fähigkeit verrechnet: " + Card.getCard(game.getCurrentlyResolvingAbility().getCardId()).getName() + ": \"" + game.getCurrentlyResolvingAbility().getAbilityText() + "\"");
		
		if (game.currentlyWaitingForAbilityToBeChosen()){
			System.out.printf("!!! Folgende Fähigkeiten müssen noch verrechnet werden: ");
			for (ResolvableAbility ability : game.getAbilityQueue())
				System.out.printf("[" + Card.getCard(ability.getCardId()).getName() + ": \"" + ability.getAbilityText() + "\"]");
			System.out.println();
		}
		
        drawPlayerState(player1, 0);
        drawPlayerState(player2, 1);
    }

    static void drawPlayerState(Player player, int playerIndex){
        System.out.println(player.getName() + " (p" + (playerIndex+1) + ")");
		
		System.out.println("TotalValue: " + player.getTotalValue());
        System.out.println("Life: " + player.getAmountOfLifeCards());
		
        System.out.printf("Hand: ");
		int i=1;
        for (Card card : player.getCardsInHand()){
            System.out.printf(i++ + "=" + card.getName() + "(" + card.getValue() + ")  ");
		}
		
        System.out.printf("\nCards on Field: ");
        for (Map.Entry<Integer, Card> entry : player.getCardsOnField().entrySet()){
			String extraText = "";
			if (!player.isCardFaceUp(entry.getKey()))
				extraText = " VERDECKT";
			if (player.getGame().isCardAbilityNegated(player, entry.getKey()))
				extraText += " ANNULLIERT";
            System.out.printf("[" + entry.getKey() + ": "+ entry.getValue().getName() + "(" + player.getValueOfCardOnFieldAt(entry.getKey()) + ")" + extraText + "]  ");
		}
		
		System.out.println("\n");
    }
	

}