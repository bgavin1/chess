package dataaccess;

import model.GameData;

import java.util.Collection;


public interface GameDAO {
    GameData createGame(String gameName);

    void updateGame(GameData game);

    GameData getGame(Integer gameID);

    Collection<GameData> listGames();

    void clear();


}
