package init;

import cards.Card;
import cards.Disciple;
import cards.EmpressThorina;
import cards.Firestorm;
import cards.GeneralKocioraw;
import cards.HeartHound;
import cards.KingMudface;
import cards.LordRoyce;
import cards.Miraj;
import cards.TheCursedOne;
import cards.TheRipper;
import cards.Winterfell;
import games.Actions;
import games.Game;
import players.Deck;
import players.Player;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import constants.Constants;

public class PlayGame {
    /**
     * Actual play game scenario
     * @param games - the array of game information
     * @param player1Final - first player info
     * @param player2Final - second player info
     * @param output - the node where we store the output
     */
    public void playGame(final ArrayList<Game> games, final Player player1Final,
                         final Player player2Final, final ArrayNode output) {
        Player player1 = player1Final;
        Player player2 = player2Final;
        // save pre game environment
        PreGame saved = new PreGame(player1, player2);

        // the playing table gets initialised and tracks what player is currently capable
        // of placing cards
        Table table = new Table();

        // iterate through every game played
        for (Game gameIterator : games) {
            // prepare game environment
            prepareGame(gameIterator, player1, player2);

            // local variables to track rounds
            int countEndTurns = 0;
            int roundNumber = 1;

            // draw cards and set the current player
            drawCardsAndIncreaseMana(player1, player2, gameIterator, roundNumber);
            table.setCurrentlyPlaying(gameIterator.getStartGame().getStartingPlayer());

            // exit when the hero is dead
            boolean heroIsDead = false;
            for (Actions actionIterator : gameIterator.getActions()) {
                // draw cards when we reached 0 or 2 EndTurns meaning each player ended
                // the current round
                if (countEndTurns == 2) {
                    roundNumber++;
                    drawCardsAndIncreaseMana(player1, player2, gameIterator, roundNumber);
                    countEndTurns = 0;
                }
                switch (actionIterator.getCommand()) {
                    case "getPlayerDeck" -> output.add(getPlayerDeck(actionIterator, gameIterator,
                            player1, player2));
                    case "getPlayerTurn" -> output.add(getPlayerTurn(actionIterator, table));
                    case "getPlayerHero" -> output.add(showPlayerHero(gameIterator,
                            actionIterator));
                    case "getCardsInHand" -> output.add(getCardsInHand(player1, player2,
                            actionIterator));
                    case "placeCard" -> placeCard(actionIterator, table, player1, player2,
                            output);
                    case "getCardsOnTable" -> output.add(getCardsOnTable(actionIterator,
                            table));
                    case "getPlayerMana" -> output.add(getPlayerMana(actionIterator, player1,
                            player2, table));
                    case "getEnvironmentCardsInHand" -> output.add(getEnvironmentCardsInHand(
                            actionIterator, player1, player2));
                    case "useEnvironmentCard" -> errorCatchUseEnvironmentCard(actionIterator,
                            table, player1, player2, output);
                    case "getCardAtPosition" -> errorCatchGetCardAtPosition(actionIterator, table,
                            output);
                    case "getFrozenCardsOnTable" -> output.add(getFrozenCardsOnTable(table,
                            actionIterator));
                    case "cardUsesAttack" -> cardUsesAttack(table, actionIterator, output);
                    case "cardUsesAbility" -> cardUsesAbility(table, actionIterator, output);
                    case "useAttackHero" -> heroIsDead = checkHeroDeath(table, actionIterator,
                            output, gameIterator, player1, player2);
                    case "useHeroAbility" -> useHeroAbility(table, actionIterator, gameIterator,
                            output, player1, player2);
                    case "getTotalGamesPlayed" -> getTotalGamesPlayed(output, player1, player2,
                            actionIterator);
                    case "getPlayerOneWins" -> getPlayerWins(output, player1, actionIterator);
                    case "getPlayerTwoWins" -> getPlayerWins(output, player2, actionIterator);
                    case "endPlayerTurn" -> {
                        if (!heroIsDead) {
                            countEndTurns++;

                            // change what player is doing actions when a turn ends
                            table.changeCurrentlyPlaying();

                            // unfreeze all frozen current player cards and reset attack status
                            unfreezePlayerCards(table);
                            resetAttackStatus(table, gameIterator);
                        }
                    }
                    default -> output.add("No command with the given name!");
                }
            }
            // save player wins
            int player1Wins = player1.getNumberOfWins();
            int player2Wins = player2.getNumberOfWins();

            // Go back to before game start scenario
            player1 = new Player(saved.getPlayer1());
            player2 = new Player(saved.getPlayer2());

            // set player wins
            player1.setNumberOfWins(player1Wins);
            player2.setNumberOfWins(player2Wins);

            // reinitialise table
            table = new Table();
        }
    }

    /**
     * finds player number of wins
     * @param output - node output
     * @param player - player info
     * @param action - current action info
     */
    private void getPlayerWins(final ArrayNode output, final Player player, final Actions action) {
        ObjectNode resNode = JsonNodeFactory.instance.objectNode();
        resNode.put("command", action.getCommand());
        resNode.put("output", player.getNumberOfWins());
        output.add(resNode);
    }

    /**
     * finds total games played
     * @param output - output node
     * @param player1 - player1 info
     * @param player2 - player2 info
     * @param action - current action info
     */
    private void getTotalGamesPlayed(final ArrayNode output, final Player player1,
                                    final Player player2, final Actions action) {
        ObjectNode resNode = JsonNodeFactory.instance.objectNode();
        resNode.put("command", action.getCommand());
        resNode.put("output", player1.getNumberOfWins() + player2.getNumberOfWins());
        output.add(resNode);
    }

