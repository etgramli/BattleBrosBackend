package de.etgramli.battlebros.view;

import de.etgramli.battlebros.model.Card;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
                    @NonNull final Map<Integer, Card> playerTwoCards,
                    @NonNull final Collection<Integer> playerOneFaceDownCardIndices,
                    @NonNull final Collection<Integer> playerTwoFaceDownCardIndices) {
        final TreeMap<Integer, CardDTO> playerOneBoard = toCardDtoMap(playerOneCards);
        final TreeMap<Integer, CardDTO> playerTwoBoard = toCardDtoMap(playerTwoCards);

        // Overwrite with zero value DTO so that the UI shows the back side (image index 0) if card is face down
        playerOneFaceDownCardIndices.forEach(index -> playerOneBoard.put(index, CardDTO.getFaceDown()));
        playerTwoFaceDownCardIndices.forEach(index -> playerTwoBoard.put(index, CardDTO.getFaceDown()));

        // Fill empty spaces with null values, so that the UI can draw nothing in middle cells
        playerOneBoard.keySet().forEach(i -> playerTwoBoard.putIfAbsent(i, null));
        playerTwoBoard.keySet().forEach(i -> playerOneBoard.putIfAbsent(i, null));

        board = List.of(
                playerOneBoard.entrySet().stream().toList(),
                playerTwoBoard.entrySet().stream().toList()
        );
    }

    public List<List<Map.Entry<Integer, CardDTO>>> getBoard() {
        return board;
    }
}
