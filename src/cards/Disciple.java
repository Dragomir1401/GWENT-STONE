package cards;

import games.CardCoordinates;
import fileio.CardInput;
import init.Table;

import java.util.ArrayList;

public class Disciple extends Card {
    public Disciple(final int mana, final  int attackDamage, final int health,
                    final String description, final ArrayList<String> colors, final String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public Disciple(final CardInput card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(),
                card.getColors(), card.getName());
    }

    public Disciple(final Card card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(),
                card.getColors(), card.getName());
    }


    /**
     * +2 health to a friendly minion
     */
    public void godsPlan(final Table table, final CardCoordinates attackedCard) {
        table.getMatrix()[attackedCard.getX()][attackedCard.getY()].
                setHealth(table.getMatrix()
                        [attackedCard.getX()][attackedCard.getY()].getHealth() + 2);
    }
}
