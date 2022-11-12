package init;

import Cards.Card;
import Cards.Firestorm;
import Cards.HeartHound;
import Cards.Winterfell;
import Games.Actions;
import Games.Game;
import Players.Deck;
import Players.Player;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
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

            for (Actions actionIterator : gameIterator.getActions()) {
                /* Draw cards when we reached 0 or 2 EndTurns meaning each player ended the current round */
                if(countEndTurns == 2) {
                    roundNumber++;
                    drawCardsAndIncreaseMana(player1, player2, gameIterator, roundNumber);
                    unfreezeCards(table);
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
                    case "endPlayerTurn" -> {
                        countEndTurns++;
                        // change what player is doing actions when a turn ends
                        table.changeCurrentlyPlaying();
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
    public void errorCatchGetCardAtPosition(Actions action, Table table, ArrayNode output)
    {
        ObjectNode res = getCardAtPosition(action, table);
        if(res != null) {
            ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
            nodeFinal.put("command", action.getCommand());
            nodeFinal.replace("output", res);
            output.add(nodeFinal);
        }
        else
            output.add("No card at that position.");
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
        if(resultCode == -1)
            output.add("Cannot steal enemy card since the player's row is full.");
        if(resultCode == -2)
            output.add("Chosen card is not of type environment.");
        if(resultCode == -3)
            output.add("Not enough mana to use environment card.");
        if(resultCode == -4)
            output.add("Chosen row does not belong to the enemy.");

    }
    public void unfreezeCards(Table table)
    {
        for(int row = 0; row < 4; row++)
            for(int column = 0; column < 5; column++)
                if(table.getMatrix()[row][column] != null)
                    if(table.getMatrix()[row][column].cardIsFrozen())
                        table.getMatrix()[row][column].unfreezeCard();
    }
    public int  useEnvironmentCard(Actions action, Table table, Player player1, Player player2){
        int result = 0;
        if(table.getCurrentlyPlaying() == 1) {
            // chosen row does not belong to the enemy.
            if(action.getAffectedRow() != 0 && action.getAffectedRow() != 1)
                return -4;
            result = useEnvironmentCardEachPlayer(action, table, player1);
        }
        if(table.getCurrentlyPlaying() == 2) {
            // chosen row does not belong to the enemy.
            if(action.getAffectedRow() != 2 && action.getAffectedRow() != 3)
                return -4;
            result = useEnvironmentCardEachPlayer(action, table, player2);
        }

        // delete cards which are dead from table
        for(int column = 0; column < 5; column++)
            if(table.getMatrix()[action.getAffectedRow()][column] != null)
                if(table.getMatrix()[action.getAffectedRow()][column].getHealth() == 0)
                    deleteCardFromTable(table, action.getAffectedRow(), column);
        // eliminate used environment card from player hand and decrease mana
        if(table.getCurrentlyPlaying() == 1) {
            player1.setCurrentMana(player1.getCurrentMana() - player1.getHand().get(action.getHandIdx()).getMana());
            player1.getHand().remove(action.getHandIdx());
        }
        if(table.getCurrentlyPlaying() == 2) {
            player2.setCurrentMana(player2.getCurrentMana() - player2.getHand().get(action.getHandIdx()).getMana());
            player2.getHand().remove(action.getHandIdx());
        }
        return result;
    }
    public void deleteCardFromTable(Table table, int row, int column){
        // card died and has to be deleted from the table
        table.getMatrix()[row][column] = null;
    }
    public int useEnvironmentCardEachPlayer(Actions action, Table table, Player player){
        // check card type and implement special methods
        Card card = player.getHand().get(action.getHandIdx());

        // not enough mana to use environment card.
        if(player.getCurrentMana() < card.getMana())
            return -3;

        if(card.getName().equals("Firestorm"))
            ((Firestorm)card).deploy(table, action.getAffectedRow());
        else if(card.getName().equals("Winterfell"))
            ((Winterfell)card).deploy(table, action.getAffectedRow());
        else if(card.getName().equals("Heart Hound")){
            int check = ((HeartHound)card).deploy(table, action.getAffectedRow());
            // error case for not being able to steal a card
            if(check == -1)
                // cannot steal enemy card since the player's row is full.
                return -1;
        }
        else {
            // chosen card is not of type environment.
            return -2;
        }
        return 1;
    }
    public ObjectNode getEnvironmentCardsInHand(Actions action, Player player1, Player player2){
        ObjectNode nodeFinal = JsonNodeFactory.instance.objectNode();
        nodeFinal.put("command", action.getCommand());
        nodeFinal.put("playerIdx", action.getPlayerIdx());

        if(action.getPlayerIdx() == 1) {
            ArrayNode handNode = environmentCardsInHandToObjectNode(player1.getHand());
            nodeFinal.replace("output", handNode);
        }
        if(action.getPlayerIdx() == 2) {
            ArrayNode deckNode = environmentCardsInHandToObjectNode(player2.getHand());
            nodeFinal.replace("output", deckNode);
        }

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
        System.out.println("Player1 mana is:   " + player1.getCurrentMana());
        System.out.println("Player2 mana is:   " + player2.getCurrentMana());
        System.out.println("Player currently doing action:    " + table.getCurrentlyPlaying());
        System.out.println("Action done is:    " + action.getCommand());
        System.out.println();
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
