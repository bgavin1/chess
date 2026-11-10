package ui;

import model.CreateGameResult;
import model.GameResultData;
import model.ListGamesResult;
import model.LoginResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PostLoginUI {

    private final ServerFacade serverFacade;
    private final Scanner scanner;
    private final String username;
    private final String authToken;

    public PostLoginUI(ServerFacade serverFacade, Scanner scanner, String username, String authToken) {
        this.serverFacade = serverFacade;
        this.scanner = scanner;
        this.username = username;
        this.authToken = authToken;
    }

    public void run() {
        String choice = "";
        while (!choice.equals("Logout")) {
            System.out.printf("Welcome, %s!", username);
            System.out.println("Please enter a command:");
            System.out.println("- Help");
            System.out.println("- Logout");
            System.out.println("- Create a Game: type 'Create'");
            System.out.println("- List all Games: type 'List'");
            System.out.println("- Play a Game: type 'Play'");
            System.out.println("- Observe a Game: type 'Observe'");
            choice = scanner.nextLine();
            if (choice.equals("Logout")) {
                logout();
            } else if (choice.equals("Create")) {
                createGame();
            } else if (choice.equals("List")) {
                listGames();
            } else if (choice.equals("Play")) {
                playGame();
            } else if (choice.equals("Observe")) {
                observeGame();
            } else if (choice.equals("Help")) {
                help();
            } else {
                System.out.println("Invalid command. Try again");
            }
        }
    }

    public void help() {
        System.out.println("Logout: Logs you out");
        System.out.println("Create a Game: Starts a new game");
        System.out.println("List all Games: Shows you a list of all ongoing games");
        System.out.println("Play a Game: Join an existing game");
        System.out.println("Observe a Game: Lets you watch an ongoing game");
        System.out.println();
    }

    public void logout() {
        PreLoginUI preLoginUI = new PreLoginUI(serverFacade, scanner);
        serverFacade.logout(authToken);
        preLoginUI.run();
    }

    public void createGame() {
        System.out.println("Name your game: ");
        String gameName = scanner.nextLine();

        CreateGameResult result = serverFacade.createGame(gameName, authToken);
        System.out.println("Game created!");

    }

    public ListGamesResult listGames() {
        ListGamesResult result = serverFacade.listGames(authToken);

        int number = 1;
        for (GameResultData game : result.games()) {
            String white = (game.whiteUsername() == null ? "Available" : game.whiteUsername());
            String black = (game.blackUsername() == null ? "Available" : game.blackUsername());

            System.out.println(number + "." + game.gameName()
            + "- White: " + white + ", Black: " + black);
            number ++;
        }
        return result;

    }

    public void playGame() {
        ListGamesResult result = listGames();
        System.out.println("What number is your game in the list? ");
        int number = Integer.parseInt(scanner.nextLine());
        System.out.println("What color would you like to be? ");
        String playerColor = scanner.nextLine();

        List<GameResultData> games = new ArrayList<>(result.games());
        Integer gameID = games.get(number - 1).gameID();

        serverFacade.playGame(playerColor, gameID, authToken);
        System.out.println("Game joined!");


    }

    public void observeGame() {
        ListGamesResult result = listGames();
        System.out.println("What number is your game in the list? ");
        int number = Integer.parseInt(scanner.nextLine());

        List<GameResultData> games = new ArrayList<>(result.games());
        Integer gameID = games.get(number - 1).gameID();

        serverFacade.observeGame(gameID);
        System.out.println("Game selected: " + games.get(number - 1).gameName());

    }



}
