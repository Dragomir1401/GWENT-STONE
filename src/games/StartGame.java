package games;

import cards.Card;
import fileio.StartGameInput;

public class StartGame {
    private int playerOneDeckIdx;
    private int playerTwoDeckIdx;
    private int shuffleSeed;
    private Card playerOneHero;
    private Card playerTwoHero;
    private int startingPlayer;

    public StartGame() {
        this.playerOneDeckIdx = 0;
        this.playerTwoDeckIdx = 0;
        this.shuffleSeed = 0;
        this.playerOneHero = new Card();
        this.playerTwoHero = new Card();
        this.startingPlayer = 0;
    }

    public StartGame(final int playerOneDeckIdx, final int playerTwoDeckIdx, final int shuffleSeed,
                     final Card playerOneHero,
                     final Card playerTwoHero, final int startingPlayer) {
        this.playerOneDeckIdx = playerOneDeckIdx;
        this.playerTwoDeckIdx = playerTwoDeckIdx;
        this.shuffleSeed = shuffleSeed;
        this.playerOneHero = playerOneHero;
        this.playerTwoHero = playerTwoHero;
        this.startingPlayer = startingPlayer;
    }

    public StartGame(final StartGameInput startGameInput, final Card hero1, final Card hero2) {
        this.playerOneDeckIdx = startGameInput.getPlayerOneDeckIdx();
        this.playerTwoDeckIdx = startGameInput.getPlayerTwoDeckIdx();
        this.shuffleSeed = startGameInput.getShuffleSeed();
        this.playerOneHero = hero1;
        this.playerTwoHero = hero2;
        this.startingPlayer = startGameInput.getStartingPlayer();
    }

    public StartGame(final StartGame startGame) {
        this.playerOneDeckIdx = startGame.getPlayerOneDeckIdx();
        this.playerTwoDeckIdx = startGame.getPlayerTwoDeckIdx();
        this.shuffleSeed = startGame.getShuffleSeed();
        this.setPlayerOneHero(new Card(startGame.getPlayerOneHero()));
        this.setPlayerTwoHero(new Card(startGame.getPlayerTwoHero()));
        this.startingPlayer = startGame.getStartingPlayer();
    }

    /**
     * getter for player one deck index
     * @return - player1 deck index
     */
    public int getPlayerOneDeckIdx() {
        return playerOneDeckIdx;
    }

    /**
     * setter for player 1 deck index
     * @param playerOneDeckIdx - player1 deck index
     */
    public void setPlayerOneDeckIdx(final int playerOneDeckIdx) {
        this.playerOneDeckIdx = playerOneDeckIdx;
    }

    /**
     * getter for player2 deck index
     * @return - player2 deck index
     */
    public int getPlayerTwoDeckIdx() {
        return playerTwoDeckIdx;
    }

    /**
     * setter for player 1 deck index
     * @param playerTwoDeckIdx - player2 deck index
     */
    public void setPlayerTwoDeckIdx(final int playerTwoDeckIdx) {
        this.playerTwoDeckIdx = playerTwoDeckIdx;
    }

    /**
     * getter for shuffle speed
     * @return - shuffle speed
     */
    public int getShuffleSeed() {
        return shuffleSeed;
    }

    /**
     * setter for shuffle speed
     * @param shuffleSeed - shuffle speed
     */
    public void setShuffleSeed(final int shuffleSeed) {
        this.shuffleSeed = shuffleSeed;
    }

    /**
     * getter for player1 her
     * @return - player1 hero
     */
    public Card getPlayerOneHero() {
        return playerOneHero;
    }

    /**
     * setter for player1 hero
     * @param playerOneHero - player1 hero
     */
    public void setPlayerOneHero(final Card playerOneHero) {
        this.playerOneHero = playerOneHero;
    }

    /**
     * getter for player2 her
     * @return - player2 hero
     */
    public Card getPlayerTwoHero() {
        return playerTwoHero;
    }

    /**
     * setter for player2 hero
     * @param playerTwoHero - player2 hero
     */
    public void setPlayerTwoHero(final Card playerTwoHero) {
        this.playerTwoHero = playerTwoHero;
    }

    /**
     * getter for starting player
     * @return - starting player
     */
    public int getStartingPlayer() {
        return startingPlayer;
    }

    /**
     * setter for starting player
     * @param startingPlayer - starting player
     */
    public void setStartingPlayer(final int startingPlayer) {
        this.startingPlayer = startingPlayer;
    }
}
