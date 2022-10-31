package de.etgramli.battlebros.model.card.effect;

import org.springframework.lang.NonNull;

public final class ValueModifyEffect extends CardEffect {

    private final int value;

    public ValueModifyEffect(@NonNull final String effectText, @NonNull final EffectApplication target, final int value) {
        super(effectText, EffectType.MODIFY_VALUE, target);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int apply(final int value) {
        return value + this.value;
    }
}
