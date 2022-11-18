package cards;

import games.CardCoordinates;
import fileio.CardInput;
import init.Table;

import java.util.ArrayList;

public class TheRipper extends Card {
    public TheRipper(final int mana, final int attackDamage, final int health,
                     final String description, final ArrayList<String> colors,
                     final String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public TheRipper(final CardInput card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(),
                card.getDescription(), card.getColors(), card.getName());
    }

    public TheRipper(final Card card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(),
                card.getDescription(), card.getColors(), card.getName());
    }

    /**
     *  -2 attack for one minion in the enemy deck
     * @param table - playing table
     * @param attackedCard - attacked card coordinates
     */
    public void weakKnees(final Table table, final CardCoordinates attackedCard) {
        if (table.getMatrix()[attackedCard.getX()][attackedCard.getY()].getAttackDamage() > 0) {
            table.getMatrix()[attackedCard.getX()][attackedCard.getY()].setAttackDamage(
                    table.getMatrix()[attackedCard.getX()][attackedCard.getY()].
                            getAttackDamage() - 2);
        }
    }
}
