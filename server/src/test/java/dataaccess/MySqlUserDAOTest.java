package dataaccess;

import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passoff.model.TestUser;
import passoff.server.TestServerFacade;
import server.Server;
import sql.MySqlUserDAO;

import static org.junit.jupiter.api.Assertions.assertEquals;

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



}
