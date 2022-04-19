package de.etgramli.battlebros.model.card.effect;

/**
 * Condition under which a card can be placed.
 */
public final class CardPlacementRestrictionEffect extends CardEffect {

    private final Type type;

    public CardPlacementRestrictionEffect(String effectText, Type type) {
        super(effectText, EffectApplication.SELF, true);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        ROUND_1,
        ROUND_2,
        ROUND_3;
    }
}
