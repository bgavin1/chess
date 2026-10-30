package sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;

public class MySqlAuthDAO implements AuthDAO {
    String insertSql = """
            INSERT INTO auth (authtoken, username)
            VALUES (?, ?)
            """;

    String getSql = """
            SELECT * FROM auth WHERE authtoken = ?
            """;
    String deleteSql = """
            DELETE FROM auth WHERE authtoken = ?
            """;

    String clearSql = """
            DELETE FROM auth
            """;

    @Override
    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(insertSql)) {
            statement.setString(1, authToken);
            statement.setString(2, username);

            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
        throw new RuntimeException(e);
    }

        return new AuthData(authToken, username);
    }

    @Override
    public AuthData getAuth(String authToken) {
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(getSql)) {
                statement.setString(1, authToken);

                try (var result = statement.executeQuery()) {
                    if (result.next()) {
                        String username = result.getString("username");
                        return new AuthData(authToken, username);
                    } else {
                        return null;
                    }
                }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteAuth(String authToken) {
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(deleteSql)) {
            statement.setString(1, authToken);

            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(clearSql)) {

            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    }




