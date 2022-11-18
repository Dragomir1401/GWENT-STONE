package cards;

import java.util.ArrayList;

public class Card {
    private final int mana;
    private int attackDamage;
    private int health;
    private final String description;
    private final ArrayList<String> colors;
    private final String name;
    private boolean isFrozen;
    private boolean hasAttackedThisRound;

    public Card() {
        this.mana = 0;
        this.attackDamage = 0;
        this.health = 0;
        this.description = "NO DESCRIPTION";
        this.colors = new ArrayList<String>();
        this.name = "NO NAME";
        this.isFrozen = false;
        this.hasAttackedThisRound = false;
    }

    public Card(final int mana, final int attackDamage, final int health, final String description,
                final ArrayList<String> colors, final String name) {
        this.mana = mana;
        this.attackDamage = attackDamage;
        this.health = health;
        this.description = description;
        this.colors = colors;
        this.name = name;
        this.isFrozen = false;
        this.hasAttackedThisRound = false;
    }

    /**
     * copy constructor
     * @param card - to copy from card
     */
    public Card(final Card card) {
        this.mana = card.getMana();
        this.attackDamage = card.getAttackDamage();
        this.health = card.getHealth();
        this.description = card.getDescription();
        this.colors = card.getColors();
        this.name = card.getName();
        this.isFrozen = card.isFrozen;
        this.hasAttackedThisRound = card.getHasAttackedThisRound();
    }

    /**
     * verifies if card is environment type
     * @return - true/false
     */
    public boolean cardIsEnvironmentCard() {
        if (name.equals("Winterfell")) {
            return true;
        }
        if (name.equals("Firestorm")) {
            return true;
        }
        if (name.equals("Heart Hound")) {
            return true;
        }
        return false;
    }

    /**
     * verifies if card is tank type
     * @return - true/false
     */
    public boolean cardIsTank() {
        if (name.equals("Goliath") || name.equals("Warden")) {
            return true;
        }
        return false;
    }

    /**
     * checks if card has frozen status
     * @return - true/false
     */
    public boolean cardIsFrozen() {
        return this.isFrozen;
    }

    /**
     * action to freeze the card
     */
    public void freezeCard() {
        this.isFrozen = true;
    }

    /**
     * action to unfreeze the card
     */
    public void unfreezeCard() {
        this.isFrozen = false;
    }

    /**
     * getter for mana
     * @return - mana
     */
    public int getMana() {
        return mana;
    }

    /**
     * getter for attack damage
     * @return - attack damage
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * getter for health
     * @return - health
     */
    public int getHealth() {
        return health;
    }

    /**
     * getter for description
     * @return - description
     */
    public String getDescription() {
        return description;
    }

    /**
     * getter for colors
     * @return - colors
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     * getter for name
     * @return - card name
     */
    public String getName() {
        return name;
    }

    /**
     * getter for frozen status
     * @return - true/false
     */
    public boolean isFrozen() {
        return isFrozen;
    }

    /**
     * setter for health
     * @param health - card life
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     * setter for attack damage
     * @param attackDamage - card attack to copy
     */
    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * checks if cards has attacked that round
     * @return - true/false
     */
    public boolean getHasAttackedThisRound() {
        return hasAttackedThisRound;
    }

    /**
     * setter for attack status
     * @param hasAttackedThisRound - wanted attack status
     */
    public void setHasAttackedThisRound(final boolean hasAttackedThisRound) {
        this.hasAttackedThisRound = hasAttackedThisRound;
    }
}
