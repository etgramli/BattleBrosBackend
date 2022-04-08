package de.etgramli.battlebros.model.card.effect;

/**
 * An effect that modifies or sets the value of a card.
 */
public final class StrengthModifierEffect extends CardEffect {
    private final int value;
    private final ModifierType type;

    public StrengthModifierEffect(final String effectText, final EffectApplication direction, final int value, final ModifierType type) {
        super(effectText, direction);
        if (value < 0) {
            throw new IllegalArgumentException("Value must be 0 or larger!");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type must not be null!");
        }
        this.value = value;
        this.type = type;
    }

    public int apply(final int value) {
        return switch (type) {
            case ADD -> value + this.value;
            case SUBTRACT -> value - this.value;
            case SET -> value;
        };
    }

    public enum ModifierType {
        ADD,
        SUBTRACT,
        SET
    }
}
