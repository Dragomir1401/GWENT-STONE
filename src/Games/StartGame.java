package Games;

import Cards.*;
import fileio.CardInput;
import fileio.StartGameInput;
import init.Start;

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

    public StartGame(StartGame startGame)
    {
        this.playerOneDeckIdx = startGame.getPlayerOneDeckIdx();
        this.playerTwoDeckIdx = startGame.getPlayerTwoDeckIdx();
        this.shuffleSeed = startGame.getShuffleSeed();
        switch(startGame.getPlayerOneHero().getName())
        {
            case "Lord Royce" -> this.playerOneHero = new LordRoyce(startGame.getPlayerOneHero());
            case "Empress Thorina" -> this.playerOneHero = new EmpressThorina(startGame.getPlayerOneHero());
            case "King Mudface" -> this.playerOneHero = new KingMudface(startGame.getPlayerOneHero());
            case "General Kocioraw" -> this.playerOneHero = new GeneralKocioraw(startGame.getPlayerOneHero());
            default -> {
                this.playerOneHero = new Card();
                System.out.println("No hero with the given name!");
            }
        }
        switch(startGame.getPlayerTwoHero().getName())
        {
            case "Lord Royce" -> this.playerTwoHero = new LordRoyce(startGame.getPlayerTwoHero());
            case "Empress Thorina" -> this.playerTwoHero = new EmpressThorina(startGame.getPlayerTwoHero());
            case "King Mudface" -> this.playerTwoHero = new KingMudface(startGame.getPlayerTwoHero());
            case "General Kocioraw" -> this.playerTwoHero = new GeneralKocioraw(startGame.getPlayerTwoHero());
            default -> {
                this.playerTwoHero = new Card();
                System.out.println("No hero with the given name!");
            }
        }
        this.startingPlayer = startGame.getStartingPlayer();
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
