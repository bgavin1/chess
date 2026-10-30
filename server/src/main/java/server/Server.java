package server;

import dataaccess.*;
import io.javalin.*;
import service.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;
import dataaccess.sql.MySqlAuthDAO;
import dataaccess.sql.MySqlDataAccess;
import dataaccess.sql.MySqlGameDAO;
import dataaccess.sql.MySqlUserDAO;

import java.sql.SQLException;
import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final UserService userService;
    private final GameService gameService;
    private final MySqlGameDAO mySqlGameDAO = new MySqlGameDAO();
    private final MySqlUserDAO mySqlUserDAO = new MySqlUserDAO();
    private final MySqlAuthDAO mySqlAuthDAO = new MySqlAuthDAO();


    public Server() {
        try {
            new MySqlDataAccess();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        userService = new UserService(mySqlUserDAO, mySqlAuthDAO);
        gameService = new GameService(mySqlGameDAO, mySqlAuthDAO);

        // Register your endpoints and exception handlers here.
        javalin.delete("/db", ctx -> {
            mySqlUserDAO.clear();
            mySqlGameDAO.clear();
            mySqlAuthDAO.clear();
            ctx.result("{}");
        });


        javalin.post("/user", ctx -> {
            RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
            RegisterResult result = userService.register(registerRequest);
            ctx.result(new Gson().toJson(result));
        });

        javalin.post("/session", ctx -> {
            LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);
            LoginResult result = userService.login(loginRequest);
            ctx.result(new Gson().toJson(result));
        });

        javalin.delete("/session", ctx -> {
            String authToken = ctx.header("Authorization");
            LogoutRequest logoutRequest = new LogoutRequest(authToken);
            userService.logout(logoutRequest);
            ctx.result("{}");
        });

        javalin.get("/game", ctx -> {
            String authToken = ctx.header("Authorization");
            ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
            ListGamesResult result = gameService.listGames(listGamesRequest);
            ctx.result(new Gson().toJson(result));
        });

        javalin.post("/game", ctx -> {
            JsonObject body = new Gson().fromJson(ctx.body(), JsonObject.class);

            String gameName = null;
            if (body.get("gameName") != null && !body.get("gameName").isJsonNull()) {
                gameName = body.get("gameName").getAsString();
            }
            String authToken = ctx.header("Authorization");
            CreateGameRequest createGameRequest = new CreateGameRequest(gameName, authToken);
            CreateGameResult result = gameService.createGame(createGameRequest);
            ctx.result(new Gson().toJson(result));
        });

        javalin.put("/game", ctx -> {
            JsonObject body = new Gson().fromJson(ctx.body(), JsonObject.class);

            String playerColor = null;
            if (body.get("playerColor") != null && !body.get("playerColor").isJsonNull()) {
                playerColor = body.get("playerColor").getAsString();
            }

            Integer gameID = null;
            if (body.get("gameID") != null && !body.get("gameID").isJsonNull()) {
                gameID = body.get("gameID").getAsInt();
            }
            String authToken = ctx.header("Authorization");
            JoinGameRequest joinGameRequest = new JoinGameRequest(playerColor, gameID, authToken);
            gameService.joinGame(joinGameRequest);
            ctx.result("{}");
        });

        configureExceptionHandlers();

    }

    private void configureExceptionHandlers() {
        javalin.exception(BadRequestException.class, (exception, ctx) -> {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", "Error: bad request")));
        });

        javalin.exception(UnauthorizedException.class, (exception, ctx) -> {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", "Error: unauthorized")));
        });

        javalin.exception(AlreadyTakenException.class, (exception, ctx) -> {
            ctx.status(403);
            ctx.result(new Gson().toJson(Map.of("message", "Error: already taken")));
        });

        javalin.exception(Exception.class, (exception, ctx) -> {
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", "Error: Unexpected error")));
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}