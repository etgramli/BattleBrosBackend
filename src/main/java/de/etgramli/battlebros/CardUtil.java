package de.etgramli.battlebros;

import de.etgramli.battlebros.model.card.Card;
import de.etgramli.battlebros.model.card.CardElement;
import de.etgramli.battlebros.model.card.effect.CardEffect;
import de.etgramli.battlebros.model.card.effect.InvalidateEffectEffect;
import de.etgramli.battlebros.model.card.effect.StrengthModifierEffect;
import de.etgramli.battlebros.model.card.effect.application.DiagonalApplication;
import de.etgramli.battlebros.model.card.effect.application.ElementApplication;
import de.etgramli.battlebros.model.card.effect.application.FacingApplication;
import de.etgramli.battlebros.model.card.effect.application.NeighborApplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CardUtil {
    private CardUtil(){}

    public static final Card ANFEURER = new CardBuilder("Anfeurer", 3, CardElement.FEUER)
            .withEffect(new StrengthModifierEffect("Benachbarte Bros haben +1.", new ElementApplication(CardElement.FEUER), 1, StrengthModifierEffect.ModifierType.ADD))
            .withImagePath("/img/Anfeurer.jpg").build();
    public static final Card FEDERBALL = new CardBuilder("Federball", 4, CardElement.LUFT)
            .withImagePath("/img/Federball.jpg").build();
    public static final Card FOENX = new CardBuilder("Fön-X", 1, CardElement.FEUER)
            .withEffect(new InvalidateEffectEffect("Der Effekt des gegenüber liegenden Bros werden annuliert.", new FacingApplication()))
            .withEffect(new InvalidateEffectEffect("Der Effekt der digonal gegenüber liegenden Bros werden annuliert.", new DiagonalApplication()))
            .build();
    public static final Card GIGAGOLEM = new CardBuilder("Gigagolem", 8, CardElement.ERDE)
            .withEffect(new StrengthModifierEffect("Benachbarte Bros haben -1", new NeighborApplication(), 2, StrengthModifierEffect.ModifierType.SUBTRACT))
            .build();
    public static final Card HOLZKOPF = new CardBuilder("Holzkopf", 5, CardElement.ERDE)
            .withEffect(new InvalidateEffectEffect("Annuliere die Fähigkeien von benachbarten Bros.", new NeighborApplication()))
            .build();
    public static final Card VERSTUMMER = new CardBuilder("Verstummer", 2, CardElement.LUFT)
            .withEffect(new InvalidateEffectEffect("Annuliere die Fähigkeit des gegenüberliegenden Bros.", new FacingApplication()))
            .build();
    public static final Card WASSERLAEUFER = new CardBuilder("Wasserläufer", 4, CardElement.WASSER)
            .withImagePath("/img/Wasserlaeufer.jpg").build();
    /*
    public static final Card WIRBELKIND = new CardBuilder("Wirbelkind", 1, CardElement.LUFT)
            .withEffect(new StrengthModifierEffect("Alle Bros mit demselben Element wie der gegenüberligende Bro haben -1.", new )).build();
    */
    public static final List<Card> FEUER_DECK = List.of(ANFEURER);

    public static class CardBuilder {
        private final String name;
        private final int value;
        private final CardElement element;
        private final List<CardEffect> effects = new ArrayList<>();
        private String imagePath;

        public CardBuilder(final String name, final int value, final CardElement element) {
            this.name = name;
            this.value = value;
            this.element = element;
        }

        public CardBuilder withEffect(final CardEffect cardEffect) {
            effects.add(cardEffect);
            return this;
        }

        public CardBuilder withEffects(final Collection<CardEffect> effects) {
            this.effects.addAll(effects);
            return this;
        }

        public CardBuilder withImagePath(final String path) {
            this.imagePath = path;
            return this;
        }

        public Card build() {
            return new Card(name, value, effects, element, imagePath);
        }
    }
}
