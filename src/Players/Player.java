package Players;

import Cards.Card;

import java.util.ArrayList;

public class Player {
    private int numberOfDecks;
    private ArrayList<Deck> decks;
    private int currentMana;
    private ArrayList<Card> hand;

    public Player(){
        this.numberOfDecks = 0;
        this.decks = new ArrayList<Deck>();
        this.currentMana = 0;
        this.hand = new ArrayList<Card>();
    }

    public Player(int numberOfDecks, ArrayList<Deck> decks, int currentMana, ArrayList<Card> hand) {
        this.numberOfDecks = numberOfDecks;
        this.decks = decks;
        this.currentMana = 0;
        this.hand = hand;
    }

    public Player(Player player) {
        this.numberOfDecks = player.numberOfDecks;
        this.decks = new ArrayList<Deck>(player.decks);
        this.currentMana = player.currentMana;
        this.hand = new ArrayList<Card>(player.hand);
    }

    @Override
    public String toString() {
        return "Player{" +
                "numberOfDecks=" + numberOfDecks +
                ", decks=" + decks +
                '}';
    }

    public int getnumberOfDecks() {
        return numberOfDecks;
    }

    public void setnumberOfDecks(int numberOfDecks) {
        this.numberOfDecks = numberOfDecks;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }

    public int getNumberOfDecks() {
        return numberOfDecks;
    }

    public void setNumberOfDecks(int numberOfDecks) {
        this.numberOfDecks = numberOfDecks;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }
}
