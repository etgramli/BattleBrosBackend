package de.etgramli.battlebros.view.messages.select;

import org.springframework.lang.NonNull;

/**
 * Message from backend to frontend to signal that the user has to select a card (and then send a message back).
 */
public record SelectCardMessage(SelectType selectType) {
    public SelectCardMessage(@NonNull final SelectType selectType) {
        this.selectType = selectType;
    }
}
