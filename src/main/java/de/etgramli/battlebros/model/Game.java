package de.etgramli.battlebros.model;

import de.etgramli.battlebros.CardUtil;
import de.etgramli.battlebros.model.card.Card;
import de.etgramli.battlebros.util.IObservable;
import de.etgramli.battlebros.util.IObserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class Game implements IObservable {

    public static final int NUMBER_OF_PLAYERS = 2;
    private final Board board;
    private final List<Player> players;
    private final LinkedList<Integer> folded = new LinkedList<>();
    private final int[] roundsWon = {0, 0};
    private int round = 1;
    private int currentPlayer = 0;

    private final List<LinkedList<Card>> playerHands;
    private final List<LinkedList<Card>> playerDecks = new ArrayList<>(NUMBER_OF_PLAYERS);

    public Game(final Collection<Player> players) {
        if (players.size() != NUMBER_OF_PLAYERS) {
            throw new IllegalArgumentException("Number of players must be %d!".formatted(NUMBER_OF_PLAYERS));
        }
        this.players = List.copyOf(players);
        board = new Board();

        // Both players start with empty hands
        playerHands = List.of(new LinkedList<>(), new LinkedList<>());

        final List<Card> testDeck = List.of(CardUtil.FEDERBALL, CardUtil.WASSERLAEUFER, CardUtil.ANFEURER);
        playerDecks.add(new LinkedList<>(testDeck));
        playerDecks.add(new LinkedList<>(testDeck));
        shuffleDecks();
        drawCardsBeforeRound();
    }

    public static int getOtherPlayerNum(final int currentPlayerNum) {
        return (currentPlayerNum + 1) % NUMBER_OF_PLAYERS;
    }

    // Getters
    public int getRoundsWon(final int playerIndex) {
        return roundsWon[playerIndex];
    }

    public int getCurrentPlayerIndex() {
        return currentPlayer;
    }

    public String getPlayerName(final int playerIndex) {
        return players.get(playerIndex).name();
    }

    // Gameplay methods
    public void playCard() {
        // ToDo: determine available spots, set card to position
        switchPlayer();
    }

    public void fold() {
        folded.addLast(currentPlayer);
        switchPlayer();
    }

    private boolean doesRoundEnd() {
        final boolean allPlayersFolded = folded.stream().distinct().count() == 2;
        return allPlayersFolded;
    }

    private void switchPlayer() {
        currentPlayer = getOtherPlayerNum(currentPlayer);
    }

    private void drawCardsBeforeRound() {
        final int cardsToDraw = switch (round) {
            case 1 -> 6;    // First round: draw 6
            case 2, 3 -> 3; // 2nd + 3rd round: draw 3
            default -> 0;   // Game ends after 3rd round
        };

        for (int i = 0; i < players.size(); ++i) {
            drawCards(i, cardsToDraw);
        }
    }

    private void advanceToNextRound() {
        // Calculate points and determine who won the current round
        final int pointsP1 = board.getPointsForPlayer(0);
        final int pointsP2 = board.getPointsForPlayer(1);
        if (pointsP1 > pointsP2) {
            roundsWon[0]++;
        } else if (pointsP2 > pointsP1) {
            roundsWon[1]++;
        } else {
            int playerFolded = folded.getFirst(); // Assuming the player who folds first placed at index ß
            roundsWon[playerFolded]++;
        }
        folded.clear(); // Reset fold state
        ++round;        // Next round
    }

    private void drawCards(final int playerIndex, final int numberOfCards) {
        Queue<Card> playerDeck = playerDecks.get(playerIndex);
        Collection<Card> playerHand = playerHands.get(playerIndex);

        for (int i = 0; i < numberOfCards; i++) {
            Card card = playerDeck.poll();
            if (card == null) {
                break;  // Deck is empty
            }
            playerHand.add(card);
        }
    }

    private void shuffleDecks() {
        playerDecks.forEach(Collections::shuffle);
    }


    private final List<IObserver> observers = new ArrayList<>();
    @Override
    public void addObserver(final IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(IObserver::update);
    }
}
