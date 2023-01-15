package de.etgramli.battlebros.model.card;

import de.etgramli.battlebros.model.card.effect.CardEffect;
import de.etgramli.battlebros.model.card.effect.EffectType;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public final class Card {
    private final String name;
    private final int value;
    private final List<CardEffect> effects;
    private final CardElement element;

    public Card(String name, int value, List<CardEffect> effects, CardElement element) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name must not be blank!");
        }
        effects = List.copyOf(effects);
        this.name = name;
        this.value = value;
        this.effects = effects;
        this.element = element;
    }

    public boolean hasInvalidatingEffect() {
        return effects.stream().map(CardEffect::getType).anyMatch(EffectType.DISABLE_EFFECT::equals);
    }

    public String name() {
        return name;
    }

    public int value() {
        return value;
    }

    public List<CardEffect> effects() {
        return effects;
    }

    public CardElement element() {
        return element;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Card) obj;
        return Objects.equals(this.name, that.name) &&
                this.value == that.value &&
                Objects.equals(this.effects, that.effects) &&
                Objects.equals(this.element, that.element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, effects, element);
    }

    @Override
    public String toString() {
        return "Card[" +
                "name=" + name + ", " +
                "value=" + value + ", " +
                "effects=" + effects + ", " +
                "element=" + element + ']';
    }

}
