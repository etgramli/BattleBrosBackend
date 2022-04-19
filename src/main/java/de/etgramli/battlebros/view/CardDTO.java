package de.etgramli.battlebros.view;

import de.etgramli.battlebros.model.card.Card;
import de.etgramli.battlebros.model.card.CardElement;
import de.etgramli.battlebros.model.card.effect.CardEffect;

import java.util.stream.Collectors;

/**
 * Contains data to be transferred to the client.
 * Infos important to the client:
 * - Name
 * - value
 * - Element
 * - Effect text
 */
public record CardDTO(String name,
                      int value,
                      CardElement element,
                      String effectText) {

    public static CardDTO of(final Card card) {
        final String effectText = card.effects().isEmpty()
                ? "(Kein Effekt)"
                : card.effects().stream().map(CardEffect::getText).collect(Collectors.joining(" "));
        return new CardDTO(card.name(), card.value(), card.element(), effectText);
    }
}
