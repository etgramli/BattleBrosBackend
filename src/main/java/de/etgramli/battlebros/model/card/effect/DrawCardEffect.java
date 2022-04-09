package de.etgramli.battlebros.model.card.effect;

import de.etgramli.battlebros.util.CollectionUtil;

import java.util.EnumSet;

public final class DrawCardEffect extends CardEffect {
    private static final EnumSet<EffectApplication> ALLOWED_VALUES = EnumSet.of(
            EffectApplication.PLAYER_I, EffectApplication.PLAYER_OTHER, EffectApplication.PLAYER_BOTH);

    public DrawCardEffect(String effectText, EffectApplication target) {
        super(effectText, target, true);
        if (!ALLOWED_VALUES.contains(target)) {
            throw new IllegalArgumentException(String.format(
                    "Target must be one of the following values: %s (but was: %s)",
                    CollectionUtil.join(", ", ALLOWED_VALUES),
                    target));
        }
    }
}
