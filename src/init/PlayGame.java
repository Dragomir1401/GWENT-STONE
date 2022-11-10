package init;
import Cards.Card;
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
            boolean drewCardAlready = false;
            int roundNumber = 0;
            // the playing table gets initialised and tracks what player is currently capable of placing cards
            Table table = new Table();
            table.setCurrentlyPlaying(gameIterator.getStartGame().getStartingPlayer());

            for (Actions actionIterator : gameIterator.getActions()) {
                /* Draw cards when we reached 0 or 2 EndTurns meaning each player ended the current round */
                if(countEndTurns % 2 == 0 && !drewCardAlready) {
                    drawCardsAndIncreaseMana(player1, player2, gameIterator, roundNumber + 1);
                    roundNumber++;
                    drewCardAlready = true;
                    countEndTurns = 0;
                }

                switch (actionIterator.getCommand()) {
                    case "getPlayerDeck" -> output.add(getPlayerDeck(actionIterator, gameIterator, player1, player2));
                    case "getPlayerTurn" -> output.add(getPlayerTurn(actionIterator, gameIterator));
                    case "getPlayerHero" -> output.add(showPlayerHero(gameIterator, actionIterator));
                    case "getCardsInHand" -> output.add(getCardsInHand(player1, player2, actionIterator));
                    case "placeCard" -> placeCard(actionIterator, table, gameIterator, player1, player2);
                    case "endPlayerTurn" -> {
                        countEndTurns++;
                        drewCardAlready = false;
                        // change what player is doing actions when a turn ends
                        changeCurrentPlayer(table);
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

    public void placeCard(Actions action, Table table, Game game, Player player1, Player player2)
    {
        if(table.getCurrentlyPlaying() == 1)
        {
            placeCardGeneral(action, table, player1);
        }

        if(table.getCurrentlyPlaying() == 2)
        {
            placeCardGeneral(action, table, player2);
        }
    }

    private void placeCardGeneral(Actions action, Table table, Player player) {
        for(int i = 0; i < 5; i++)
        {
            if(table.getMatrix()[0][i] == null) {
                table.getMatrix()[0][i] = player.getHand().get(action.getHandIdx());
                player.getHand().remove(action.getHandIdx());
                break;
            }
        }
    }

    public void changeCurrentPlayer(Table table)
    {
        if(table.getCurrentlyPlaying() == 1)
            table.setCurrentlyPlaying(2);
        if(table.getCurrentlyPlaying() == 2)
            table.setCurrentlyPlaying(1);
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

    public ObjectNode getPlayerTurn(Actions action, Game game){

        // Create output node
        ObjectNode nodeOutput = JsonNodeFactory.instance.objectNode();
        nodeOutput.put("command", action.getCommand());
        nodeOutput.put("output", game.getStartGame().getStartingPlayer());
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
