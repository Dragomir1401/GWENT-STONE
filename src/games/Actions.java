package games;

public class Actions {
    private String command;
    private int handIdx;
    private CardCoordinates cardAttacker;
    private CardCoordinates cardAttacked;
    private int affectedRow;
    private int playerIdx;
    private int x;
    private int y;

    public Actions() {
        this.command = "NO COMMAND";
        this.handIdx = 0;
        this.cardAttacker = new CardCoordinates();
        this.cardAttacked = new CardCoordinates();
        this.affectedRow = 0;
        this.playerIdx = 0;
        this.x = 0;
        this.y = 0;
    }

    public Actions(final String command, final int handIdx, final CardCoordinates cardAttacker,
                   final CardCoordinates cardAttacked, final int affectedRow,
                   final int playerIdx, final int x, final int y) {
        this.command = command;
        this.handIdx = handIdx;
        this.cardAttacker = cardAttacker;
        this.cardAttacked = cardAttacked;
        this.affectedRow = affectedRow;
        this.playerIdx = playerIdx;
        this.x = x;
        this.y = y;
    }

    public Actions(final Actions action) {
        this.command = action.getCommand();
        this.handIdx = action.getHandIdx();
        this.cardAttacker = new CardCoordinates(action.getCardAttacker());
        this.cardAttacked = new CardCoordinates(action.getCardAttacked());
        this.affectedRow = action.getAffectedRow();
        this.playerIdx = action.getPlayerIdx();
        this.x = action.getX();
        this.y = action.getY();
    }

    /**
     * getter for command
     * @return command string
     */
    public String getCommand() {
        return command;
    }

    /**
     * setter for command
     * @param command - command string
     */
    public void setCommand(final String command) {
        this.command = command;
    }

    /**
     * getter for hand index
     * @return - hand index
     */
    public int getHandIdx() {
        return handIdx;
    }

    /**
     * setter for hand index
     * @param handIdx - index of card in hand
     */
    public void setHandIdx(final int handIdx) {
        this.handIdx = handIdx;
    }

    /**
     * getter for card attacker coordinates
     * @return - attacker card coordinates
     */
    public CardCoordinates getCardAttacker() {
        return cardAttacker;
    }

    /**
     * setter for card attacker coordinates
     * @param cardAttacker - attacker card coordinates
     */
    public void setCardAttacker(final CardCoordinates cardAttacker) {
        this.cardAttacker = cardAttacker;
    }

    /**
     * getter for card attacked coordinates
     * @return - attacked card coordinates
     */
    public CardCoordinates getCardAttacked() {
        return cardAttacked;
    }

    /**
     * setter for card attacked coordinates
     * @param cardAttacked - attacked card coordinates
     */
    public void setCardAttacked(final CardCoordinates cardAttacked) {
        this.cardAttacked = cardAttacked;
    }

    /**
     * getter for affected row
     * @return - the affected row
     */
    public int getAffectedRow() {
        return affectedRow;
    }

    /**
     * setter for affected row
     * @param affectedRow - the row affected
     */
    public void setAffectedRow(final int affectedRow) {
        this.affectedRow = affectedRow;
    }

    /**
     * getter for player index
     * @return - player index
     */
    public int getPlayerIdx() {
        return playerIdx;
    }

    /**
     * setter for player index
     * @param playerIdx - player index
     */
    public void setPlayerIdx(final int playerIdx) {
        this.playerIdx = playerIdx;
    }

    /**
     * getter for x coordinate
     * @return - x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * setter for x coordinate
     * @param x - coordinate
     */
    public void setX(final int x) {
        this.x = x;
    }

    /**
     * getter for y coordinate
     * @return - y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * setter for x coordinate
     * @param y - coordinate
     */
    public void setY(final int y) {
        this.y = y;
    }
}
