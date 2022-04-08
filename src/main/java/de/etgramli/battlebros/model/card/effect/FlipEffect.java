package de.etgramli.battlebros.model.card.effect;

import de.etgramli.battlebros.model.card.effect.application.EffectApplication;

/**
 * Effect that flips a card.
 */
public final class FlipEffect extends CardEffect {
    public FlipEffect(String effectText, EffectApplication direction) {
        super(effectText, direction);
    }
}
