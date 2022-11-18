package cards;

import fileio.CardInput;
import init.Table;
import java.util.ArrayList;
import constants.Constants;

public class EmpressThorina extends Card {
    public EmpressThorina(final int mana, final int attackDamage, final int health,
                          final String description, final ArrayList<String> colors,
                          final String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public EmpressThorina(final CardInput card) {
        super(card.getMana(), card.getAttackDamage(), Constants.DEFAULT_HEALTH,
                card.getDescription(), card.getColors(), card.getName());
    }

    public EmpressThorina(final Card card) {
        super(card.getMana(), card.getAttackDamage(), Constants.DEFAULT_HEALTH,
                card.getDescription(), card.getColors(), card.getName());
    }

    /**
     * destroys the highest health card on a row
     * @param table - playing table
     * @param affectedRow - row that is affected
     */
    public void lowBlow(final Table table, final int affectedRow) {
        // find max health card on row
        int maxHealth = 0;
        for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
            if (table.getMatrix()[affectedRow][col] != null) {
                if (table.getMatrix()[affectedRow][col].getHealth() > maxHealth) {
                    maxHealth = table.getMatrix()[affectedRow][col].getHealth();
                }
            }
        }

        // delete the found card
        for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
            if (table.getMatrix()[affectedRow][col] != null) {
                if (maxHealth == table.getMatrix()[affectedRow][col].getHealth()) {
                    table.getMatrix()[affectedRow][col] = null;
                    for (int shift = col; shift < Constants.NUMBER_OF_ROWS; shift++) {
                        table.getMatrix()[affectedRow][shift] = table.getMatrix()
                                [affectedRow][shift + 1];
                    }
                    table.getMatrix()[affectedRow][Constants.NUMBER_OF_ROWS] = null;
                    break;
                }
            }
        }
    }
}


