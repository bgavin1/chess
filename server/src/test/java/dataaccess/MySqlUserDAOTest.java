package dataaccess;

import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import passoff.model.TestUser;
import passoff.server.DatabaseTests;
import passoff.server.TestServerFacade;
import server.Server;
import sql.MySqlUserDAO;
import java.lang.reflect.Field;
import dataaccess.DatabaseManager;
import dataaccess.DataAccessException;



import static org.junit.jupiter.api.Assertions.*;

public class MySqlUserDAOTest extends DatabaseTests {



    @Test
    void testCreateUser() {
        MySqlUserDAO userDAO = new MySqlUserDAO();
        UserData user = new UserData("gavin", "123", "email");
        boolean result = userDAO.createUser(user);
        assertEquals(true, result);
    }

    @Test
    void testGetUser() {
        MySqlUserDAO userDAO = new MySqlUserDAO();
        UserData user = new UserData("gavin", "123", "email");
        userDAO.createUser(user);
        UserData result = userDAO.getUser("gavin");
        assertEquals("gavin", result.username());
        assertTrue(BCrypt.checkpw("123", result.password()));
        assertEquals("email", result.email());


    }

    @Test
    void testClear() {
        MySqlUserDAO userDAO = new MySqlUserDAO();
        UserData user = new UserData("gavin", "123", "email");
        userDAO.createUser(user);
        userDAO.clear();
        assertNull(userDAO.getUser("gavin"));
    }

    @Test
    void testNullUsername() {
        MySqlUserDAO userDAO = new MySqlUserDAO();
        UserData user = new UserData(null, "123", "email");
        assertFalse(userDAO.createUser(user));

    }

    @Test
    void testGetUserNull() {
        MySqlUserDAO userDAO = new MySqlUserDAO();
        UserData user = new UserData("gavin", "123", "email");
        assertNull(userDAO.getUser(null));
    }

    @Test
    void testCreateUserDataAccessException() throws Exception {
        MySqlUserDAO userDAO = new MySqlUserDAO();
        UserData user = new UserData("gavin", "123", "email");

        Field urlField = DatabaseManager.class.getDeclaredField("connectionUrl");
        urlField.setAccessible(true);

        String originalUrl = (String) urlField.get(null);

        try {
            urlField.set(null, "jdbc:mysql://localhost:3307");

            assertFalse(userDAO.createUser(user));

        } finally {
            urlField.set(null, originalUrl);
        }
    }






}
