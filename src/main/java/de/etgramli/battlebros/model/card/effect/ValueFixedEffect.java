package de.etgramli.battlebros.model.card.effect;

/**
 * The value of a card can not be changed.
 */
public final class ValueFixedEffect extends CardEffect {
    public ValueFixedEffect(String effectText, EffectApplication target) {
        super(effectText, target);
    }
}
