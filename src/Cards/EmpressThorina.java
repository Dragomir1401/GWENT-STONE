package Cards;

import fileio.CardInput;

import java.util.ArrayList;

public class EmpressThorina extends Card{
    public EmpressThorina(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public EmpressThorina(CardInput card)
    {
        super(card.getMana(), card.getAttackDamage(), 30, card.getDescription(), card.getColors(),
                card.getName());
    }

    /**
     * destroys the highest health card on a row
     */
    public void lowblow()
    {

    }
}


