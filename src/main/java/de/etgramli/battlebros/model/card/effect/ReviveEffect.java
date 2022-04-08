package de.etgramli.battlebros.model.card.effect;

import de.etgramli.battlebros.model.card.effect.application.EffectApplication;

/**
 * Effect that brings back a card from graveyard.
 */
public final class ReviveEffect extends CardEffect {
    public ReviveEffect(String effectText, EffectApplication direction) {
        super(effectText, direction);
    }
}
