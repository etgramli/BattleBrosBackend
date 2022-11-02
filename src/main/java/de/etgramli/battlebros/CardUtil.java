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
import java.util.Set;

/**
 * Card List:
 * - Abbrenngolem
 * - Anfeuerer
 * - Antiwicht
 * - Aquak
 * - Ausbrecher
 * - Baumkrone
 * - Blaetterdach
 * - Blumenstrauss
 * - Buddelwurf
 * - Daemond
 * - Drahtesel
 * - Erdwurm
 * - Erzbengel
 * - Extrablatt
 * - Fackeldackel
 * - Feiges Huhn
 * - Felsenfest
 * - Fesslerkraken
 * - Feuersalamander
 * - Flammenwerfer
 * - Flebbe und Ut
 * - Fleischwolf
 * - Fliegende Klatsche
 * - Fliegenpilz
 * - Foenix
 * - Gaertnerzwerg
 * - Geroellakaempfer
 * - Gittermastkranich
 * - Goldgolem
 * - Haihammer
 * - Heilqualle
 * - Heisser Feger
 * - Hitzkopf
 * - Holzkopf
 * - Kanonenfutterer
 * - Katerpult
 * - Kohlkopf
 * - Lavaboy
 * - Luftikuss
 * - Luftschlange
 * - Magmann
 * - Meeresfruechte
 * - Nagellachs
 * - Nebelbank
 * - Omniwicht
 * - Orakel von Delfin
 * - Potzblitz
 * - Rammbock
 * - Schaurige Wolke
 * - Schildfisch
 * - Schluckspecht
 * - Seemannsgarnele
 * - Senkschlange
 * - Streichelholz
 * - Toller Hecht
 * - U.B.O.
 * - Unterweltfaehrmann
 * - Verascher
 * - Verbieter
 * - Verduenner
 * - Verstummer
 * - Vulklon
 * - Walnuss
 * - Wasserlaeufer
 * - Welsbrocken
 * - Wirbelkind
 * - Wolkendecke
 * - Wolkenkratzer
 * - Wucherer
 * - Zombiene

 */
public final class CardUtil {
    private CardUtil(){}

