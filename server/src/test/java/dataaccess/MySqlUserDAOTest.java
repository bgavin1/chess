package dataaccess;

import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import passoff.model.TestUser;
import passoff.server.TestServerFacade;
import server.Server;
import sql.MySqlUserDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import org.mockito.MockedStatic;
import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.*;

public class MySqlUserDAOTest {
    private static final TestUser TEST_USER = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");

    private static TestServerFacade serverFacade;

    private static Server server;

    private static Class<?> databaseManagerClass;

    @BeforeAll
    public static void startServer() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        serverFacade = new TestServerFacade("localhost", Integer.toString(port));
    }

    @BeforeEach
    public void setUp() {
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

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
    void testCreateUserDataAccessException() {
        MySqlUserDAO userDAO = new MySqlUserDAO();
        UserData user = new UserData("gavin", "123", "email");

        try (MockedStatic<DatabaseManager> mocked =
                     Mockito.mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection)
                    .thenThrow(new DataAccessException("Connection failed"));

            assertFalse(userDAO.createUser(user));
        }
    }


}
