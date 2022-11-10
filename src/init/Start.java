package init;

import Cards.Card;
import Games.Actions;
import Games.CardCoordinates;
import Games.Game;
import Games.StartGame;
import Players.Deck;
import Players.Player;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.*;

import java.util.ArrayList;

import Cards.*;


public class Start {
    Player player1;
    Player player2;
    ArrayList<Game> games;

    public DecksInput getPlayerDecks(Input data, int id)
    {
        if(id == 1)
            return data.getPlayerOneDecks();
        if(id == 2)
            return data.getPlayerTwoDecks();
        else
            return null;
    }

    public ArrayList<Deck> createMyDeckOfDecks(Input data, int player_id){
        /**
         * Creates each player list of decks
         */
        ArrayList<Deck> my_decks = new ArrayList<Deck>();

        for(ArrayList<CardInput> deck : getPlayerDecks(data, player_id).getDecks()) {
            Deck my_deck = new Deck();
            for (CardInput card : deck) {
                switch (card.getName()) {

                    case "Sentinel":
                        my_deck.getCards().add(new Sentinel(card));
                        break;

                    case "Berserker":
                        my_deck.getCards().add(new Berserker(card));
                        break;

                    case "Goliath":
                        my_deck.getCards().add(new Goliath(card));
                        break;

                    case "Warden":
                        my_deck.getCards().add(new Warden(card));
                        break;

                    case "Miraj":
                        my_deck.getCards().add(new Miraj(card));
                        break;

                    case "The Ripper":
                        my_deck.getCards().add(new TheRipper(card));
                        break;

                    case "Disciple":
                        my_deck.getCards().add(new Disciple(card));
                        break;

                    case "The Cursed One":
                        my_deck.getCards().add(new TheCursedOne(card));
                        break;

                    case "Firestorm":
                        my_deck.getCards().add(new Firestorm(card));
                        break;

                    case "Winterfell":
                        my_deck.getCards().add(new Winterfell(card));
                        break;

                    case "Heart Hound":
                        my_deck.getCards().add(new HeartHound(card));
                        break;

                    default:
                        System.out.println("No card with the given name!");
                        break;
                }

            }
            my_decks.add(my_deck);
        }
        return my_decks;
    }
    public Start(Input data, ArrayNode output) {
        /**
         * Start function of the program
         * It parses the input as first task
         * Gets the deck of decks and constructs the players
         * Then gets the games and constructs game objects
         */
        ArrayList<Deck> first_player_decks = createMyDeckOfDecks(data, 1);
        ArrayList<Deck> second_player_decks = createMyDeckOfDecks(data, 2);

        player1 = new Player(data.getPlayerOneDecks().getNrDecks(), first_player_decks, 0, null);
        player2 = new Player(data.getPlayerTwoDecks().getNrDecks(), second_player_decks, 0, null);

        getGames(data);

        ArrayList<Game> games = getGames(data);

        // Start the actual playing of the game
        PlayGame playGame = new PlayGame();
        playGame.playGame(games, player1, player2, output);
    }

    public ArrayList<Game> getGames(Input data){
        /**
         * gets each start game scenario
         * parses each round commands
         * parses game coordinates
         */
        ArrayList<Game> games = new ArrayList<Game>();

        for(GameInput game_parser : data.getGames())
        {
            Card hero1;
            Card hero2;
            // Select the first player hero
            switch (game_parser.getStartGame().getPlayerOneHero().getName()){
                case "Lord Royce":
                    hero1 = new LordRoyce(game_parser.getStartGame().getPlayerOneHero());
                    break;

                case "Empress Thorina":
                    hero1 = new EmpressThorina(game_parser.getStartGame().getPlayerOneHero());
                    break;

                case "King Mudface":
                    hero1 = new KingMudface(game_parser.getStartGame().getPlayerOneHero());
                    break;

                case "General Kocioraw":
                    hero1 = new GeneralKocioraw(game_parser.getStartGame().getPlayerOneHero());
                    break;

                default:
                    hero1 = new Card();
                    System.out.println("No hero with the given name!");
                    break;
            }
            // Select the second player hero
            switch (game_parser.getStartGame().getPlayerTwoHero().getName()){
                case "Lord Royce":
                    hero2 = new LordRoyce(game_parser.getStartGame().getPlayerTwoHero());
                    break;

                case "Empress Thorina":
                    hero2 = new EmpressThorina(game_parser.getStartGame().getPlayerTwoHero());
                    break;

                case "King Mudface":
                    hero2 = new KingMudface(game_parser.getStartGame().getPlayerTwoHero());
                    break;

                case "General Kocioraw":
                    hero2 = new GeneralKocioraw(game_parser.getStartGame().getPlayerTwoHero());
                    break;

                default:
                    hero2 = new Card();
                    System.out.println("No hero with the given name!");
                    break;
            }

            // Create the StartGame instance
            StartGame startGame = new StartGame(game_parser.getStartGame().getPlayerOneDeckIdx(),
                    game_parser.getStartGame().getPlayerTwoDeckIdx(), game_parser.getStartGame().getShuffleSeed(),
                    hero1, hero2, game_parser.getStartGame().getStartingPlayer());

            // Create the actions array
            ArrayList<Actions> actions = new ArrayList<Actions>();

            for(ActionsInput action : game_parser.getActions())
            {
                // Deep copy coordinates for attack this round
                CardCoordinates my_attacker_card = new CardCoordinates();
                CardCoordinates my_attacked_card = new CardCoordinates();

                if(action.getCardAttacker() != null)
                    my_attacker_card = new CardCoordinates(action.getCardAttacker().getX(),
                        action.getCardAttacker().getY());

                if(action.getCardAttacked() != null)
                    my_attacked_card = new CardCoordinates(action.getCardAttacked().getX(),
                        action.getCardAttacked().getY());

                Actions my_action = new Actions(action.getCommand(), action.getHandIdx(), my_attacker_card,
                        my_attacked_card, action.getAffectedRow(), action.getPlayerIdx(), action.getX(),
                        action.getY());

                actions.add(my_action);
            }

            // set in the games array what we collected
            Game game = new Game(startGame, actions);
            games.add(game);
        }

        return games;
    }


}
