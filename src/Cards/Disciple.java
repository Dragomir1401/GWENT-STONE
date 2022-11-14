package Cards;

import Games.CardCoordinates;
import fileio.CardInput;
import init.Table;

import java.util.ArrayList;

public class Disciple extends Card {
    public Disciple(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public Disciple(CardInput card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(), card.getColors(), card.getName());
    }

    public Disciple(Card card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(), card.getColors(), card.getName());
    }


    /**
     * +2 health to a friendly minion
     */
    public void godsPlan(Table table, CardCoordinates attackedCard) {
        table.getMatrix()[attackedCard.getX()][attackedCard.getY()].setHealth(table.getMatrix()[attackedCard.getX()][attackedCard.getY()].getHealth() + 2);
    }
}
