package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;


import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class MySqlGameDAO implements GameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();

    String insertSql = """
            INSERT INTO game (gameid, whiteusername, blackusername, gamename, gameobject)
            VALUES (?, ?, ?, ?, ?)
            """;

    String checkSql = """
            SELECT gameid FROM game WHERE gameid = ?
            """;

    String updateSql = """
            UPDATE game SET whiteusername = ?, blackusername = ?, gameobject = ? WHERE gameid = ?
            """;

    String getSql = """
            SELECT * FROM game WHERE gameid = ?
            """;

    String listSql = """
            SELECT * FROM game
            """;
    String clearSql = """
            DELETE FROM game
            """;

    @Override
    public GameData createGame(String gameName) {
        Random rand = new Random();
        Integer gameID = rand.nextInt(1,1000000);

        while (checkGameID(gameID)) {
            gameID = rand.nextInt(1,1000000);
        }

        ChessGame myGame = new ChessGame();
        Gson gson = new Gson();
        String json = gson.toJson(myGame);

        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(insertSql)) {
            statement.setInt(1, gameID);
            statement.setString(2, null);
            statement.setString(3, null);
            statement.setString(4, gameName);
            statement.setString(5, json);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        GameData newGame = new GameData(gameID, null, null, gameName, myGame);
        return newGame;



    }

    public boolean checkGameID(Integer gameID) {
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(checkSql)) {
            statement.setInt(1, gameID);

            try (var result = statement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateGame(GameData game) {
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(updateSql)) {
                Integer gameID = game.gameID();
                Gson gson = new Gson();
                String json = gson.toJson(game.game());
                statement.setString(1, game.whiteUsername());
                statement.setString(2, game.blackUsername());
                statement.setString(3, json);
                statement.setInt(4, gameID);

                statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public GameData getGame(Integer gameID) {
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(getSql)) {
            statement.setInt(1, gameID);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    String whiteUsername = result.getString("whiteusername");
                    String blackUsername = result.getString("blackusername");
                    String gameName = result.getString("gamename");
                    Gson gson = new Gson();
                    ChessGame restoredGame = gson.fromJson(result.getString("gameobject"), ChessGame.class);
                    return new GameData(gameID, whiteUsername, blackUsername, gameName, restoredGame);
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<GameData> listGames() {
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(listSql)) {

                try (var result = statement.executeQuery()) {
                    while (result.next()) {
                        Integer gameID = result.getInt("gameid");
                        String whiteUsername = result.getString("whiteusername");
                        String blackUsername = result.getString("blackusername");
                        String gameName = result.getString("gamename");
                        Gson gson = new Gson();
                        ChessGame restoredGame = gson.fromJson(result.getString("gameobject"), ChessGame.class);
                        games.put(gameID, new GameData(gameID, whiteUsername, blackUsername, gameName, restoredGame));
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return games.values();
    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(clearSql)) {

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }



}
