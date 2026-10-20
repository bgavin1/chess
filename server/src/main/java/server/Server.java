package server;

import dataaccess.*;
import io.javalin.*;
import service.*;
import com.google.gson.Gson;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final UserService userService;
    private final GameService gameService;
    private final MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
    private final MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    private final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();


    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        userService = new UserService(memoryUserDAO, memoryAuthDAO);
        gameService = new GameService(memoryGameDAO, memoryAuthDAO);

        // Register your endpoints and exception handlers here.
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
            LogoutRequest logoutRequest = new Gson().fromJson(ctx.body(), LogoutRequest.class);
            userService.logout(logoutRequest);
            ctx.result("{}");
        });

        javalin.get("/game", ctx -> {
            ListGamesRequest listGamesRequest = new Gson().fromJson(ctx.body(), ListGamesRequest.class);
            ListGamesResult result = gameService.listGames(listGamesRequest);
            ctx.result(new Gson().toJson(result));
        });

        javalin.post("/game", ctx -> {
            CreateGameRequest createGameRequest = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
            CreateGameResult result = gameService.createGame(createGameRequest);
            ctx.result(new Gson().toJson(result));
        });

        javalin.put("/game", ctx -> {
            JoinGameRequest joinGameRequest = new Gson().fromJson(ctx.body(), JoinGameRequest.class);
            gameService.joinGame(joinGameRequest);
            ctx.result("{}");
        });




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