    public static final Card ABBRENNGOLEM = new CardBuilder("Abbrenngolem", 5, CardElement.FEUER).build();
    public static final Card ANFEURER = new CardBuilder("Anfeurer", 2, CardElement.FEUER).build();
    public static final Card ANTIWICHT = new CardBuilder("Antiwicht", 3, CardElement.NEUTRAL).build();
    public static final Card AQUAK = new CardBuilder("Aquak", 0, CardElement.WASSER).build();
    public static final Card AUSBRECHER = new CardBuilder("Ausbrecher", 0, CardElement.FEUER)
            .withEffect(CardEffect.createFlipDownEffect("Wenn Ausbrecher ins Spiel kommt, verdecke ich einen beliebigen Bro im Spiel", EffectApplication.ANY))
            .build();
    public static final Card BAUMKRONE = new CardBuilder("Baumkrone", 3, CardElement.ERDE).build();
    public static final Card BLAETTERDACH = new CardBuilder("Blätterdach", 2, CardElement.ERDE).build();
    public static final Card BLUMENSTRAUSS = new CardBuilder("Blumenstrauß", 0, CardElement.ERDE).build();
    public static final Card BUDDELWURF = new CardBuilder("Buddelwurf", 0, CardElement.ERDE).build();
    public static final Card DAEMOND = new CardBuilder("Daemond", 4, CardElement.LUFT).build();
    public static final Card DRAHTESEL = new CardBuilder("Drahtesel", 3, CardElement.ERDE).build();
    public static final Card ERDWURM = new CardBuilder("Erdwurm", 3, CardElement.ERDE).build();
    public static final Card ERZENGEL = new CardBuilder("Erzengel", 2, CardElement.LUFT).build();
    public static final Card EXTRABLATT = new CardBuilder("Extrablatt", 2, CardElement.ERDE).build();
    public static final Card FACKELDACKEL = new CardBuilder("Fackeldackel", 4, CardElement.FEUER).build();
    public static final Card FEIGES_HUHN = new CardBuilder("Feiges Huhn", 2, CardElement.LUFT).build();
    public static final Card FELSENFEST = new CardBuilder("Felsenfest", 3, CardElement.ERDE).build();
    public static final Card FESSLERKRAKEN = new CardBuilder("Fesslerkraken", 1, CardElement.WASSER).build();
    public static final Card FEUERSALAMANDER = new CardBuilder("Feuersalamander", 3, CardElement.FEUER).build();
    public static final Card FLAMMENWERFER = new CardBuilder("Flammenwerfer", 0, CardElement.FEUER).build();
    public static final Card FLEBBE_UND_UT = new CardBuilder("Flebbe und Ut", 2, CardElement.WASSER).build();
    public static final Card FLEISCHWOLF = new CardBuilder("Fleischwolf", 3, CardElement.ERDE).build();
    public static final Card FLIEGENDE_KLATSCHE = new CardBuilder("Fliegende Klatsche", 2, CardElement.LUFT).build();
    public static final Card FLIEGENPILZ = new CardBuilder("Fliegenpilz", 2, CardElement.LUFT).build();
    public static final Card FOENIX = new CardBuilder("Fönix", 2, CardElement.FEUER).build();
    public static final Card GAERTNERZWERG = new CardBuilder("Gärtnerzwerg", 3, CardElement.ERDE).build();
    public static final Card GEROELLAKAEMPFER = new CardBuilder("Geröllakämpfer", 6, CardElement.ERDE).build();
    public static final Card GITTERMASTKRANICH = new CardBuilder("Gittermastkranich", 0, CardElement.LUFT).build();
    public static final Card GOLDGOLEM = new CardBuilder("Goldgolem", 6, CardElement.ERDE).build();
    public static final Card HAIHAMMER = new CardBuilder("Haihammer", 1, CardElement.WASSER).build();
    public static final Card HEILQUALLE = new CardBuilder("Heilqualle", 1, CardElement.WASSER).build();
    public static final Card HEISSER_FEGER = new CardBuilder("Heißer Feger", 0, CardElement.FEUER).build();
    public static final Card HITZKOPF = new CardBuilder("Hitzkopf", 3, CardElement.FEUER).build();
    public static final Card HOLZKOPF = new CardBuilder("Holzkopf", 3, CardElement.ERDE).build();
    public static final Card KANONENFUTTERER = new CardBuilder("Kanonenfutterer", 2, CardElement.FEUER).build();
    public static final Card KATERPULT = new CardBuilder("Katerpult", 1, CardElement.ERDE).build();
    public static final Card KOHLKOPF = new CardBuilder("Kohlkopf", 1, CardElement.FEUER).build();
    public static final Card LAVABOY = new CardBuilder("Lavaboy", 4, CardElement.FEUER).build();
    public static final Card LUFTIKUS = new CardBuilder("Luftikus", 0, CardElement.LUFT).build();
    public static final Card LUFTSCHLANGE = new CardBuilder("Luftschlange", 3, CardElement.LUFT).build();
    public static final Card MAGMANN = new CardBuilder("Magmann", 2, CardElement.FEUER).build();
    public static final Card MEERESFRUECHTE = new CardBuilder("Meeresfrüchte", 1, CardElement.WASSER).build();
    public static final Card NAGELLACHS = new CardBuilder("Nagellachs", 1, CardElement.WASSER).build();
    public static final Card NEBELBANK = new CardBuilder("Nebelbank", 2, CardElement.LUFT).build();
    public static final Card OMNIWICHT = new CardBuilder("Omniwicht", 3, CardElement.ALL).build();
    public static final Card ORAKELVONDELFIN = new CardBuilder("Orakel von Delfin", 2, CardElement.WASSER).build();
    public static final Card POTZBLITZ = new CardBuilder("Potzblitz", 1, CardElement.FEUER).build();
    public static final Card RAMMBOCK = new CardBuilder("Rammbock", 2, CardElement.ERDE).build();
    public static final Card SCHAURIGE_WOLKE = new CardBuilder("Schaurige Wolke", 1, CardElement.LUFT).build();
    public static final Card SCHILDFISCH = new CardBuilder("Schildfisch", 2, CardElement.WASSER).build();
    public static final Card SCHLUCKSPECHT = new CardBuilder("Schluckspecht", 0, CardElement.LUFT).build();
    public static final Card SEEMANNSGARNELE = new CardBuilder("Seemannsgarnele", 1, CardElement.WASSER).build();
    public static final Card SENKSCHLANGE = new CardBuilder("Senkschlange", 2, CardElement.WASSER).build();
    public static final Card STREICHELHOLZ = new CardBuilder("Streichelholz", 2, CardElement.FEUER).build();
    public static final Card TOLLER_HECHT = new CardBuilder("Toller Hecht", 1, CardElement.WASSER).build();
    public static final Card UBO = new CardBuilder("U.B.O.", 4, CardElement.WASSER).build();
    public static final Card UNTERWELTFAEHRMANN = new CardBuilder("Unterweltfährmann", 0, CardElement.WASSER).build();
    public static final Card VERASCHER = new CardBuilder("Verascher", 2, CardElement.FEUER).build();
    public static final Card VERBIETER = new CardBuilder("Verbieter", 2, CardElement.LUFT).build();
    public static final Card VERDUENNER = new CardBuilder("Verdünner", 2, CardElement.LUFT).build();
    public static final Card VERSTUMMER = new CardBuilder("Verstummer", 1, CardElement.LUFT).build();
    public static final Card VULKLON = new CardBuilder("Vulklon", 9, CardElement.FEUER).build();
    public static final Card WALNUSS = new CardBuilder("Walnuss", 2, CardElement.WASSER).build();
    public static final Card WASSERLAEUFER = new CardBuilder("Wasserläufer", 3, CardElement.WASSER).build();
    public static final Card WELSBROCKEN = new CardBuilder("Welsbrocken", 5, CardElement.WASSER).build();
    public static final Card WIRBELKIND = new CardBuilder("Wirbelkind", 2, CardElement.LUFT).build();
    public static final Card WOLKENDECKE = new CardBuilder("Wolkendecke", 3, CardElement.LUFT).build();
    public static final Card WOLKENKRATZER = new CardBuilder("Wolkenkratzer", 4, CardElement.LUFT).build();
    public static final Card WUCHERER = new CardBuilder("Wucherer", 5, CardElement.ERDE).build();
    public static final Card ZOMBIENE = new CardBuilder("Zombiene", 2, CardElement.ERDE).build();


