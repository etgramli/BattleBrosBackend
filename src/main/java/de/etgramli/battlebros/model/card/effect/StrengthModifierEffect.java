package de.etgramli.battlebros.model.card.effect;

import de.etgramli.battlebros.model.card.effect.application.EffectApplication;

public class StrengthModifierEffect extends CardEffect {
    private final int value;
    private final ModifierType type;

    public StrengthModifierEffect(final String effectText, final EffectApplication direction, final int value, final ModifierType type) {
        super(effectText, direction);
        this.value = value;
        this.type = type;
    }

    public int apply(final int value) {
        return switch (type) {
            case ADD -> value + this.value;
            case SUBTRACT -> value - this.value;
            case SET -> this.value;
        };
    }

    public enum ModifierType {
        ADD,
        SUBTRACT,
        SET
    }
}
