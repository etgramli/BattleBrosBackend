package de.etgramli.battlebros.model.card;

import de.etgramli.battlebros.model.card.effect.CardEffect;
import de.etgramli.battlebros.model.card.effect.EffectType;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public record Card(String name,
                   int value,
                   List<CardEffect> effects,
                   CardElement element) {
    public Card {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name must not be blank!");
        }
        effects = List.copyOf(effects);
    }

    public boolean hasInvalidatingEffect() {
        return effects.stream().map(CardEffect::getType).anyMatch(EffectType.DISABLE_EFFECT::equals);
    }
}
