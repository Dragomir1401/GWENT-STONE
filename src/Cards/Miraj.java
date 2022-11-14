package Cards;

import Games.CardCoordinates;
import fileio.CardInput;
import init.Table;

import java.util.ArrayList;

public class Miraj extends Card {
    public Miraj(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public Miraj(CardInput card)
    {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(), card.getColors(),
                card.getName());
    }

    public Miraj(Card card)
    {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(), card.getColors(),
                card.getName());
    }
    /**
     * swap between his life and one minion life in the enemy deck
     */
    public void skyjack(Table table, CardCoordinates attackerCard, CardCoordinates attackedCard) {
        int aux = table.getMatrix()[attackerCard.getX()][attackerCard.getY()].getHealth();
        table.getMatrix()[attackerCard.getX()][attackerCard.getY()].setHealth(table.getMatrix()[attackedCard.getX()][attackedCard.getY()].getHealth());
        table.getMatrix()[attackedCard.getX()][attackedCard.getY()].setHealth(aux);
    }
}
