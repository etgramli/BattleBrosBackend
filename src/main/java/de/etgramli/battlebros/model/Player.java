package de.etgramli.battlebros.model;

public class Player {

    private String name;

    private Deck deck;
    private GameZone gameZoneDeck = new GameZone(false, false);
    private GameZone gameZoneLife = new GameZone(false, false);
    private GameZone gameZoneHand = new GameZone(true, false);
    private GameZone gameZoneDiscard = new GameZone(true, true);

    private GameField field = new GameField();

    public Player(String name, Deck deck){
        this.name = name;
        this.deck = deck;
    }

    public void setUpGame(){
        gameZoneDeck.addCards(deck.getCards());
        addLifeCards(3);
        drawCards(6);
    }

    public int drawCards(int amount){ //returns the amount of cards actually drawn
        int cardsDrawn = 0;
        for (int i=0; i<= amount; i++) {
            if (drawACard())
                cardsDrawn++;
            else
                break;
        }
        return cardsDrawn;
    }

    public boolean drawACard(){
        if (gameZoneDeck.getAmountOfCards() <= 0)
            return false;
        gameZoneHand.addCard(gameZoneDeck.removeCard(0));
        return true;
    }

    public int addLifeCards(int amount){ //returns the amount of cards actually added
        int cardsAdded = 0;
        for (int i=0; i<= amount; i++) {
            if (addALifeCard())
                cardsAdded++;
            else
                break;
        }
        return cardsAdded;
    }

    public boolean addALifeCard(){
        if (gameZoneDeck.getAmountOfCards() <= 0)
            return false;
        gameZoneLife.addCard(gameZoneDeck.removeCard(0));
        return true;
    }

    public int getTotalValue(){
        int total = 0;
        for(Card card : field.getAllCards()){
            total += card.getValue();
        }
        return total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
