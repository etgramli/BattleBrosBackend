package de.etgramli.battlebros.model.card.effect;

import de.etgramli.battlebros.model.card.effect.application.EffectApplication;

public final class InvalidateEffectEffect extends CardEffect {
    public InvalidateEffectEffect(String effectText, EffectApplication direction) {
        super(effectText, direction);
    }
}
