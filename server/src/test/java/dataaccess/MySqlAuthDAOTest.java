package dataaccess;

import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import passoff.server.TestServerFacade;
import server.Server;
import sql.MySqlAuthDAO;
import sql.MySqlGameDAO;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlAuthDAOTest {
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
    void testCreateAuthDataAccessException() {
        MySqlAuthDAO authDAO = new MySqlAuthDAO();

        try (MockedStatic<DatabaseManager> mocked =
                     Mockito.mockStatic(DatabaseManager.class)) {

            mocked.when(DatabaseManager::getConnection)
                    .thenThrow(new DataAccessException("Connection failed"));

            assertThrows(RuntimeException.class,
                    () -> authDAO.createAuth("Test"));
        }
    }

    @Test
    void testGetAuthDataAccessException() {
        MySqlAuthDAO authDAO = new MySqlAuthDAO();

        try (MockedStatic<DatabaseManager> mocked =
                     Mockito.mockStatic(DatabaseManager.class)) {

            mocked.when(DatabaseManager::getConnection)
                    .thenThrow(new DataAccessException("Connection failed"));

            assertThrows(RuntimeException.class,
                    () -> authDAO.getAuth("Test"));
        }
    }

    @Test
    void testDeleteAuthDataAccessException() {
        MySqlAuthDAO authDAO = new MySqlAuthDAO();

        try (MockedStatic<DatabaseManager> mocked =
                     Mockito.mockStatic(DatabaseManager.class)) {

            mocked.when(DatabaseManager::getConnection)
                    .thenThrow(new DataAccessException("Connection failed"));

            assertThrows(RuntimeException.class,
                    () -> authDAO.deleteAuth("Test"));
        }
    }

    @Test
    void testClearDataAccessException() {
        MySqlAuthDAO authDAO = new MySqlAuthDAO();

        try (MockedStatic<DatabaseManager> mocked =
                     Mockito.mockStatic(DatabaseManager.class)) {

            mocked.when(DatabaseManager::getConnection)
                    .thenThrow(new DataAccessException("Connection failed"));

            assertThrows(RuntimeException.class,
                    () -> authDAO.clear());
        }
    }


}
