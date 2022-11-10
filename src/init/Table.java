package init;

import Cards.Card;

public class Table {
    private Card[][] matrix;
    private int currentlyPlaying;
    public Table(){
        this.matrix = new Card[4][5];
        this.currentlyPlaying = 0;
    }

    public Table(Card[][] matrix, int currentlyPlaying) {
        this.matrix = matrix;
        this.currentlyPlaying = currentlyPlaying;
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
