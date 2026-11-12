package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.CreateGameResult;
import model.GameResultData;
import model.ListGamesResult;
import model.LoginResult;
import chess.ChessBoard;

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
            System.out.printf("Welcome, %s! ", username);
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
        try {
            serverFacade.logout(authToken);
        } catch (RuntimeException e) {
            System.out.println("Logout failed: " + e.getMessage());
        }
    }

    public void createGame() {
        System.out.println("Name your game: ");
        String gameName = scanner.nextLine();

        try {
            CreateGameResult result = serverFacade.createGame(gameName, authToken);
            System.out.println("Game created!");
        } catch (RuntimeException e) {
            System.out.println("Create Game failure: " + e.getMessage());
        }
    }

    public ListGamesResult listGames() {
        try {
            ListGamesResult result = serverFacade.listGames(authToken);

            int number = 1;
            for (GameResultData game : result.games()) {
                String white = (game.whiteUsername() == null ? "Available" : game.whiteUsername());
                String black = (game.blackUsername() == null ? "Available" : game.blackUsername());

                System.out.println(number + "." + game.gameName()
                        + "- White: " + white + ", Black: " + black);
                number++;
            }
            return result;
        } catch (RuntimeException e) {
            System.out.println("Failure to list games: " + e.getMessage());
            return null;
        }
    }

    public void playGame() {
        try {
            ChessGame game = new ChessGame();
            ListGamesResult result = listGames();
            System.out.println("What number is your game in the list? ");
            int number = Integer.parseInt(scanner.nextLine());
            System.out.println("What color would you like to be? ");
            String playerColor = scanner.nextLine().toUpperCase();

            List<GameResultData> games = new ArrayList<>(result.games());
            if (number > games.size() || number < 1) {
                throw new RuntimeException("Invalid list number chosen");
            }
            Integer gameID = games.get(number - 1).gameID();

            serverFacade.playGame(playerColor, gameID, authToken);
            System.out.println("Game joined!");
            drawBoard(game.getBoard(), ChessGame.TeamColor.valueOf(playerColor));
        } catch (RuntimeException e) {
            System.out.println("Failure to join game: "+ e.getMessage());
        }

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

    public void drawBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        List<Character> columns = new ArrayList<>(List.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'));
        int rowStart;
        int rowEnd;
        int rowChange;
        int colStart;
        int colEnd;
        int colChange;

        if (perspective == ChessGame.TeamColor.WHITE) {
            rowStart = 8;
            rowEnd = 1;
            rowChange = -1;
            colStart = 8;
            colEnd = 1;
            colChange = -1;
        } else {
            columns = columns.reversed();
            rowStart = 1;
            rowEnd = 8;
            rowChange = 1;
            colStart = 1;
            colEnd = 8;
            colChange = 1;
        }
        System.out.print("   ");
        for (char item : columns) {
            System.out.print(" " + item + " ");
        }
        System.out.println();
        for (int row = rowStart;  rowChange * (row - rowEnd) <= 0; row += rowChange) {
            System.out.print(" " + row + " ");
            for (int col = colStart; colChange * (col - colEnd) <= 0; col += colChange) {

                if ((row + col) % 2 == 0) {
                    System.out.print("\u001B[43m");
                } else {
                    System.out.print("\u001B[47m");
                }
                ChessPosition myPosition = new ChessPosition(row, col);
                ChessPiece myPiece = board.getPiece(myPosition);
                String myPieceLetter = getPieceSymbol(myPiece);
                if (myPiece != null) {
                    if (myPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        System.out.print("\u001B[31m");
                    } else {
                        System.out.print("\u001B[34m");
                    }
                }
                System.out.print(" " + myPieceLetter + " ");
                System.out.print("\u001B[0m");
            }
            System.out.print(" " + row + " ");
            System.out.println();
        }
        System.out.print("   ");
        for (char item : columns) {
            System.out.print(" " + item + " ");
        }
        System.out.println();
    }

    public String getPieceSymbol(ChessPiece piece) {
        if (piece == null) {
            return " ";
        } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            return "K";
        } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            return "Q";
        } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            return "B";
        } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            return "N";
        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            return "R";
        } else {
            return "P";
        }
    }


}
