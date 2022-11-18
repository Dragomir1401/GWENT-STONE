package cards;

import fileio.CardInput;

import java.util.ArrayList;

public class Warden extends Card {
    public Warden(final int mana, final int attackDamage, final int health,
                  final String description, final ArrayList<String> colors,
                  final String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public Warden(final CardInput card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(),
                card.getDescription(), card.getColors(), card.getName());
    }

    public Warden(final Card card) {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(),
                card.getDescription(), card.getColors(), card.getName());
    }
}

