package dataaccess;

import model.GameData;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import chess.ChessGame;

public class GameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();
    Random rand = new Random();

    public boolean createGame(String gameName) {
        Integer gameID = rand.nextInt(1,1000000);

        while (games.containsKey(gameID)) {
            gameID = rand.nextInt(1,1000000);
        }
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        games.put(newGame.gameID(), newGame);

        return true;
    }





}