    public static final Set<Card> SPECIAL_CARDS = Set.of(
            new CardBuilder("Verschwindie-Bus", 6, CardElement.LUFT)
                    .withEffect(new EffectBuilder("Wirf diesen Bro im nächsten Zug ab.", EffectType.MOVE_TO_GRAVEYARD, EffectApplication.SELF).build())
                    .build()
    );

    /**
     * Alphabetical and immutable list of all available cards.
     */
    public static final Set<Card> ALL_CARDS = Set.of(
            ABBRENNGOLEM, ANFEURER, ANTIWICHT, AQUAK, AUSBRECHER, BAUMKRONE, BLAETTERDACH, BLUMENSTRAUSS, BUDDELWURF,
            DAEMOND, DRAHTESEL, ERDWURM, ERZENGEL, EXTRABLATT, FACKELDACKEL, FEIGES_HUHN, FELSENFEST, FESSLERKRAKEN,
            FEUERSALAMANDER, FLAMMENWERFER, FLEBBE_UND_UT, FLEISCHWOLF, FLIEGENDE_KLATSCHE, FLIEGENPILZ, FOENIX,
            GAERTNERZWERG, GEROELLAKAEMPFER, GITTERMASTKRANICH, GOLDGOLEM, HAIHAMMER, HEILQUALLE, HEISSER_FEGER,
            HITZKOPF, HOLZKOPF, KANONENFUTTERER, KATERPULT, KOHLKOPF, LAVABOY, LUFTIKUS, LUFTSCHLANGE, MAGMANN,
            MEERESFRUECHTE, NAGELLACHS, NEBELBANK, OMNIWICHT, ORAKELVONDELFIN, POTZBLITZ, RAMMBOCK, SCHAURIGE_WOLKE,
            SCHILDFISCH, SCHLUCKSPECHT, SEEMANNSGARNELE, SENKSCHLANGE, STREICHELHOLZ, TOLLER_HECHT, UBO,
            UNTERWELTFAEHRMANN, VERASCHER, VERBIETER, VERDUENNER, VERSTUMMER, VULKLON, WALNUSS, WASSERLAEUFER,
            WELSBROCKEN, WIRBELKIND, WOLKENDECKE, WOLKENKRATZER, WUCHERER, ZOMBIENE
    );

    public static final Map<String, List<Card>> PRE_BUILT_DECKS = Map.of(
            "Feurio!", List.of(ANFEURER, AUSBRECHER, BAUMKRONE, BUDDELWURF, FESSLERKRAKEN, FEUERSALAMANDER, HITZKOPF, KOHLKOPF, LAVABOY, VERBIETER, VERSTUMMER, WIRBELKIND),
            "Dämond rising", List.of(AQUAK, DAEMOND, FLIEGENPILZ, HOLZKOPF, SCHAURIGE_WOLKE, SCHILDFISCH, WASSERLAEUFER, ZOMBIENE)
    );

    static {
        assert ALL_CARDS.size() == 70 : "All available cards do not match expected quantity 36 (was: %d)".formatted(ALL_CARDS.size());
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

        public CardBuilder(final String name, final int value, final CardElement element) {
            this.name = name;
            this.value = value;
            this.element = element;
        }

        public CardBuilder withEffect(final CardEffect cardEffect) {
            effects.add(cardEffect);
            return this;
        }

        public Card build() {
            return new Card(name, value, effects, element);
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
