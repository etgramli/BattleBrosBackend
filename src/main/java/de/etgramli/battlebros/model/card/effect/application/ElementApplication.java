package de.etgramli.battlebros.model.card.effect.application;

import de.etgramli.battlebros.model.card.CardElement;

public record ElementApplication(CardElement element) implements EffectApplication {

    public CardElement getElement() {
        return element;
    }
}
