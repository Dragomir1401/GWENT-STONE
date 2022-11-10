package Cards;

import fileio.CardInput;

import java.util.ArrayList;

public class Miraj extends Card {
    public Miraj(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public Miraj(CardInput card)
    {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(), card.getColors(),
                card.getName());
    }

    /**
     * swap between his life and one minion life in the enemy deck
     */
    public void skyjack() {

    }
}
