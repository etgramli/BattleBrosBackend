package de.etgramli.battlebros.view;

import de.etgramli.battlebros.model.Card;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BoardDTOTest {

    @Test
    void getBoard_emptyPlayerBoards() {
        final Map<Integer, Card> playedCards = Collections.emptyMap();

        final List<List<Map.Entry<Integer, CardDTO>>> board = BoardDTO.from(playedCards, playedCards,
                Collections.emptyList(), Collections.emptyList()).board();

        assertEquals(2, board.size());
        final List<Map.Entry<Integer, CardDTO>> p0row = board.get(0);
        final List<Map.Entry<Integer, CardDTO>> p1row = board.get(1);
        assertEquals(p0row, p1row);
        assertEquals(0, p0row.size());
        assertEquals(0, p1row.size());
    }

    @Test
    void getBoard_oneEmptyPlayerBoard() {
        final Map<Integer, Card> playedCardsOne = Collections.emptyMap();
        final Map<Integer, Card> playedCardsTwo = Map.of(0, Card.getCard(1), 1, Card.getCard(2), 2, Card.getCard(3));

        final List<List<Map.Entry<Integer, CardDTO>>> board = BoardDTO.from(playedCardsOne, playedCardsTwo,
                Collections.emptyList(), Collections.emptyList()).board();

        assertEquals(2, board.size());
        final List<Map.Entry<Integer, CardDTO>> p0row = board.get(0);
        final List<Map.Entry<Integer, CardDTO>> p1row = board.get(1);
        assertEquals(p0row.size(), p1row.size());
        assertEquals(3, p0row.size());
    }

    @Test
    void getBoard_samePlayerBoards() {
        final Map<Integer, Card> playedCards = Map.of(0, Card.getCard(1), 1, Card.getCard(2), 2, Card.getCard(3));

        final List<List<Map.Entry<Integer, CardDTO>>> board = BoardDTO.from(playedCards, playedCards,
                Collections.emptyList(), Collections.emptyList()).board();

        assertEquals(2, board.size());
        final List<Map.Entry<Integer, CardDTO>> p0row = board.get(0);
        final List<Map.Entry<Integer, CardDTO>> p1row = board.get(1);
        assertEquals(p0row, p1row);
        assertEquals(playedCards.size(), p0row.size());
        assertEquals(playedCards.size(), p1row.size());
        assertEquals(0, p0row.get(0).getKey());
        assertEquals(2, p0row.get(p0row.size() - 1).getKey());
    }

    @Test
    void getBoard_otherPlayerHasOneCardOnTheLeftMore() {
        final Map<Integer, Card> otherPlayedCards = Map.of(-1, Card.getCard(1), 0, Card.getCard(5));
        final Map<Integer, Card> thisPlayedCards = Map.of(0, Card.getCard(2));

        final List<List<Map.Entry<Integer, CardDTO>>> board = BoardDTO.from(otherPlayedCards, thisPlayedCards,
                Collections.emptyList(), Collections.emptyList()).board();

        assertEquals(2, board.size());
        final List<Map.Entry<Integer, CardDTO>> p0row = board.get(0);
        final List<Map.Entry<Integer, CardDTO>> p1row = board.get(1);
        assertNotEquals(p0row, p1row);
        assertEquals(p0row.size(), p1row.size());
        assertEquals(2, p0row.size());

        assertEquals(p0row.get(0).getKey(), p1row.get(0).getKey());
        assertEquals(p0row.get(p0row.size() - 1).getKey(), p1row.get(p1row.size() - 1).getKey());
    }

    @Test
    void getBoard_otherPlayerHasOneCardOnTheRightMore() {
        final Map<Integer, Card> otherPlayedCards = Map.of(0, Card.getCard(5));
        final Map<Integer, Card> thisPlayedCards = Map.of(0, Card.getCard(2), 1, Card.getCard(4));

        final List<List<Map.Entry<Integer, CardDTO>>> board = BoardDTO.from(otherPlayedCards, thisPlayedCards,
                Collections.emptyList(), Collections.emptyList()).board();

        assertEquals(2, board.size());
        final List<Map.Entry<Integer, CardDTO>> p0row = board.get(0);
        final List<Map.Entry<Integer, CardDTO>> p1row = board.get(1);
        assertNotEquals(p0row, p1row);
        assertEquals(p0row.size(), p1row.size());
        assertEquals(2, p0row.size());

        assertEquals(p0row.get(0).getKey(), p1row.get(0).getKey());
        assertEquals(p0row.get(p0row.size() - 1).getKey(), p1row.get(p1row.size() - 1).getKey());
    }

    @Test
    void getBoard_onePlayerHavingOneCardMoreOnOtherSide() {
        final Map<Integer, Card> otherPlayedCards = Map.of(-1, Card.getCard(1), 0, Card.getCard(5));
        final Map<Integer, Card> thisPlayedCards = Map.of(0, Card.getCard(2), 1, Card.getCard(7));

        final List<List<Map.Entry<Integer, CardDTO>>> board = BoardDTO.from(otherPlayedCards, thisPlayedCards,
                Collections.emptyList(), Collections.emptyList()).board();

        assertEquals(2, board.size());
        final List<Map.Entry<Integer, CardDTO>> p0row = board.get(0);
        final List<Map.Entry<Integer, CardDTO>> p1row = board.get(1);
        assertNotEquals(p0row, p1row);
        assertEquals(p0row.size(), p1row.size());
        assertEquals(3, p0row.size());

        assertEquals(p0row.get(0).getKey(), p1row.get(0).getKey());
        assertEquals(p0row.get(p0row.size() - 1).getKey(), p1row.get(p1row.size() - 1).getKey());
    }
}
