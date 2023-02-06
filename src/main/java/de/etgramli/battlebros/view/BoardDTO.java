package de.etgramli.battlebros.view;

import de.etgramli.battlebros.model.Card;
import de.etgramli.battlebros.model.GameInterface;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static de.etgramli.battlebros.util.CollectionUtil.listFromMap;

public record BoardDTO(List<List<Map.Entry<Integer, CardDTO>>> board) {

    @NonNull
    private static TreeMap<Integer, CardDTO> toCardDtoMap(@NonNull final Map<Integer, Card> playedCards,
                                                          @NonNull final Collection<Integer> faceDownPositions) {
        return playedCards.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> CardDTO.from(e.getValue(), !faceDownPositions.contains(e.getKey())),
                (a, b) -> b,
                TreeMap::new));
    }

    @NonNull
    static BoardDTO from(@NonNull final Map<Integer, Card> playerOneCards,
                         @NonNull final Map<Integer, Card> playerTwoCards,
                         @NonNull final Collection<Integer> playerOneFaceDownCardIndices,
                         @NonNull final Collection<Integer> playerTwoFaceDownCardIndices) {
        final TreeMap<Integer, CardDTO> playerOneBoard = toCardDtoMap(playerOneCards, playerOneFaceDownCardIndices);
        final TreeMap<Integer, CardDTO> playerTwoBoard = toCardDtoMap(playerTwoCards, playerTwoFaceDownCardIndices);

        // Fill empty spaces with null values, so that the UI can draw nothing in middle cells
        playerOneBoard.keySet().forEach(i -> playerTwoBoard.putIfAbsent(i, null));
        playerTwoBoard.keySet().forEach(i -> playerOneBoard.putIfAbsent(i, null));

        return new BoardDTO(List.of(listFromMap(playerOneBoard), listFromMap(playerTwoBoard)));
    }

    @NonNull
    public static BoardDTO from(@NonNull final GameInterface game) {
        return from(game.getCardsInPlay(0),
                    game.getCardsInPlay(1),
                    game.getPositionsOfFaceDownCards(0),
                    game.getPositionsOfFaceDownCards(1));
    }
}
