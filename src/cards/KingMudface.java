package cards;

import fileio.CardInput;
import init.Table;
import constants.Constants;
import java.util.ArrayList;

public class KingMudface extends Card {
    public KingMudface(final int mana, final int attackDamage, final int health,
                       final String description, final ArrayList<String> colors,
                       final String name) {
        super(mana, attackDamage, health, description, colors, name);
    }
    public KingMudface(final CardInput card) {
        super(card.getMana(), card.getAttackDamage(), Constants.DEFAULT_HEALTH,
                card.getDescription(), card.getColors(), card.getName());
    }

    public KingMudface(final Card card) {
        super(card.getMana(), card.getAttackDamage(), Constants.DEFAULT_HEALTH,
                card.getDescription(), card.getColors(), card.getName());
    }

    /**
     * +1 health for all cards on a row
     * @param table - playing table
     * @param affectedRow - row that is affected
     */
    public void earthBorn(final Table table, final int affectedRow) {
        for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
            if (table.getMatrix()[affectedRow][col] != null) {
                table.getMatrix()[affectedRow][col].setHealth(table.getMatrix()
                        [affectedRow][col].getHealth() + 1);
            }
        }
    }

}
