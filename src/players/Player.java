package players;

import cards.Sentinel;
import cards.Winterfell;
import cards.Berserker;
import cards.Disciple;
import cards.Firestorm;
import cards.Goliath;
import cards.HeartHound;
import cards.Miraj;
import cards.TheRipper;
import cards.TheCursedOne;
import cards.Warden;
import cards.Card;

import java.util.ArrayList;

public class Player {
    private int numberOfDecks;
    private ArrayList<Deck> decks;
    private int currentMana;
    private ArrayList<Card> hand;
    private int numberOfWins;

    public Player() {
        this.numberOfDecks = 0;
        this.decks = new ArrayList<>();
        this.currentMana = 0;
        this.hand = new ArrayList<>();
        this.numberOfWins = 0;
    }

    public Player(final int numberOfDecks, final ArrayList<Deck> decks, final int currentMana,
                  final ArrayList<Card> hand) {
        this.numberOfDecks = numberOfDecks;
        this.decks = decks;
        this.currentMana = currentMana;
        this.hand = hand;
        this.numberOfWins = 0;
    }

    public Player(final Player player) {
        this.numberOfDecks = player.getNumberOfDecks();
        this.decks = new ArrayList<Deck>();
        for (Deck deck : player.getDecks()) {
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

    /**
     * increases wins for player
     */
    public void winGame() {
        this.numberOfWins++;
    }

    /**
     * gets player decks
     * @return - decks
     */

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    /**
     * setter for player decks
     * @param decks - player decks
     */

    public void setDecks(final ArrayList<Deck> decks) {
        this.decks = decks;
    }

    /**
     * getter for player mana
     * @return - player mana
     */
    public int getCurrentMana() {
        return currentMana;
    }

    /**
     * setter for player mana
     * @param currentMana - player mana
     */
    public void setCurrentMana(final int currentMana) {
        this.currentMana = currentMana;
    }

    /**
     * getter for number of decks
     * @return
     */
    public int getNumberOfDecks() {
        return numberOfDecks;
    }

    /**
     * getter for player hand
     * @return - card list
     */
    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * setter for player hand
     * @param hand - card list
     */
    public void setHand(final ArrayList<Card> hand) {
        this.hand = hand;
    }

    /**
     * getter for number of wins
     * @return - number of wins
     */
    public int getNumberOfWins() {
        return numberOfWins;
    }

    /**
     * setter for number of wins
     * @param numberOfWins - number of player wins
     */
    public void setNumberOfWins(final int numberOfWins) {
        this.numberOfWins = numberOfWins;
    }
}
