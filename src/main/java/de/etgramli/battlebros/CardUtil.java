package de.etgramli.battlebros;

import de.etgramli.battlebros.model.card.Card;
import de.etgramli.battlebros.model.card.CardElement;
import de.etgramli.battlebros.model.card.effect.CardEffect;
import de.etgramli.battlebros.model.card.effect.EffectApplication;
import de.etgramli.battlebros.model.card.effect.EffectType;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CardUtil {
    private CardUtil(){}

    public static final Card ANFEURER = new CardBuilder("Anfeurer", 3, CardElement.FEUER)
            .build();
    public static final Card AQUAK = new CardBuilder("Aquak", 2, CardElement.WASSER)
            .build();
    public static final Card AUSBRECHER = new CardBuilder("Ausbrecher", 0, CardElement.FEUER)
            .withEffect(CardEffect.createFlipDownEffect("Wenn Ausbrecher ins Spiel kommt, verdecke ich einen beliebigen Bro im Spiel", EffectApplication.ANY))
            .build();
    public static final Card BAUMKRONE = new CardBuilder("Baumkrone", 4, CardElement.ERDE).build();
    public static final Card BLUMENGROUP = new CardBuilder("Blu Men Group", 5, CardElement.ERDE)
            // ToDo: effect
            .build();
    public static final Card BUDDELWURF = new CardBuilder("Buddelwurf", 3, CardElement.ERDE)
            // ToDo: effect
            .build();
    public static final Card DAEMOND = new CardBuilder("Daemond", 8, CardElement.LUFT)
            .withEffect(CardEffect.createFlipDownEffect("Wenn Dämond ins Spiel kommt, verdeckt er sich selbst.", EffectApplication.SELF))
            .build();
    public static final Card DRAHTESEL = new CardBuilder("Drahtesel", 3, CardElement.ERDE)
            // ToDo: effect
            .build();
    public static final Card FEDERBALL = new CardBuilder("Federball", 4, CardElement.LUFT).build();
    public static final Card FELSENFEST = new CardBuilder("Felsenfest", 4, CardElement.ERDE)
            .build();
    public static final Card FESSLERKRAKEN = new CardBuilder("Fessler-Kraken", 0, CardElement.WASSER)
            //.withEffect() // ToDo: effect
            .build();
    public static final Card FEUERSALAMANDER = new CardBuilder("Feuer-Salamander", 4, CardElement.FEUER).build();
    public static final Card FLIEGENPILZ = new CardBuilder("Fliegenpilz", 3, CardElement.LUFT)
            .build();
    public static final Card FOENX = new CardBuilder("Fön-X", 1, CardElement.FEUER)
            .build();
    public static final Card GIGAGOLEM = new CardBuilder("Gigagolem", 8, CardElement.ERDE)
            .build();
    public static final Card GITTERMASTKRANICH = new CardBuilder("Gittermastkranich", 3, CardElement.LUFT)
            // ToDo: effect
            .build();
    public static final Card HITZKOPF = new CardBuilder("Hitzkopf", 3, CardElement.FEUER)
            .build();
    public static final Card HOLZKOPF = new CardBuilder("Holzkopf", 5, CardElement.ERDE)
            .build();
    public static final Card KANONENFUTTERER = new CardBuilder("Kanonenfutterer", 3, CardElement.FEUER)
            // ToDo: effect
            .build();
    public static final Card KLOBRILLE = new CardBuilder("Klobrille", 2, CardElement.WASSER)
            // ToDo: effect
            .build();
    public static final Card KOHLKOPF = new CardBuilder("Kohlkopf", 6, CardElement.FEUER)
            // ToDo: effect
            .build();
    public static final Card LAVABOY = new CardBuilder("Lava-Boy", 5, CardElement.FEUER)
            .build();
    public static final Card OHBOEE = new CardBuilder("Oh, Böe!", 3, CardElement.LUFT)
            // ToDo: Effect
            .build();
    public static final Card ORAKELVONDELFISCH = new CardBuilder("Orakel von Delfisch", 4, CardElement.WASSER)
            .build();
    public static final Card SCHAURIGE_WOLKE = new CardBuilder("Schaurige Wolke", 6, CardElement.LUFT)
            // todo: effect
            .build();
    public static final Card SCHILDFISCH = new CardBuilder("Schildfisch", 3, CardElement.WASSER)
            // ToDo: effect
            .build();
    public static final Card SENKSCHLANGE = new CardBuilder("Senk-Schlange", 1, CardElement.WASSER)
            .build();
    public static final Card SPIEGELWICHT = new CardBuilder("Spiegelwicht", 0, CardElement.WASSER)
            .build();
    public static final Card STREICHELHOLZ = new CardBuilder("Streichelholz", 3, CardElement.FEUER)
            // todo: effect
            .build();
    public static final Card TAUDEGEN = new CardBuilder("Taudegen", 3, CardElement.ERDE)
            // todo: effect
            .build();
    public static final Card VERBIETER = new CardBuilder("Verbieter", 1, CardElement.LUFT)
            // ToDo: effect
            .build();
    public static final Card VERSTUMMER = new CardBuilder("Verstummer", 2, CardElement.LUFT)
            .build();
    public static final Card WASSERLAEUFER = new CardBuilder("Wasserläufer", 4, CardElement.WASSER).build();
    public static final Card WIRBELKIND = new CardBuilder("Wirbelkind", 1, CardElement.LUFT)
            // ToDo: effect
            .build();
    public static final Card ZOMBIENE = new CardBuilder("Zombiene", 1, CardElement.ERDE)
            // ToDo Effect
            .build();
    public static final Card ZWITTERAAL = new CardBuilder("Zwitteraal", 6, CardElement.WASSER)
            .build();

    /**
     * Alphabetical and immutable list of all available cards.
     */
    public static final List<Card> ALL_CARDS = List.of(
            ANFEURER, AQUAK, AUSBRECHER, BAUMKRONE, BLUMENGROUP, BUDDELWURF, DAEMOND, DRAHTESEL, FEDERBALL, FELSENFEST,
            FESSLERKRAKEN, FEUERSALAMANDER, FLIEGENPILZ, FOENX, GIGAGOLEM, GITTERMASTKRANICH, HITZKOPF, HOLZKOPF,
            KANONENFUTTERER, KLOBRILLE, KOHLKOPF, LAVABOY, OHBOEE, ORAKELVONDELFISCH, SCHAURIGE_WOLKE, SCHILDFISCH,
            SENKSCHLANGE, SPIEGELWICHT, STREICHELHOLZ, TAUDEGEN, VERBIETER, VERSTUMMER, WASSERLAEUFER, WIRBELKIND,
            ZOMBIENE, ZWITTERAAL
    );

    public static final Map<String, List<Card>> PRE_BUILT_DECKS = Map.of(
            "Feurio!", List.of(ANFEURER, AUSBRECHER, BAUMKRONE, BUDDELWURF, FESSLERKRAKEN, FEUERSALAMANDER, HITZKOPF, KOHLKOPF, LAVABOY, VERBIETER, VERSTUMMER, WIRBELKIND),
            "Dämond rising", List.of(AQUAK, DAEMOND, FEDERBALL, FLIEGENPILZ, GIGAGOLEM, HOLZKOPF, SCHAURIGE_WOLKE, SCHILDFISCH, SPIEGELWICHT, TAUDEGEN, WASSERLAEUFER, ZOMBIENE)
    );
    static {
        assert ALL_CARDS.size() == 36 : "All available cards do not match expected quantity 36 (was: %d)".formatted(ALL_CARDS.size());
        for (Map.Entry<String, List<Card>> deck : PRE_BUILT_DECKS.entrySet()) {
            assert deck.getValue().size() == 12 : "Pre-built deck %s does not have 12 cards (was: %d)"
                    .formatted(deck.getKey(), deck.getValue().size());
        }
    }

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

    public static class EffectBuilder {
        private final String text;
        private final EffectType type;
        private boolean onEntering;
        private final EffectApplication target;
        private EffectApplication source;

        public EffectBuilder(@NonNull final String text,
                             @NonNull final EffectType type,
                             @NonNull final EffectApplication target) {
            this.text = text;
            this.type = type;
            onEntering = false;
            this.target = target;
            this.source = null;
        }

        public EffectBuilder onEntering() {
            onEntering = true;
            return this;
        }

        public EffectBuilder notOnEntering() {
            onEntering = false;
            return this;
        }

        public EffectBuilder withSource(@NonNull final EffectApplication source) {
            this.source = source;
            return this;
        }

        public CardEffect build() {
            return new CardEffect(text, type, target, source, onEntering);
        }
    }
}
