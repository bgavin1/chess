package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Test;
import passoff.server.DatabaseTests;
import dataaccess.sql.MySqlAuthDAO;
import java.lang.reflect.Field;


import static org.junit.jupiter.api.Assertions.*;

public class MySqlAuthDAOTest extends DatabaseTests {



    @Test
    void testCreateAuth() {
        MySqlAuthDAO authDAO = new MySqlAuthDAO();
        String username = "1";
        AuthData result = authDAO.createAuth(username);
        assertNotNull(result);
    }

    @Test
    void testGetAuth() {
        MySqlAuthDAO authDAO = new MySqlAuthDAO();
        String username = "1";
        AuthData auth = authDAO.createAuth(username);
        AuthData result = authDAO.getAuth(auth.authToken());
        assertEquals(result, auth);
    }

    @Test
    void testDeleteAuth() {
        MySqlAuthDAO authDAO = new MySqlAuthDAO();
        String username = "1";
        AuthData auth = authDAO.createAuth(username);
        authDAO.deleteAuth(auth.authToken());
        assertNull(authDAO.getAuth(auth.authToken()));
    }

    @Test
    void testClear() {
        MySqlAuthDAO authDAO = new MySqlAuthDAO();
        String username = "1";
        AuthData auth = authDAO.createAuth(username);
        authDAO.clear();
        assertNull(authDAO.getAuth(auth.authToken()));
    }

    @Test
    void testCreateAuthDataAccessException() throws Exception {
        MySqlAuthDAO authDAO = new MySqlAuthDAO();

        Field urlField = DatabaseManager.class.getDeclaredField("connectionUrl");
        urlField.setAccessible(true);

        String originalUrl = (String) urlField.get(null);

        try {
            urlField.set(null, "jdbc:mysql://localhost:3307");

            assertThrows(RuntimeException.class,
                    () -> authDAO.createAuth("Test"));

        } finally {
            urlField.set(null, originalUrl);
        }
    }

    @Test
    void testGetAuthDataAccessException() throws Exception {
        MySqlAuthDAO authDAO = new MySqlAuthDAO();

        Field urlField = DatabaseManager.class.getDeclaredField("connectionUrl");
        urlField.setAccessible(true);

        String originalUrl = (String) urlField.get(null);

        try {
            urlField.set(null, "jdbc:mysql://localhost:3307");

            assertThrows(RuntimeException.class,
                    () -> authDAO.getAuth("Test"));

        } finally {
            urlField.set(null, originalUrl);
        }
    }

    @Test
    void testDeleteAuthDataAccessException() throws Exception {
        MySqlAuthDAO authDAO = new MySqlAuthDAO();

        Field urlField = DatabaseManager.class.getDeclaredField("connectionUrl");
        urlField.setAccessible(true);

        String originalUrl = (String) urlField.get(null);

        try {
            urlField.set(null, "jdbc:mysql://localhost:3307");

            assertThrows(RuntimeException.class,
                    () -> authDAO.deleteAuth("Test"));

        } finally {
            urlField.set(null, originalUrl);
        }
    }

    @Test
    void testClearAuthDataAccessException() throws Exception {
        MySqlAuthDAO authDAO = new MySqlAuthDAO();

        Field urlField = DatabaseManager.class.getDeclaredField("connectionUrl");
        urlField.setAccessible(true);

        String originalUrl = (String) urlField.get(null);

        try {
            urlField.set(null, "jdbc:mysql://localhost:3307");

            assertThrows(RuntimeException.class,
                    () -> authDAO.clear());

        } finally {
            urlField.set(null, originalUrl);
        }
    }




}
