package cards;

import games.CardCoordinates;
import fileio.CardInput;
import init.Table;
import java.util.ArrayList;

public class Miraj extends Card {
    public Miraj(final int mana, final int attackDamage, final int health,
                 final String description, final ArrayList<String> colors,
                 final String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public Miraj(final CardInput card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(),
                card.getDescription(), card.getColors(), card.getName());
    }

    public Miraj(final Card card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(),
                card.getDescription(), card.getColors(), card.getName());
    }

    /**
     * swap between his life and one minion life in the enemy deck
     * @param table - playing table
     * @param attackerCard - card attacker coordinates
     * @param attackedCard - card attacked coordinates
     */
    public void skyjack(final Table table, final CardCoordinates attackerCard,
                        final CardCoordinates attackedCard) {
        int aux = table.getMatrix()[attackerCard.getX()][attackerCard.getY()].getHealth();
        table.getMatrix()[attackerCard.getX()][attackerCard.getY()].setHealth(table.getMatrix()
                [attackedCard.getX()][attackedCard.getY()].getHealth());
        table.getMatrix()[attackedCard.getX()][attackedCard.getY()].setHealth(aux);
    }
}
