package de.etgramli.battlebros.model;

import de.etgramli.battlebros.model.card.Card;
import de.etgramli.battlebros.model.card.effect.CardEffect;
import de.etgramli.battlebros.model.card.effect.FlipEffect;
import de.etgramli.battlebros.model.card.effect.InvalidateEffectEffect;
import de.etgramli.battlebros.model.card.effect.ProhibitCardPlacementEffect;
import de.etgramli.battlebros.model.card.effect.ReviveEffect;
import de.etgramli.battlebros.model.card.effect.StrengthModifierEffect;
import de.etgramli.battlebros.model.card.effect.application.DiagonalApplication;
import de.etgramli.battlebros.model.card.effect.application.EffectApplication;
import de.etgramli.battlebros.model.card.effect.application.ElementApplication;
import de.etgramli.battlebros.model.card.effect.application.FacingApplication;
import de.etgramli.battlebros.model.card.effect.application.NeighborApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
    private static final Logger logger = LoggerFactory.getLogger(Board.class);

    private final ArrayList<ArrayList<CardTuple>> playedCards;

    public Board() {
        playedCards = new ArrayList<>(Game.NUMBER_OF_PLAYERS);
        playedCards.add(new ArrayList<>());
        playedCards.add(new ArrayList<>());
    }

    public Board(final Collection<Card> p1cards, final Collection<Card> p2cards) {
        this();
        playedCards.add(p1cards.stream().map(CardTuple::new).collect(Collectors.toCollection(ArrayList::new)));
        playedCards.add(p2cards.stream().map(CardTuple::new).collect(Collectors.toCollection(ArrayList::new)));
    }

    public List<CardTuple> getPlayedCards(final int playerIndex) {
        return playedCards.get(playerIndex).stream().filter(Objects::nonNull).toList();
    }

    /**
     * Null-safe stream of played cards (filters out null values in the list).
     *
     * @return Stream of CardTuples.
     */
    private Stream<CardTuple> playedCardStream() {
        return playedCards.stream().flatMap(Collection::stream).filter(Objects::nonNull);
    }

    private Set<CardTuple> playedCards() {
        return playedCardStream().collect(Collectors.toSet());
    }

    /**
     * Place a card on the board.
     *
     * @param playerIndex The index of the player placing the card (0 or 1).
     * @param card        The card to be placed, must not be null.
     * @param boardIndex  The index position on the board to place the card on (or -1 to place to the left of all cards).
     * @return True if the position was valid.
     */
    public boolean playCard(int playerIndex, final Card card, final int boardIndex) {
        assert playedCards.get(0).size() == playedCards.get(1).size();  // Pre-condition

        if (playerIndex < 0 || playerIndex > 1) {
            throw new IllegalArgumentException("Player index must be 0 or 1, was: " + playerIndex);
        }
        if (card == null) {
            throw new IllegalArgumentException("Card must not be null!");
        }
        if (boardIndex < -1 || boardIndex > playedCards.get(0).size()) {
            throw new IllegalArgumentException(String.format(
                    "boardIndex out of bounds! (valid: [-1;%d]; was: %d)", playedCards.get(0).size(), boardIndex));
        }

        final int otherPlayer = (playerIndex + 1) % 2;
        final ArrayList<CardTuple> currentPlayerCards = playedCards.get(playerIndex);
        final ArrayList<CardTuple> otherPlayerCards = playedCards.get(otherPlayer);

        final boolean retVal;
        if (boardIndex == -1) {
            currentPlayerCards.add(0, new CardTuple(card));
            otherPlayerCards.add(0, null);  // Make sure both ArrayLists have same size
            retVal = true;
        } else if (boardIndex == playedCards.get(0).size()) {
            currentPlayerCards.add(new CardTuple(card));
            otherPlayerCards.add(null);     // Make sure both ArrayLists have same size
            retVal = true;
        } else if (currentPlayerCards.get(boardIndex) == null) {
            currentPlayerCards.set(boardIndex, new CardTuple(card));
            retVal = true;
        } else {
            retVal = false;
        }

        if (retVal) {
            final Collection<CardEffect> onPlaceEffects = card.effects().stream()
                    .filter(CardEffect::isOnEntering)
                    .collect(Collectors.toSet());
            // ToDo: validate onPlaceEffects, if not disabled
            for (CardEffect effect : onPlaceEffects) {
                if (effect instanceof FlipEffect) {
                    // ToDo
                } else if (effect instanceof ReviveEffect) {
                    // ToDo
                }
            }
        }

        assert playedCards.get(0).size() == playedCards.get(1).size();  // Post-condition
        return retVal;
    }

    public Set<BoardPosition> getValidPositionsToPlayCard(final int playerIndex) {
        final Set<BoardPosition> validPositions = new HashSet<>();

        final ArrayList<CardTuple> playerRow = playedCards.get(playerIndex);
        final ArrayList<CardTuple> otherPlayerRow = playedCards.get(Game.getOtherPlayerNum(playerIndex));
        for (int rowNum = 0; rowNum < playerRow.size(); ++rowNum) {
            if (playerRow.get(rowNum) == null) {    // No card played here
                final BoardPosition position = new BoardPosition(playerIndex, rowNum);
                if (otherPlayerRow.get(rowNum) != null) {
                    // In front of other player's card
                    validPositions.add(position);
                }
                if (rowNum < playerRow.size() - 1 && playerRow.get(rowNum + 1) != null) {   // Has right neighbor
                    validPositions.add(position);
                }
                if (rowNum > 0 && playerRow.get(rowNum - 1) != null) {  // Has left neighbor
                    validPositions.add(position);
                }
            } else {
                if (rowNum + 1 == playerRow.size()) {
                    // Right end of row has card -> new card right to that
                    validPositions.add(new BoardPosition(playerIndex, rowNum + 1));
                } else if (rowNum == 0) {
                    // Left end of row has card -> new card left to that
                    validPositions.add(new BoardPosition(playerIndex, rowNum - 1));
                }
            }
        }

        validPositions.removeAll(getPositionsBlockedByEffect());

        return validPositions;
    }

    private Set<BoardPosition> getPositionsBlockedByEffect() {
        final Set<BoardPosition> blockedPositions = new HashSet<>();

        for (int playerNum = 0; playerNum < playedCards.size(); ++playerNum) {
            final ArrayList<CardTuple> currentRow = playedCards.get(playerNum);
            for (int rowNum = 0; rowNum < currentRow.size(); ++rowNum) {
                final List<ProhibitCardPlacementEffect> blockingEffects = currentRow.get(rowNum).card.effects().stream()
                        .filter(cardEffect -> cardEffect instanceof ProhibitCardPlacementEffect)
                        .map(cardEffect -> (ProhibitCardPlacementEffect) cardEffect)
                        .toList();
                for (ProhibitCardPlacementEffect effect : blockingEffects) {
                    EffectApplication application = effect.getDirection();
                    if (application instanceof FacingApplication) {
                        blockedPositions.add(new BoardPosition(Game.getOtherPlayerNum(playerNum), rowNum));
                    } else if (application instanceof NeighborApplication) {
                        blockedPositions.add(new BoardPosition(playerNum, rowNum - 1));
                        blockedPositions.add(new BoardPosition(playerNum, rowNum + 1));
                    }
                }
            }
        }
        return blockedPositions;
    }

    public int getPointsForPlayer(final int playerIndex) {
        int points = 0;
        for (int i = 0; i < playedCards.get(playerIndex).size(); ++i) {
            points += getEffectiveValue(playerIndex, i);
        }
        return points;
    }

    public int getEffectiveValue(final int playerIndex, final int cardIndex) {
        final List<CardTuple> sameRow = playedCards.get(playerIndex);
        final CardTuple cardTuple = sameRow.get(cardIndex);
        if (cardTuple.isFlipped()) {
            return 0;
        }
        final Card card = cardTuple.card;

        final Set<CardEffect> elementEffects = new HashSet<>();
        // Effects affecting the same elements
        for (CardEffect effect : getEffects()) {
            EffectApplication direction = effect.getDirection();
            if (direction instanceof ElementApplication) {
                if (card.element().equals(((ElementApplication) direction).getElement())) {
                    elementEffects.add(effect);
                }
            }
        }
        // Get neighbor effects
        if (cardIndex > 0) {
            elementEffects.addAll(sameRow.get(cardIndex - 1).card.effects());
        }
        if (cardIndex < sameRow.size() - 1) {
            elementEffects.addAll(sameRow.get(cardIndex + 1).card.effects());
        }
        // Get facing card effect
        final List<CardTuple> otherRow = playedCards.get(Game.getOtherPlayerNum(playerIndex));
        if (cardIndex > 0 && cardIndex < otherRow.size()) {
            elementEffects.addAll(otherRow.get(cardIndex).card.effects());
        }

        int value = card.value();
        for (CardEffect effect : elementEffects) {
            if (effect instanceof StrengthModifierEffect strengthModifierEffect) {
                value = strengthModifierEffect.apply(value);
            }
        }

        return value;
    }

    private Set<CardTuple> getInvalidatedEffectCards() {
        final Set<CardTuple> invalidatingEffectCards = playedCardStream()
                .filter(cardTuple -> cardTuple.card.hasInvalidatingEffect())
                .filter(cardTuple -> !cardTuple.isFlipped())
                .collect(Collectors.toSet());
        // ToDo: maybe add a directed (cyclic) graph to determine cycles -> ERROR

        // Get target positions
        final Set<BoardPosition> targets = new HashSet<>();
        for (CardTuple cardTuple : invalidatingEffectCards) {
            Optional<BoardPosition> optionalCardPosition = getPosition(cardTuple);
            if (optionalCardPosition.isEmpty()) {
                logger.error("Card with effect apparently not played! (%s)".formatted(cardTuple));
                continue;
            }
            BoardPosition cardPosition = optionalCardPosition.get();
            final int otherPlayerNum = Game.getOtherPlayerNum(cardPosition.playerRow);
            for (CardEffect effect : cardTuple.card.effects()) {
                if (effect instanceof InvalidateEffectEffect) {
                    final EffectApplication application = effect.getDirection();
                    if (application instanceof NeighborApplication) {
                        targets.add(new BoardPosition(cardPosition.playerRow, cardPosition.position - 1));
                        targets.add(new BoardPosition(cardPosition.playerRow, cardPosition.position + 1));
                    } else if (application instanceof FacingApplication) {
                        targets.add(new BoardPosition(otherPlayerNum, cardPosition.position));
                    } else if (application instanceof DiagonalApplication) {
                        targets.add(new BoardPosition(otherPlayerNum, cardPosition.position - 1));
                        targets.add(new BoardPosition(otherPlayerNum, cardPosition.position + 1));
                    }
                }
            }
        }

        final Set<CardTuple> invalidatedEffectCards = new HashSet<>();
        for (CardTuple cardTuple : playedCards()) {
            Optional<BoardPosition> position = getPosition(cardTuple);
            if (position.isPresent() && !targets.contains(position.get())) {
                invalidatedEffectCards.add(cardTuple);
            }
        }
        return invalidatedEffectCards;
    }

    private Optional<BoardPosition> getPosition(final CardTuple cardTuple) {
        for (int currentPlayerNum = 0; currentPlayerNum < Game.NUMBER_OF_PLAYERS; ++currentPlayerNum) {
            final List<CardTuple> currentPlayerRow = playedCards.get(currentPlayerNum);
            for (int position = 0; position < currentPlayerRow.size(); ++position) {
                CardTuple found = currentPlayerRow.get(position);
                if (cardTuple == found) {
                    return Optional.of(new BoardPosition(currentPlayerNum, position));
                }
            }
        }
        return Optional.empty();
    }

    private Set<CardEffect> getEffects() {
        final Set<CardTuple> invalidateEffectEffects = getInvalidatedEffectCards();

        return playedCardStream()
                .filter(cardTuple -> !invalidateEffectEffects.contains(cardTuple))
                .flatMap(cardTuple -> cardTuple.card.effects().stream())
                .collect(Collectors.toSet());
    }

    public record BoardPosition(int playerRow, int position) {
    }

    public static class CardTuple {
        public final Card card;
        private boolean isFlipped;  // True = face down, False = face up

        CardTuple(final Card card) {
            this.card = card;
            this.isFlipped = false;
        }

        boolean isFlipped() {
            return isFlipped;
        }

        void flip() {
            isFlipped = !isFlipped;
        }

        void setFaceUp() {
            isFlipped = false;
        }

        void setFaceDown() {
            isFlipped = true;
        }
    }
}
