package Cards;

import fileio.CardInput;
import init.Table;
import init.PlayGame;
import java.util.ArrayList;

public class EmpressThorina extends Card{
    public EmpressThorina(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public EmpressThorina(CardInput card)
    {
        super(card.getMana(), card.getAttackDamage(), 30, card.getDescription(), card.getColors(),
                card.getName());
    }

    /**
     * destroys the highest health card on a row
     */
    public void lowBlow(Table table, int affectedRow)
    {
        // find max health card on row
        int maxHealth = 0;
        for(int col = 0; col < 5; col++)
            if(table.getMatrix()[affectedRow][col].getHealth() > maxHealth)
                maxHealth = table.getMatrix()[affectedRow][col].getHealth();

        // delete the found card
        for(int col = 0; col < 5; col++)
            if(maxHealth == table.getMatrix()[affectedRow][col].getHealth()) {
                table.getMatrix()[affectedRow][col] = null;
                for(int shift = col; shift < 4; shift++)
                    table.getMatrix()[affectedRow][shift] = table.getMatrix()[affectedRow][shift + 1];
                table.getMatrix()[affectedRow][4] = null;
                break;
            }
    }
}


