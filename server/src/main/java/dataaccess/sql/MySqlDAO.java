package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;

import java.sql.SQLException;

public abstract class MySqlDAO {

        protected void clear(String sql) {
            try (var conn = DatabaseManager.getConnection();
                 var statement = conn.prepareStatement(sql)) {

                statement.executeUpdate();

            } catch (SQLException | DataAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

