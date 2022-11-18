package cards;

import fileio.CardInput;
import init.Table;
import constants.Constants;
import java.util.ArrayList;

public class HeartHound extends Card {
    public HeartHound(final int mana, final int attackDamage, final int health,
                      final String description, final ArrayList<String> colors,
                      final String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public HeartHound(final CardInput card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(),
                card.getDescription(), card.getColors(), card.getName());
    }

    public HeartHound(final Card card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(),
                card.getDescription(), card.getColors(), card.getName());
    }

    /**
     * steal the highest health enemy minion and put it on your side
     * returns the stolen card and removes it from the enemy line
     * @param table - playing table
     * @param affectedRow - row that is affected
     * @return - 1 for success, -1 for error
     */
    public int deploy(final Table table, final int affectedRow) {
        int destinationRow = 0;
        if (affectedRow == 0) {
            destinationRow = Constants.ROW3;
        }
        if (affectedRow == 1) {
            destinationRow = 2;
        }
        if (affectedRow == 2) {
            destinationRow = 1;
        }

        // exit all together when the destinationRow is full
        boolean destinationRowIsFull = true;
        for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
            if (table.getMatrix()[destinationRow][col] == null) {
                destinationRowIsFull = false;
            }
        }

        if (destinationRowIsFull) {
            return -1;
        }

        // find the max health card on the affected row
        int maxHealth = 0;
        for (int column = 0; column < Constants.NUMBER_OF_COLUMNS; column++) {
            if (table.getMatrix()[affectedRow][column] != null) {
                if (table.getMatrix()[affectedRow][column].getHealth() > maxHealth) {
                    maxHealth = table.getMatrix()[affectedRow][column].getHealth();
                }
            }
        }

        for (int column = 0; column < Constants.NUMBER_OF_COLUMNS; column++) {
            if (table.getMatrix()[affectedRow][column] != null) {
                if (table.getMatrix()[affectedRow][column].getHealth() == maxHealth) {
                    // steal the card and shift the row
                    Card stolenCard = table.getMatrix()[affectedRow][column];
                    for (int shift = column; shift < Constants.NUMBER_OF_ROWS; shift++) {
                        table.getMatrix()[affectedRow][shift] = table.getMatrix()
                                [affectedRow][shift + 1];
                    }
                    table.getMatrix()[affectedRow][Constants.NUMBER_OF_ROWS] = null;
                    // place it on our row


                    // case when we have empty place already even to the left
                    for (int check = 0; check < column; check++) {
                        if (table.getMatrix()[destinationRow][check] != null) {
                            table.getMatrix()[destinationRow][check] = stolenCard;
                            // exit because we placed the new card to empty places to the left
                            return 1;
                        }
                    }

                    for (int shift = Constants.NUMBER_OF_ROWS; shift > column; shift--) {
                        table.getMatrix()[destinationRow][shift] = table.getMatrix()
                                [destinationRow][shift - 1];
                    }
                    table.getMatrix()[destinationRow][column] = stolenCard;
                    return 1;
                }
            }
        }
        return 1;
    }
}
