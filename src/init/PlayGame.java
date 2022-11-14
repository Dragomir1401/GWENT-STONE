package init;

import Cards.*;
import Games.Actions;
import Games.Game;
import Players.Deck;
import Players.Player;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.naming.ldap.PagedResultsControl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class PlayGame {
    public void playGame(ArrayList<Game> games, Player player1, Player player2, ArrayNode output) {
        /*
          actual play game scenario
         */

        // iterate through every game played
        for (Game gameIterator : games) {
            // prepare game environment
            prepareGame(gameIterator, player1, player2);
            // save pre game environment
            PreGame saved = new PreGame(games, player1, player2);

            /*
              Local variables to track rounds
             */
            int countEndTurns = 0;
            int roundNumber = 1;
            // the playing table gets initialised and tracks what player is currently capable of placing cards
            Table table = new Table();

            // Draw cards and set the current player
            drawCardsAndIncreaseMana(player1, player2, gameIterator, roundNumber);
            table.setCurrentlyPlaying(gameIterator.getStartGame().getStartingPlayer());
            // exit when the hero is dead
            boolean heroIsDead = false;
            for (Actions actionIterator : gameIterator.getActions()) {
                /* Draw cards when we reached 0 or 2 EndTurns meaning each player ended the current round */
                if(countEndTurns == 2) {
                    roundNumber++;
                    drawCardsAndIncreaseMana(player1, player2, gameIterator, roundNumber);
                    countEndTurns = 0;
                }
                debug(actionIterator, table, player1, player2);

                switch (actionIterator.getCommand()) {
                    case "getPlayerDeck" -> output.add(getPlayerDeck(actionIterator, gameIterator, player1, player2));
                    case "getPlayerTurn" -> output.add(getPlayerTurn(actionIterator, table));
                    case "getPlayerHero" -> output.add(showPlayerHero(gameIterator, actionIterator));
                    case "getCardsInHand" -> output.add(getCardsInHand(player1, player2, actionIterator));
                    case "placeCard" -> placeCard(actionIterator, table, player1, player2, output);
                    case "getCardsOnTable" -> output.add(getCardsOnTable(actionIterator, table));
                    case "getPlayerMana" -> output.add(getPlayerMana(actionIterator, player1, player2, table));
                    case "getEnvironmentCardsInHand" -> output.add(getEnvironmentCardsInHand(actionIterator, player1, player2));
                    case "useEnvironmentCard" -> errorCatchUseEnvironmentCard(actionIterator, table, player1, player2, output);
                    case "getCardAtPosition" -> errorCatchGetCardAtPosition(actionIterator, table, output);
                    case "getFrozenCardsOnTable" -> output.add(getFrozenCardsOnTable(table, actionIterator));
                    case "cardUsesAttack" -> cardUsesAttack(table, actionIterator, output);
                    case "cardUsesAbility" -> cardUsesAbility(table, actionIterator, output);
                    case "useAttackHero" -> heroIsDead = checkHeroDeath(table, actionIterator, output, gameIterator);
                    case "useHeroAbility" -> useHeroAbility(table, actionIterator, gameIterator, output, player1, player2);
                    case "endPlayerTurn" -> {
                        if (!heroIsDead) {
                            countEndTurns++;
                            // change what player is doing actions when a turn ends
                            table.changeCurrentlyPlaying();
                            // unfreeze all frozen current player cards and reset attack status
                            unfreezePlayerCards(table);
                            resetAttackStatus(table);
                        }
                    }
                    default -> output.add("No command with the given name!");
                }
            }
            /* Go back to before game start scenario */
            games = saved.getGames();
            player1 = saved.getPlayer1();
            player2 = saved.getPlayer2();
        }
    }
    public void useHeroAbility(Table table, Actions action, Game game,ArrayNode output, Player player1, Player player2)
    {
        if(table.getCurrentlyPlaying() == 1)
            useHeroAbilityEachPlayer(table, action, game.getStartGame().getPlayerOneHero(), output, player1);
        if(table.getCurrentlyPlaying() == 2)
            useHeroAbilityEachPlayer(table, action, game.getStartGame().getPlayerTwoHero(), output, player2);

    }
    public void useHeroAbilityEachPlayer(Table table, Actions action, Card hero, ArrayNode output, Player player) {
        // not enough mana error
        if(player.getCurrentMana() < hero.getMana())
        {
            catchErrorHeroAbility(action, output, "Not enough mana to use hero's ability.");
            return;
        }

        // hero already attacked this turn error
        if(hero.getHasAttackedThisRound())
        {
            catchErrorHeroAbility(action, output, "Hero has already attacked this turn.");
            return;
        }

        // selected row does not belong to the enemy error
        if(hero.getName().equals("Lord Royce") || hero.getName().equals("Empress Thorina"))
        {
            if(table.getCurrentlyPlaying() == 1)
                if(action.getAffectedRow() != 0 || action.getAffectedRow() != 1)
                {
                    catchErrorHeroAbility(action, output, "Selected row does not belong to the enemy.");
                    return;
                }
            if(table.getCurrentlyPlaying() == 2)
                if(action.getAffectedRow() != 2 || action.getAffectedRow() != 3)
                {
                    catchErrorHeroAbility(action, output, "Selected row does not belong to the enemy.");
                    return;
                }
        }
        // selected row does not belong to the current player.
        if(hero.getName().equals("General Kocioraw") || hero.getName().equals("King Mudface"))
        {
            if(table.getCurrentlyPlaying() == 1)
                if(action.getAffectedRow() != 2 || action.getAffectedRow() != 3)
                {
                    catchErrorHeroAbility(action, output, "Selected row does not belong to the current player.");
                    return;
                }
            if(table.getCurrentlyPlaying() == 2)
                if(action.getAffectedRow() != 0 || action.getAffectedRow() != 1)
                {
                    catchErrorHeroAbility(action, output, "Selected row does not belong to the current player.");
                    return;
                }
        }

        // actual use of hero ability
        switch (hero.getName()){
            case "Lord Royce" -> ((LordRoyce)hero).subZero(table, action.getAffectedRow());
            case "Empress Thorina" -> ((EmpressThorina)hero).lowBlow(table, action.getAffectedRow());
            case "King Mudface" -> ((KingMudface)hero).earthBorn(table, action.getAffectedRow());
            case "General Kocioraw" -> ((GeneralKocioraw)hero).bloodThirst(table, action.getAffectedRow());
            default -> output.add("No card with the given name!");
        }

        // mark hero as used for that round
        hero.setHasAttackedThisRound(true);
    }
    public void catchErrorHeroAbility(Actions action, ArrayNode output, String error){
        ObjectNode errorNode = JsonNodeFactory.instance.objectNode();
        errorNode.put("command", action.getCommand());
        errorNode.put("affectedRow", action.getAffectedRow());
        errorNode.put("error", error);
        output.add(errorNode);
    }
    public boolean checkHeroDeath(Table table, Actions actions, ArrayNode output, Game game)
    {
        int resCode = useAttackHero(table, actions, output, game);
        if(resCode == -1)
        {
            ObjectNode endGameNode = JsonNodeFactory.instance.objectNode();
            if(table.getCurrentlyPlaying() == 1)
                 endGameNode.put("gameEnded", "Player one killed the enemy hero.");
            if(table.getCurrentlyPlaying() == 2)
                endGameNode.put("gameEnded", "Player two killed the enemy hero.");
            output.add(endGameNode);
            return true;
        }
        else return false;
    }
    public int useAttackHero(Table table, Actions action, ArrayNode output, Game game){
        // frozen card error
        Card attackerCard = table.getMatrix()[action.getCardAttacker().getX()][action.getCardAttacker().getY()];

        if(attackerCard == null)
            return 1;

        if(attackerCard.isFrozen()) {
            catchErrorsAttack(action, output, "Attacker card is frozen.", true);
            return 1;
        }

        // already attacked error
        if(attackerCard.getHasAttackedThisRound())
        {
            catchErrorsAttack(action, output, "Attacker card has already attacked this turn.", true);
            return 1;
        }

        // did not target an enemy tank if it exists on enemy rows case

        // check if enemy has tanks on his side
        boolean enemyRowHasTanks = false;
        if (table.getCurrentlyPlaying() == 1)
            for (int row = 0; row < 2; row++)
                for (int col = 0; col < 5; col++)
                    if (table.getMatrix()[row][col] != null)
                        if (table.getMatrix()[row][col].cardIsTank())
                            enemyRowHasTanks = true;
        if (table.getCurrentlyPlaying() == 2)
            for (int row = 2; row < 4; row++)
                for (int col = 0; col < 5; col++)
                    if (table.getMatrix()[row][col] != null)
                        if (table.getMatrix()[row][col].cardIsTank())
                            enemyRowHasTanks = true;
        if (enemyRowHasTanks) {
            catchErrorsAttack(action, output, "Attacked card is not of type 'Tank'.", true);
            return 1;
        }

        if(table.getCurrentlyPlaying() == 2) {
            Card playerOneHero = game.getStartGame().getPlayerOneHero();
            playerOneHero.setHealth(playerOneHero.getHealth() - attackerCard.getAttackDamage());

            // mark the card as used this round
            attackerCard.setHasAttackedThisRound(true);
            if(playerOneHero.getHealth() < 1)
                return -1;
        }

        if(table.getCurrentlyPlaying() == 1) {
            Card playerTwoHero = game.getStartGame().getPlayerTwoHero();
            playerTwoHero.setHealth(playerTwoHero.getHealth() - attackerCard.getAttackDamage());

            // mark the card as used this round
            attackerCard.setHasAttackedThisRound(true);
            if(playerTwoHero.getHealth() < 1)
                return -1;
        }

        return 1;
    }
    public void cardUsesAbility(Table table, Actions action, ArrayNode output)
    {
        // frozen card error
        Card attackerCard = table.getMatrix()[action.getCardAttacker().getX()][action.getCardAttacker().getY()];

        Card attackedCard = table.getMatrix()[action.getCardAttacked().getX()][action.getCardAttacked().getY()];

        if(attackerCard == null)
            return;
        if(attackedCard == null)
            return;

        if(attackerCard.isFrozen()) {
            catchErrorsAttack(action, output, "Attacker card is frozen.", false);
            return;
        }

        // already attacked error
        if(attackerCard.getHasAttackedThisRound())
        {
            catchErrorsAttack(action, output, "Attacker card has already attacked this turn.", false);
            return;
        }

        // trying to use heal on enemy players
        if(attackerCard.getName().equals("Disciple"))
        {
            if(table.getCurrentlyPlaying() == 1)
                if(action.getCardAttacked().getX() != 2 && action.getCardAttacked().getX() != 3) {
                    catchErrorsAttack(action, output, "Attacked card does not belong to the current player.", false);
                    return;
                }
            if(table.getCurrentlyPlaying() == 2)
                if(action.getCardAttacked().getX() != 0 && action.getCardAttacked().getX() != 1) {
                    catchErrorsAttack(action, output, "Attacked card does not belong to the current player.", false);
                    return;
                }
        }

        // trying to use attack abilities on friendly minions
        if(attackerCard.getName().equals("The Ripper") || attackerCard.getName().equals("Miraj") ||
                attackerCard.getName().equals("The Cursed One")) {
            if (table.getCurrentlyPlaying() == 1) {
                if (action.getCardAttacked().getX() != 0 && action.getCardAttacked().getX() != 1) {
                    catchErrorsAttack(action, output, "Attacked card does not belong to the enemy.", false);
                    return;
                }
            } else if (table.getCurrentlyPlaying() == 2) {
                if (action.getCardAttacked().getX() != 2 && action.getCardAttacked().getX() != 3) {
                    catchErrorsAttack(action, output, "Attacked card does not belong to the enemy.", false);
                    return;
                }
            }

            // did not target an enemy tank if it exists on enemy rows case

            // check if enemy has tanks on his side
            boolean enemyRowHasTanks = false;

            if (table.getCurrentlyPlaying() == 1)
                for (int row = 0; row < 2; row++)
                    for (int col = 0; col < 5; col++)
                        if (table.getMatrix()[row][col] != null)
                            if (table.getMatrix()[row][col].cardIsTank())
                                enemyRowHasTanks = true;


            if (table.getCurrentlyPlaying() == 2)
                for (int row = 2; row < 4; row++)
                    for (int col = 0; col < 5; col++)
                        if (table.getMatrix()[row][col] != null)
                            if (table.getMatrix()[row][col].cardIsTank())
                                enemyRowHasTanks = true;

            if (!attackedCard.cardIsTank() && enemyRowHasTanks) {
                catchErrorsAttack(action, output, "Attacked card is not of type 'Tank'.", false);
                return;
            }
        }

        // actual use ability action
        switch (attackerCard.getName())
        {
            case "The Ripper" -> ((TheRipper)attackerCard).weakKnees(table, action.getCardAttacked());
            case "Miraj" -> ((Miraj)attackerCard).skyjack(table, action.getCardAttacker(), action.getCardAttacked());
            case "The Cursed One" -> {
                ((TheCursedOne) attackerCard).shapeshift(table, action.getCardAttacked());
                if(attackedCard.getHealth() < 1)
                    table.deleteCardFromTable(action.getCardAttacked().getX(), action.getCardAttacked().getY());
            }
            case "Disciple" -> ((Disciple)attackerCard).godsPlan(table, action.getCardAttacked());
            default -> output.add("No card with the given name!");
        }

        attackerCard.setHasAttackedThisRound(true);

    }
    public void unfreezePlayerCards(Table table){
        if(table.getCurrentlyPlaying() == 1)
            for(int row = 0; row < 2; row++)
                for(int col = 0; col < 5; col++)
                    if(table.getMatrix()[row][col] != null)
                        if(table.getMatrix()[row][col].isFrozen())
                            table.getMatrix()[row][col].unfreezeCard();

        if(table.getCurrentlyPlaying() == 2)
            for(int row = 2; row < 4; row++)
                for(int col = 0; col < 5; col++)
                    if(table.getMatrix()[row][col] != null)
                        if(table.getMatrix()[row][col].isFrozen())
                            table.getMatrix()[row][col].unfreezeCard();

    }
    public void cardUsesAttack(Table table, Actions action, ArrayNode output)
    {
        // frozen card error
        Card attackerCard = table.getMatrix()[action.getCardAttacker().getX()][action.getCardAttacker().getY()];

        Card attackedCard = table.getMatrix()[action.getCardAttacked().getX()][action.getCardAttacked().getY()];

        if(attackerCard == null)
            return;
        if(attackedCard == null)
            return;

        if(attackerCard.isFrozen()) {
           catchErrorsAttack(action, output, "Attacker card is frozen.", false);
           return;
       }

        // already attacked error
       if(attackerCard.getHasAttackedThisRound())
       {
            catchErrorsAttack(action, output, "Attacker card has already attacked this turn.", false);
            return;
       }

       if(table.getCurrentlyPlaying() == 1) {
           if (action.getCardAttacked().getX() != 0 && action.getCardAttacked().getX() != 1) {
               catchErrorsAttack(action, output, "Attacked card does not belong to the enemy.", false);
               return;
           }
       } else if (table.getCurrentlyPlaying() == 2) {
           if (action.getCardAttacked().getX() != 2 && action.getCardAttacked().getX() != 3) {
               catchErrorsAttack(action, output, "Attacked card does not belong to the enemy.", false);
               return;
           }
       }

       // did not target an enemy tank if it exists on enemy rows

        // check if enemy has tanks on his side
        boolean enemyRowHasTanks = false;

        if(table.getCurrentlyPlaying() == 1)
            for(int row = 0; row < 2; row++)
                for(int col = 0; col < 5; col++)
                    if(table.getMatrix()[row][col] != null)
                        if(table.getMatrix()[row][col].cardIsTank())
                            enemyRowHasTanks = true;


        if(table.getCurrentlyPlaying() == 2)
            for(int row = 2; row < 4; row++)
                for(int col = 0; col < 5; col++)
                    if(table.getMatrix()[row][col] != null)
                        if(table.getMatrix()[row][col].cardIsTank())
                            enemyRowHasTanks = true;

        if(!attackedCard.cardIsTank() && enemyRowHasTanks){
            catchErrorsAttack(action, output, "Attacked card is not of type 'Tank'.", false);
            return;
        }

        // actual attack action
        attackedCard.setHealth(attackedCard.getHealth() - attackerCard.getAttackDamage());
        if(attackedCard.getHealth() < 1)
            table.deleteCardFromTable(action.getCardAttacked().getX(), action.getCardAttacked().getY());
        attackerCard.setHasAttackedThisRound(true);
    }
    public void catchErrorsAttack(Actions action, ArrayNode output, String error, boolean attackedIsHero)
    {
        // general method to catch all type of attack errors
        ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
        nodeFinal.put("command", action.getCommand());

        ObjectNode attackerNode = JsonNodeFactory.instance.objectNode();
        attackerNode.put("x", action.getCardAttacker().getX());
        attackerNode.put("y", action.getCardAttacker().getY());
        nodeFinal.replace("cardAttacker", attackerNode);

        if(!attackedIsHero) {
            ObjectNode attackedNode = JsonNodeFactory.instance.objectNode();
            attackedNode.put("x", action.getCardAttacked().getX());
            attackedNode.put("y", action.getCardAttacked().getY());
            nodeFinal.replace("cardAttacked", attackedNode);
        }

        nodeFinal.put("error", error);

        output.add(nodeFinal);
    }
    public ObjectNode getFrozenCardsOnTable(Table table, Actions action)
    {
        ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
        nodeFinal.put("command", action.getCommand());

        ArrayNode frozenCardsNode = JsonNodeFactory.instance.arrayNode();
        for(int row = 0; row < 4; row++)
            for(int col = 0; col < 5; col++)
                if(table.getMatrix()[row][col] != null)
                    if(table.getMatrix()[row][col].cardIsFrozen())
                        frozenCardsNode.add(cardToObjectNode(table.getMatrix()[row][col]));

        nodeFinal.replace("output", frozenCardsNode);

        return nodeFinal;
    }
    public void errorCatchGetCardAtPosition(Actions action, Table table, ArrayNode output)
    {
        ObjectNode res = getCardAtPosition(action, table);
        if(res != null) {
            ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
            nodeFinal.put("command", action.getCommand());
            nodeFinal.replace("output", res);
            nodeFinal.put("x", action.getX());
            nodeFinal.put("y", action.getY());
            output.add(nodeFinal);
        }
        else {
            ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
            nodeFinal.put("command", action.getCommand());
            nodeFinal.put("output", "No card available at that position.");
            nodeFinal.put("x", action.getX());
            nodeFinal.put("y", action.getY());
            output.add(nodeFinal);
        }
    }
    public ObjectNode getCardAtPosition(Actions action, Table table)
    {
        if(table.getMatrix()[action.getX()][action.getY()] == null)
            return null;
        return cardToObjectNode(table.getMatrix()[action.getX()][action.getY()]);
    }
    public void errorCatchUseEnvironmentCard(Actions action, Table table, Player player1, Player player2, ArrayNode output)
    {
        int resultCode = useEnvironmentCard(action, table, player1, player2);
        if(resultCode < 0) {
            ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
            nodeFinal.put("affectedRow", action.getAffectedRow());
            nodeFinal.put("command", action.getCommand());
            if (resultCode == -1)
                nodeFinal.put("error", "Cannot steal enemy card since the player's row is full.");
            if (resultCode == -2)
                nodeFinal.put("error", "Chosen card is not of type environment.");
            if (resultCode == -3)
                nodeFinal.put("error", "Not enough mana to use environment card.");
            if (resultCode == -4)
                nodeFinal.put("error", "Chosen row does not belong to the enemy.");
            nodeFinal.put("handIdx", action.getHandIdx());

            output.add(nodeFinal);
        }
    }
    public void resetAttackStatus(Table table)
    {
        for(int row = 0; row < 4; row++)
            for(int column = 0; column < 5; column++)
                if(table.getMatrix()[row][column] != null) {
                    if(table.getMatrix()[row][column].getHasAttackedThisRound())
                        table.getMatrix()[row][column].setHasAttackedThisRound(false);
                }
    }
    public int  useEnvironmentCard(Actions action, Table table, Player player1, Player player2){
        int resultCode = 0;
        if(table.getCurrentlyPlaying() == 1) {
            // not the environment type card
            if(!player1.getHand().get(action.getHandIdx()).cardIsEnvironmentCard())
                return -2;
            // not enough mana to place the card
            if(player1.getCurrentMana() < player1.getHand().get(action.getHandIdx()).getMana())
                return -3;
            // chosen row does not belong to the enemy.
            if(action.getAffectedRow() != 0 && action.getAffectedRow() != 1)
                return -4;
            resultCode = useEnvironmentCardEachPlayer(action, table, player1);
        }
        if(table.getCurrentlyPlaying() == 2) {
            // not the environment type card
            if(!player2.getHand().get(action.getHandIdx()).cardIsEnvironmentCard())
                return -2;
            // not enough mana to place the card
            if(player2.getCurrentMana() < player2.getHand().get(action.getHandIdx()).getMana())
                return -3;
            // chosen row does not belong to the enemy.
            if(action.getAffectedRow() != 2 && action.getAffectedRow() != 3)
                return -4;
            resultCode = useEnvironmentCardEachPlayer(action, table, player2);
        }
        if(resultCode > 0) {
            // delete cards which are dead from table
            for (int column = 0; column < 5; column++)
                if (table.getMatrix()[action.getAffectedRow()][column] != null) {
                    if (table.getMatrix()[action.getAffectedRow()][column].getHealth() <= 0) {
                        table.deleteCardFromTable(action.getAffectedRow(), column);
                        column--;
                    }
                }
            // eliminate used environment card from player hand and decrease mana
            if (table.getCurrentlyPlaying() == 1) {
                player1.setCurrentMana(player1.getCurrentMana() - player1.getHand().get(action.getHandIdx()).getMana());
                player1.getHand().remove(action.getHandIdx());
            }
            if (table.getCurrentlyPlaying() == 2) {
                player2.setCurrentMana(player2.getCurrentMana() - player2.getHand().get(action.getHandIdx()).getMana());
                player2.getHand().remove(action.getHandIdx());
            }
        }
        return resultCode;
    }

    public int useEnvironmentCardEachPlayer(Actions action, Table table, Player player){
        // check card type and implement special methods
        Card card = player.getHand().get(action.getHandIdx());

        // not enough mana to use environment card.
        if(player.getCurrentMana() < card.getMana())
            return -3;

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
                if (check == -1)
                    // cannot steal enemy card since the player's row is full.
                    return -1;
                break;
            default:
                // chosen card is not of type environment.
                return -2;
        }
        return 1;
    }
    public ObjectNode getEnvironmentCardsInHand(Actions action, Player player1, Player player2){
        ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
        nodeFinal.put("command", action.getCommand());

        if(action.getPlayerIdx() == 1) {
            ArrayNode handNode = environmentCardsInHandToObjectNode(player1.getHand());
            nodeFinal.replace("output", handNode);
        }
        if(action.getPlayerIdx() == 2) {
            ArrayNode handNode = environmentCardsInHandToObjectNode(player2.getHand());
            nodeFinal.replace("output", handNode);
        }
        nodeFinal.put("playerIdx", action.getPlayerIdx());

        return nodeFinal;
    }
    public ArrayNode environmentCardsInHandToObjectNode(ArrayList<Card> hand){
        ArrayNode handNode = JsonNodeFactory.instance.arrayNode();
        for (Card card : hand) {
            if(card.cardIsEnvironmentCard()) {
                ObjectNode cardNode = cardToObjectNode(card);
                handNode.add(cardNode);
            }
        }
        return handNode;
    }
    public ObjectNode getCardsOnTable(Actions action, Table table){
        ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();

        nodeFinal.put("command", action.getCommand());

        ArrayNode rowsNode = JsonNodeFactory.instance.arrayNode();
        for(int row = 0; row < 4; row++)
        {
            ArrayNode rowNode = JsonNodeFactory.instance.arrayNode();
            for (int column = 0; column < 5; column++) {
                if(table.getMatrix()[row][column] != null) {
                    ObjectNode cardNode = cardToObjectNode(table.getMatrix()[row][column]);
                    rowNode.add(cardNode);
                }
            }
            rowsNode.add(rowNode);
        }

        nodeFinal.replace("output", rowsNode);

        return nodeFinal;
    }
    public ObjectNode getPlayerMana(Actions action, Player player1, Player player2, Table table){
        // Create output node
        ObjectNode nodeOutput = JsonNodeFactory.instance.objectNode();
        nodeOutput.put("command", action.getCommand());
        if(action.getPlayerIdx() == 1)
            nodeOutput.put("output", player1.getCurrentMana());
        if(action.getPlayerIdx() == 2)
            nodeOutput.put("output", player2.getCurrentMana());
        nodeOutput.put("playerIdx", action.getPlayerIdx());

        return nodeOutput;
    }
    public void debug(Actions action, Table table, Player player1, Player player2)
    {
//        System.out.println("Player1 mana is:   " + player1.getCurrentMana());
//        System.out.println("Player2 mana is:   " + player2.getCurrentMana());
//        System.out.println("Player currently doing action:    " + table.getCurrentlyPlaying());
        System.out.println("Action done is:    " + action.getCommand());
        if(action.getCommand().equals("useEnvironmentCard") && table.getCurrentlyPlaying() == 1)
            System.out.println("Env card used is " + player1.getHand().get(action.getHandIdx()).getName()) ;
        if(action.getCommand().equals("useEnvironmentCard") && table.getCurrentlyPlaying() == 2)
            System.out.println("Env card used is " + player2.getHand().get(action.getHandIdx()).getName()) ;
        System.out.println();
        System.out.println("---------TABLE----------");
        System.out.println();

        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++)
                if(table.getMatrix()[i][j] != null)
                    System.out.println(table.getMatrix()[i][j].getName() + " is frozen: " + table.getMatrix()[i][j].isFrozen());
            else
                System.out.println("NULL");
            System.out.println();
        }
    }
    public void placeCard(Actions action, Table table, Player player1, Player player2, ArrayNode output)
    {
        if(table.getCurrentlyPlaying() == 1)
        {
            if(placeCardIsError(action, player1, output))
                return;
            // place card
            placeCardGeneral(action, table, player1, output);
        }

        if(table.getCurrentlyPlaying() == 2)
        {
            if(placeCardIsError(action, player2, output))
                return;
            // place card
            placeCardGeneral(action, table, player2, output);
        }
    }
    public boolean placeCardIsError(Actions action, Player player, ArrayNode output)
    {
        if(player.getHand().get(action.getHandIdx()).cardIsEnvironmentCard()) {
            output.add(createPlaceCardErrorFormat(action, "Cannot place environment card on table."));
            return true;
        }
        if(player.getHand().get(action.getHandIdx()).getMana() > player.getCurrentMana()) {
            output.add(createPlaceCardErrorFormat(action, "Not enough mana to place card on table."));
            return true;
        }
        return false;
    }
    private int isFirstRowCardOrSecondRowCard(Card card)
    {
        // 1 -> firstRow   2 -> secondRow
        if(card.getName().equals("The Ripper") || card.getName().equals("Miraj") ||
                card.getName().equals("Goliath") || card.getName().equals("Warden"))
            return 2;
        if(card.getName().equals("Sentinel") || card.getName().equals("Berserker") ||
                card.getName().equals("The Cursed One") || card.getName().equals("Disciple"))
            return 1;
        return 0;
    }
    private void placeCardGeneral(Actions action, Table table, Player player, ArrayNode output) {
        boolean placedCard = false;
        // save mana cost of the card in case we need to decrease mana player after bypassing all error cases
        int manaCost = player.getHand().get(action.getHandIdx()).getMana();
        for(int i = 0; i < 5; i++)
        {
            // Place on first row
            if(isFirstRowCardOrSecondRowCard(player.getHand().get(action.getHandIdx())) == 1) {
                if (table.getCurrentlyPlaying() == 1)
                    if (table.getMatrix()[3][i] == null) {
                        table.getMatrix()[3][i] = player.getHand().get(action.getHandIdx());
                        player.getHand().remove(action.getHandIdx());
                        placedCard = true;
                        break;
                    }
                if (table.getCurrentlyPlaying() == 2)
                    if (table.getMatrix()[0][i] == null) {
                        table.getMatrix()[0][i] = player.getHand().get(action.getHandIdx());
                        player.getHand().remove(action.getHandIdx());
                        placedCard = true;
                        break;
                    }
            }
            // Place on second row
            if (isFirstRowCardOrSecondRowCard(player.getHand().get(action.getHandIdx())) == 2) {
                if(table.getCurrentlyPlaying() == 1)
                    if (table.getMatrix()[2][i] == null) {
                        table.getMatrix()[2][i] = player.getHand().get(action.getHandIdx());
                        player.getHand().remove(action.getHandIdx());
                        placedCard = true;
                        break;
                    }
                if(table.getCurrentlyPlaying() == 2)
                    if (table.getMatrix()[1][i] == null) {
                        table.getMatrix()[1][i] = player.getHand().get(action.getHandIdx());
                        player.getHand().remove(action.getHandIdx());
                        placedCard = true;
                        break;
                    }
            }
        }
        if(!placedCard)
            output.add(createPlaceCardErrorFormat(action,"Cannot place card on table since row is full."));
        else {
            // decrease mana by the cost of the card because we bypassed all error cases
            player.setCurrentMana(player.getCurrentMana() - manaCost);
        }

    }
    public ObjectNode createPlaceCardErrorFormat(Actions action, String error)
    {
        ObjectNode nodeOutput = JsonNodeFactory.instance.objectNode();
        nodeOutput.put("command", action.getCommand());
        nodeOutput.put("handIdx", action.getHandIdx());
        nodeOutput.put("error", error);
        return nodeOutput;
    }
    public ObjectNode getCardsInHand(Player player1, Player player2, Actions action)
    {
        ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
        nodeFinal.put("command", action.getCommand());
        nodeFinal.put("playerIdx", action.getPlayerIdx());

        if(action.getPlayerIdx() == 1) {
            ArrayNode handNode = handToObjectNode(player1.getHand());
            nodeFinal.replace("output", handNode);
        }
        if(action.getPlayerIdx() == 2) {
            ArrayNode deckNode = handToObjectNode(player2.getHand());
            nodeFinal.replace("output", deckNode);
        }

        return nodeFinal;
    }
    public void prepareGame(Game game, Player player1, Player player2){
        // Shuffle the decks chosen by each player
        Collections.shuffle(player1.getDecks().get(game.getStartGame().getPlayerOneDeckIdx()).getCards(),
                new Random(game.getStartGame().getShuffleSeed()));
        Collections.shuffle(player2.getDecks().get(game.getStartGame().getPlayerTwoDeckIdx()).getCards(),
                new Random(game.getStartGame().getShuffleSeed()));

        // Create player hands

        player1.setHand(new ArrayList<Card>());
        player2.setHand(new ArrayList<Card>());

    }
    public ObjectNode cardToObjectNode(Card card) {
        ObjectNode nodeCard = JsonNodeFactory.instance.objectNode();

        nodeCard.put("mana", card.getMana());
        // For environment cards don't put health and attack damage
        if(!card.cardIsEnvironmentCard()) {
            nodeCard.put("attackDamage", card.getAttackDamage());
            nodeCard.put("health", card.getHealth());
        }
        nodeCard.put("description", card.getDescription());

        ArrayNode nodeColors = JsonNodeFactory.instance.arrayNode();
        for(String color : card.getColors())
            nodeColors.add(color);

        nodeCard.replace("colors", nodeColors);
        nodeCard.put("name", card.getName());

        return nodeCard;
    }
    public ArrayNode deckToObjectNode(Deck deck) {
        ArrayNode deckNode = JsonNodeFactory.instance.arrayNode();
        for (Card card : deck.getCards()) {
            ObjectNode cardNode = cardToObjectNode(card);
            deckNode.add(cardNode);
        }
        return deckNode;
    }
    public ArrayNode handToObjectNode(ArrayList<Card> hand) {
        ArrayNode handNode = JsonNodeFactory.instance.arrayNode();
        for (Card card : hand) {
            ObjectNode cardNode = cardToObjectNode(card);
            handNode.add(cardNode);
        }
        return handNode;
    }
    public ObjectNode showPlayerDeck(Actions action, Game game, Player player, int playerIndex){
        ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();

        nodeFinal.put("command", action.getCommand());
        nodeFinal.put("playerIdx", playerIndex);

        if(playerIndex == 1) {
            ArrayNode deckNode = deckToObjectNode(player.getDecks().get(game.getStartGame().getPlayerOneDeckIdx()));
            nodeFinal.replace("output", deckNode);
        }
        if(playerIndex == 2) {
            ArrayNode deckNode = deckToObjectNode(player.getDecks().get(game.getStartGame().getPlayerTwoDeckIdx()));
            nodeFinal.replace("output", deckNode);
        }

        return nodeFinal;
    }

    public ObjectNode getPlayerDeck(Actions action, Game game, Player player1, Player player2){
        ObjectNode nodeOutput = JsonNodeFactory.instance.objectNode();
        if(action.getPlayerIdx() == 1)
            nodeOutput = showPlayerDeck(action, game, player1, 1);
        if(action.getPlayerIdx() == 2)
            nodeOutput = showPlayerDeck(action, game, player2, 2);

        return nodeOutput;
    }

    public ObjectNode getPlayerTurn(Actions action, Table table){

        // Create output node
        ObjectNode nodeOutput = JsonNodeFactory.instance.objectNode();
        nodeOutput.put("command", action.getCommand());
        nodeOutput.put("output", table.getCurrentlyPlaying());
        return nodeOutput;
    }
    public void drawCardsAndIncreaseMana(Player player1, Player player2, Game game, int manaIncrease){
        // Draw first card from the deck for each player and remove it from the deck
        if(!player1.getDecks().get(game.getStartGame().getPlayerOneDeckIdx()).getCards().isEmpty()) {
            player1.getHand().add(player1.getDecks().get(game.getStartGame().getPlayerOneDeckIdx()).getCards().get(0));
            player1.getDecks().get(game.getStartGame().getPlayerOneDeckIdx()).getCards().remove(0);
        }

        if(!player2.getDecks().get(game.getStartGame().getPlayerTwoDeckIdx()).getCards().isEmpty()) {
                player2.getHand().add(player2.getDecks().get(game.getStartGame().getPlayerTwoDeckIdx()).getCards().get(0));
                player2.getDecks().get(game.getStartGame().getPlayerTwoDeckIdx()).getCards().remove(0);
            }
        // increase mana at start of the round when players draw cards
        if(manaIncrease > 10)
            manaIncrease = 10;
        player1.setCurrentMana(player1.getCurrentMana() + manaIncrease);
        player2.setCurrentMana(player2.getCurrentMana() + manaIncrease);
    }
    public ObjectNode heroCardToObjectNode(Card card) {
        ObjectNode nodeCard = JsonNodeFactory.instance.objectNode();

        nodeCard.put("mana", card.getMana());
        nodeCard.put("description", card.getDescription());

        ArrayNode nodeColors = JsonNodeFactory.instance.arrayNode();
        for(String color : card.getColors())
            nodeColors.add(color);

        nodeCard.replace("colors", nodeColors);
        nodeCard.put("name", card.getName());
        nodeCard.put("health", card.getHealth());

        return nodeCard;
    }
    public ObjectNode showPlayerHero(Game game, Actions action){
        ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();

        nodeFinal.put("command", action.getCommand());
        nodeFinal.put("playerIdx", action.getPlayerIdx());

        ObjectNode heroNode = JsonNodeFactory.instance.objectNode();
        if(action.getPlayerIdx() == 1)
            heroNode = heroCardToObjectNode(game.getStartGame().getPlayerOneHero());
        if(action.getPlayerIdx() == 2)
            heroNode = heroCardToObjectNode(game.getStartGame().getPlayerTwoHero());

        nodeFinal.replace("output", heroNode);

        return nodeFinal;
    }
}
