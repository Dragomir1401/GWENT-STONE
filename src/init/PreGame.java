package init;

import players.Player;

public class PreGame {
    private Player player1;
    private Player player2;

    public PreGame(final Player player1, final Player player2) {
        this.player1 = new Player(player1);
        this.player2 = new Player(player2);
    }

    /**
     * getter for player1
     * @return - player1 info
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * setter for player1
     * @param player1 - player1 info
     */
    public void setPlayer1(final Player player1) {
        this.player1 = player1;
    }

    /**
     * getter for player2
     * @return - player2 info
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * setter for player2
     * @param player2 - player2 info
     */
    public void setPlayer2(final Player player2) {
        this.player2 = player2;
    }
}
