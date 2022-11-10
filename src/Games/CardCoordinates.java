package Games;

import Cards.Card;

public class CardCoordinates {
    private int x, y;

    public CardCoordinates(){
        this.x = 0;
        this.y = 0;
    }

    public CardCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{"
                + "x="
                + x
                + ", y="
                + y
                + '}';
    }
}
