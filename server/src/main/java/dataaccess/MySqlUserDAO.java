package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class MySqlUserDAO implements UserDAO{
    String sql = """
            INSERT INTO users (username, password, email)
            VALUES (?, ?, ?)
            """;


    @Override
    public boolean createUser(UserData user) {
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql)) {
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

}
