package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class MySqlUserDAO implements UserDAO{
    String insertSql = """
            INSERT INTO users (username, password, email)
            VALUES (?, ?, ?)
            """;

    String getSql = """
            SELECT * FROM users WHERE username = ?
            """;

    String clearSql = """
DELETE FROM users
""";


    @Override
    public boolean createUser(UserData user) {
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(insertSql)) {
            statement.setString(1, user.username());
            statement.setString(2, user.password());
            statement.setString(3, user.email());

            statement.executeUpdate();
        } catch (SQLException e) {
            return false;
        } catch (DataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    public UserData getUser(String username) {
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(getSql)) {
            statement.setString(1, username);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    String user = result.getString("username");
                    String pass = result.getString("password");
                    String email = result.getString("email");
                    return new UserData(user, pass, email);
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
