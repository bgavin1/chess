package sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class MySqlUserDAO implements UserDAO {
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
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(insertSql)) {
            statement.setString(1, user.username());
            statement.setString(2, hashedPassword);
            statement.setString(3, user.email());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("User insert failed");
            e.printStackTrace();
            return false;
        } catch (DataAccessException e) {
            System.out.println("User insert failed");
            e.printStackTrace();
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