    /**
     * selects what player uses its hero
     * @param table - table info
     * @param action - action info
     * @param game - game info
     * @param output - output node
     * @param player1 - player1 info
     * @param player2 - player2 info
     */
    private void useHeroAbility(final Table table, final Actions action, final Game game,
                               final ArrayNode output, final Player player1,
                               final Player player2) {
        if (table.getCurrentlyPlaying() == 1) {
            useHeroAbilityEachPlayer(table, action, game.getStartGame().getPlayerOneHero(),
                    output, player1);
        }
        if (table.getCurrentlyPlaying() == 2) {
            useHeroAbilityEachPlayer(table, action, game.getStartGame().getPlayerTwoHero(),
                    output, player2);
        }
    }

    /**
     * catches errors in the use hero command and after that uses the hero ability
     * @param table - table info
     * @param action - action info
     * @param hero - hero card info
     * @param output - output node
     * @param player - player info
     */
    private void useHeroAbilityEachPlayer(final Table table, final Actions action, final Card hero,
                                         final ArrayNode output, final Player player) {
        // not enough mana error
        if (player.getCurrentMana() < hero.getMana()) {
            catchErrorHeroAbility(action, output, "Not enough mana to use hero's ability.");
            return;
        }

        // hero already attacked this turn error
        if (hero.getHasAttackedThisRound()) {
            catchErrorHeroAbility(action, output, "Hero has already attacked this turn.");
            return;
        }

        // selected row does not belong to the enemy error
        if (hero.getName().equals("Lord Royce") || hero.getName().equals("Empress Thorina")) {
            if (table.getCurrentlyPlaying() == 1) {
                if (action.getAffectedRow() != 0 && action.getAffectedRow() != 1) {
                    catchErrorHeroAbility(action, output,
                            "Selected row does not belong to the enemy.");
                    return;
                }
            }
            if (table.getCurrentlyPlaying() == 2) {
                if (action.getAffectedRow() != 2 && action.getAffectedRow() != Constants.ROW3) {
                    catchErrorHeroAbility(action, output,
                            "Selected row does not belong to the enemy.");
                    return;
                }
            }
        }
        // selected row does not belong to the current player.
        if (hero.getName().equals("General Kocioraw") || hero.getName().equals(
                "King Mudface")) {
            if (table.getCurrentlyPlaying() == 1) {
                if (action.getAffectedRow() != 2 && action.getAffectedRow() != Constants.ROW3) {
                    catchErrorHeroAbility(action, output,
                            "Selected row does not belong to the current player.");
                    return;
                }
            }
            if (table.getCurrentlyPlaying() == 2) {
                if (action.getAffectedRow() != 0 && action.getAffectedRow() != 1) {
                    catchErrorHeroAbility(action, output,
                            "Selected row does not belong to the current player.");
                    return;
                }
            }
        }

        // actual use of hero ability
        switch (hero.getName()) {
            case "Lord Royce" -> ((LordRoyce) hero).subZero(table, action.getAffectedRow());
            case "Empress Thorina" -> ((EmpressThorina) hero).lowBlow(table,
                    action.getAffectedRow());
            case "King Mudface" -> ((KingMudface) hero).earthBorn(table, action.getAffectedRow());
            case "General Kocioraw" -> ((GeneralKocioraw) hero).bloodThirst(table,
                    action.getAffectedRow());
            default -> output.add("No card with the given name!");
        }

        // mark hero as used for that round
        hero.setHasAttackedThisRound(true);
        // decrease player mana
        player.setCurrentMana(player.getCurrentMana() - hero.getMana());
    }

    /**
     *  error handler hero uses ability
     * @param action - action info
     * @param output - output node
     * @param error - error string to make method general
     */
    private void catchErrorHeroAbility(final Actions action, final ArrayNode output,
                                      final String error) {
        ObjectNode errorNode = JsonNodeFactory.instance.objectNode();
        errorNode.put("command", action.getCommand());
        errorNode.put("affectedRow", action.getAffectedRow());
        errorNode.put("error", error);
        output.add(errorNode);
    }

