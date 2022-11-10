package Games;

import fileio.ActionsInput;
import fileio.StartGameInput;

import java.util.ArrayList;

public class Game {
    private StartGame startGame;
    private ArrayList<Actions> actions;

    public Game() {
        this.startGame = new StartGame();
        this.actions = new ArrayList<Actions>();
    }

    public Game(StartGame startGame, ArrayList<Actions> actions) {
        this.startGame = startGame;
        this.actions = actions;
    }

    public StartGame getStartGame() {
        return startGame;
    }

    public void setStartGame(final StartGame startGame) {
        this.startGame = startGame;
    }

    public ArrayList<Actions> getActions() {
        return actions;
    }

    public void setActions(final ArrayList<Actions> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "GameInput{"
                +  "startGame="
                + startGame
                + ", actions="
                + actions
                + '}';
    }
}
