package de.etgramli.battlebros.view;

import de.etgramli.battlebros.model.Card;
import org.springframework.lang.NonNull;

/**
 * Contains data to be transferred to the client.
 * Info important to the client:
 * - Id: to determine the card image
 */
public record CardDTO(int id) {

    @NonNull
    public static CardDTO of(@NonNull final Card card) {
        return new CardDTO(card.getId());
    }

    public static CardDTO getFaceDown() {
        return CARD_FACE_DOWN;
    }

    private static final CardDTO CARD_FACE_DOWN = new CardDTO(0);
}
