package de.etgramli.battlebros.model.card.effect;

import de.etgramli.battlebros.model.card.effect.application.EffectApplication;

/**
 * Effect that prevents a card from being flipped.
 */
public final class PreventFlipEffect extends CardEffect {
    public PreventFlipEffect(String effectText, EffectApplication direction) {
        super(effectText, direction);
    }
}
