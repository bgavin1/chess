package ui;

import model.CreateGameResult;
import model.LoginResult;

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
            System.out.println("- Join a Game: type 'Join'");
            System.out.println("- Observe a Game: type 'Observe'");
            choice = scanner.nextLine();
            if (choice.equals("Logout")) {
                logout();
            } else if (choice.equals("Create")) {
                createGame();
            } else if (choice.equals("List")) {
                listGames();
            } else if (choice.equals("Join")) {
                joinGame();
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

    }

    public void listGames() {

    }

    public void joinGame() {

    }

    public void observeGame() {

    }



}
