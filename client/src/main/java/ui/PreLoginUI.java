package ui;

import model.LoginResult;
import model.RegisterResult;

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
        while (!choice.equals("Quit")) {
            System.out.println("Welcome!");
            System.out.println("Please enter a command:");
            System.out.println("- Help");
            System.out.println("- Login");
            System.out.println("- Register");
            System.out.println("- Quit");
            choice = scanner.nextLine();
            if (choice.equals("Login")) {
                login();
            } else if (choice.equals("Register")) {
                register();
            } else if (choice.equals("Quit")) {
                quit();
            } else if (choice.equals("Help")) {
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

        LoginResult result = serverFacade.login(username, password);
        PostLoginUI postLoginUI = new PostLoginUI(serverFacade, scanner, username, result.authToken());
        postLoginUI.run();



    }

    public void register() {
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        System.out.println("Email: ");
        String email = scanner.nextLine();

        RegisterResult result = serverFacade.register(username, password, email);
        PostLoginUI postLoginUI = new PostLoginUI(serverFacade, scanner, username, result.authToken());
        postLoginUI.run();

    }

    public void quit() {
        System.out.println("Goodbye!");
    }
}
