package Cards;

import fileio.CardInput;

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

    void deploy() {
        /**
         * steal the highest health enemy minion and put it on your side
         */
    }
}
