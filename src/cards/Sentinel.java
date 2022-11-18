package cards;

import fileio.CardInput;

import java.util.ArrayList;

public class Sentinel extends Card {
    public Sentinel(final int mana, final int attackDamage, final int health,
                    final String description, final ArrayList<String> colors,
                    final String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public Sentinel(final CardInput card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(),
                card.getDescription(), card.getColors(), card.getName());
    }

    public Sentinel(final Card card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(),
                card.getDescription(), card.getColors(), card.getName());
    }
}
