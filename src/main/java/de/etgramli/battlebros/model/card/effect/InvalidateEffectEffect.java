package de.etgramli.battlebros.model.card.effect;

/**
 * Effect that invalidates another effect.
 */
public final class InvalidateEffectEffect extends CardEffect {
    public InvalidateEffectEffect(String effectText, EffectApplication direction) {
        super(effectText, direction);
    }
}
