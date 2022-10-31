package de.etgramli.battlebros.model.card.effect;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

/**
 * Abstract Effect type to set basic attributes common to all effects like source, target, text and
 * whether the effect is activated on the card entering the game.
 */
public sealed class CardEffect permits ValueModifyEffect {
    private final String effectText;
    private final EffectType type;
    private final EffectApplication target;
    private final EffectApplication source;
    private final boolean onEntering;

    public static final CardEffect EMPTY = new CardEffect("(Keine Fähigkeit)", null, EffectApplication.NONE, EffectApplication.NONE, false);

    public static CardEffect createFlipDownEffect(@NonNull final String text, @NonNull final EffectApplication target) {
        return new CardEffect(text, EffectType.FLIP_FACE_DOWN, target);
    }

    public CardEffect(final String effectText, final EffectType type, final EffectApplication target) {
        this(effectText, type, target, null, false);
    }

    public CardEffect(final String effectText, final EffectType type, final EffectApplication target, final boolean onEntering) {
        this(effectText, type, target, null, onEntering);
    }

    public CardEffect(final String effectText, final EffectType type, final EffectApplication target, final EffectApplication source, final boolean onEntering) {
        if (StringUtils.isBlank(effectText)) {
            throw new IllegalArgumentException("Effect text must not be blank!");
        }
        if (target == null) {
            throw new IllegalArgumentException("Effect target must not be null!");
        }
        this.effectText = effectText;
        this.type = type;
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

    public EffectType getType() {
        return type;
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
