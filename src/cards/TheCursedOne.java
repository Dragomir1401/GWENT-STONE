package cards;

import games.CardCoordinates;
import fileio.CardInput;
import init.Table;

import java.util.ArrayList;

public class TheCursedOne extends Card {
    public TheCursedOne(final int mana, final int attackDamage, final int health,
                        final String description, final ArrayList<String> colors,
                        final String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public TheCursedOne(final CardInput card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(),
                card.getDescription(), card.getColors(), card.getName());
    }

    public TheCursedOne(final Card card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(),
                card.getDescription(), card.getColors(), card.getName());
    }

    /**
     * swap between life and attack on an enemy minion
     * @param table - playing table
     * @param attackedCard - attacked card coordinates
     */
    public void shapeShift(final Table table, final CardCoordinates attackedCard) {
        int aux = table.getMatrix()[attackedCard.getX()][attackedCard.getY()].getHealth();
        table.getMatrix()[attackedCard.getX()][attackedCard.getY()].setHealth(table.getMatrix()
                [attackedCard.getX()][attackedCard.getY()].getAttackDamage());
        table.getMatrix()[attackedCard.getX()][attackedCard.getY()].setAttackDamage(aux);
    }
}
