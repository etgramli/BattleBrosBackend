package de.etgramli.battlebros.view.dto;

import de.etgramli.battlebros.model.Card;
import org.springframework.lang.NonNull;

/**
 * Contains data to be transferred to the client.
 * Info important to the client:
 * @param id: Determines the card image
 * @param isFaceUp: True if the card should be showed face-up
 */
public record CardDTO(int id, boolean isFaceUp) {

    @NonNull
    public static CardDTO from(@NonNull final Card card) {
        return new CardDTO(card.getId(), true);
    }

    @NonNull
    public static CardDTO from(@NonNull final Card card, final boolean isFaceUp) {
        return new CardDTO(card.getId(), isFaceUp);
    }
}
