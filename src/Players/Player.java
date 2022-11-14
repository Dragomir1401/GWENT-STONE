package Players;

import Cards.*;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int numberOfDecks;
    private ArrayList<Deck> decks;
    private int currentMana;
    private ArrayList<Card> hand;
    private int numberOfWins;

    public Player(){
        this.numberOfDecks = 0;
        this.decks = new ArrayList<Deck>();
        this.currentMana = 0;
        this.hand = new ArrayList<Card>();
        this.numberOfWins = 0;
    }

    public Player(int numberOfDecks, ArrayList<Deck> decks, int currentMana, ArrayList<Card> hand) {
        this.numberOfDecks = numberOfDecks;
        this.decks = decks;
        this.currentMana = currentMana;
        this.hand = hand;
        this.numberOfWins = 0;
    }

    public Player(Player player) {
        this.numberOfDecks = player.getNumberOfDecks();
        this.decks = new ArrayList<Deck>();
        for(Deck deck : player.getDecks()) {
            Deck newDeck = new Deck();
            for (Card card : deck.getCards()) {
                Card newCard = new Card();
                switch (card.getName()) {
                    case "Sentinel" -> newCard = new Sentinel(card);
                    case "Berserker" -> newCard = new Berserker(card);
                    case "Goliath" -> newCard = new Goliath(card);
                    case "Warden" -> newCard = new Warden(card);
                    case "Miraj" -> newCard = new Miraj(card);
                    case "The Ripper" -> newCard = new TheRipper(card);
                    case "Disciple" -> newCard = new Disciple(card);
                    case "The Cursed One" -> newCard = new TheCursedOne(card);
                    case "Firestorm" -> newCard = new Firestorm(card);
                    case "Winterfell" -> newCard = new Winterfell(card);
                    case "Heart Hound" -> newCard = new HeartHound(card);
                    default -> System.out.println("No card with the given name!");
                }
                newDeck.getCards().add(newCard);
            }
            this.decks.add(newDeck);
        }
        this.currentMana = player.getCurrentMana();
        this.hand = new ArrayList<Card>();
    }

    @Override
    public String toString() {
        return "Player{" +
                "numberOfDecks=" + numberOfDecks +
                ", decks=" + decks +
                '}';
    }

    public void winGame(){
        this.numberOfWins++;
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

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public void setNumberOfWins(int numberOfWins) {
        this.numberOfWins = numberOfWins;
    }
}
