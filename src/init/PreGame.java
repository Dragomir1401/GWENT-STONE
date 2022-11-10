package init;

import Games.Game;
import Players.Player;

import java.util.ArrayList;

public class PreGame {
    private ArrayList<Game> games;
    private Player player1;
    private Player player2;

    public PreGame(ArrayList<Game> games, Player player1, Player player2) {
        this.games = new ArrayList<Game>();
        for(Game game : games)
        {
            this.games.add(game);
        }
        this.player1 = new Player(player1);
        this.player2 = new Player(player2);
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }
}
