package Cards;

import Games.CardCoordinates;
import fileio.CardInput;
import init.Table;

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

    public TheRipper(Card card)
    {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(), card.getColors(),
                card.getName());
    }

    /**
     * -2 attack for one minion in the enemy deck
     */
    public void weakKnees(Table table, CardCoordinates attackedCard) {
        if(table.getMatrix()[attackedCard.getX()][attackedCard.getY()].getAttackDamage() > 0)
            table.getMatrix()[attackedCard.getX()][attackedCard.getY()].setAttackDamage(table.getMatrix()[attackedCard.getX()][attackedCard.getY()].getAttackDamage() - 2);
    }
}
