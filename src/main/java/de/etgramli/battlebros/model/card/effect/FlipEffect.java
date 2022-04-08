package de.etgramli.battlebros.model.card.effect;

/**
 * Effect that flips a card.
 */
public final class FlipEffect extends CardEffect {
    public FlipEffect(String effectText, EffectApplication direction) {
        super(effectText, direction);
    }

    public FlipEffect(String effectText, EffectApplication direction, boolean onEnter) {
        super(effectText, direction, onEnter);
    }
}
