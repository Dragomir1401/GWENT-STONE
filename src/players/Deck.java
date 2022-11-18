package players;

import cards.Card;

import java.util.ArrayList;

public class Deck {
    private int numberOfCards;
    private ArrayList<cards.Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
        this.numberOfCards = 0;
    }

    public Deck(final int numberOfCards, final ArrayList<Card> cards) {
        this.numberOfCards = numberOfCards;
        this.cards = cards;
    }

    /**
     * getter for cards in deck
     * @return - list of cards
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * setter for cards in deck
     * @param cards - list of cards
     */
    public void setCards(final ArrayList<Card> cards) {
        this.cards = cards;
    }
}
