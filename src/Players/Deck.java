package Players;

import Cards.Card;

import java.util.ArrayList;

public class Deck {
    private int number_of_cards;
    ArrayList<Cards.Card> cards;

    public Deck(){
        this.cards = new ArrayList<>();
        this.number_of_cards = 0;
    }

    public Deck(int number_of_cards, ArrayList<Card> cards) {
        this.number_of_cards = number_of_cards;
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "Deck{" +
                "number_of_cards=" + number_of_cards +
                ", cards=" + cards +
                '}';
    }

    public int getNumber_of_cards() {
        return number_of_cards;
    }

    public void setNumber_of_cards(int number_of_cards) {
        this.number_of_cards = number_of_cards;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
}
