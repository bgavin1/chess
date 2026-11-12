package ui;

import model.LoginResult;
import model.RegisterResult;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class PreLoginUI {

    private final ServerFacade serverFacade;
    private final Scanner scanner;

    public PreLoginUI(ServerFacade serverFacade, Scanner scanner) {
        this.serverFacade = serverFacade;
        this.scanner = scanner;
    }

    public void run() {
        String choice = "";
        while (!choice.equals("QUIT")) {
            System.out.println("Welcome!");
            System.out.println("Please enter a command:");
            System.out.println("- Help");
            System.out.println("- Login");
            System.out.println("- Register");
            System.out.println("- Quit");
            choice = scanner.nextLine().toUpperCase();
            if (choice.equals("LOGIN")) {
                login();
            } else if (choice.equals("REGISTER")) {
                register();
            } else if (choice.equals("QUIT")) {
                quit();
            } else if (choice.equals("HELP")) {
                help();
            } else {
                System.out.println("Invalid command. Try again");
            }
        }
    }

    public void help() {
        System.out.println("Register: create an account");
        System.out.println("Login: if you've already registered an account, log in");
        System.out.println("Quit: End the session");
        System.out.println();
    }

    public void login() {
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();

        try {
            LoginResult result = serverFacade.login(username, password);
            PostLoginUI postLoginUI = new PostLoginUI(serverFacade, scanner, username, result.authToken());
            postLoginUI.run();
        } catch (RuntimeException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    public void register() {
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        System.out.println("Email: ");
        String email = scanner.nextLine();

        try {
            RegisterResult result = serverFacade.register(username, password, email);
            PostLoginUI postLoginUI = new PostLoginUI(serverFacade, scanner, username, result.authToken());
            postLoginUI.run();
        } catch (RuntimeException e) {
            System.out.println("Registering failed: " + e.getMessage());
        }
    }

    public void quit() {
        System.out.println("Goodbye!");
    }
}
