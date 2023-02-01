package de.etgramli.battlebros.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbilityTest004 {
	
	//Remember:
	// At the beginning of a game: 3 life cards are added THEN 6 hand cards are drawn
	// Top card of the deck = index 0 of gameZoneDeck
	// so: first 3 cards of deck go to life & next 6 cards after that go to hand (deckIdx 3 == handIndx 0)

	int kanonenfuttererID = 4;
	int firstPlayerIdx = 0;
	int secondPlayerIdx = 1;

    @Test
	void testKanonenfutterer1() {
		
		//MAKE PLAYERS + GAME
		Deck deck1 = new Deck();
		deck1.addCard(Card.getCard(18));
        deck1.addCard(Card.getCard(19));
        deck1.addCard(Card.getCard(20));
        deck1.addCard(Card.getCard(kanonenfuttererID)); //ausbrecher will be index 0 in hand
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
        assertTrue(deck1.checkIfLegal());

        List<Card> listOfCardsForDeck2 = new ArrayList<>();
		listOfCardsForDeck2.add(Card.getCard(18));
        listOfCardsForDeck2.add(Card.getCard(19));
        listOfCardsForDeck2.add(Card.getCard(20));
        listOfCardsForDeck2.add(Card.getCard(kanonenfuttererID)); //ausbrecher will be index 0 in hand
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
        assertTrue(deck2.checkIfLegal());

        Player player1 = new Player("Josh-1", deck1);
        Player player2 = new Player("Eti-2", deck2);
		
		Game game = new Game(player1, player2);

		game.startGameWithoutShuffling();
		
		System.out.println(game.getCardIDsInHand(firstPlayerIdx));
		System.out.println(game.getCardIDsInHand(secondPlayerIdx));
		Assertions.assertTrue(game.getCardsInHand(firstPlayerIdx).get(0).getId() == kanonenfuttererID);
		Assertions.assertTrue(game.getCardsInHand(secondPlayerIdx).get(0).getId() == kanonenfuttererID);
		
		//first player plays a card
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 1, 0));
		
		//that card is stille face up
		Assertions.assertFalse(game.getPositionsOfFaceDownCards(firstPlayerIdx).contains(0));
		Assertions.assertTrue(game.getPositionsOfFaceDownCards(firstPlayerIdx).isEmpty());
		
		//second player plays their kanonenfutterer
		Assertions.assertTrue(game.playCard(secondPlayerIdx, 0, 0));
		
		//now the card played by the first player should be face down
		Assertions.assertTrue(game.getPositionsOfFaceDownCards(firstPlayerIdx).contains(0));
		Assertions.assertFalse(game.getPositionsOfFaceDownCards(firstPlayerIdx).isEmpty());
		
		//and kanonenfutterer still face up
		Assertions.assertFalse(game.getPositionsOfFaceDownCards(secondPlayerIdx).contains(0));
		Assertions.assertTrue(game.getPositionsOfFaceDownCards(secondPlayerIdx).isEmpty());
		
		//first player now plays their kanonenfutterer without an opposite bro for it to turn face down
		Assertions.assertTrue(game.playCard(firstPlayerIdx, 0, -1));
		
		//there still should be zero face down cards on second player's side of the field
		Assertions.assertFalse(game.getPositionsOfFaceDownCards(secondPlayerIdx).contains(0));
		Assertions.assertTrue(game.getPositionsOfFaceDownCards(secondPlayerIdx).isEmpty());
		
		//even if they play a bro on that spot
		Assertions.assertTrue(game.playCard(secondPlayerIdx, 1, -1));
		Assertions.assertFalse(game.getPositionsOfFaceDownCards(secondPlayerIdx).contains(0));
		Assertions.assertFalse(game.getPositionsOfFaceDownCards(secondPlayerIdx).contains(-1));
		Assertions.assertTrue(game.getPositionsOfFaceDownCards(secondPlayerIdx).isEmpty());
	}
	
}