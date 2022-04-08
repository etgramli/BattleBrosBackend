package de.etgramli.battlebros.model.card.effect;

import de.etgramli.battlebros.model.card.effect.application.EffectApplication;
import org.apache.commons.lang3.StringUtils;

/**
 * Abstract Effect type to set basic attributes common to all effects like source, target, text and
 * whether the effect is activated on the card entering the game.
 */
public sealed abstract class CardEffect permits FlipEffect, InvalidateEffectEffect, PreventFlipEffect,
        ProhibitCardPlacementEffect, ReviveEffect, StrengthModifierEffect {
    private final String effectText;
    private final EffectApplication target;
    private final EffectApplication source;
    private final boolean onEntering;

    protected CardEffect(final String effectText, final EffectApplication target) {
        this(effectText, target, null, false);
    }

    protected CardEffect(final String effectText, final EffectApplication target, final boolean onEntering) {
        this(effectText, target, null, onEntering);
    }

    protected CardEffect(final String effectText, final EffectApplication target, final EffectApplication source, final boolean onEntering) {
        if (StringUtils.isBlank(effectText)) {
            throw new IllegalArgumentException("Effect text must not be blank!");
        }
        if (target == null) {
            throw new IllegalArgumentException("Effect target must not be null!");
        }
        this.effectText = effectText;
        this.target = target;
        this.onEntering = onEntering;
        this.source = source;
    }

    /**
     * Gets the user-readable description of the effect.
     * @return Non-null, not empty string.
     */
    public String getText() {
        return effectText;
    }

    public EffectApplication getTarget() {
        return target;
    }

    public EffectApplication getSource() {
        return source;
    }

    /**
     * Determine whether the effect is activated only on the card entering the game.
     * @return True if effect only activated on entering.
     */
    public boolean isOnEntering() {
        return onEntering;
    }
}
