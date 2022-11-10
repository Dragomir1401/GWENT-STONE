package Cards;

import fileio.CardInput;

import java.util.ArrayList;

public class Disciple extends Card {
    public Disciple(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public Disciple(CardInput card)
    {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(), card.getColors(),
                card.getName());
    }

    /**
     * +2 health to a minion
     */
    public void gods_plan() {

    }
}
