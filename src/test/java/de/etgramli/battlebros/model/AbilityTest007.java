package de.etgramli.battlebros.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbilityTest007 {
	
	//Remember:
	// At the beginning of a game: 3 life cards are added THEN 6 hand cards are drawn
	// Top card of the deck = index 0 of gameZoneDeck
	// so: first 3 cards of deck go to life & next 6 cards after that go to hand (deckIdx 3 == handIndx 0)

	int potzblitzID = 7;
	int firstPlayerIdx = 0;
	int secondPlayerIdx = 1;

    @Test
	void testPotzblitz2() {
		
		//MAKE PLAYERS + GAME
		Deck deck1 = new Deck();
		deck1.addCard(Card.getCard(18));
        deck1.addCard(Card.getCard(19));
        deck1.addCard(Card.getCard(20));
        deck1.addCard(Card.getCard(potzblitzID)); //card will be index 0 in hand
        deck1.addCard(Card.getCard(1));
        deck1.addCard(Card.getCard(3));
        deck1.addCard(Card.getCard(2));
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

        List<Card> listOfCardsForDeck2 = new ArrayList<>();
		listOfCardsForDeck2.add(Card.getCard(18));
        listOfCardsForDeck2.add(Card.getCard(19));
        listOfCardsForDeck2.add(Card.getCard(20));
        listOfCardsForDeck2.add(Card.getCard(potzblitzID)); //card will be index 0 in hand
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(3));
        listOfCardsForDeck2.add(Card.getCard(2));
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

        Player player1 = new Player("Josh-1", deck1);
        Player player2 = new Player("Eti-2", deck2);
		
		Game game = new Game(player1, player2);

		game.startGameWithoutShuffling();
		
		System.out.println(game.getCardIDsInHand(firstPlayerIdx));
		System.out.println(game.getCardIDsInHand(secondPlayerIdx));
		Assertions.assertTrue(game.getCardsInHand(firstPlayerIdx).get(0).getId() == potzblitzID);
		Assertions.assertTrue(game.getCardsInHand(secondPlayerIdx).get(0).getId() == potzblitzID);
		
		
		
		
		//first player plays a card with power val 3
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 1, 0));
		
		//that card is stille face up
		Assertions.assertFalse(game.getPositionsOfFaceDownCards(firstPlayerIdx).contains(0));
		Assertions.assertTrue(game.getPositionsOfFaceDownCards(firstPlayerIdx).isEmpty());
		
		//second player plays their potzblitz (powval 1)
		Assertions.assertTrue(game.playCard(secondPlayerIdx, 0, 0));
		
		//both players now have 5 cards in hand, so player 1 need to discard(choose) a single card from their hand
		
		//now second player cannot choose any card
		Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, secondPlayerIdx, 0));
		Assertions.assertFalse(game.pass(secondPlayerIdx));
		Assertions.assertFalse(game.playCard(secondPlayerIdx, 0, 1));
		Assertions.assertFalse(game.discardCard(secondPlayerIdx, 1));
		Assertions.assertFalse(game.chooseCancel(secondPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(secondPlayerIdx));
		
		//first player can't do anything either, except...
		Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, -1));
		Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, 6));
		Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 0));
		Assertions.assertFalse(game.pass(firstPlayerIdx));
		Assertions.assertFalse(game.playCard(firstPlayerIdx, 0, 1));
		Assertions.assertFalse(game.discardCard(firstPlayerIdx, 1));
		Assertions.assertFalse(game.chooseCancel(firstPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(firstPlayerIdx));
		Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 0));
		
		//choose a bro in their hand to discard
		Assertions.assertTrue(game.chooseCardInHand(firstPlayerIdx, 4));
		
		//and then it's their turn
		Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, 0));
		Assertions.assertTrue(game.pass(firstPlayerIdx));
		
		//second player discards all their cards befor passing as well
		Assertions.assertTrue(game.discardCard(secondPlayerIdx, 4));
		Assertions.assertTrue(game.discardCard(secondPlayerIdx, 3));
		Assertions.assertTrue(game.discardCard(secondPlayerIdx, 1));
		Assertions.assertTrue(game.discardCard(secondPlayerIdx, 0));
		Assertions.assertTrue(game.discardCard(secondPlayerIdx, 0));
		Assertions.assertFalse(game.discardCard(secondPlayerIdx, 0));
		Assertions.assertTrue(game.pass(secondPlayerIdx));
		
		//firstplayer had a total val of 3, secondplayer only of 1, so now it is firstplayer's turn
		//they play their potzblitz
		Assertions.assertFalse(game.playCard(secondPlayerIdx, 0, 0));
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 0, 0));
		
		//because first player has way more cards in hand than second player, second player only has to discard(choose) one card.
		Assertions.assertFalse(game.playCard(secondPlayerIdx, 1, 0));
		
		Assertions.assertTrue(game.chooseCardInHand(secondPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInHand(secondPlayerIdx, 0));
		
		Assertions.assertTrue(game.playCard(secondPlayerIdx, 1, 0));
	}
	
	@Test
	void testPotzblitz3() {
		
		//MAKE PLAYERS + GAME
		Deck deck1 = new Deck();
		deck1.addCard(Card.getCard(18));
        deck1.addCard(Card.getCard(19));
        deck1.addCard(Card.getCard(20));
        deck1.addCard(Card.getCard(potzblitzID)); //card will be index 0 in hand
        deck1.addCard(Card.getCard(1));
        deck1.addCard(Card.getCard(3));
        deck1.addCard(Card.getCard(2));
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

        List<Card> listOfCardsForDeck2 = new ArrayList<>();
		listOfCardsForDeck2.add(Card.getCard(18));
        listOfCardsForDeck2.add(Card.getCard(19));
        listOfCardsForDeck2.add(Card.getCard(20));
        listOfCardsForDeck2.add(Card.getCard(potzblitzID)); //card will be index 0 in hand
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(3));
        listOfCardsForDeck2.add(Card.getCard(2));
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

        Player player1 = new Player("Josh-1", deck1);
        Player player2 = new Player("Eti-2", deck2);
		
		Game game = new Game(player1, player2);

		game.startGameWithoutShuffling();
		
		System.out.println(game.getCardIDsInHand(firstPlayerIdx));
		System.out.println(game.getCardIDsInHand(secondPlayerIdx));
		Assertions.assertTrue(game.getCardsInHand(firstPlayerIdx).get(0).getId() == potzblitzID);
		Assertions.assertTrue(game.getCardsInHand(secondPlayerIdx).get(0).getId() == potzblitzID);
		
		
		
		
		//first player plays a card with power val 3 (now 5 cards in hand)
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 1, 0));
		
		//that card is stille face up
		Assertions.assertFalse(game.getPositionsOfFaceDownCards(firstPlayerIdx).contains(0));
		Assertions.assertTrue(game.getPositionsOfFaceDownCards(firstPlayerIdx).isEmpty());
		
		//second player passes (now 6 cards in hand)
		Assertions.assertTrue(game.pass(secondPlayerIdx));
		
		//first player discards a card and then passes too
		Assertions.assertTrue(game.discardCard(firstPlayerIdx, 2)); //now 4 cards in hand
		Assertions.assertTrue(game.pass(firstPlayerIdx));
		
		//battle ends -> cards in hand: firstP=7 secondP=9
		//first player won the battle, so now it's their turn again
		
		//firstplayer plays their potzblitz
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 0, 0));
		
		//now has to discard 2 cards (they have 9 cards in hand vs firstplayer's 6)
		Assertions.assertFalse(game.pass(secondPlayerIdx));
		Assertions.assertFalse(game.discardCard(secondPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInHand(secondPlayerIdx, -1));
		Assertions.assertFalse(game.chooseCardInHand(secondPlayerIdx, 20));
		Assertions.assertFalse(game.chooseCancel(secondPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(secondPlayerIdx));
		Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, secondPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 0));
		Assertions.assertFalse(game.pass(firstPlayerIdx));
		Assertions.assertFalse(game.discardCard(firstPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, -1));
		Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, 20));
		Assertions.assertFalse(game.chooseCancel(firstPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(firstPlayerIdx));
		
		Assertions.assertTrue(game.chooseCardInHand(secondPlayerIdx, 0));
		
		Assertions.assertFalse(game.pass(secondPlayerIdx));
		Assertions.assertFalse(game.discardCard(secondPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInHand(secondPlayerIdx, -1));
		Assertions.assertFalse(game.chooseCardInHand(secondPlayerIdx, 20));
		Assertions.assertFalse(game.chooseCancel(secondPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(secondPlayerIdx));
		Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, secondPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 0));
		Assertions.assertFalse(game.pass(firstPlayerIdx));
		Assertions.assertFalse(game.discardCard(firstPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, -1));
		Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, 20));
		Assertions.assertFalse(game.chooseCancel(firstPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(firstPlayerIdx));
		
		Assertions.assertTrue(game.chooseCardInHand(secondPlayerIdx, 0));
		
		Assertions.assertFalse(game.chooseCardInHand(secondPlayerIdx, 0));
		Assertions.assertTrue(game.pass(secondPlayerIdx));
		
		
		
	}
	
	@Test
	void testPotzblitz4() {
		
		//MAKE PLAYERS + GAME
		Deck deck1 = new Deck();
		deck1.addCard(Card.getCard(18));
        deck1.addCard(Card.getCard(19));
        deck1.addCard(Card.getCard(20));
        deck1.addCard(Card.getCard(potzblitzID)); //card will be index 0 in hand
        deck1.addCard(Card.getCard(1));
        deck1.addCard(Card.getCard(3));
        deck1.addCard(Card.getCard(2));
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

        List<Card> listOfCardsForDeck2 = new ArrayList<>();
		listOfCardsForDeck2.add(Card.getCard(18));
        listOfCardsForDeck2.add(Card.getCard(19));
        listOfCardsForDeck2.add(Card.getCard(20));
        listOfCardsForDeck2.add(Card.getCard(potzblitzID)); //card will be index 0 in hand
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(3));
        listOfCardsForDeck2.add(Card.getCard(2));
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

        Player player1 = new Player("Josh-1", deck1);
        Player player2 = new Player("Eti-2", deck2);
		
		Game game = new Game(player1, player2);

		game.startGameWithoutShuffling();
		
		System.out.println(game.getCardIDsInHand(firstPlayerIdx));
		System.out.println(game.getCardIDsInHand(secondPlayerIdx));
		Assertions.assertTrue(game.getCardsInHand(firstPlayerIdx).get(0).getId() == potzblitzID);
		Assertions.assertTrue(game.getCardsInHand(secondPlayerIdx).get(0).getId() == potzblitzID);
		
		
		
		
		//first player plays a card with power val 3 (now 5 cards in hand)
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 1, 0));
		
		//that card is stille face up
		Assertions.assertFalse(game.getPositionsOfFaceDownCards(firstPlayerIdx).contains(0));
		Assertions.assertTrue(game.getPositionsOfFaceDownCards(firstPlayerIdx).isEmpty());
		
		//second player passes (now 6 cards in hand)
		Assertions.assertTrue(game.pass(secondPlayerIdx));
		
		//first player plays their potzblitz
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 0, 1)); //now 4 cards in hand
		
		//now has to discard 2 cards (they have 6 cards in hand vs firstplayer's 4)
		Assertions.assertFalse(game.pass(secondPlayerIdx));
		Assertions.assertFalse(game.discardCard(secondPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInHand(secondPlayerIdx, -1));
		Assertions.assertFalse(game.chooseCardInHand(secondPlayerIdx, 20));
		Assertions.assertFalse(game.chooseCancel(secondPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(secondPlayerIdx));
		Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, secondPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 0));
		Assertions.assertFalse(game.pass(firstPlayerIdx));
		Assertions.assertFalse(game.discardCard(firstPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, -1));
		Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, 20));
		Assertions.assertFalse(game.chooseCancel(firstPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(firstPlayerIdx));
		
		Assertions.assertTrue(game.chooseCardInHand(secondPlayerIdx, 0));
		
		Assertions.assertFalse(game.pass(secondPlayerIdx));
		Assertions.assertFalse(game.discardCard(secondPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInHand(secondPlayerIdx, -1));
		Assertions.assertFalse(game.chooseCardInHand(secondPlayerIdx, 20));
		Assertions.assertFalse(game.chooseCancel(secondPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(secondPlayerIdx));
		Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, secondPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 0));
		Assertions.assertFalse(game.pass(firstPlayerIdx));
		Assertions.assertFalse(game.discardCard(firstPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, -1));
		Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, 20));
		Assertions.assertFalse(game.chooseCancel(firstPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(firstPlayerIdx));
		
		Assertions.assertTrue(game.chooseCardInHand(secondPlayerIdx, 0));
		
		Assertions.assertFalse(game.chooseCardInHand(secondPlayerIdx, 0));
		Assertions.assertFalse(game.pass(secondPlayerIdx));
		Assertions.assertTrue(game.pass(firstPlayerIdx));
		
		
		
	}
	
	
	@Test
	void testPotzblitz5() {
		
		//MAKE PLAYERS + GAME
		Deck deck1 = new Deck();
		deck1.addCard(Card.getCard(18));
        deck1.addCard(Card.getCard(19));
        deck1.addCard(Card.getCard(20));
        deck1.addCard(Card.getCard(potzblitzID)); //card will be index 0 in hand
        deck1.addCard(Card.getCard(1));
        deck1.addCard(Card.getCard(3));
        deck1.addCard(Card.getCard(2));
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

        List<Card> listOfCardsForDeck2 = new ArrayList<>();
		listOfCardsForDeck2.add(Card.getCard(18));
        listOfCardsForDeck2.add(Card.getCard(19));
        listOfCardsForDeck2.add(Card.getCard(20));
        listOfCardsForDeck2.add(Card.getCard(potzblitzID)); //card will be index 0 in hand
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(3));
        listOfCardsForDeck2.add(Card.getCard(2));
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

        Player player1 = new Player("Josh-1", deck1);
        Player player2 = new Player("Eti-2", deck2);
		
		Game game = new Game(player1, player2);

		game.startGameWithoutShuffling();
		
		System.out.println(game.getCardIDsInHand(firstPlayerIdx));
		System.out.println(game.getCardIDsInHand(secondPlayerIdx));
		Assertions.assertTrue(game.getCardsInHand(firstPlayerIdx).get(0).getId() == potzblitzID);
		Assertions.assertTrue(game.getCardsInHand(secondPlayerIdx).get(0).getId() == potzblitzID);
		
		
		
		
		//first player plays a card with power val 3 
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 1, 0)); // (now 5 cards in hand)
		
		//second player discards a card
		Assertions.assertTrue(game.discardCard(secondPlayerIdx, 1)); //now 5 in hand
		
		//first player discards a card
		Assertions.assertTrue(game.discardCard(firstPlayerIdx, 1)); //(now 4 cards in hand)
		
		
		//second player discards a card
		Assertions.assertTrue(game.discardCard(secondPlayerIdx, 1)); //now 4 in hand
		
		//first player discards a card
		Assertions.assertTrue(game.discardCard(firstPlayerIdx, 1)); //(now 3 cards in hand)
		
		//second player discards a card
		Assertions.assertTrue(game.discardCard(secondPlayerIdx, 1)); //now 3 in hand
		
		//first player discards a card
		Assertions.assertTrue(game.discardCard(firstPlayerIdx, 1)); //(now 2 cards in hand)
		
		//second player discards a card
		Assertions.assertTrue(game.discardCard(secondPlayerIdx, 1)); //now 2 in hand


		//first player discards a card
		Assertions.assertTrue(game.discardCard(firstPlayerIdx, 1)); //(now 1 cards in hand)
		
		//second player discards a card
		Assertions.assertTrue(game.discardCard(secondPlayerIdx, 1)); //now 1 in hand
		
		//first player discards a card
		Assertions.assertTrue(game.discardCard(firstPlayerIdx, 0)); //(now 0 cards in hand)
		
		//second player plays their potzblitz
		Assertions.assertTrue(game.playCard(secondPlayerIdx, 0, 0)); //now 0 in hand
		
		//first player has to discard, but can't (hand empty)
		Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInHand(secondPlayerIdx, 0));
		Assertions.assertFalse(game.pass(firstPlayerIdx));
		Assertions.assertFalse(game.pass(secondPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(firstPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(secondPlayerIdx));
		Assertions.assertFalse(game.chooseCancel(secondPlayerIdx));
		
		//..which is why they're allowed to cancel
		Assertions.assertTrue(game.chooseCancel(firstPlayerIdx));
		
		Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInHand(secondPlayerIdx, 0));
		Assertions.assertFalse(game.pass(secondPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(firstPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(secondPlayerIdx));
		Assertions.assertFalse(game.chooseCancel(secondPlayerIdx));
		Assertions.assertFalse(game.chooseCancel(firstPlayerIdx));
		Assertions.assertFalse(game.playCard(firstPlayerIdx,0,1));
		Assertions.assertFalse(game.playCard(firstPlayerIdx,0,-1));
		Assertions.assertFalse(game.playCard(firstPlayerIdx,1,0));
		Assertions.assertFalse(game.playCard(firstPlayerIdx,1,1));
		Assertions.assertFalse(game.playCard(firstPlayerIdx,0,2));
		Assertions.assertFalse(game.playCard(firstPlayerIdx,0,-2));
		Assertions.assertFalse(game.playCard(firstPlayerIdx,0,0));
		Assertions.assertFalse(game.playCard(secondPlayerIdx,0,1));
		Assertions.assertFalse(game.playCard(secondPlayerIdx,0,-1));
		Assertions.assertFalse(game.playCard(secondPlayerIdx,1,0));
		Assertions.assertFalse(game.playCard(secondPlayerIdx,1,1));
		Assertions.assertFalse(game.playCard(secondPlayerIdx,0,2));
		Assertions.assertFalse(game.playCard(secondPlayerIdx,0,-2));
		Assertions.assertFalse(game.playCard(secondPlayerIdx,0,0));
		
		Assertions.assertTrue(game.pass(firstPlayerIdx));
		
	}
	
	
}