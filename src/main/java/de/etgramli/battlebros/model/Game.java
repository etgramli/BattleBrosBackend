package de.etgramli.battlebros.model;

import de.etgramli.battlebros.CardUtil;
import de.etgramli.battlebros.model.card.Card;
import de.etgramli.battlebros.util.IObservable;
import de.etgramli.battlebros.util.IObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public final class Game implements IObservable {

    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    public static final int NUMBER_OF_PLAYERS = 2;

    private final Board board;
    private final List<Player> players;
    private final LinkedList<Integer> folded = new LinkedList<>();
    private final int[] roundsWon = {0, 0};
    private int round = 1;
    private int currentPlayer = 0;

    private final List<List<Card>> playerHands;
    // Needs to be a LinkedList so that it also can be used as a queue
    private final List<LinkedList<Card>> playerDecks = new ArrayList<>(NUMBER_OF_PLAYERS);

    public Game(final Collection<Player> players) {
        if (players.size() != NUMBER_OF_PLAYERS) {
            throw new IllegalArgumentException("Number of players must be %d!".formatted(NUMBER_OF_PLAYERS));
        }
        this.players = List.copyOf(players);
        board = new Board();

        // Both players start with empty hands
        playerHands = List.of(new ArrayList<>(), new ArrayList<>());

        // For the start use pre-built decks
        final List<List<Card>> randomDecks = new ArrayList<>(List.of(CardUtil.FEURIO, CardUtil.DAEMOND_RISING));
        Collections.shuffle(randomDecks);
        randomDecks.stream().map(LinkedList::new).forEach(playerDecks::add);

        shuffleDecks();
        drawCardsBeforeRound();
        logger.info("Initialized game");
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

    public List<Card> getPlayerHand(final int playerIndex) {
        if (playerIndex < 0 || playerIndex > 1) {
            throw new IllegalArgumentException("playerIndex must be 0 or 1, but was " + playerIndex);
        }
        return List.copyOf(playerHands.get(playerIndex));
    }

    public List<List<Board.CardTuple>> getPlayedCards() {
        return board.getImmutableState();
    }

    // Gameplay methods
    public boolean playCard(final int cardHandIndex, final Board.BoardPosition position) {
        final Set<Board.BoardPosition> validPositions = board.getValidPositionsToPlayCard(currentPlayer);
        if (!validPositions.contains(position)) {
            return false;
        }

        final List<Card> currentPlayerHand = playerHands.get(currentPlayer);
        if (!board.playCard(currentPlayerHand.get(cardHandIndex), position)) {
            return false;
        }

        currentPlayerHand.remove(cardHandIndex);
        switchPlayer();
        return true;
    }

    public Set<Board.BoardPosition> getValidPositions() {
        return board.getValidPositionsToPlayCard(currentPlayer);
    }

    public void fold() {
        logger.info("Player %d folded".formatted(currentPlayer));
        folded.addLast(currentPlayer);
        if (doesRoundEnd()) {
            advanceToNextRound();
        } else {
            switchPlayer();
        }
    }

    private boolean doesRoundEnd() {
        return folded.stream().distinct().count() == 2; // Both players folded
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
        final Queue<Card> playerDeck = playerDecks.get(playerIndex);
        final List<Card> playerHand = playerHands.get(playerIndex);

        int i;
        for (i = 0; i < numberOfCards; i++) {
            Card card = playerDeck.poll();
            if (card == null) {
                break;  // Deck is empty
            }
            playerHand.add(card);
        }
        logger.info("Player %d had to draw %d cards, actually took %d".formatted(playerIndex, numberOfCards, i));
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
