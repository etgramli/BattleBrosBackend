package de.etgramli.battlebros.model;

import de.etgramli.battlebros.model.card.Card;
import de.etgramli.battlebros.model.card.CardElement;
import de.etgramli.battlebros.model.card.effect.CardEffect;
import de.etgramli.battlebros.model.card.effect.EffectType;
import de.etgramli.battlebros.model.card.effect.ValueModifyEffect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Board {
    private static final Logger logger = LoggerFactory.getLogger(Board.class);

    // Need list implementation with fast index access, allows null values and can add elements at the start
    // Fast index access -> Place, flip, ... a card at a certain position -> array-based impl
    // Null values -> keep implementation simpler, so that an index from one row works on the other player's row as well
    // Add elements at both sides, because that is what both players can do
    private final ArrayList<ArrayList<CardTuple>> playedCards;

    private final List<List<Card>> graveyards;

    public Board() {
        playedCards = new ArrayList<>(GameInterface.NUMBER_OF_PLAYERS);
        playedCards.add(new ArrayList<>());
        playedCards.add(new ArrayList<>());

        graveyards = new ArrayList<>(GameInterface.NUMBER_OF_PLAYERS);
        graveyards.add(new ArrayList<>());
        graveyards.add(new ArrayList<>());
    }

    /**
     * Determine the played cards of a specific player.
     * @param playerIndex Player's index ([0,1]).
     * @return List of CardTuples, not null, may be empty, no null elements.
     */
    public List<CardTuple> getPlayedCards(final int playerIndex) {
        return playedCards.get(playerIndex).stream().filter(Objects::nonNull).toList();
    }

    public void movePlayedCardsToGraveyard() {
        for (int playerNum = 0; playerNum < playedCards.size(); ++playerNum) {
            final List<CardTuple> playerRow = playedCards.get(playerNum);
            final List<Card> graveYard = graveyards.get(playerNum);
            playerRow.stream()
                    .filter(Objects::nonNull)
                    .map(cardTuple -> cardTuple.card)
                    .forEach(graveYard::add);
        }
    }

    public List<Card> getGraveyard(final int playerNum) {
        return Collections.unmodifiableList(graveyards.get(playerNum));
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

    public boolean playCard(final Card card, final BoardPosition position) {
        return playCard(position.playerRow, card, position.position);
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
                if (effect.getType().equals(EffectType.FLIP_FACE_DOWN)) {
                    // ToDo
                } else if (effect.getType().equals(EffectType.REVIVE)) {
                    // ToDo
                }
            }
        }

        assert playedCards.get(0).size() == playedCards.get(1).size();  // Post-condition
        return retVal;
    }

    /**
     * Determine the spots where the specified player may place cards.
     * @param playerIndex Player's index ([0,1]).
     * @return Set of BoardPositions, not null, may be empty.
     */
    public Set<BoardPosition> getValidPositionsToPlayCard(final int playerIndex) {
        final Set<BoardPosition> validPositions = new HashSet<>();

        final ArrayList<CardTuple> playerRow = playedCards.get(playerIndex);
        final ArrayList<CardTuple> otherPlayerRow = playedCards.get(GameInterface.getOtherPlayerNum(playerIndex));
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

    /**
     * Return the positions where no cards can be played due to an effect.
     * @return Set of BoardPosition, not null, maybe empty.
     */
    private Set<BoardPosition> getPositionsBlockedByEffect() {
        final Set<BoardPosition> blockedPositions = new HashSet<>();

        for (int playerNum = 0; playerNum < playedCards.size(); ++playerNum) {
            final ArrayList<CardTuple> currentRow = playedCards.get(playerNum);
            for (int rowNum = 0; rowNum < currentRow.size(); ++rowNum) {
                final List<CardEffect> blockingEffects = currentRow.get(rowNum).card.effects().stream()
                        .filter(cardEffect -> cardEffect.getType().equals(EffectType.RESTRICT_CARD_PLACEMENT))
                        .toList();
                for (CardEffect effect : blockingEffects) {
                    switch (effect.getTarget()) {
                        case FACING -> blockedPositions.add(new BoardPosition(GameInterface.getOtherPlayerNum(playerNum), rowNum));
                        case NEIGHBOR -> {
                            blockedPositions.add(new BoardPosition(playerNum, rowNum - 1));
                            blockedPositions.add(new BoardPosition(playerNum, rowNum + 1));
                        }
                    }
                }
            }
        }
        return blockedPositions;
    }

    /**
     * Return the effective amount (after effect application) of all cards of the selected player.
     * @param playerIndex The index of the player ([0, 1]).
     * @return Non-negative integer.
     */
    public int getPointsForPlayer(final int playerIndex) {
        int points = 0;
        for (int i = 0; i < playedCards.get(playerIndex).size(); ++i) {
            points += getEffectiveValue(playerIndex, i);
        }
        return points;
    }

    /**
     * Determine the effective value of a card on the board (after effect application, non-flipped cards).
     * @param playerIndex The row index of the player ([0,1]).
     * @param cardIndex Index of the card.
     * @return Non-negative integer.
     */
    public int getEffectiveValue(final int playerIndex, final int cardIndex) {
        final List<CardTuple> sameRow = playedCards.get(playerIndex);
        final CardTuple cardTuple = sameRow.get(cardIndex);
        if (cardTuple == null || cardTuple.isFlipped()) {
            return 0;
        }
        final Card card = cardTuple.card;

        final Set<CardEffect> elementEffects = new HashSet<>();
        for (CardEffect effect : getActiveEffects()) {
            CardElement element = card.element();
            switch (effect.getTarget()) {
                case ELEMENT_ERDE -> {
                    if (CardElement.ERDE.equals(element)) elementEffects.add(effect);
                }
                case ELEMENT_FEUER -> {
                    if (CardElement.FEUER.equals(element)) elementEffects.add(effect);
                }
                case ELEMENT_LUFT -> {
                    if (CardElement.LUFT.equals(element)) elementEffects.add(effect);
                }
                case ELEMENT_WASSER -> {
                    if (CardElement.WASSER.equals(element)) elementEffects.add(effect);
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
        final List<CardTuple> otherRow = playedCards.get(GameInterface.getOtherPlayerNum(playerIndex));
        if (cardIndex > 0 && cardIndex < otherRow.size()) {
            elementEffects.addAll(otherRow.get(cardIndex).card.effects());
        }

        int value = card.value();
        for (CardEffect effect : elementEffects) {
            if (effect instanceof ValueModifyEffect valueModifierEffect) {
                value = valueModifierEffect.apply(value);
            }
        }

        return value;
    }

    /**
     * Determine the CardTuple, of which the effects are invalidated by other card's effects.
     * @return Set of CardTuples, not null, maybe empty.
     */
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
            final int otherPlayerNum = GameInterface.getOtherPlayerNum(cardPosition.playerRow);
            for (CardEffect effect : cardTuple.card.effects()) {
                if (effect.getType().equals(EffectType.DISABLE_EFFECT)) {
                    switch (effect.getTarget()) {
                        case NEIGHBOR -> {
                            targets.add(new BoardPosition(cardPosition.playerRow, cardPosition.position - 1));
                            targets.add(new BoardPosition(cardPosition.playerRow, cardPosition.position + 1));
                        }
                        case FACING -> targets.add(new BoardPosition(otherPlayerNum, cardPosition.position));
                        case DIAGONAL -> {
                            targets.add(new BoardPosition(otherPlayerNum, cardPosition.position - 1));
                            targets.add(new BoardPosition(otherPlayerNum, cardPosition.position + 1));
                        }
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

    /**
     * Determine the position of a CardTuple on the board.
     * @param cardTuple Non-null CardTuple to search on the board.
     * @return Position on the board or empty, if not on the board.
     */
    private Optional<BoardPosition> getPosition(final CardTuple cardTuple) {
        for (int currentPlayerNum = 0; currentPlayerNum < GameInterface.NUMBER_OF_PLAYERS; ++currentPlayerNum) {
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

    /**
     * Return the effects that are not invalidated by other effects.
     * @return Set of CardEffects, not null, maybe empty.
     */
    private Set<CardEffect> getActiveEffects() {
        final Set<CardTuple> invalidateEffectEffects = getInvalidatedEffectCards();

        return playedCardStream()
                .filter(cardTuple -> !invalidateEffectEffects.contains(cardTuple))
                .flatMap(cardTuple -> cardTuple.card.effects().stream())
                .collect(Collectors.toSet());
    }

    public List<List<CardTuple>> getImmutableState() {
        return playedCards.stream()
                .map(Collections::unmodifiableList)
                .toList();
    }

    /**
     * Record to represent the position on the board.
     * @param playerRow Index of the player's row ([0,1]).
     * @param position Index of the card in the row.
     */
    public record BoardPosition(int playerRow, int position) {}

    /**
     * State of the card on the board. Card itself and whether if it is flipped or not.
     */
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
