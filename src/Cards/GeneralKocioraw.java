package Cards;

import fileio.CardInput;
import init.Table;

import java.util.ArrayList;

public class GeneralKocioraw extends Card{
    public GeneralKocioraw(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public GeneralKocioraw(CardInput card)
    {
        super(card.getMana(), card.getAttackDamage(), 30, card.getDescription(), card.getColors(),
                card.getName());
    }

    /**
     * +1 attack for all cards on a row
     */
    public void bloodThirst(Table table, int affectedRow){
        for(int col = 0; col < 5; col++)
            table.getMatrix()[affectedRow][col].setAttackDamage(table.getMatrix()[affectedRow][col].getAttackDamage() + 1);

    }
}
