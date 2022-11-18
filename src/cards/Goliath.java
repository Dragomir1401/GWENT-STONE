package cards;

import fileio.CardInput;

import java.util.ArrayList;

public class Goliath extends Card {
    public Goliath(final int mana, final int attackDamage, final int health,
                   final String description, final ArrayList<String> colors,
                   final String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public Goliath(final CardInput card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(),
                card.getColors(), card.getName());
    }

    public Goliath(final Card card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(),
                card.getColors(), card.getName());
    }

}