    /**
     * method which verifies if hero died after attack on him
     * @param table - table info
     * @param actions - action info
     * @param output - output node
     * @param game - current game info
     * @param player1 - player1 info
     * @param player2 - player2 info
     * @return - true if hero is dead, else false
     */
    private boolean checkHeroDeath(final Table table, final Actions actions, final ArrayNode output,
                                  final Game game, final Player player1,
                                  final Player player2) {
        int resCode = useAttackHero(table, actions, output, game);
        if (resCode == Constants.ERROR1) {
            ObjectNode endGameNode = JsonNodeFactory.instance.objectNode();
            if (table.getCurrentlyPlaying() == 1) {
                endGameNode.put("gameEnded", "Player one killed the enemy hero.");
                player1.winGame();
            }
            if (table.getCurrentlyPlaying() == 2) {
                endGameNode.put("gameEnded", "Player two killed the enemy hero.");
                player2.winGame();
            }
            output.add(endGameNode);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Constants.ERROR1 if attack ended with error, else 1 for normal behaviour
     * @param table - table info
     * @param action - action info
     * @param output - output node
     * @param game - current game info
     * @return -
     */
    private int useAttackHero(final Table table, final Actions action, final ArrayNode output,
                             final Game game) {
        // frozen card error
        Card attackerCard = table.getMatrix()
                [action.getCardAttacker().getX()][action.getCardAttacker().getY()];

        if (attackerCard == null) {
            return 1;
        }

        if (attackerCard.isFrozen()) {
            catchErrorsAttack(action, output, "Attacker card is frozen.", true);
            return 1;
        }

        // already attacked error
        if (attackerCard.getHasAttackedThisRound()) {
            catchErrorsAttack(action, output, "Attacker card has already attacked this turn.",
                    true);
            return 1;
        }

        // did not target an enemy tank if it exists on enemy rows case

        // check if enemy has tanks on his side
        if (enemyRowsHaveTanks(table)) {
            catchErrorsAttack(action, output, "Attacked card is not of type 'Tank'.",
                    true);
            return 1;
        }

        if (table.getCurrentlyPlaying() == 2) {
            Card playerOneHero = game.getStartGame().getPlayerOneHero();
            playerOneHero.setHealth(playerOneHero.getHealth() - attackerCard.getAttackDamage());

            // mark the card as used this round
            attackerCard.setHasAttackedThisRound(true);
            if (playerOneHero.getHealth() < 1) {
                return Constants.ERROR1;
            }
        }

        if (table.getCurrentlyPlaying() == 1) {
            Card playerTwoHero = game.getStartGame().getPlayerTwoHero();
            playerTwoHero.setHealth(playerTwoHero.getHealth() - attackerCard.getAttackDamage());

            // mark the card as used this round
            attackerCard.setHasAttackedThisRound(true);
            if (playerTwoHero.getHealth() < 1) {
                return Constants.ERROR1;
            }
        }

        return 1;
    }

    /**
     * returns if enemy player has tanks on the table
     * @param table - playing table
     * @return - if enemy has tanks on his rows of the table
     */
    private boolean enemyRowsHaveTanks(final Table table) {
        boolean enemyRowHasTanks = false;
        if (table.getCurrentlyPlaying() == 1) {
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
                    if (table.getMatrix()[row][col] != null) {
                        if (table.getMatrix()[row][col].cardIsTank()) {
                            return true;
                        }
                    }
                }
            }
        }

        if (table.getCurrentlyPlaying() == 2) {
            for (int row = 2; row < Constants.NUMBER_OF_ROWS; row++) {
                for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
                    if (table.getMatrix()[row][col] != null) {
                        if (table.getMatrix()[row][col].cardIsTank()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * catches use ability error else does to specific ability for the card
     * @param table - table info
     * @param action - action info
     * @param output - output info
     */
    private void cardUsesAbility(final Table table, final Actions action, final ArrayNode output) {
        // frozen card error
        Card attackerCard = table.getMatrix()
                [action.getCardAttacker().getX()][action.getCardAttacker().getY()];

        Card attackedCard = table.getMatrix()
                [action.getCardAttacked().getX()][action.getCardAttacked().getY()];

        if (attackerCard == null) {
            return;
        }
        if (attackedCard == null) {
            return;
        }

        if (attackerCard.isFrozen()) {
            catchErrorsAttack(action, output, "Attacker card is frozen.", false);
            return;
        }

        // already attacked error
        if (attackerCard.getHasAttackedThisRound()) {
            catchErrorsAttack(action, output, "Attacker card has already attacked this turn.",
                    false);
            return;
        }

        // trying to use heal on enemy players
        if (attackerCard.getName().equals("Disciple")) {
            if (table.getCurrentlyPlaying() == 1) {
                if (action.getCardAttacked().getX() != 2 && action.getCardAttacked().getX()
                        != Constants.ROW3) {
                    catchErrorsAttack(action, output,
                            "Attacked card does not belong to the current player.", false);
                    return;
                }
            }
            if (table.getCurrentlyPlaying() == 2) {
                if (action.getCardAttacked().getX() != 0 && action.getCardAttacked().getX() != 1) {
                    catchErrorsAttack(action, output,
                            "Attacked card does not belong to the current player.", false);
                    return;
                }
            }
        }

        // trying to use attack abilities on friendly minions
        if (attackerCard.getName().equals("The Ripper") || attackerCard.getName().equals("Miraj")
                || attackerCard.getName().equals("The Cursed One")) {
            if (table.getCurrentlyPlaying() == 1) {
                if (action.getCardAttacked().getX() != 0 && action.getCardAttacked().getX() != 1) {
                    catchErrorsAttack(action, output,
                            "Attacked card does not belong to the enemy.", false);
                    return;
                }
            } else if (table.getCurrentlyPlaying() == 2) {
                if (action.getCardAttacked().getX() != 2 && action.getCardAttacked().getX()
                        != Constants.ROW3) {
                    catchErrorsAttack(action, output,
                            "Attacked card does not belong to the enemy.", false);
                    return;
                }
            }

            // did not target an enemy tank if it exists on enemy rows case

            // check if enemy has tanks on his side
            if (!attackedCard.cardIsTank() && enemyRowsHaveTanks(table)) {
                catchErrorsAttack(action, output, "Attacked card is not of type 'Tank'.",
                        false);
                return;
            }
        }

        // actual use ability action
        switch (attackerCard.getName()) {
            case "The Ripper" -> ((TheRipper) attackerCard).weakKnees(table,
                    action.getCardAttacked());
            case "Miraj" -> ((Miraj) attackerCard).skyjack(table, action.getCardAttacker(),
                    action.getCardAttacked());
            case "The Cursed One" -> {
                ((TheCursedOne) attackerCard).shapeShift(table, action.getCardAttacked());
                if (attackedCard.getHealth() < 1) {
                    table.deleteCardFromTable(action.getCardAttacked().getX(),
                            action.getCardAttacked().getY());
                }
            }
            case "Disciple" -> ((Disciple) attackerCard).godsPlan(table, action.getCardAttacked());
            default -> output.add("No card with the given name!");
        }

        attackerCard.setHasAttackedThisRound(true);
    }

    /**
     * unfreezes the current player card on table
     * @param table - table info
     */
    private void unfreezePlayerCards(final Table table) {
        if (table.getCurrentlyPlaying() == 1) {
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
                    if (table.getMatrix()[row][col] != null) {
                        if (table.getMatrix()[row][col].isFrozen()) {
                            table.getMatrix()[row][col].unfreezeCard();
                        }
                    }
                }
            }
        }

        if (table.getCurrentlyPlaying() == 2) {
            for (int row = 2; row < Constants.NUMBER_OF_ROWS; row++) {
                for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
                    if (table.getMatrix()[row][col] != null) {
                        if (table.getMatrix()[row][col].isFrozen()) {
                            table.getMatrix()[row][col].unfreezeCard();
                        }
                    }
                }
            }
        }

    }

    /**
     *  catches attack command errors and if it passes attack is done
     * @param table - table info
     * @param action - action info
     * @param output - output node
     */
    private void cardUsesAttack(final Table table, final Actions action, final ArrayNode output) {
        // frozen card error
        Card attackerCard = table.getMatrix()
                [action.getCardAttacker().getX()][action.getCardAttacker().getY()];

        Card attackedCard = table.getMatrix()
                [action.getCardAttacked().getX()][action.getCardAttacked().getY()];

        if (attackerCard == null) {
            return;
        }
        if (attackedCard == null) {
            return;
        }

        if (attackerCard.isFrozen()) {
           catchErrorsAttack(action, output, "Attacker card is frozen.", false);
           return;
        }

        // already attacked error
       if (attackerCard.getHasAttackedThisRound()) {
            catchErrorsAttack(action, output, "Attacker card has already attacked this turn.",
                    false);
            return;
       }

       if (table.getCurrentlyPlaying() == 1) {
           if (action.getCardAttacked().getX() != 0 && action.getCardAttacked().getX() != 1) {
               catchErrorsAttack(action, output, "Attacked card does not belong to the enemy.",
                       false);
               return;
           }
       } else if (table.getCurrentlyPlaying() == 2) {
           if (action.getCardAttacked().getX() != 2 && action.getCardAttacked().getX()
                   != Constants.ROW3) {
               catchErrorsAttack(action, output, "Attacked card does not belong to the enemy.",
                       false);
               return;
           }
       }

        // did not target an enemy tank if it exists on enemy rows

        // check if enemy has tanks on his side
        if (!attackedCard.cardIsTank() && enemyRowsHaveTanks(table)) {
            catchErrorsAttack(action, output, "Attacked card is not of type 'Tank'.",
                    false);
            return;
        }

        // actual attack action
        attackedCard.setHealth(attackedCard.getHealth() - attackerCard.getAttackDamage());
        if (attackedCard.getHealth() < 1) {
            table.deleteCardFromTable(action.getCardAttacked().getX(),
                    action.getCardAttacked().getY());
        }
        attackerCard.setHasAttackedThisRound(true);
    }

    /**
     * catches errors in attack command
     * @param action - action input
     * @param output - output info
     * @param error - specific error string
     * @param attackedIsHero - boolean if the attacked card is hero or not
     */
    private void catchErrorsAttack(final Actions action, final ArrayNode output, final String error,
                                  final boolean attackedIsHero) {
        // general method to catch all type of attack errors
        ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
        nodeFinal.put("command", action.getCommand());

        ObjectNode attackerNode = JsonNodeFactory.instance.objectNode();
        attackerNode.put("x", action.getCardAttacker().getX());
        attackerNode.put("y", action.getCardAttacker().getY());
        nodeFinal.replace("cardAttacker", attackerNode);

        if (!attackedIsHero) {
            ObjectNode attackedNode = JsonNodeFactory.instance.objectNode();
            attackedNode.put("x", action.getCardAttacked().getX());
            attackedNode.put("y", action.getCardAttacked().getY());
            nodeFinal.replace("cardAttacked", attackedNode);
        }

        nodeFinal.put("error", error);
        output.add(nodeFinal);
    }

    /**
     * shows all frozen cards on table
     * @param table - table info
     * @param action - cation info
     * @return - output node
     */
    private ObjectNode getFrozenCardsOnTable(final Table table, final Actions action) {
        ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
        nodeFinal.put("command", action.getCommand());

        ArrayNode frozenCardsNode = JsonNodeFactory.instance.arrayNode();
        for (int row = 0; row < Constants.NUMBER_OF_ROWS; row++) {
            for (int col = 0; col < Constants.NUMBER_OF_COLUMNS; col++) {
                if (table.getMatrix()[row][col] != null) {
                    if (table.getMatrix()[row][col].cardIsFrozen()) {
                        frozenCardsNode.add(cardToObjectNode(table.getMatrix()[row][col]));
                    }
                }
            }
        }

        nodeFinal.replace("output", frozenCardsNode);
        return nodeFinal;
    }

    /**
     * tries error cases for getting ad card at a certain position
     * @param action - action info
     * @param table - table info
     * @param output - output node
     */
    private void errorCatchGetCardAtPosition(final Actions action, final Table table,
                                            final ArrayNode output) {
        ObjectNode res = getCardAtPosition(action, table);
        if (res != null) {
            ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
            nodeFinal.put("command", action.getCommand());
            nodeFinal.replace("output", res);
            nodeFinal.put("x", action.getX());
            nodeFinal.put("y", action.getY());
            output.add(nodeFinal);
        } else {
            ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
            nodeFinal.put("command", action.getCommand());
            nodeFinal.put("output", "No card available at that position.");
            nodeFinal.put("x", action.getX());
            nodeFinal.put("y", action.getY());
            output.add(nodeFinal);
        }
    }

    /**
     * finds the card at given coordinates
     * @param action - action info
     * @param table - table info
     * @return - output node with the card at the given position
     */
    private ObjectNode getCardAtPosition(final Actions action, final Table table) {
        if (table.getMatrix()[action.getX()][action.getY()] == null) {
            return null;
        }
        return cardToObjectNode(table.getMatrix()[action.getX()][action.getY()]);
    }

    /**
     * tries error for catching errors at use of environment cards
     * @param action - action info
     * @param table - table info
     * @param player1 - player1 info
     * @param player2 - player2 info
     * @param output - output node
     */
    private void errorCatchUseEnvironmentCard(final Actions action, final Table table,
                                             final Player player1, final Player player2,
                                             final ArrayNode output) {
        int resultCode = useEnvironmentCard(action, table, player1, player2);
        if (resultCode < 0) {
            ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
            nodeFinal.put("affectedRow", action.getAffectedRow());
            nodeFinal.put("command", action.getCommand());
            if (resultCode == Constants.ERROR1) {
                nodeFinal.put("error", "Cannot steal enemy card since the player's row is full.");
            }
            if (resultCode == Constants.ERROR2) {
                nodeFinal.put("error", "Chosen card is not of type environment.");
            }
            if (resultCode == Constants.ERROR3) {
                nodeFinal.put("error", "Not enough mana to use environment card.");
            }
            if (resultCode == -Constants.NUMBER_OF_ROWS) {
                nodeFinal.put("error", "Chosen row does not belong to the enemy.");
            }
            nodeFinal.put("handIdx", action.getHandIdx());
            output.add(nodeFinal);
        }
    }

    /**
     * sets attack status(if card has attacked) back to false
     * @param table - table info
     * @param game - game info
     */
    private void resetAttackStatus(final Table table, final Game game) {
        for (int row = 0; row < Constants.NUMBER_OF_ROWS; row++) {
            for (int column = 0; column < Constants.NUMBER_OF_COLUMNS; column++) {
                if (table.getMatrix()[row][column] != null) {
                    if (table.getMatrix()[row][column].getHasAttackedThisRound()) {
                        table.getMatrix()[row][column].setHasAttackedThisRound(false);
                    }
                }
            }
        }
        game.getStartGame().getPlayerOneHero().setHasAttackedThisRound(false);
        game.getStartGame().getPlayerTwoHero().setHasAttackedThisRound(false);
    }

    /**
     * error cases for environment card use
     * @param action - action info
     * @param table - table info
     * @param player1 - player1 info
     * @param player2 - player2 info
     * @return - specific negative value for each error
     */
    private int  useEnvironmentCard(final Actions action, final Table table, final Player player1,
                                   final Player player2) {
        int resultCode = 0;
        if (table.getCurrentlyPlaying() == 1) {
            // not the environment type card
            if (!player1.getHand().get(action.getHandIdx()).cardIsEnvironmentCard()) {
                return Constants.ERROR2;
            }
            // not enough mana to place the card
            if (player1.getCurrentMana() < player1.getHand().get(action.getHandIdx()).getMana()) {
                return Constants.ERROR3;
            }
            // chosen row does not belong to the enemy.
            if (action.getAffectedRow() != 0 && action.getAffectedRow() != 1) {
                return -Constants.NUMBER_OF_ROWS;
            }
            resultCode = useEnvironmentCardEachPlayer(action, table, player1);
        }
        if (table.getCurrentlyPlaying() == 2) {
            // not the environment type card
            if (!player2.getHand().get(action.getHandIdx()).cardIsEnvironmentCard()) {
                return Constants.ERROR2;
            }
            // not enough mana to place the card
            if (player2.getCurrentMana() < player2.getHand().get(action.getHandIdx()).getMana()) {
                return Constants.ERROR3;
            }
            // chosen row does not belong to the enemy.
            if (action.getAffectedRow() != 2 && action.getAffectedRow() != Constants.ROW3) {
                return -Constants.NUMBER_OF_ROWS;
            }
            resultCode = useEnvironmentCardEachPlayer(action, table, player2);
        }
        if (resultCode > 0) {
            // delete cards which are dead from table
            for (int column = 0; column < Constants.NUMBER_OF_COLUMNS; column++) {
                if (table.getMatrix()[action.getAffectedRow()][column] != null) {
                    if (table.getMatrix()[action.getAffectedRow()][column].getHealth() <= 0) {
                        table.deleteCardFromTable(action.getAffectedRow(), column);
                        column--;
                    }
                }
            }
            // eliminate used environment card from player hand and decrease mana
            if (table.getCurrentlyPlaying() == 1) {
                player1.setCurrentMana(player1.getCurrentMana() - player1.getHand().get(
                        action.getHandIdx()).getMana());
                player1.getHand().remove(action.getHandIdx());
            }
            if (table.getCurrentlyPlaying() == 2) {
                player2.setCurrentMana(player2.getCurrentMana() - player2.getHand().get(
                        action.getHandIdx()).getMana());
                player2.getHand().remove(action.getHandIdx());
            }
        }
        return resultCode;
    }

    /**
     * actual implementation of use of environment card
     * @param action - action info
     * @param table - table info
     * @param player - player info
     * @return - negative number for specific error, 1 for normal behaviour
     */
    private int useEnvironmentCardEachPlayer(final Actions action, final Table table,
                                            final Player player) {
        // check card type and implement special methods
        Card card = player.getHand().get(action.getHandIdx());
        // not enough mana to use environment card.
        if (player.getCurrentMana() < card.getMana()) {
            return Constants.ERROR3;
        }
        switch (card.getName()) {
            case "Firestorm":
                ((Firestorm) card).deploy(table, action.getAffectedRow());
                break;
            case "Winterfell":
                ((Winterfell) card).deploy(table, action.getAffectedRow());
                break;
            case "Heart Hound":
                int check = ((HeartHound) card).deploy(table, action.getAffectedRow());
                // error case for not being able to steal a card
                if (check == Constants.ERROR1) {
                    // cannot steal enemy card since the player's row is full.
                    return Constants.ERROR1;
                }
                break;
            default:
                // chosen card is not of type environment.
                return Constants.ERROR2;
        }
        return 1;
    }

    /**
     * shows all environment cards in hand of a player
     * @param action - action info
     * @param player1 - player info
     * @param player2 - player info
     * @return - output node
     */
    private ObjectNode getEnvironmentCardsInHand(final Actions action, final Player player1,
                                                final Player player2) {
        ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
        nodeFinal.put("command", action.getCommand());

        if (action.getPlayerIdx() == 1) {
            ArrayNode handNode = environmentCardsInHandToObjectNode(player1.getHand());
            nodeFinal.replace("output", handNode);
        }
        if (action.getPlayerIdx() == 2) {
            ArrayNode handNode = environmentCardsInHandToObjectNode(player2.getHand());
            nodeFinal.replace("output", handNode);
        }
        nodeFinal.put("playerIdx", action.getPlayerIdx());

        return nodeFinal;
    }

    /**
     * transforms environment cards in output nodes
     * @param hand - list of cards in hand of the player
     * @return - output array node
     */
    private ArrayNode environmentCardsInHandToObjectNode(final ArrayList<Card> hand) {
        ArrayNode handNode = JsonNodeFactory.instance.arrayNode();
        for (Card card : hand) {
            if (card.cardIsEnvironmentCard()) {
                ObjectNode cardNode = cardToObjectNode(card);
                handNode.add(cardNode);
            }
        }
        return handNode;
    }

    /**
     * gets output for what cards are on table
     * @param action - action info
     * @param table - table info
     * @return - output node
     */
    private ObjectNode getCardsOnTable(final Actions action, final Table table) {
        ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
        nodeFinal.put("command", action.getCommand());

        ArrayNode rowsNode = JsonNodeFactory.instance.arrayNode();
        for (int row = 0; row < Constants.NUMBER_OF_ROWS; row++) {
            ArrayNode rowNode = JsonNodeFactory.instance.arrayNode();
            for (int column = 0; column < Constants.NUMBER_OF_COLUMNS; column++) {
                if (table.getMatrix()[row][column] != null) {
                    ObjectNode cardNode = cardToObjectNode(table.getMatrix()[row][column]);
                    rowNode.add(cardNode);
                }
            }
            rowsNode.add(rowNode);
        }

        nodeFinal.replace("output", rowsNode);
        return nodeFinal;
    }

    /**
     * returns current player mana
     * @param action - action info
     * @param player1 - player1 info
     * @param player2 - player2 info
     * @param table - table info
     * @return - output node for command pattern
     */
    private ObjectNode getPlayerMana(final Actions action, final Player player1,
                                    final Player player2, final Table table) {
        // Create output node
        ObjectNode nodeOutput = JsonNodeFactory.instance.objectNode();
        nodeOutput.put("command", action.getCommand());
        if (action.getPlayerIdx() == 1) {
            nodeOutput.put("output", player1.getCurrentMana());
        }
        if (action.getPlayerIdx() == 2) {
            nodeOutput.put("output", player2.getCurrentMana());
        }
        nodeOutput.put("playerIdx", action.getPlayerIdx());
        return nodeOutput;
    }

    /**
     * separate card placement per player
     * @param action - action info
     * @param table - table info
     * @param player1 - player1 info
     * @param player2 - player2 info
     * @param output - output node
     */
    private void placeCard(final Actions action, final Table table, final Player player1,
                          final Player player2, final ArrayNode output) {
        if (table.getCurrentlyPlaying() == 1) {
            if (placeCardIsError(action, player1, output)) {
                return;
            }
            // place card
            placeCardGeneral(action, table, player1, output);
        }

        if (table.getCurrentlyPlaying() == 2) {
            if (placeCardIsError(action, player2, output)) {
                return;
            }
            // place card
            placeCardGeneral(action, table, player2, output);
        }
    }

    /**
     * checks error for card placing
     * @param action - action info
     * @param player - player info
     * @param output - output info
     * @return - yes for error, no for normal behaviour
     */
    private boolean placeCardIsError(final Actions action, final Player player,
                                    final ArrayNode output) {
        if (player.getHand().get(action.getHandIdx()).cardIsEnvironmentCard()) {
            output.add(createPlaceCardErrorFormat(action,
                    "Cannot place environment card on table."));
            return true;
        }
        if (player.getHand().get(action.getHandIdx()).getMana() > player.getCurrentMana()) {
            output.add(createPlaceCardErrorFormat(action,
                    "Not enough mana to place card on table."));
            return true;
        }
        return false;
    }

    /**
     * checks what row type of card is
     * @param card - checked card
     * @return - 1 -> firstRow   2 -> secondRow
     */
    private int isFirstRowCardOrSecondRowCard(final Card card) {
        // 1 -> firstRow   2 -> secondRow
        if (card.getName().equals("The Ripper") || card.getName().equals("Miraj")
                || card.getName().equals("Goliath") || card.getName().equals("Warden")) {
            return 2;
        }
        if (card.getName().equals("Sentinel") || card.getName().equals("Berserker")
                || card.getName().equals("The Cursed One") || card.getName().equals("Disciple")) {
            return 1;
        }
        return 0;
    }

    /**
     * actual card placing action
     * @param action - action info
     * @param table - table info
     * @param player - player info
     * @param output - output node
     */
    private void placeCardGeneral(final Actions action, final Table table, final Player player,
                                  final ArrayNode output) {
        boolean placedCard = false;
        // save mana cost of the card in case we need to decrease mana player
        // after bypassing all error cases
        int manaCost = player.getHand().get(action.getHandIdx()).getMana();
        for (int i = 0; i < Constants.NUMBER_OF_COLUMNS; i++) {
            // Place on first row
            if (isFirstRowCardOrSecondRowCard(player.getHand().get(action.getHandIdx())) == 1) {
                if (table.getCurrentlyPlaying() == 1) {
                    if (table.getMatrix()[Constants.ROW3][i] == null) {
                        table.getMatrix()[Constants.ROW3][i] = player.getHand().
                                get(action.getHandIdx());
                        player.getHand().remove(action.getHandIdx());
                        placedCard = true;
                        break;
                    }
                }
                if (table.getCurrentlyPlaying() == 2) {
                    if (table.getMatrix()[0][i] == null) {
                        table.getMatrix()[0][i] = player.getHand().get(action.getHandIdx());
                        player.getHand().remove(action.getHandIdx());
                        placedCard = true;
                        break;
                    }
                }
            }
            // Place on second row
            if (isFirstRowCardOrSecondRowCard(player.getHand().get(action.getHandIdx())) == 2) {
                if (table.getCurrentlyPlaying() == 1) {
                    if (table.getMatrix()[2][i] == null) {
                        table.getMatrix()[2][i] = player.getHand().get(action.getHandIdx());
                        player.getHand().remove(action.getHandIdx());
                        placedCard = true;
                        break;
                    }
                }
                if (table.getCurrentlyPlaying() == 2) {
                    if (table.getMatrix()[1][i] == null) {
                        table.getMatrix()[1][i] = player.getHand().get(action.getHandIdx());
                        player.getHand().remove(action.getHandIdx());
                        placedCard = true;
                        break;
                    }
                }
            }
        }
        if (!placedCard) {
            output.add(createPlaceCardErrorFormat(action,
                    "Cannot place card on table since row is full."));
        } else {
            // decrease mana by the cost of the card because we bypassed all error cases
            player.setCurrentMana(player.getCurrentMana() - manaCost);
        }
    }

    /**
     * formats error for card placement
     * @param action - action info
     * @param error - specific error
     * @return - output node
     */
    private ObjectNode createPlaceCardErrorFormat(final Actions action, final String error) {
        ObjectNode nodeOutput = JsonNodeFactory.instance.objectNode();
        nodeOutput.put("command", action.getCommand());
        nodeOutput.put("handIdx", action.getHandIdx());
        nodeOutput.put("error", error);
        return nodeOutput;
    }

    /**
     * show card in hand of the given player
     * @param player1 - player1 info
     * @param player2 - player2 info
     * @param action - action info
     * @return - output node
     */
    private ObjectNode getCardsInHand(final Player player1, final Player player2,
                                     final Actions action) {
        ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
        nodeFinal.put("command", action.getCommand());
        nodeFinal.put("playerIdx", action.getPlayerIdx());

        if (action.getPlayerIdx() == 1) {
            ArrayNode handNode = handToObjectNode(player1.getHand());
            nodeFinal.replace("output", handNode);
        }
        if (action.getPlayerIdx() == 2) {
            ArrayNode deckNode = handToObjectNode(player2.getHand());
            nodeFinal.replace("output", deckNode);
        }

        return nodeFinal;
    }

    /**
     * sets player hands and shuffles decks
     * @param game - game info
     * @param player1 - player1 info
     * @param player2 - player2 info
     */
    private void prepareGame(final Game game, final Player player1,
                            final Player player2) {
        // Shuffle the decks chosen by each player
        Collections.shuffle(player1.getDecks().get(game.getStartGame().getPlayerOneDeckIdx()).
                        getCards(), new Random(game.getStartGame().getShuffleSeed()));
        Collections.shuffle(player2.getDecks().get(game.getStartGame().getPlayerTwoDeckIdx()).
                        getCards(), new Random(game.getStartGame().getShuffleSeed()));

        // Create player hands
        player1.setHand(new ArrayList<>());
        player2.setHand(new ArrayList<>());
    }

    /**
     * transform card in output node
     * @param card - chosen card
     * @return - output node
     */
    private ObjectNode cardToObjectNode(final Card card) {
        ObjectNode nodeCard = JsonNodeFactory.instance.objectNode();

        nodeCard.put("mana", card.getMana());
        // For environment cards don't put health and attack damage
        if (!card.cardIsEnvironmentCard()) {
            nodeCard.put("attackDamage", card.getAttackDamage());
            nodeCard.put("health", card.getHealth());
        }
        nodeCard.put("description", card.getDescription());

        ArrayNode nodeColors = JsonNodeFactory.instance.arrayNode();
        for (String color : card.getColors()) {
            nodeColors.add(color);
        }

        nodeCard.replace("colors", nodeColors);
        nodeCard.put("name", card.getName());

        return nodeCard;
    }

    /**
     * transforms deck in output node
     * @param deck - chosen deck
     * @return - output node
     */
    private ArrayNode deckToObjectNode(final Deck deck) {
        ArrayNode deckNode = JsonNodeFactory.instance.arrayNode();
        for (Card card : deck.getCards()) {
            ObjectNode cardNode = cardToObjectNode(card);
            deckNode.add(cardNode);
        }
        return deckNode;
    }

    /**
     * creates output node from player hand
     * @param hand - chosen player hand
     * @return - output array node
     */
    private ArrayNode handToObjectNode(final ArrayList<Card> hand) {
        ArrayNode handNode = JsonNodeFactory.instance.arrayNode();
        for (Card card : hand) {
            ObjectNode cardNode = cardToObjectNode(card);
            handNode.add(cardNode);
        }
        return handNode;
    }

    /**
     * creates output format for showing given player deck
     * @param action - action info
     * @param game - game info
     * @param player - player info
     * @param playerIndex - what player deck is targeted
     * @return - output node
     */
    private ObjectNode showPlayerDeck(final Actions action, final Game game,
                                     final Player player, final int playerIndex) {
        ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();

        nodeFinal.put("command", action.getCommand());
        nodeFinal.put("playerIdx", playerIndex);

        if (playerIndex == 1) {
            ArrayNode deckNode = deckToObjectNode(player.getDecks().get(game.getStartGame().
                    getPlayerOneDeckIdx()));
            nodeFinal.replace("output", deckNode);
        }
        if (playerIndex == 2) {
            ArrayNode deckNode = deckToObjectNode(player.getDecks().get(game.getStartGame().
                    getPlayerTwoDeckIdx()));
            nodeFinal.replace("output", deckNode);
        }

        return nodeFinal;
    }

    /**
     * separates show deck for each player
     * @param action - action info
     * @param game - game info
     * @param player1 - player1 info
     * @param player2 - player2 info
     * @return - output node
     */
    private ObjectNode getPlayerDeck(final Actions action, final Game game,
                                    final Player player1, final Player player2) {
        ObjectNode nodeOutput = JsonNodeFactory.instance.objectNode();
        if (action.getPlayerIdx() == 1) {
            nodeOutput = showPlayerDeck(action, game, player1, 1);
        }
        if (action.getPlayerIdx() == 2) {
            nodeOutput = showPlayerDeck(action, game, player2, 2);
        }

        return nodeOutput;
    }

    /**
     * show what player turn is
     * @param action -action info
     * @param table - table info
     * @return - output node
     */
    private ObjectNode getPlayerTurn(final Actions action, final Table table) {
        // Create output node
        ObjectNode nodeOutput = JsonNodeFactory.instance.objectNode();
        nodeOutput.put("command", action.getCommand());
        nodeOutput.put("output", table.getCurrentlyPlaying());
        return nodeOutput;
    }

    /**
     * each player draws cards and mana is increased at beginning of round
     * @param player1 - player1 info
     * @param player2 - player2 info
     * @param game - game info
     * @param manaIncrease - gradual increased mana increase
     */
    private void drawCardsAndIncreaseMana(final Player player1, final Player player2,
                                         final Game game, final int manaIncrease) {
        // Draw first card from the deck for each player and remove it from the deck
        if (!player1.getDecks().get(game.getStartGame().getPlayerOneDeckIdx()).getCards().
                isEmpty()) {
            player1.getHand().add(player1.getDecks().get(game.getStartGame().
                    getPlayerOneDeckIdx()).getCards().get(0));
            player1.getDecks().get(game.getStartGame().getPlayerOneDeckIdx()).
                    getCards().remove(0);
        }

        if (!player2.getDecks().get(game.getStartGame().getPlayerTwoDeckIdx()).getCards().
                isEmpty()) {
                player2.getHand().add(player2.getDecks().get(game.getStartGame().
                        getPlayerTwoDeckIdx()).getCards().get(0));
                player2.getDecks().get(game.getStartGame().getPlayerTwoDeckIdx()).
                        getCards().remove(0);
            }
        // increase mana at start of the round when players draw cards
        int localManaIncrease = manaIncrease;
        if (manaIncrease > Constants.MAX_MANA_INCREASE) {
            localManaIncrease = Constants.MAX_MANA_INCREASE;
        }
        player1.setCurrentMana(player1.getCurrentMana() + localManaIncrease);
        player2.setCurrentMana(player2.getCurrentMana() + localManaIncrease);
    }

    /**
     * transforms hero card in output node
     * @param card - chosen card
     * @return - output node
     */
    private ObjectNode heroCardToObjectNode(final Card card) {
        ObjectNode nodeCard = JsonNodeFactory.instance.objectNode();

        nodeCard.put("mana", card.getMana());
        nodeCard.put("description", card.getDescription());

        ArrayNode nodeColors = JsonNodeFactory.instance.arrayNode();
        for (String color : card.getColors()) {
            nodeColors.add(color);
        }

        nodeCard.replace("colors", nodeColors);
        nodeCard.put("name", card.getName());
        nodeCard.put("health", card.getHealth());

        return nodeCard;
    }

    /**
     * transforms hero in output node
     * @param game - game info
     * @param action - action info
     * @return - output node
     */
    private ObjectNode showPlayerHero(final Game game, final Actions action) {
        ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();

        nodeFinal.put("command", action.getCommand());
        nodeFinal.put("playerIdx", action.getPlayerIdx());

        ObjectNode heroNode = JsonNodeFactory.instance.objectNode();
        if (action.getPlayerIdx() == 1) {
            heroNode = heroCardToObjectNode(game.getStartGame().getPlayerOneHero());
        }
        if (action.getPlayerIdx() == 2) {
            heroNode = heroCardToObjectNode(game.getStartGame().getPlayerTwoHero());
        }

        nodeFinal.replace("output", heroNode);
        return nodeFinal;
    }
}
