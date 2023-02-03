package de.etgramli.battlebros.view.messages.select;

import org.springframework.lang.NonNull;

/**
 * Message from backend to frontend to signal that the user has to select a card (and then send a message back).
 */
public record SelectCardMessage(SelectCardType selectCardType) {
    public SelectCardMessage(@NonNull final SelectCardType selectCardType) {
        this.selectCardType = selectCardType;
    }
}
