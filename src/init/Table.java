package init;

import cards.Card;
import constants.Constants;

public class Table {
    private Card[][] matrix;
    private int currentlyPlaying;
    public Table() {
        this.matrix = new Card[Constants.NUMBER_OF_ROWS][Constants.NUMBER_OF_COLUMNS];
        this.currentlyPlaying = 0;
    }

    public Table(final Table table) {
        this.matrix = new Card[Constants.NUMBER_OF_ROWS][Constants.NUMBER_OF_COLUMNS];
        this.currentlyPlaying = table.getCurrentlyPlaying();
    }

    public Table(final Card[][] matrix, final int currentlyPlaying) {
        this.matrix = matrix;
        this.currentlyPlaying = currentlyPlaying;
    }

    /**
     * changes what player does actions
     */
    public void changeCurrentlyPlaying() {
        if (currentlyPlaying == 1) {
            currentlyPlaying = 2;
            return;
        }
        if (currentlyPlaying == 2) {
            currentlyPlaying = 1;
        }
    }

    /**
     * deletes a card with coordinates given from table
     * @param row - x coordinate
     * @param column - y coordinate
     */
    public void deleteCardFromTable(final int row, final int column) {
        // card died and has to be deleted from the table
        matrix[row][column] = null;
        for (int shift = column; shift < Constants.NUMBER_OF_ROWS; shift++) {
            matrix[row][shift] = matrix[row][shift + 1];
        }
        matrix[row][Constants.NUMBER_OF_ROWS] = null;
    }

    /**
     * getter for actual matrix
     * @return - matrix table
     */
    public Card[][] getMatrix() {
        return matrix;
    }

    /**
     * getter for what player is doing actions
     * @return - current player
     */
    public int getCurrentlyPlaying() {
        return currentlyPlaying;
    }

    /**
     * setter for current player
     * @param currentlyPlaying - player doing actions
     */
    public void setCurrentlyPlaying(final int currentlyPlaying) {
        this.currentlyPlaying = currentlyPlaying;
    }
}
