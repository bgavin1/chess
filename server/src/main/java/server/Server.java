package server;

import io.javalin.*;
import service.BadRequestException;
import com.google.gson.Gson;
import service.UnauthorizedException;
import service.AlreadyTakenException;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
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