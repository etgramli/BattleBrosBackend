package de.etgramli.battlebros.view;

import de.etgramli.battlebros.model.Card;
import org.springframework.lang.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class BoardDTO {
    private final List<List<Map.Entry<Integer, CardDTO>>> board;

    @NonNull
    private static TreeMap<Integer, CardDTO> toCardDtoMap(@NonNull final Map<Integer, Card> playedCards) {
        final TreeMap<Integer, CardDTO> playerOneBoard = new TreeMap<>();
        for (Map.Entry<Integer, Card> entry : playedCards.entrySet()) {
            playerOneBoard.put(entry.getKey(), CardDTO.of(entry.getValue()));
        }
        return playerOneBoard;
    }

    public BoardDTO(@NonNull final Map<Integer, Card> playerOneCards,
                    @NonNull final Map<Integer, Card> playerTwoCards) {
        final TreeMap<Integer, CardDTO> playerOneBoard = toCardDtoMap(playerOneCards);
        final TreeMap<Integer, CardDTO> playerTwoBoard = toCardDtoMap(playerTwoCards);

        // Fill missing indices
        final int minIndex;
        final int maxIndex;
        if (playerOneBoard.isEmpty() && playerTwoBoard.isEmpty()) {
            board = List.of(Collections.emptyList(), Collections.emptyList());
            return;
        } else if (playerOneBoard.isEmpty()) {
            minIndex = playerTwoBoard.firstKey();
            maxIndex = playerTwoBoard.lastKey();
        } else if (playerTwoBoard.isEmpty()) {
            minIndex = playerOneBoard.firstKey();
            maxIndex = playerOneBoard.lastKey();
        } else {
            minIndex = Integer.min(playerOneBoard.firstKey(), playerTwoBoard.firstKey());
            maxIndex = Integer.max(playerOneBoard.lastKey(), playerTwoBoard.lastKey());
        }

        IntStream.rangeClosed(minIndex, maxIndex).forEach(index -> {
            playerOneBoard.putIfAbsent(index, null);
            playerTwoBoard.putIfAbsent(index, null);
        });

        board = List.of(
                playerOneBoard.entrySet().stream().toList(),
                playerTwoBoard.entrySet().stream().toList()
        );
    }

    public List<List<Map.Entry<Integer, CardDTO>>> getBoard() {
        return board;
    }
}
