package de.etgramli.battlebros.model;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    static Game game;
	static Player player1;
    static Player player2;
    static boolean terminate = false;


    public static void main(String[] args) {
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
        listOfCardsForDeck2.add(Card.getCard(1));
        listOfCardsForDeck2.add(Card.getCard(2));
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
        listOfCardsForDeck2.add(Card.getCard(18));
        listOfCardsForDeck2.add(Card.getCard(19));
        listOfCardsForDeck2.add(Card.getCard(20));
        Deck deck2 = new Deck(listOfCardsForDeck2);
        assertTrue(deck2.checkIfLegal());
		
		/*List<Card> listOfCardsForDeck2 = new ArrayList<>();
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
        assertTrue(deck2.checkIfLegal());*/

        player1 = new Player("Joshua", deck1);
        player2 = new Player("Etienne", deck2);

        game = new Game(player1, player2);

        game.startGame();
        drawGameState(player1, player2);
		
		gameLoop();
    }
	
	static void gameLoop(){
		while(!terminate){
			try{
				if (doGameAction())
					drawGameState(player1, player2);
			} catch(Exception ignored){
				ignored.printStackTrace();
			}
		}
		System.out.println("ERFOLGREICH BEENDET...");
	}

	static void printGameActionInstructions(){
		System.out.println("Player(p1|p2), Action(play|discard|pass|choose|chooseHand|chooseAbility|chooseMany|exit), Param(play:handIdx,fieldIdx|discard:handIdx|pass:none|choose:playerIdx,fieldIdx or Y/N|chooseHand:handIdx|chooseAbility:abilityIdx|chooseMany:playerIdx,fieldIdx,repeat)");
	}
	
	static boolean doGameAction() {
		Scanner in = new Scanner(System.in);
		String inputs = in.nextLine();

		if (inputs.trim().equalsIgnoreCase("exit")) {
			terminate = true;
			return false;
		}

		String[] inputParts = inputs.split(",");

		int actorIdx;
		String input = inputParts[0].trim().toLowerCase();
		if (input.equals("p1"))
			actorIdx = 0;
		else if (input.equals("p2"))
			actorIdx = 1;
		else
			return false;

		input = inputParts[1].trim().toLowerCase();
		if (input.equals("play")) {
			if (inputParts.length != 4)
				return false;
			int handIdx = Integer.parseInt(inputParts[2].trim());
			int fieldIdx = Integer.parseInt(inputParts[3].trim());
			return game.playCard(actorIdx, handIdx - 1, fieldIdx);
		} else if (input.equals("discard")) {
			if (inputParts.length != 3)
				return false;
			int handIdx = Integer.parseInt(inputParts[2].trim());
			return game.discardCard(actorIdx, handIdx - 1);
		} else if (input.equals("pass")) {
			if (inputParts.length != 2)
				return false;
			return game.pass(actorIdx);
		} else if (input.equals("choose")) {
			if (inputParts.length == 3){
				String inputParts3 = inputParts[2].trim().toLowerCase();
				if (inputParts3.equals("y"))
					return game.chooseAccept(actorIdx);
				else if (inputParts3.equals("n"))
					return game.chooseCancel(actorIdx);
				else
					return false;
			} else if (inputParts.length == 4){
				int playerIdx = Integer.parseInt(inputParts[2].trim());
				int fieldIdx = Integer.parseInt(inputParts[3].trim());
				return game.chooseCardInPlay(actorIdx, playerIdx - 1, fieldIdx);
			} else
				return false;
		} else if (input.equals("choosehand")) {
			if (inputParts.length != 3)
				return false;
			int handIdx = Integer.parseInt(inputParts[2].trim());
			return game.chooseCardInHand(actorIdx, handIdx - 1);
		} else if (input.equals("chooseability")) {
			if (inputParts.length != 3)
				return false;
			int abilityIdx = Integer.parseInt(inputParts[2].trim());
			return game.chooseAbilityToResolve(actorIdx, abilityIdx - 1);
		} else if (input.equals("choosemany")){
			if (inputParts.length < 2 || inputParts.length%2 != 0)
				return false;
			List<Pair<Integer,Integer>> selections = new ArrayList<>();
			int i = 2;
			while (i < inputParts.length){
				int playerIdx = Integer.parseInt(inputParts[i].trim());
				int fieldIdx = Integer.parseInt(inputParts[i+1].trim());
				selections.add(new ImmutablePair<>(playerIdx-1, fieldIdx));
				i += 2;
			}
			return game.chooseCardsInPlay(actorIdx, selections);
		}
		return false;
	}

    static void drawGameState(Player player1, Player player2) {
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
		printGameActionInstructions();
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
			boolean isFaceUp = player.isCardFaceUp(entry.getKey());
			String faceDownText = "";
			if (!isFaceUp)
				faceDownText = " VERDECKT";
            System.out.printf("[" + entry.getKey() + ": "+ entry.getValue().getName() + "(" + player.getValueOfCardOnFieldAt(entry.getKey()) + ")" + faceDownText + "]  ");
		}
		
		System.out.println("\n");
    }

}