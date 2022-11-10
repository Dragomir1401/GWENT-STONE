package Cards;

import fileio.CardInput;

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

    void deploy(){
        /**
         * all enemy cards skip a round
         */
    }
}
