package client;

import dataaccess.sql.MySqlAuthDAO;
import dataaccess.sql.MySqlGameDAO;
import dataaccess.sql.MySqlUserDAO;
import model.AuthData;
import model.CreateGameResult;
import model.LoginResult;
import model.RegisterResult;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;


import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade serverFacade;
    private final MySqlGameDAO mySqlGameDAO = new MySqlGameDAO();
    private final MySqlUserDAO mySqlUserDAO = new MySqlUserDAO();
    private final MySqlAuthDAO mySqlAuthDAO = new MySqlAuthDAO();

    @BeforeEach
    public void init() {
        server = new Server();
        var port = server.run(0);
        serverFacade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
        mySqlUserDAO.clear();
        mySqlAuthDAO.clear();
        mySqlGameDAO.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    void loginTest() {
        serverFacade.register("player1", "password", "p1@email.com");
        LoginResult result = serverFacade.login("player1", "password");
        assertNotNull(result);
    }

    @Test
    void registerTest() {
        RegisterResult result = serverFacade.register("player1", "password", "p1@email.com");
        String myAuthToken = result.authToken();
        assertEquals(new RegisterResult("player1", myAuthToken), result);
    }

    @Test
    void logoutTest() {
        serverFacade.register("player1", "password", "p1@email.com");
        LoginResult result = serverFacade.login("player1", "password");
        String authToken = result.authToken();
        serverFacade.logout(authToken);
        assertThrows(RuntimeException.class, () -> {
            serverFacade.listGames(authToken);
        });
    }

    @Test
    void createGameTest() {
        serverFacade.register("player1", "password", "p1@email.com");
        LoginResult result = serverFacade.login("player1", "password");
        String authToken = result.authToken();
        serverFacade.createGame("myGame", authToken);
        assertNotNull(serverFacade.listGames(result.authToken()));

    }

    @Test
    void playGameTest() {
        serverFacade.register("player1", "password", "p1@email.com");
        LoginResult result = serverFacade.login("player1", "password");
        String authToken = result.authToken();
        CreateGameResult gameResult = serverFacade.createGame("myGame", authToken);
        Integer gameID = gameResult.gameID();
        serverFacade.playGame("WHITE", gameID, authToken);
    }

    @Test
    void badLoginTest() {
        serverFacade.register("player1", "password", "p1@email.com");
        assertThrows(RuntimeException.class, () -> {
            serverFacade.login("player2", "password");
        });
    }

    @Test
    void badRegisterTest() {
        assertThrows(RuntimeException.class, () -> {
            serverFacade.register(null, "password", null);
        });    }

    @Test
    void badLogoutTest() {
        assertThrows(RuntimeException.class, () -> {
            serverFacade.logout(null);
        });
    }

    @Test
    void badCreateGameTest() {
        serverFacade.register("player1", "password", "p1@email.com");
        serverFacade.login("player1", "password");
        assertThrows(RuntimeException.class, () -> {
            serverFacade.createGame(null, null);
        });
    }

    @Test
    void badListGameTest() {
        serverFacade.register("player1", "password", "p1@email.com");
        serverFacade.login("player1", "password");
        assertThrows(RuntimeException.class, () -> {
            serverFacade.listGames(null);
        });
    }

    @Test
    void badPlayGameTest() {
        serverFacade.register("player1", "password", "p1@email.com");
        LoginResult result = serverFacade.login("player1", "password");
        String authToken = result.authToken();
        CreateGameResult gameResult = serverFacade.createGame("myGame", authToken);
        Integer gameID = gameResult.gameID();
        serverFacade.playGame("WHITE", gameID, authToken);
        assertThrows(RuntimeException.class, () -> {
            serverFacade.playGame("WHITE", gameID, authToken);
        });
    }

    @Test
    void badPlayGameTest2() {
        serverFacade.register("player1", "password", "p1@email.com");
        LoginResult result = serverFacade.login("player1", "password");
        String authToken = result.authToken();
        CreateGameResult gameResult = serverFacade.createGame("myGame", authToken);
        Integer gameID = gameResult.gameID();
        serverFacade.playGame("WHITE", gameID, authToken);
        assertThrows(RuntimeException.class, () -> {
            serverFacade.playGame(null, gameID, authToken);
        });
    }

    @Test
    void badRegisterTest2() {
        RegisterResult result = serverFacade.register("player1", "password", "p1@email.com");
        assertThrows(RuntimeException.class, () -> {
            serverFacade.register("player1", "password", "p1@email.com");
        });
    }
}
