package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    @Test
    void testListGames() {
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        GameService gameService = new GameService(memoryGameDAO, memoryAuthDAO);
        String user = "gavin";
        AuthData auth = memoryAuthDAO.createAuth(user);
        ListGamesRequest request = new ListGamesRequest(auth.authToken());
        ListGamesResult result = gameService.listGames(request);
        assertNotNull(result);
    }

    @Test
    void testCreateGame() {
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        GameService gameService = new GameService(memoryGameDAO, memoryAuthDAO);
        String user = "gavin";
        String gameName = "game1";
        AuthData auth = memoryAuthDAO.createAuth(user);
        CreateGameRequest request = new CreateGameRequest(gameName, auth.authToken());
        CreateGameResult result = gameService.createGame(request);
        assertNotNull(result.gameID());
    }

    @Test
    void testJoinGame() {
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        GameService gameService = new GameService(memoryGameDAO, memoryAuthDAO);
        String user = "gavin";
        String gameName = "game1";
        AuthData auth = memoryAuthDAO.createAuth(user);
        CreateGameRequest request = new CreateGameRequest(gameName, auth.authToken());
        CreateGameResult result = gameService.createGame(request);
        String playerColor = "WHITE";
        JoinGameRequest joinRequest = new JoinGameRequest(playerColor, result.gameID(), auth.authToken());
        gameService.joinGame(joinRequest);
        GameData game = memoryGameDAO.getGame(result.gameID());
        assertEquals("gavin", game.whiteUsername());
    }

    @Test
    void testBadRequestJoinGame() {
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        GameService gameService = new GameService(memoryGameDAO, memoryAuthDAO);
        String user = "gavin";
        String gameName = "game1";
        AuthData auth = memoryAuthDAO.createAuth(user);
        CreateGameRequest request = new CreateGameRequest(gameName, auth.authToken());
        CreateGameResult result = gameService.createGame(request);
        String playerColor = "WHITE";
        JoinGameRequest joinRequest = new JoinGameRequest(null, result.gameID(), auth.authToken());
        assertThrows(BadRequestException.class, () -> {
            gameService.joinGame(joinRequest);
        });
    }

    @Test
    void testUnauthorizedJoinGame() {
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        GameService gameService = new GameService(memoryGameDAO, memoryAuthDAO);
        String user = "gavin";
        String gameName = "game1";
        AuthData auth = memoryAuthDAO.createAuth(user);
        CreateGameRequest request = new CreateGameRequest(gameName, auth.authToken());
        CreateGameResult result = gameService.createGame(request);
        String playerColor = "WHITE";
        JoinGameRequest joinRequest = new JoinGameRequest(playerColor, result.gameID(), null);
        assertThrows(UnauthorizedException.class, () -> {
            gameService.joinGame(joinRequest);
        });
    }

    @Test
    void testBadWhiteUserJoinGame() {
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        GameService gameService = new GameService(memoryGameDAO, memoryAuthDAO);
        String user = "gavin";
        String gameName = "game1";
        AuthData auth = memoryAuthDAO.createAuth(user);
        CreateGameRequest request = new CreateGameRequest(gameName, auth.authToken());
        CreateGameResult result = gameService.createGame(request);
        String playerColor = "WHITE";
        JoinGameRequest joinRequest = new JoinGameRequest(playerColor, result.gameID(), auth.authToken());
        gameService.joinGame(joinRequest);
        assertThrows(AlreadyTakenException.class, () -> {
            gameService.joinGame(joinRequest);
        });
    }

    @Test
    void testBadBlackUserJoinGame() {
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        GameService gameService = new GameService(memoryGameDAO, memoryAuthDAO);
        String user = "gavin";
        String gameName = "game1";
        AuthData auth = memoryAuthDAO.createAuth(user);
        CreateGameRequest request = new CreateGameRequest(gameName, auth.authToken());
        CreateGameResult result = gameService.createGame(request);
        String playerColor = "BLACK";
        JoinGameRequest joinRequest = new JoinGameRequest(playerColor, result.gameID(), auth.authToken());
        gameService.joinGame(joinRequest);
        assertThrows(AlreadyTakenException.class, () -> {
            gameService.joinGame(joinRequest);
        });
    }





}
