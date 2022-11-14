package Cards;

import fileio.CardInput;
import init.Table;

import java.util.ArrayList;

public class Firestorm extends Card {
    public Firestorm(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public Firestorm(CardInput card)
    {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(), card.getColors(),
                card.getName());
    }

    public Firestorm(Card card)
    {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(), card.getColors(),
                card.getName());
    }

    public void deploy(Table table, int affectedRow) {
        /**
         * -1 health to all minions on a row
         */
        for(int column = 0; column < 5; column++)
            if(table.getMatrix()[affectedRow][column] != null)
                table.getMatrix()[affectedRow][column].setHealth(table.getMatrix()[affectedRow][column].getHealth() - 1);
    }
}
