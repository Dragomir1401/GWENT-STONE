package Games;

import Cards.Card;
import fileio.CardInput;
import fileio.StartGameInput;

public class StartGame {
    private int playerOneDeckIdx;
    private int playerTwoDeckIdx;
    private int shuffleSeed;
    private Card playerOneHero;
    private Card playerTwoHero;
    private int startingPlayer;

    public StartGame(){
        this.playerOneDeckIdx = 0;
        this.playerTwoDeckIdx = 0;
        this.shuffleSeed = 0;
        this.playerOneHero = new Card();
        this.playerTwoHero = new Card();
        this.startingPlayer = 0;
    }

    public StartGame(int playerOneDeckIdx, int playerTwoDeckIdx, int shuffleSeed, Card playerOneHero,
                     Card playerTwoHero, int startingPlayer) {
        this.playerOneDeckIdx = playerOneDeckIdx;
        this.playerTwoDeckIdx = playerTwoDeckIdx;
        this.shuffleSeed = shuffleSeed;
        this.playerOneHero = playerOneHero;
        this.playerTwoHero = playerTwoHero;
        this.startingPlayer = startingPlayer;
    }


    public int getPlayerOneDeckIdx() {
        return playerOneDeckIdx;
    }

    public void setPlayerOneDeckIdx(final int playerOneDeckIdx) {
        this.playerOneDeckIdx = playerOneDeckIdx;
    }

    public int getPlayerTwoDeckIdx() {
        return playerTwoDeckIdx;
    }

    public void setPlayerTwoDeckIdx(final int playerTwoDeckIdx) {
        this.playerTwoDeckIdx = playerTwoDeckIdx;
    }

    public int getShuffleSeed() {
        return shuffleSeed;
    }

    public void setShuffleSeed(final int shuffleSeed) {
        this.shuffleSeed = shuffleSeed;
    }

    public Card getPlayerOneHero() {
        return playerOneHero;
    }

    public void setPlayerOneHero(final Card playerOneHero) {
        this.playerOneHero = playerOneHero;
    }

    public Card getPlayerTwoHero() {
        return playerTwoHero;
    }

    public void setPlayerTwoHero(final Card playerTwoHero) {
        this.playerTwoHero = playerTwoHero;
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }

    public void setStartingPlayer(final int startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    @Override
    public String toString() {
        return "StartGameInput{"
                + "playerOneDeckIdx="
                + playerOneDeckIdx
                + ", playerTwoDeckIdx="
                + playerTwoDeckIdx
                + ", shuffleSeed="
                + shuffleSeed
                +  ", playerOneHero="
                + playerOneHero
                + ", playerTwoHero="
                + playerTwoHero
                + ", startingPlayer="
                + startingPlayer
                + '}';
    }
}
