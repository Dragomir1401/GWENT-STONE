package Cards;

import Games.CardCoordinates;

import java.util.ArrayList;

public class Card {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private boolean isFrozen;
    private boolean hasAttackedThisRound;

    public Card(){
        this.mana = 0;
        this.attackDamage = 0;
        this.health = 0;
        this.description = "NO DESCRIPTION";
        this.colors = new ArrayList<String>();
        this.name = "NO NAME";
        this.isFrozen = false;
        this.hasAttackedThisRound = false;
    }

    public Card(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        this.mana = mana;
        this.attackDamage = attackDamage;
        this.health = health;
        this.description = description;
        this.colors = colors;
        this.name = name;
        this.isFrozen = false;
        this.hasAttackedThisRound = false;
    }

    public Card(Card card)
    {
        this.mana = card.getMana();
        this.attackDamage = card.getAttackDamage();
        this.health = card.getHealth();
        this.description = card.getDescription();
        this.colors = card.getColors();
        this.name = card.getName();
        this.isFrozen = card.isFrozen;
        this.hasAttackedThisRound = card.getHasAttackedThisRound();
    }

    @Override
    public String toString() {
        return "Card{" +
                "mana=" + mana +
                ", attackDamage=" + attackDamage +
                ", health=" + health +
                ", description='" + description + '\'' +
                ", colors=" + colors +
                ", name='" + name + '\'' +
                '}';
    }
    public boolean cardIsEnvironmentCard()
    {
        if(name.equals("Winterfell"))
            return true;
        if(name.equals("Firestorm"))
            return true;
        if(name.equals("Heart Hound"))
            return true;
        return false;
    }

    public boolean cardIsTank()
    {
        if(name.equals("Goliath") || name.equals("Warden"))
            return true;
        return false;
    }

    public boolean cardIsFrozen()
    {
        return this.isFrozen;
    }
    public void freezeCard(){
        this.isFrozen = true;
    }

    public void unfreezeCard(){
        this.isFrozen = false;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    public boolean getHasAttackedThisRound() {
        return hasAttackedThisRound;
    }

    public void setHasAttackedThisRound(boolean hasAttackedThisRound) {
        this.hasAttackedThisRound = hasAttackedThisRound;
    }
}
