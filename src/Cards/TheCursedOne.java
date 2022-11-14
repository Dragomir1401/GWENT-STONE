package Cards;

import Games.CardCoordinates;
import fileio.CardInput;
import init.Table;

import java.util.ArrayList;

public class TheCursedOne extends Card {
    public TheCursedOne(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamage, health, description, colors, name);
    }

    public TheCursedOne(CardInput card)
    {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(), card.getColors(),
                card.getName());
    }

    public TheCursedOne(Card card)
    {
        super(card.getMana(), card.getAttackDamage(), card.getHealth(), card.getDescription(), card.getColors(),
                card.getName());
    }

    /**
     * swap between life and attack on an enemy minion
     */
    public void shapeshift(Table table, CardCoordinates attackedCard) {
        int aux = table.getMatrix()[attackedCard.getX()][attackedCard.getY()].getHealth();
        table.getMatrix()[attackedCard.getX()][attackedCard.getY()].setHealth(table.getMatrix()[attackedCard.getX()][attackedCard.getY()].getAttackDamage());
        table.getMatrix()[attackedCard.getX()][attackedCard.getY()].setAttackDamage(aux);
    }
}
