package Cards;

import fileio.CardInput;
import init.Table;

import java.util.ArrayList;

public class LordRoyce extends Card {
    public LordRoyce(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public LordRoyce(CardInput card)
    {
        super(card.getMana(), card.getAttackDamage(), 30, card.getDescription(), card.getColors(),
                card.getName());
    }

    public LordRoyce(Card card)
    {
        super(card.getMana(), card.getAttackDamage(), 30, card.getDescription(), card.getColors(),
                card.getName());
    }

    /**
     * freezes the highest attack enemy card on a row
     */
    public void subZero(Table table, int affectedRow)
    {
        // find max attack card on row
        int maxAttack = 0;
        for(int col = 0; col < 5; col++)
            if(table.getMatrix()[affectedRow][col] != null)
                if(table.getMatrix()[affectedRow][col].getAttackDamage() > maxAttack)
                    maxAttack = table.getMatrix()[affectedRow][col].getAttackDamage();

        // freeze the found card
        for(int col = 0; col < 5; col++)
            if(table.getMatrix()[affectedRow][col] != null)
                if(maxAttack == table.getMatrix()[affectedRow][col].getAttackDamage()) {
                    table.getMatrix()[affectedRow][col].freezeCard();
                    break;
            }
    }
}
