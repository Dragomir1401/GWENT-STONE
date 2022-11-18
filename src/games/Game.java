package games;

import java.util.ArrayList;

public class Game {
    private StartGame startGame;
    private ArrayList<Actions> actions;

    public Game() {
        this.startGame = new StartGame();
        this.actions = new ArrayList<Actions>();
    }

    public Game(final StartGame startGame, final  ArrayList<Actions> actions) {
        this.startGame = startGame;
        this.actions = actions;
    }

    /**
     * getter for start game
     * @return - start game class
     */
    public StartGame getStartGame() {
        return startGame;
    }

    /**
     * setter for start game
     * @param startGame  - start game class
     */
    public void setStartGame(final StartGame startGame) {
        this.startGame = startGame;
    }

    /**
     * getter for actions
     * @return - actions class
     */
    public ArrayList<Actions> getActions() {
        return actions;
    }

    /**
     * setter for actions
     * @param actions - actions class
     */
    public void setActions(final ArrayList<Actions> actions) {
        this.actions = actions;
    }
}
