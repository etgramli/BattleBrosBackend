package de.etgramli.battlebros.model.board;

import de.etgramli.battlebros.CardUtil;
import de.etgramli.battlebros.model.card.Card;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void getPlayedCards_boardIsEmpty_getEmptyLists() {
        Board board = new Board();
        List<Board.CardTuple> p1Cards = board.getPlayedCards(0);
        List<Board.CardTuple> p2Cards = board.getPlayedCards(1);

        assertTrue(p1Cards.isEmpty());
        assertTrue(p2Cards.isEmpty());
    }

    @Test
    void getPointsForPlayer_noCards_zeroPoints() {
        Board board = new Board();

        assertEquals(0, board.getPointsForPlayer(0));
        assertEquals(0, board.getPointsForPlayer(1));
    }

    @Test
    void playCard_emptyBoard_oneCardCurrentPlayerNullOtherPlayer() {
        Board board = new Board();

        assertTrue(board.playCard(0, CardUtil.ANFEURER, -1));

        assertEquals(1, board.getPlayedCards(0).size());
        assertEquals(0, board.getPlayedCards(1).size());    // Has to be zero because nulls are filtered out
        assertSame(board.getPlayedCards(0).get(0).card, CardUtil.ANFEURER);
    }

    @Test
    void playCard_emptyBoard_cannotPlayCardsOnSamePosition() {
        Board board = new Board();

        assertTrue(board.playCard(0, CardUtil.ANFEURER, -1));

        assertFalse(board.playCard(0, CardUtil.ANFEURER, 0));
    }

    @Test
    void getEffectiveValue() {
    }
}