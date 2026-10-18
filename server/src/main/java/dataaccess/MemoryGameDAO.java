package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MemoryGameDAO implements GameDAO{
    private final Map<Integer, GameData> games = new HashMap<>();
    Random rand = new Random();

    @Override
    public GameData createGame(String gameName) {
        Integer gameID = rand.nextInt(1,1000000);

        while (games.containsKey(gameID)) {
            gameID = rand.nextInt(1,1000000);
        }
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        games.put(newGame.gameID(), newGame);

        return newGame;
    }

    @Override
    public void updateGame(GameData game) {
        Integer thisGameID = game.gameID();
        games.put(thisGameID, game);
    }

    @Override
    public GameData getGame(Integer gameID) {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return games.values();
    }

    @Override
    public void clear() {
        games.clear();
    }
}
