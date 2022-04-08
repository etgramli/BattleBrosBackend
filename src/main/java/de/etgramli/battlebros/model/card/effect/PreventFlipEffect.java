package de.etgramli.battlebros.model.card.effect;

/**
 * Effect that prevents a card from being flipped.
 */
public final class PreventFlipEffect extends CardEffect {
    public PreventFlipEffect(String effectText, EffectApplication direction) {
        super(effectText, direction);
    }
}
