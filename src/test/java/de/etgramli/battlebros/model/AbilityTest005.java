package de.etgramli.battlebros.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbilityTest005 {
	
	//Remember:
	// At the beginning of a game: 3 life cards are added THEN 6 hand cards are drawn
	// Top card of the deck = index 0 of gameZoneDeck
	// so: first 3 cards of deck go to life & next 6 cards after that go to hand (deckIdx 3 == handIndx 0)

	int verascherID = 5;
	int firstPlayerIdx = 0;
	int secondPlayerIdx = 1;

    @Test
	void testKanonenfutterer1() {
		
		//MAKE PLAYERS + GAME
		Deck deck1 = new Deck();
		deck1.addCard(Card.getCard(18));
        deck1.addCard(Card.getCard(19));
        deck1.addCard(Card.getCard(20));
        deck1.addCard(Card.getCard(verascherID)); //ausbrecher will be index 0 in hand
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
        //assertTrue(deck1.checkIfLegal());

        List<Card> listOfCardsForDeck2 = new ArrayList<>();
		listOfCardsForDeck2.add(Card.getCard(18));
        listOfCardsForDeck2.add(Card.getCard(19));
        listOfCardsForDeck2.add(Card.getCard(20));
        listOfCardsForDeck2.add(Card.getCard(verascherID)); //ausbrecher will be index 0 in hand
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
        //assertTrue(deck2.checkIfLegal());

        Player player1 = new Player("Josh-1", deck1);
        Player player2 = new Player("Eti-2", deck2);
		
		Game game = new Game(player1, player2);

		game.startGameWithoutShuffling();
		
		System.out.println(game.getCardIDsInHand(firstPlayerIdx));
		System.out.println(game.getCardIDsInHand(secondPlayerIdx));
		Assertions.assertTrue(game.getCardsInHand(firstPlayerIdx).get(0).getId() == verascherID);
		Assertions.assertTrue(game.getCardsInHand(secondPlayerIdx).get(0).getId() == verascherID);
		
		//first player plays a card with power val 3
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 1, 0));
		
		//that card is stille face up
		Assertions.assertFalse(game.getPositionsOfFaceDownCards(firstPlayerIdx).contains(0));
		Assertions.assertTrue(game.getPositionsOfFaceDownCards(firstPlayerIdx).isEmpty());
		
		//second player plays their verascher (power val 2)
		Assertions.assertTrue(game.playCard(secondPlayerIdx, 0, 0));
		
		//now second player cannot choose any card
		Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, secondPlayerIdx, 0));
		Assertions.assertFalse(game.pass(secondPlayerIdx));
		Assertions.assertFalse(game.playCard(secondPlayerIdx, 0, 1));
		Assertions.assertFalse(game.discardCard(secondPlayerIdx, 1));
		Assertions.assertFalse(game.chooseCancel(secondPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(secondPlayerIdx));
		
		//first player can't do anything either, except...
		Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 0));
		Assertions.assertFalse(game.pass(firstPlayerIdx));
		Assertions.assertFalse(game.playCard(firstPlayerIdx, 0, 1));
		Assertions.assertFalse(game.discardCard(firstPlayerIdx, 1));
		Assertions.assertFalse(game.chooseCancel(firstPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(firstPlayerIdx));
		
		//choose his own bro in play, because that bro has the highest power val
		Assertions.assertTrue(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 0));
		
		//now the card played by the first player should be face down
		Assertions.assertTrue(game.getPositionsOfFaceDownCards(firstPlayerIdx).contains(0));
		Assertions.assertFalse(game.getPositionsOfFaceDownCards(firstPlayerIdx).isEmpty());
		
		//and verascher still face up
		Assertions.assertFalse(game.getPositionsOfFaceDownCards(secondPlayerIdx).contains(0));
		Assertions.assertTrue(game.getPositionsOfFaceDownCards(secondPlayerIdx).isEmpty());
		
		//first player now plays their verascher
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 0, -1));
		
		//now there are two veraschers on the field, both with powval 2, so second player can choose either one to turn face down
		Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, 0));
		Assertions.assertTrue(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, -1));
	}
	
	@Test
	void testKanonenfutterer2() {
		
		//MAKE PLAYERS + GAME
		Deck deck1 = new Deck();
		deck1.addCard(Card.getCard(18));
        deck1.addCard(Card.getCard(19));
        deck1.addCard(Card.getCard(20));
        deck1.addCard(Card.getCard(verascherID)); //ausbrecher will be index 0 in hand
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
        //assertTrue(deck1.checkIfLegal());

        List<Card> listOfCardsForDeck2 = new ArrayList<>();
		listOfCardsForDeck2.add(Card.getCard(18));
        listOfCardsForDeck2.add(Card.getCard(19));
        listOfCardsForDeck2.add(Card.getCard(20));
        listOfCardsForDeck2.add(Card.getCard(verascherID)); //ausbrecher will be index 0 in hand
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
		Assertions.assertTrue(game.getCardsInHand(firstPlayerIdx).get(0).getId() == verascherID);
		Assertions.assertTrue(game.getCardsInHand(secondPlayerIdx).get(0).getId() == verascherID);
		
		//first player plays a card with power val 3
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 1, 0));
		
		//that card is stille face up
		Assertions.assertFalse(game.getPositionsOfFaceDownCards(firstPlayerIdx).contains(0));
		Assertions.assertTrue(game.getPositionsOfFaceDownCards(firstPlayerIdx).isEmpty());
		
		//second player plays their verascher (power val 2)
		Assertions.assertTrue(game.playCard(secondPlayerIdx, 0, 0));
		
		//now second player cannot choose any card
		Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, secondPlayerIdx, 0));
		Assertions.assertFalse(game.pass(secondPlayerIdx));
		Assertions.assertFalse(game.playCard(secondPlayerIdx, 0, 1));
		Assertions.assertFalse(game.discardCard(secondPlayerIdx, 1));
		Assertions.assertFalse(game.chooseCancel(secondPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(secondPlayerIdx));
		
		//first player can't do anything either, except...
		Assertions.assertFalse(game.chooseCardInHand(firstPlayerIdx, 0));
		Assertions.assertFalse(game.chooseCardInPlay(firstPlayerIdx, secondPlayerIdx, 0));
		Assertions.assertFalse(game.pass(firstPlayerIdx));
		Assertions.assertFalse(game.playCard(firstPlayerIdx, 0, 1));
		Assertions.assertFalse(game.discardCard(firstPlayerIdx, 1));
		Assertions.assertFalse(game.chooseCancel(firstPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(firstPlayerIdx));
		
		//choose his own bro in play, because that bro has the highest power val
		Assertions.assertTrue(game.chooseCardInPlay(firstPlayerIdx, firstPlayerIdx, 0));
		
		//now the card played by the first player should be face down
		Assertions.assertTrue(game.getPositionsOfFaceDownCards(firstPlayerIdx).contains(0));
		Assertions.assertFalse(game.getPositionsOfFaceDownCards(firstPlayerIdx).isEmpty());
		
		//and verascher still face up
		Assertions.assertFalse(game.getPositionsOfFaceDownCards(secondPlayerIdx).contains(0));
		Assertions.assertTrue(game.getPositionsOfFaceDownCards(secondPlayerIdx).isEmpty());
		
		//first player now plays their verascher
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 0, -1));
		
		//now there are two veraschers on the field, both with powval 2, so second player can choose either one to turn face down
		Assertions.assertFalse(game.chooseCancel(secondPlayerIdx));
		Assertions.assertFalse(game.chooseAccept(secondPlayerIdx));
		Assertions.assertFalse(game.chooseCardInHand(secondPlayerIdx, 2));
		Assertions.assertFalse(game.chooseCardInPlay(secondPlayerIdx, firstPlayerIdx, 0));
		Assertions.assertTrue(game.chooseCardInPlay(secondPlayerIdx, secondPlayerIdx, 0));
	}
	
}