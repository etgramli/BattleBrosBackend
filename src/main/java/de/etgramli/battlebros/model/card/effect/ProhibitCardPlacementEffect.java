package de.etgramli.battlebros.model.card.effect;

import de.etgramli.battlebros.model.card.effect.application.EffectApplication;

/**
 * Effect that prohibits a card from being placed at a certain position.
 */
public final class ProhibitCardPlacementEffect extends CardEffect {
    public ProhibitCardPlacementEffect(String effectText, EffectApplication direction) {
        super(effectText, direction);
    }
}
