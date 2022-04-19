package de.etgramli.battlebros;

import de.etgramli.battlebros.model.card.Card;
import de.etgramli.battlebros.model.card.CardElement;
import de.etgramli.battlebros.model.card.effect.CardEffect;
import de.etgramli.battlebros.model.card.effect.DrawCardEffect;
import de.etgramli.battlebros.model.card.effect.EffectApplication;
import de.etgramli.battlebros.model.card.effect.FlipEffect;
import de.etgramli.battlebros.model.card.effect.InvalidateEffectEffect;
import de.etgramli.battlebros.model.card.effect.ProhibitCardPlacementEffect;
import de.etgramli.battlebros.model.card.effect.ReviveEffect;
import de.etgramli.battlebros.model.card.effect.ValueFixedEffect;
import de.etgramli.battlebros.model.card.effect.ValueModifierEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CardUtil {
    private CardUtil(){}

    public static final Card ANFEURER = new CardBuilder("Anfeurer", 3, CardElement.FEUER)
            .withEffect(new ValueModifierEffect("Benachbarte Bros haben +1.", EffectApplication.ELEMENT_FEUER, 1, ValueModifierEffect.ModifierType.ADD))
            .build();
    public static final Card AQUAK = new CardBuilder("Aquak", 2, CardElement.WASSER)
            .withEffect(new DrawCardEffect("Wenn Aquak ins Spiel kommt, ziehe ich eine Karte von meinem Deck.", EffectApplication.PLAYER_I))
            .build();
    public static final Card AUSBRECHER = new CardBuilder("Ausbrecher", 0, CardElement.FEUER)
            .withEffect(new FlipEffect("Wenn Ausbrecher ins Spiel kommt, verdecke ich einen beliebigen Bro im Spiel", EffectApplication.ANY, true, false))
            .build();
    public static final Card BAUMKRONE = new CardBuilder("Baumkrone", 4, CardElement.ERDE).build();
    public static final Card DAEMOND = new CardBuilder("Dämond", 8, CardElement.LUFT)
            .withEffect(new FlipEffect("Wenn Dämond ins Spiel kommt, verdeckt er sich selbst.", EffectApplication.SELF, true, false))
            .build();
    public static final Card FEDERBALL = new CardBuilder("Federball", 4, CardElement.LUFT).build();
    public static final Card FELSENFEST = new CardBuilder("Felsenfest", 4, CardElement.ERDE)
            .withEffect(new ValueFixedEffect("Der Wert von Felsenfest kann nicht verändert werden.", EffectApplication.SELF))
            .build();
    public static final Card FEUERSALAMANDER = new CardBuilder("Feuer-Salamander", 4, CardElement.FEUER).build();
    public static final Card FLIEGENPILZ = new CardBuilder("Fliegenpilz", 3, CardElement.LUFT)
            .withEffect(new ProhibitCardPlacementEffect("Es können keine Bros gegenüber vom Fliegenpilz gespielt werden, es sei denn keine anderen Bros sind im Spiel.", EffectApplication.FACING))
            .build();
    public static final Card FOENX = new CardBuilder("Fön-X", 1, CardElement.FEUER)
            .withEffect(new InvalidateEffectEffect("Der Effekt des gegenüber liegenden Bros werden annuliert.", EffectApplication.FACING))
            .withEffect(new InvalidateEffectEffect("Der Effekt der digonal gegenüber liegenden Bros werden annuliert.", EffectApplication.DIAGONAL))
            .build();
    public static final Card GIGAGOLEM = new CardBuilder("Gigagolem", 8, CardElement.ERDE)
            .withEffect(new ValueModifierEffect("Benachbarte Bros haben -1", EffectApplication.NEIGHBOR, 2, ValueModifierEffect.ModifierType.SUBTRACT))
            .build();
    public static final Card HITZKOPF = new CardBuilder("Hitzkopf", 3, CardElement.FEUER)
            .withEffect(new ValueModifierEffect("Alle Feuer-Bros außer Hitzkopf haben +1.", EffectApplication.ELEMENT_FEUER, 1, ValueModifierEffect.ModifierType.ADD))
            .build();
    public static final Card HOLZKOPF = new CardBuilder("Holzkopf", 5, CardElement.ERDE)
            .withEffect(new InvalidateEffectEffect("Annuliere die Fähigkeien von benachbarten Bros.", EffectApplication.NEIGHBOR))
            .build();
    public static final Card LAVABOY = new CardBuilder("Lava-Boy", 5, CardElement.FEUER)
            .withEffect(new DrawCardEffect("Wenn Lava-Boy ins Spiel kommt, zieht mein Gegner eine Karte von seinem/ihrem Deck.", EffectApplication.PLAYER_OTHER))
            .build();
    public static final Card ORAKELVONDELFISCH = new CardBuilder("Orakel von Delfisch", 4, CardElement.WASSER)
            .withEffect(new DrawCardEffect("Wenn Orakel von Delfisch ins Spiel kommt, ziehen beide Spieler eine Karte.", EffectApplication.PLAYER_BOTH))
            .build();
    public static final Card SENKSCHLANGE = new CardBuilder("Senk-Schlange", 1, CardElement.WASSER)
            .withEffect(new FlipEffect("Wenn Senk-Schlange ins Spiel kommt, verdecke den gegenüberligenden Bro.", EffectApplication.FACING, true, false))
            .build();
    public static final Card SPIEGELWICHT = new CardBuilder("Spiegelwicht", 0, CardElement.WASSER)
            .withEffect(new ValueModifierEffect("Spiegelwicht hat den aufgedruckten Wert des gegeüberliegenden Bros.", EffectApplication.FACING, 0, ValueModifierEffect.ModifierType.SET))
            .build();
    public static final Card VERSTUMMER = new CardBuilder("Verstummer", 2, CardElement.LUFT)
            .withEffect(new InvalidateEffectEffect("Annuliere die Fähigkeit des gegenüberliegenden Bros.", EffectApplication.FACING))
            .build();
    public static final Card WASSERLAEUFER = new CardBuilder("Wasserläufer", 4, CardElement.WASSER).build();
    public static final Card ZOMBIENE = new CardBuilder("Zombiene", 1, CardElement.ERDE)
            .withEffect(new ReviveEffect("Wenn Zombiene ins Spiel kommt, lege ich einen beliebigen Bro vom Abwurfstapel neben sie."))
            .withEffect(new ValueModifierEffect("Benachbarte Bros haben -1.", EffectApplication.NEIGHBOR, 1, ValueModifierEffect.ModifierType.SUBTRACT))
            .build();
    public static final Card ZWITTERAAL = new CardBuilder("Zwitteraal", 6, CardElement.WASSER)
            .withEffect(new ProhibitCardPlacementEffect("Es können keine Bros benachbart neben Zwitteraal gespielt werden.", EffectApplication.NEIGHBOR))
            .build();


    public static final Map<String, List<Card>> PRE_BUILT_DECKS = Map.of(
            "Feurio!", List.of(ANFEURER, AUSBRECHER, FEUERSALAMANDER, FOENX, HITZKOPF, LAVABOY, VERSTUMMER),
            "Dämond rising", List.of(BAUMKRONE, DAEMOND, FELSENFEST, FLIEGENPILZ, GIGAGOLEM, HOLZKOPF, ZOMBIENE),
            "Platscher!", List.of(AQUAK, ORAKELVONDELFISCH, SENKSCHLANGE, SPIEGELWICHT, WASSERLAEUFER, ZWITTERAAL)
    );

    public static final List<Card> FEURIO = PRE_BUILT_DECKS.get("Feurio!");
    public static final List<Card> DAEMOND_RISING = PRE_BUILT_DECKS.get("Dämond rising");

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

        public CardBuilder withImagePath(final String path) {
            this.imagePath = path;
            return this;
        }

        public Card build() {
            return new Card(name, value, effects, element, imagePath);
        }
    }
}
