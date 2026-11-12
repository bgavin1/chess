package client;

import chess.*;
import ui.PreLoginUI;
import ui.ServerFacade;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        final ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
        final Scanner scanner = new Scanner(System.in);
        PreLoginUI preLoginUI = new PreLoginUI(serverFacade, scanner);
        preLoginUI.run();
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
    }
}
