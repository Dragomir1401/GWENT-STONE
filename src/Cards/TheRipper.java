package Cards;

import fileio.CardInput;

import java.util.ArrayList;

public class TheRipper extends Card {
    public TheRipper(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public TheRipper(CardInput card)
    {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(), card.getColors(),
                card.getName());
    }

    /**
     * -2 attack for one minion in the enemy deck
     */
    public void weak_kness() {

    }
}
