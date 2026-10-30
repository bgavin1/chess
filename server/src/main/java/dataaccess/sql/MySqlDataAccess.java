package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;

import java.sql.SQLException;

public class MySqlDataAccess {

    public MySqlDataAccess() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection();
            var statement = conn.createStatement()) {

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (username VARCHAR(255) PRIMARY KEY, 
                        password VARCHAR(255) NOT NULL, 
                        email VARCHAR(255) NOT NULL
                )
                """);
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS auth (authtoken VARCHAR(255) PRIMARY KEY, 
                        username VARCHAR(255) NOT NULL
                )
                """);
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS game (gameid INT PRIMARY KEY, 
                        whiteusername VARCHAR(255),
                    blackusername VARCHAR(255),
                    gamename VARCHAR(255) NOT NULL,
                    gameobject JSON NOT NULL
                )
                """);
        }
    }




}
