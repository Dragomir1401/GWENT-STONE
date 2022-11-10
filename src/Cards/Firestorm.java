package Cards;

import fileio.CardInput;

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

    void deploy() {
        /**
         * -1 health to all minions on a row
         */
    }
}
