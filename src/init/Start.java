package init;

import cards.Card;
import games.Actions;
import games.CardCoordinates;
import games.Game;
import games.StartGame;
import players.Deck;
import players.Player;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.DecksInput;
import fileio.GameInput;
import fileio.Input;
import java.util.ArrayList;
import cards.LordRoyce;
import cards.KingMudface;
import cards.EmpressThorina;
import cards.GeneralKocioraw;
import cards.Sentinel;
import cards.Winterfell;
import cards.Berserker;
import cards.Disciple;
import cards.Firestorm;
import cards.Goliath;
import cards.HeartHound;
import cards.Miraj;
import cards.TheRipper;
import cards.TheCursedOne;
import cards.Warden;


public class Start {
    private Player player1;
    private Player player2;
    private ArrayList<Game> games;

    /**
     * Finds the deck input for each player
     * @param data - input
     * @param id - player id
     * @return - the player deck
     */
    protected DecksInput getPlayerDecks(final Input data, final int id) {
        if (id == 1) {
            return data.getPlayerOneDecks();
        }
        if (id == 2) {
            return data.getPlayerTwoDecks();
        } else {
            return null;
        }
    }

    /**
     * Creates each player list of decks
     * @param data - input
     * @param playerId - what player deck we create
     * @return - the array of decks
     */
    protected ArrayList<Deck> createMyDeckOfDecks(final Input data, final int playerId) {
        ArrayList<Deck> myDecks = new ArrayList<>();

        for (ArrayList<CardInput> deck : getPlayerDecks(data, playerId).getDecks()) {
            Deck myDeck = new Deck();
            for (CardInput card : deck) {
                switch (card.getName()) {
                    case "Sentinel" -> myDeck.getCards().add(new Sentinel(card));
                    case "Berserker" -> myDeck.getCards().add(new Berserker(card));
                    case "Goliath" -> myDeck.getCards().add(new Goliath(card));
                    case "Warden" -> myDeck.getCards().add(new Warden(card));
                    case "Miraj" -> myDeck.getCards().add(new Miraj(card));
                    case "The Ripper" -> myDeck.getCards().add(new TheRipper(card));
                    case "Disciple" -> myDeck.getCards().add(new Disciple(card));
                    case "The Cursed One" -> myDeck.getCards().add(new TheCursedOne(card));
                    case "Firestorm" -> myDeck.getCards().add(new Firestorm(card));
                    case "Winterfell" -> myDeck.getCards().add(new Winterfell(card));
                    case "Heart Hound" -> myDeck.getCards().add(new HeartHound(card));
                    default -> System.out.println("No card with the given name!");
                }

            }
            myDecks.add(myDeck);
        }
        return myDecks;
    }

    /**
     * Start function of the program
     * It parses the input as first task
     * Gets the deck of decks and constructs the players
     * Then gets the games and constructs game objects
     * @param data - the actual input
     * @param output - the output where we store all the output commands
     */
    public Start(final Input data, final ArrayNode output) {
        ArrayList<Deck> firstPlayerDecks = createMyDeckOfDecks(data, 1);
        ArrayList<Deck> secondPlayerDecks = createMyDeckOfDecks(data, 2);

        player1 = new Player(data.getPlayerOneDecks().getNrDecks(), firstPlayerDecks,
                0, null);
        player2 = new Player(data.getPlayerTwoDecks().getNrDecks(), secondPlayerDecks,
                0, null);

        getGames(data);
        ArrayList<Game> myGames = getGames(data);

        // Start the actual playing of the game
        PlayGame playGame = new PlayGame();
        // entry point of actual implementation
        playGame.playGame(myGames, player1, player2, output);
    }

    /**
     * Gets each start game scenario
     * Parses each round commands
     * Parses game coordinates
     * @param data - the actual input
     * @return - the resulted array of games
     */
    protected ArrayList<Game> getGames(final Input data) {
        ArrayList<Game> myGames = new ArrayList<>();

        for (GameInput gameParser : data.getGames()) {
            Card hero1;
            Card hero2;
            // Select the first player hero
            hero1 = heroSelection((gameParser.getStartGame().getPlayerOneHero()));
            // Select the second player hero
            hero2 = heroSelection(gameParser.getStartGame().getPlayerTwoHero());
            // Create the StartGame instance
            StartGame startGame = new StartGame(gameParser.getStartGame(), hero1, hero2);

            // Create the actions array
            ArrayList<Actions> actions = new ArrayList<>();

            for (ActionsInput action : gameParser.getActions()) {
                // Deep copy coordinates for attack this round
                CardCoordinates myAttackerCard = new CardCoordinates();
                CardCoordinates myAttackedCard = new CardCoordinates();

                if (action.getCardAttacker() != null) {
                    myAttackerCard = new CardCoordinates(action.getCardAttacker().getX(),
                            action.getCardAttacker().getY());
                }

                if (action.getCardAttacked() != null) {
                    myAttackedCard = new CardCoordinates(action.getCardAttacked().getX(),
                            action.getCardAttacked().getY());
                }

                Actions myAction = new Actions(action.getCommand(), action.getHandIdx(),
                        myAttackerCard, myAttackedCard, action.getAffectedRow(),
                        action.getPlayerIdx(), action.getX(), action.getY());

                actions.add(myAction);
            }

            // set in the games array what we collected
            Game game = new Game(startGame, actions);
            myGames.add(game);
        }

        return myGames;
    }

    /**
     * Method to select hero based on heroInput
     * @param heroInput - input card of the given hero
     * @return - hero card to use in games
     */
    protected Card heroSelection(final CardInput heroInput) {
        Card hero = new Card();
        switch (heroInput.getName()) {
            case "Lord Royce" -> hero = new LordRoyce(heroInput);
            case "Empress Thorina" -> hero = new EmpressThorina(heroInput);
            case "King Mudface" -> hero = new KingMudface(heroInput);
            case "General Kocioraw" -> hero = new GeneralKocioraw(heroInput);
            default -> {
                hero = new Card();
                System.out.println("No hero with the given name!");
            }
        }
        return hero;
    }
}
