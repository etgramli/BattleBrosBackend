package de.etgramli.battlebros.model.card.effect;

/**
 * Effect that brings back a card from graveyard.
 */
public final class ReviveEffect extends CardEffect {
    public ReviveEffect(String effectText) {
        super(effectText, EffectApplication.NONE, true);
    }
}
