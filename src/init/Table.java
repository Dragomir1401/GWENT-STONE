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

    public void changeCurrentlyPlaying(){
        if(currentlyPlaying == 1) {
            currentlyPlaying = 2;
            return;
        }
        if(currentlyPlaying == 2)
            currentlyPlaying = 1;
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
