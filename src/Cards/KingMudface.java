package Cards;

import fileio.CardInput;

import java.util.ArrayList;

public class KingMudface extends Card{
    public KingMudface(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamage, health, description, colors, name);
    }
    public KingMudface(CardInput card)
    {
        super(card.getMana(), card.getAttackDamage(), 30, card.getDescription(), card.getColors(),
                card.getName());
    }

    /**
     * +1 health for all cards on a row
     */
    public void earthborn(){

    }

}
