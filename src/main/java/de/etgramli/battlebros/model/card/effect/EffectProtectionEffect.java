package de.etgramli.battlebros.model.card.effect;

/**
 * Protect an effect of being invalidated.
 */
public final class EffectProtectionEffect extends CardEffect {
    public EffectProtectionEffect(String effectText, EffectApplication target) {
        super(effectText, target);
    }
}
