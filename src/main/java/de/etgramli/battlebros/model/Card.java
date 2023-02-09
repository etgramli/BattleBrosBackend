package de.etgramli.battlebros.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Card {
	
	//TODO:
	// > Für jede Karte ein String-Attribut mit Fähigkeits-Text hinzufügen.
	// > Für jede Karte (bei der es Sinn macht) einen kleinen FAQ-Text hinzufügen, der die Fähigkeit bzw. Regel-CornerCases erklärt.
	//   Diese Kartenspezifischen FAQs würden wahrsch. dafür sorgen, dass kein umfassendes Regeldokument nötig ist 
	//   und die Regeln wie auf der Regelkarte gedruckt ausreichen.
	// > Beispiel für FAQ-Texte:
	//   Verascher: "Mein Gegner wählt den Bro, der verdeckt wird. Dabei kann mein Gegner nur den Bro wählen, der den höchsten Wert hat. Wertveränderungen
	//               durch andere Fähigkeiten werden miteinbezogen. Bei Gleichstand muss er einen der Bros wählen, der den höchsten Wert hat. Es muss ein Bro gewählt werden."
	//   Blätterdach: "Fähigkeiten, die die benachbarten Bros verdecken oder abwerfen würden, werden immernoch ausgeführt mitsamt allen anderen Auswirkungen, nur wird
	//	               das Verdecken und Abwerfen der benachbarten Bros verhindert. Benachbarte Bros können immernoch als Ziel von Fähigkeiten gewählt werden, die sie
	//	               verdecken oder abwerfen würden. Am Ende des Kampfes werden die benachbarten Bros wie gewöhnlich zusammen mit allen anderen Bros auf dem Spielfeld auf den Abwurfstapel gelegt."

    private final int id;
    private final String name;
    private final int value;
    private final List<Element> elements;
	private final String abilityText;
	private final String abilityFaq;

    public int getValue(){
        return value;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public List<Element> getElements(){
        return List.copyOf(elements);
    }
	
	public String getAbilityText(){
		return abilityText;
	}
	
	public String getAbilityFaq(){
		return abilityFaq;
	}

    public static Card getCard(int id){
        return cardCatalogue.get(id - 1);
    }

    private Card(int id,
                 String name,
                 int value,
                 List<Element> elements,
				 String abilityText,
				 String abilityFaq){
        this.id = id;
        this.name = name;
        this.value = value;
        this.elements = new ArrayList<>(elements);
		this.abilityText = abilityText;
		this.abilityFaq = abilityFaq;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (id != card.id) return false;
        if (value != card.value) return false;
        if (!name.equals(card.name)) return false;
        if (!elements.equals(card.elements)) return false;
        if (!Objects.equals(abilityText, card.abilityText)) return false;
        return Objects.equals(abilityFaq, card.abilityFaq);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + value;
        result = 31 * result + elements.hashCode();
        result = 31 * result + (abilityText != null ? abilityText.hashCode() : 0);
        result = 31 * result + (abilityFaq != null ? abilityFaq.hashCode() : 0);
        return result;
    }

    private static final List<Card> cardCatalogue = List.of(
        new Card(1, "Feuersalamander", 3, List.of(Element.FIRE), "", ""),		//no ability, no test needed
        new Card(2, "Ausbrecher", 0, List.of(Element.FIRE), "", ""),			//ability done, test done
        new Card(3, "Flammenwerfer", 0, List.of(Element.FIRE), "", ""),			//ability done
        new Card(4, "Kanonenfutterer", 2, List.of(Element.FIRE), "", ""),		//ability done, test done
        new Card(5, "Verascher", 2, List.of(Element.FIRE), "", ""),				//ability done, test done
        new Card(6, "Fönix", 3, List.of(Element.FIRE), "", ""),
        new Card(7, "Potzblitz", 1, List.of(Element.FIRE), "", ""),				//ability done, test done
        new Card(8, "Magmann", 2, List.of(Element.FIRE), "", ""),				//ability done, test done
        new Card(9, "Lavaboy", 4, List.of(Element.FIRE), "", ""),				//ability done, test done
        new Card(10, "Fackeldackel", 4, List.of(Element.FIRE), "", ""),			//ability done
        new Card(11, "Abbrenngolem", 5, List.of(Element.FIRE), "", ""),			//ability done
        new Card(12, "Heißer Feger", 0, List.of(Element.FIRE), "", ""),			//ability done
        new Card(13, "Vulklon", 0, List.of(Element.FIRE), "", ""),				//ability done
        new Card(14, "Streichelholz", 2, List.of(Element.FIRE), "", ""),		//ability done
        new Card(15, "Anfeuerer", 2, List.of(Element.FIRE), "", ""),			//ability done
        new Card(16, "Hitzkopf", 3, List.of(Element.FIRE), "", ""),				//ability done
        new Card(17, "Kohlkopf", 1, List.of(Element.FIRE), "", ""),				//ability done
        new Card(18, "Wasserläufer", 3, List.of(Element.WATER), "", ""),		//no ability, no test needed
        new Card(19, "Haihammer", 1, List.of(Element.WATER), "", ""),			//ability done, some tests done
        new Card(20, "Senkschlange", 2, List.of(Element.WATER), "", ""),		//ability done, some tests done
        new Card(21, "Aquak", 0, List.of(Element.WATER), "", ""),				//ability done
        new Card(22, "Seemannsgarnele", 1, List.of(Element.WATER), "", ""),		//ability done
        new Card(23, "Nagellachs", 1, List.of(Element.WATER), "", ""),			//ability done
        new Card(24, "Orakel von Delfin", 2, List.of(Element.WATER), "", ""),	//	TODO 1:zeige nur dem actor die oberste karte(evtl einfach ziehen), 2:wähle oben oder unten
        new Card(25, "Unterweltfährmann", 0, List.of(Element.WATER), "", ""),	//	TODO 1:wähle karte in abwurf, 2:falls nötig wähle leere position
        new Card(26, "U.B.O.", 4, List.of(Element.WATER), "", ""),				//ability done
        new Card(27, "Welsbrocken", 5, List.of(Element.WATER), "", ""),			//ability done
        new Card(28, "Fesslerkraken", 1, List.of(Element.WATER), "", ""),		//ability done
        new Card(29, "Flebbe und Ut", 2, List.of(Element.WATER), "", ""),		//	TODO wähle links oder rechts
        new Card(30, "Meeresfrüchte", 1, List.of(Element.WATER), "", ""),		//ability done
        new Card(31, "Heilqualle", 1, List.of(Element.WATER), "", ""),			//ability done
        new Card(32, "Toller Hecht", 1, List.of(Element.WATER), "", ""),		//ability done
        new Card(33, "Walnuss", 2, List.of(Element.WATER), "", ""),				//ability done
        new Card(34, "Schildfisch", 2, List.of(Element.WATER), "", ""),			//ability done
        new Card(35, "Erdwurm", 3, List.of(Element.EARTH), "", ""),				//no ability, no test needed
        new Card(36, "Katerpult", 1, List.of(Element.EARTH), "", ""),			//ability done
        new Card(37, "Rammbock", 2, List.of(Element.EARTH), "", ""),			//ability done
        new Card(38, "Zombiene", 3, List.of(Element.EARTH), "", ""),
        new Card(39, "Fleischwolf", 3, List.of(Element.EARTH), "", ""),			//ability done
        new Card(40, "Wucherer", 5, List.of(Element.EARTH), "", ""),			//ability done
        new Card(41, "Geröllakämpfer", 6, List.of(Element.EARTH), "", ""),		//ability done
        new Card(42, "Goldgolem", 5, List.of(Element.EARTH), "", ""),			//ability done
        new Card(43, "Blumenstrauß", 0, List.of(Element.EARTH), "", ""),		//ability done
        new Card(44, "Buddelwurf", 2, List.of(Element.EARTH), "", ""),
        new Card(45, "Extrablatt", 3, List.of(Element.EARTH), "", ""),
        new Card(46, "Felsenfest", 3, List.of(Element.EARTH), "", ""),			//ability done
        new Card(47, "Holzkopf", 3, List.of(Element.EARTH), "", ""),			//ability done, some tests done
        new Card(48, "Gärtnerzwerg", 3, List.of(Element.EARTH), "", ""),		//	TODO 1:zeige beiden spielern die karte, 2:wähle evtl links oder rechts
        new Card(49, "Drahtesel", 3, List.of(Element.EARTH), "", ""),			//ability done
        new Card(50, "Blätterdach", 2, List.of(Element.EARTH), "", ""),
        new Card(51, "Baumkrone", 3, List.of(Element.EARTH), "", ""),
        new Card(52, "Luftschlange", 3, List.of(Element.AIR), "", ""),			//no ability, no test needed
        new Card(53, "Gittermastkranich", 0, List.of(Element.AIR), "", ""),		//ability done
        new Card(54, "Fliegende Klatsche", 2, List.of(Element.AIR), "", ""),	//ability done
        new Card(55, "Verbieter", 2, List.of(Element.AIR), "", ""),				//ability done
        new Card(56, "Verstummer", 2, List.of(Element.AIR), "", ""),			//ability TODO (not working)
		//hier fehlt verdünner
        new Card(58, "Fliegenpilz", 2, List.of(Element.AIR), "", ""),			//ability done
        new Card(59, "Erzbengel", 3, List.of(Element.AIR), "", ""),
        new Card(60, "Schaurige Wolke", 2, List.of(Element.AIR), "", ""),
        new Card(61, "Dämond", 4, List.of(Element.AIR), "", ""),				//ability done
        new Card(62, "Wolkenkratzer", 4, List.of(Element.AIR), "", ""),			//ability done
        new Card(63, "Schluckspecht", 0, List.of(Element.AIR), "", ""),			//ability done
        new Card(64, "Luftikuss", 0, List.of(Element.AIR), "", ""),				//ability done
        new Card(65, "Wirbelkind", 2, List.of(Element.AIR), "", ""),			//ability done
        new Card(66, "Feiges Huhn", 2, List.of(Element.AIR), "", ""),			//ability done
        new Card(67, "Nebelbank", 2, List.of(Element.AIR), "", ""),
        new Card(68, "Wolkendecke", 3, List.of(Element.AIR), "", ""),
        new Card(69, "Anitwicht", 3, List.of(), "", ""),						//no ability, no test needed
        new Card(70, "Omniwicht", 3, List.of(Element.AIR, Element.WATER, Element.EARTH, Element.FIRE), "", "")	//no ability, no test needed
    );
}
