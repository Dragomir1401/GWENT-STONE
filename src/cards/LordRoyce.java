package cards;

import fileio.CardInput;
import init.Table;
import constants.Constants;
import java.util.ArrayList;

public class LordRoyce extends Card {
    public LordRoyce(final int mana, final int attackDamage, final int health,
                     final String description, final ArrayList<String> colors,
                     final String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public LordRoyce(final CardInput card) {
        super(card.getMana(), card.getAttackDamage(), Constants.DEFAULT_HEALTH,
                card.getDescription(), card.getColors(), card.getName());
    }

    public LordRoyce(final Card card) {
        super(card.getMana(), card.getAttackDamage(), Constants.DEFAULT_HEALTH,
                card.getDescription(), card.getColors(), card.getName());
    }

    /**
     * freezes the highest attack enemy card on a row
     * @param table - playing table
     * @param affectedRow - row that gets affected
     */
    public void subZero(final Table table, final int affectedRow) {
        // find max attack card on row
        int maxAttack = 0;
        for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
            if (table.getMatrix()[affectedRow][col] != null) {
                if (table.getMatrix()[affectedRow][col].getAttackDamage() > maxAttack) {
                    maxAttack = table.getMatrix()[affectedRow][col].getAttackDamage();
                }
            }
        }

        // freeze the found card
        for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
            if (table.getMatrix()[affectedRow][col] != null) {
                if (maxAttack == table.getMatrix()[affectedRow][col].getAttackDamage()) {
                    table.getMatrix()[affectedRow][col].freezeCard();
                    break;
                }
            }
        }
    }
}
