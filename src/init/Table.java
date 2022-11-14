package init;

import Cards.Card;

public class Table {
    private Card[][] matrix;
    private int currentlyPlaying;
    public Table(){
        this.matrix = new Card[4][5];
        this.currentlyPlaying = 0;
    }

    public Table(Table table){
        this.matrix = new Card[4][5];
        this.currentlyPlaying = table.getCurrentlyPlaying();
    }

    public Table(Card[][] matrix, int currentlyPlaying) {
        this.matrix = matrix;
        this.currentlyPlaying = currentlyPlaying;
    }

    public void changeCurrentlyPlaying(){
        if(currentlyPlaying == 1) {
            currentlyPlaying = 2;
            return;
        }
        if(currentlyPlaying == 2)
            currentlyPlaying = 1;
    }

    public void deleteCardFromTable(int row, int column){
        // card died and has to be deleted from the table
        matrix[row][column] = null;
        for(int shift = column; shift < 4; shift++)
            matrix[row][shift] = matrix[row][shift + 1];
        matrix[row][4] = null;
    }

    public Card[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(Card[][] matrix) {
        this.matrix = matrix;
    }

    public int getCurrentlyPlaying() {
        return currentlyPlaying;
    }

    public void setCurrentlyPlaying(int currentlyPlaying) {
        this.currentlyPlaying = currentlyPlaying;
    }
}
