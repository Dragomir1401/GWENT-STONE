package games;

public class CardCoordinates {
    private int x, y;

    public CardCoordinates() {
        this.x = 0;
        this.y = 0;
    }

    public CardCoordinates(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public CardCoordinates(final CardCoordinates cardCoordinates) {
        this.x = cardCoordinates.getX();
        this.y = cardCoordinates.getY();
    }

    /**
     * gets x coordinate
     * @return - x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * sets x coordiante
     * @param x - x coordinate
     */
    public void setX(final int x) {
        this.x = x;
    }

    /**
     * gets y coordinate
     * @return - y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * sets x coordiante
     * @param y - y coordinate
     */
    public void setY(final int y) {
        this.y = y;
    }
}
