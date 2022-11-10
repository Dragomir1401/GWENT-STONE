package Games;

import fileio.Coordinates;

import java.awt.font.TextHitInfo;

public class Actions {
    private String command;
    private int handIdx;
    private CardCoordinates cardAttacker;
    private CardCoordinates cardAttacked;
    private int affectedRow;
    private int playerIdx;
    private int x;
    private int y;

    public Actions(){
        this.command = "NO COMMAND";
        this.handIdx = 0;
        this.cardAttacker = new CardCoordinates();
        this.cardAttacked = new CardCoordinates();
        this.affectedRow = 0;
        this.playerIdx = 0;
        this.x = 0;
        this.y = 0;
    }

    public Actions(String command, int handIdx, CardCoordinates cardAttacker, CardCoordinates cardAttacked, int affectedRow, int playerIdx, int x, int y) {
        this.command = command;
        this.handIdx = handIdx;
        this.cardAttacker = cardAttacker;
        this.cardAttacked = cardAttacked;
        this.affectedRow = affectedRow;
        this.playerIdx = playerIdx;
        this.x = x;
        this.y = y;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public int getHandIdx() {
        return handIdx;
    }

    public void setHandIdx(final int handIdx) {
        this.handIdx = handIdx;
    }

    public CardCoordinates getCardAttacker() {
        return cardAttacker;
    }

    public void setCardAttacker(final CardCoordinates cardAttacker) {
        this.cardAttacker = cardAttacker;
    }

    public CardCoordinates getCardAttacked() {
        return cardAttacked;
    }

    public void setCardAttacked(final CardCoordinates cardAttacked) {
        this.cardAttacked = cardAttacked;
    }

    public int getAffectedRow() {
        return affectedRow;
    }

    public void setAffectedRow(final int affectedRow) {
        this.affectedRow = affectedRow;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    public void setPlayerIdx(final int playerIdx) {
        this.playerIdx = playerIdx;
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
        return "ActionsInput{"
                +  "command='"
                + command + '\''
                +  ", handIdx="
                + handIdx
                +  ", cardAttacker="
                + cardAttacker
                +  ", cardAttacked="
                + cardAttacked
                + ", affectedRow="
                + affectedRow
                + ", playerIdx="
                + playerIdx
                + ", x="
                + x
                + ", y="
                + y
                + '}';
    }
}
