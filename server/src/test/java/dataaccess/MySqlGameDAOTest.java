package dataaccess;

import model.GameData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passoff.model.TestUser;
import passoff.server.TestServerFacade;
import server.Server;
import sql.MySqlGameDAO;
import java.lang.reflect.Field;
import dataaccess.DatabaseManager;
import dataaccess.DataAccessException;


import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlGameDAOTest {private static final TestUser TEST_USER = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");

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
    void testCreateGame() {
        MySqlGameDAO gameDAO = new MySqlGameDAO();
        String gameName = "1";
        GameData result = gameDAO.createGame(gameName);
        assertNotNull(result);
    }

    @Test
    void testUpdateGame() {
        MySqlGameDAO gameDAO = new MySqlGameDAO();
        String gameName = "1";
        GameData game = gameDAO.createGame(gameName);
        gameDAO.updateGame(game);
        assertNotNull(game);

    }

    @Test
    void testGetGame() {
        MySqlGameDAO gameDAO = new MySqlGameDAO();
        String gameName = "1";
        GameData game = gameDAO.createGame(gameName);
        GameData result = gameDAO.getGame(game.gameID());
        assertEquals(result, game);

    }

    @Test
    void testListGames() {
        MySqlGameDAO gameDAO = new MySqlGameDAO();
        String gameName = "1";
        GameData game = gameDAO.createGame(gameName);
        Collection<GameData> result = gameDAO.listGames();
        assertNotNull(result);

    }

    @Test
    void testClear() {
        MySqlGameDAO gameDAO = new MySqlGameDAO();
        String gameName = "1";
        GameData game = gameDAO.createGame(gameName);
        gameDAO.clear();
        assertNull(gameDAO.getGame(game.gameID()));
    }

    @Test
    void testCreateNullGameName() {
        MySqlGameDAO gameDAO = new MySqlGameDAO();
        String gameName = null;
        assertThrows(RuntimeException.class,
                () -> gameDAO.createGame(gameName));
    }

    @Test
    void testBadGameID() {
        MySqlGameDAO gameDAO = new MySqlGameDAO();
        Integer gameID = null;
        assertThrows(RuntimeException.class,
                () -> gameDAO.checkGameID(gameID));
    }

    @Test
    void testBadUpdateGame() {
        MySqlGameDAO gameDAO = new MySqlGameDAO();
        GameData game = null;
        assertThrows(RuntimeException.class,
                () -> gameDAO.updateGame(game));
    }

    @Test
    void testCreateGameDataAccessException() throws Exception {
        MySqlGameDAO gameDAO = new MySqlGameDAO();

        Field urlField = DatabaseManager.class.getDeclaredField("connectionUrl");
        urlField.setAccessible(true);

        String originalUrl = (String) urlField.get(null);

        try {
            urlField.set(null, "jdbc:mysql://localhost:3307");

            assertThrows(RuntimeException.class,
                    () -> gameDAO.createGame("Test Game"));

        } finally {
            urlField.set(null, originalUrl);
        }
    }

    @Test
    void testUpdateGameDataAccessException() throws Exception {
        MySqlGameDAO gameDAO = new MySqlGameDAO();
        GameData game = null;

        Field urlField = DatabaseManager.class.getDeclaredField("connectionUrl");
        urlField.setAccessible(true);

        String originalUrl = (String) urlField.get(null);

        try {
            urlField.set(null, "jdbc:mysql://localhost:3307");

            assertThrows(RuntimeException.class,
                    () -> gameDAO.updateGame(game));

        } finally {
            urlField.set(null, originalUrl);
        }
    }

    @Test
    void testGetGameDataAccessException() throws Exception {
        MySqlGameDAO gameDAO = new MySqlGameDAO();
        Integer gameID = null;

        Field urlField = DatabaseManager.class.getDeclaredField("connectionUrl");
        urlField.setAccessible(true);

        String originalUrl = (String) urlField.get(null);

        try {
            urlField.set(null, "jdbc:mysql://localhost:3307");

            assertThrows(RuntimeException.class,
                    () -> gameDAO.getGame(gameID));

        } finally {
            urlField.set(null, originalUrl);
        }
    }




}
