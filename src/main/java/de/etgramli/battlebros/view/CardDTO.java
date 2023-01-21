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

    @NonNull
    public static CardDTO of(final int cardId) {
        return CardDTO.of(Card.getCard(cardId));
    }
}
