package Cards;

import fileio.CardInput;
import init.Table;

import java.util.ArrayList;

public class Winterfell extends Card{
    public Winterfell(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public Winterfell(CardInput card)
    {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(), card.getColors(),
                card.getName());
    }

    public void deploy(Table table, int affectedRow){
        /**
         * all enemy cards skip a round
         */
        for(int column = 0; column < 5; column++) {
            if(table.getMatrix()[affectedRow][column] != null)
                table.getMatrix()[affectedRow][column].freezeCard();
        }
    }
}
