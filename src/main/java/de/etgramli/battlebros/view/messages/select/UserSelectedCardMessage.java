package de.etgramli.battlebros.view.messages.select;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Message expected as a response to SelectCardMessage.
 */
public class UserSelectedCardMessage extends MessageWithId {
    private final int playerIndex; // ToDo: remove
    private final SelectCardType selectCardType;
    private final List<Map.Entry<Integer,Integer>> indices;

    public UserSelectedCardMessage(final int playerIndex,
                                   @NonNull final SelectCardType selectCardType,
                                   @NonNull final Collection<Map.Entry<Integer, Integer>> indices) {
        this.playerIndex = playerIndex;
        this.selectCardType = selectCardType;
        this.indices = List.copyOf(indices);
    }

    public UserSelectedCardMessage(@NonNull final MessageWithId messageWithId,
                            final int playerIndex,
                            @NonNull final SelectCardType selectCardType,
                            @NonNull final Collection<Map.Entry<Integer, Integer>> indices) {
        super(messageWithId.getId());
        this.playerIndex = playerIndex;
        this.selectCardType = selectCardType;
        this.indices = List.copyOf(indices);
    }

    @JsonCreator
    public UserSelectedCardMessage(@NonNull final String id,
                                   final int playerIndex,
                                   @NonNull final SelectCardType selectCardType,
                                   @NonNull final Collection<Map.Entry<Integer, Integer>> indices) {
        super(id);
        this.playerIndex = playerIndex;
        this.selectCardType = selectCardType;
        this.indices = List.copyOf(indices);
    }

    public List<Pair<Integer, Integer>> getIndices() {
        return indices.stream().map(Pair::of).collect(Collectors.toList());
    }

    public SelectCardType getSelectCardType() {
        return selectCardType;
    }

    public int getPlayerIndex() {
        return playerIndex;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSelectedCardMessage that = (UserSelectedCardMessage) o;

        if (playerIndex != that.playerIndex) return false;
        if (selectCardType != that.selectCardType) return false;
        return indices.equals(that.indices);
    }

    @Override
    public int hashCode() {
        int result = playerIndex;
        result = 31 * result + selectCardType.hashCode();
        result = 31 * result + indices.hashCode();
        return result;
    }
}
