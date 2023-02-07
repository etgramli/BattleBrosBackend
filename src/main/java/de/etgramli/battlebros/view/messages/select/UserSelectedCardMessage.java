package de.etgramli.battlebros.view.messages.select;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Message expected as a response to SelectCardMessage.
 */
public record UserSelectedCardMessage(int playerIndex, // ToDo: remove
                                      SelectCardType selectCardType,
                                      List<Map.Entry<Integer,Integer>> indices) {

    public List<Pair<Integer, Integer>> getIndices() {
        return indices.stream().map(Pair::of).collect(Collectors.toList());
    }

    public int getFirstPlayerRow() {
        return indices.get(0).getKey();
    }

    public int getFirstIndex() {
        return indices.get(0).getValue();
    }

    public boolean validate() {
        if (playerIndex < 0 || playerIndex > 1) {
            return false;
        }
        if (indices.isEmpty()) {
            return false;
        }
        return switch (selectCardType) {
            case SELECT_MY_HAND_CARD -> indices.size() == 1 && indices.get(0).getValue() > 0;
            case SELECT_MY_PLAYED_CARD, SELECT_ANY_PLAYED_CARD, SELECT_OPPONENT_PLAYED_CARD, SELECT_DISCARDED_CARD -> indices.size() == 1;
            case SELECT_ANY_PLAYED_CARDS, SELECT_DISCARDED_CARDS -> true;
            case SELECT_SUCCESS -> false;
        };
    }
}
