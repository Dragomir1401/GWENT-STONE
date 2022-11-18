package cards;

import fileio.CardInput;
import init.Table;
import constants.Constants;
import java.util.ArrayList;

public class Winterfell extends Card {
    public Winterfell(final int mana, final int attackDamage, final int health,
                      final String description, final ArrayList<String> colors,
                      final String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public Winterfell(final CardInput card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(),
                card.getDescription(), card.getColors(), card.getName());
    }

    public Winterfell(final Card card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(),
                card.getDescription(), card.getColors(), card.getName());
    }

    /**
     * all enemy cards skip a round
     * @param table - playing table
     * @param affectedRow - row that is affected
     */
    public void deploy(final Table table, final int affectedRow) {
        for (int column = 0; column < Constants.NUMBER_OF_COLUMNS; column++) {
            if (table.getMatrix()[affectedRow][column] != null) {
                table.getMatrix()[affectedRow][column].freezeCard();
            }
        }
    }
}
