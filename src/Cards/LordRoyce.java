package Cards;

import fileio.CardInput;

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

    /**
     * freezes the highest attack enemy card on a row
     */
    public void subzero()
    {

    }
}
