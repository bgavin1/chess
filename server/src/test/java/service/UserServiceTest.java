package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Test;
import service.exceptions.BadRequestException;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Test
    public void testRegister() {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        UserService userService = new UserService(memoryUserDAO, memoryAuthDAO);
        String user = "gavin";
        String pass = "123";
        String email = "gavinbell02@gmail.com";
        RegisterRequest registerRequest = new RegisterRequest(user, pass, email);
        RegisterResult result = userService.register(registerRequest);
        assertEquals("gavin", result.username());
        assertNotNull(result.authToken());
    }

    @Test
    public void badRequestTestRegister() {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        UserService userService = new UserService(memoryUserDAO, memoryAuthDAO);
        String user = null;
        String pass = "123";
        String email = "gavinbell02@gmail.com";
        RegisterRequest registerRequest = new RegisterRequest(user, pass, email);
        assertThrows(BadRequestException.class, () -> {
            userService.register(registerRequest);
        });

    }

    @Test
    public void testLogin() {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        UserService userService = new UserService(memoryUserDAO, memoryAuthDAO);
        String user = "gavin";
        String pass = "123";
        String email = "gavinbell02@gmail.com";
        RegisterRequest registerRequest = new RegisterRequest(user, pass, email);
        RegisterResult registerResult = userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest(user, pass);
        LoginResult result = userService.login(loginRequest);
        assertEquals("gavin", result.username());
        assertNotNull(result.authToken());
    }

    @Test
    public void testLogout() {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        UserService userService = new UserService(memoryUserDAO, memoryAuthDAO);
        String user = "gavin";
        String pass = "123";
        String email = "gavinbell02@gmail.com";
        RegisterRequest registerRequest = new RegisterRequest(user, pass, email);
        RegisterResult registerResult = userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest(user, pass);
        LoginResult loginResult = userService.login(loginRequest);
        LogoutRequest logoutRequest = new LogoutRequest(loginResult.authToken());
        userService.logout(logoutRequest);
        assertNull(memoryAuthDAO.getAuth(logoutRequest.authToken()));

    }

}
