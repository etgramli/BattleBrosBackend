package de.etgramli.battlebros.view.messages.select;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

/**
 * Message from backend to frontend to signal that the user has to select a card (and then send a message back).
 */
public final class SelectCardMessage extends MessageWithId {
    private final SelectCardType selectCardType;

    @JsonCreator
    public SelectCardMessage(@NonNull @JsonProperty("id") final String id,
                             @NonNull @JsonProperty("selectCardType") final SelectCardType selectCardType) {
        super(id);
        this.selectCardType = selectCardType;
    }

    public SelectCardMessage(@NonNull final SelectCardType selectCardType) {
        this.selectCardType = selectCardType;
    }

    public SelectCardType getSelectCardType() {
        return selectCardType;
    }
}
