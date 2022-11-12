package Cards;

import fileio.CardInput;
import init.Table;
import init.PlayGame;
import java.util.ArrayList;

public class HeartHound extends Card {
    public HeartHound(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public HeartHound(CardInput card)
    {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(), card.getColors(),
                card.getName());
    }

    public int deploy(Table table, int affectedRow) {
        /**
         * steal the highest health enemy minion and put it on your side
         * returns the stolen card and removes it from the enemy line
         */
        int maxHealth = 0;
        for(int column = 0; column < 5; column++)
            if(table.getMatrix()[affectedRow][column] != null)
                if(table.getMatrix()[affectedRow][column].getHealth() > maxHealth)
                    maxHealth = table.getMatrix()[affectedRow][column].getHealth();

        Card stolenCard = null;
        for(int column = 0; column < 5; column++)
            if(table.getMatrix()[affectedRow][column] != null)
                if(table.getMatrix()[affectedRow][column].getHealth() == maxHealth) {
                    // steal the card and shift the row
                    stolenCard = table.getMatrix()[affectedRow][column];
                    for(int shift = column; shift < 4; shift++)
                        table.getMatrix()[affectedRow][shift] = table.getMatrix()[affectedRow][shift + 1];
                    table.getMatrix()[affectedRow][4] = null;
                    // place it on our row
                    int destinationRow = 0;
                    if(affectedRow == 0)
                        destinationRow = 3;
                    if(affectedRow == 1)
                        destinationRow = 2;
                    if(affectedRow == 2)
                        destinationRow = 1;

                    boolean placed = false;
                    for(int check = 0; check < 5; check++)
                        if(table.getMatrix()[destinationRow][check] != null)
                            placed = true;
                    // error case for destinationRow full
                    if(!placed)
                        return -1;
                    else {
                        for(int shift = column; shift < 4; shift++)
                            table.getMatrix()[destinationRow][shift] = table.getMatrix()[destinationRow][shift + 1];
                        table.getMatrix()[destinationRow][column] = stolenCard;
                    }
                    break;
            }

        return 1;
    }
}
