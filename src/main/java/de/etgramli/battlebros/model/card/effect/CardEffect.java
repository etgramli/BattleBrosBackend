package de.etgramli.battlebros.model.card.effect;

import de.etgramli.battlebros.model.card.effect.application.EffectApplication;

public abstract class CardEffect {
    private final String effectText;
    private final EffectApplication direction;
    private final boolean onEntering;

    protected CardEffect(final String effectText, final EffectApplication direction) {
        this(effectText, direction, false);
    }

    protected CardEffect(final String effectText, final EffectApplication direction, final boolean onEntering) {
        this.effectText = effectText;
        this.direction = direction;
        this.onEntering = onEntering;
    }

    public String getText() {
        return effectText;
    }

    public EffectApplication getDirection() {
        return direction;
    }

    public boolean isOnEntering() {
        return onEntering;
    }
}
