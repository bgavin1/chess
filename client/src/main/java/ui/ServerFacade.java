package ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ServerFacade {

    private String serverAddress;

    public ServerFacade(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public LoginResult login(String username, String password) {
        try {
            URI uri = URI.create(serverAddress + "/session");
            LoginRequest loginRequest = new LoginRequest(username, password);
            Gson gson = new Gson();
            String json = gson.toJson(loginRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                LoginResult result = new Gson().fromJson(response.body(), LoginResult.class);
                return result;
            } else if (response.statusCode() == 400){
                throw new RuntimeException("Invalid username/password. Try something else");
            } else if (response.statusCode() == 401){
                throw new RuntimeException("No user with that information");
            } else {
                throw new RuntimeException("Server error");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public RegisterResult register(String username, String password, String email) {
        try {
            URI uri = URI.create(serverAddress + "/user");
            RegisterRequest registerRequest = new RegisterRequest(username, password, email);
            Gson gson = new Gson();
            String json = gson.toJson(registerRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                RegisterResult result = new Gson().fromJson(response.body(), RegisterResult.class);
                return result;
            } else if (response.statusCode() == 400){
                throw new RuntimeException("Invalid username/password. Try something else");
            } else if (response.statusCode() == 401 || response.statusCode() == 403){
                throw new RuntimeException("Username/password already taken");
            } else {
                throw new RuntimeException("server error");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout(String authToken) {
        try {
            URI uri = URI.create(serverAddress + "/session");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Authorization", authToken)
                    .DELETE()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {

            } else if (response.statusCode() == 400) {
                throw new RuntimeException("No user to log out");
            } else {
                throw new RuntimeException("server error");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CreateGameResult createGame(String gameName, String authToken) {
        try {
            URI uri = URI.create(serverAddress + "/game");
            JsonObject body = new JsonObject();
            body.addProperty("gameName", gameName);
            Gson gson = new Gson();
            String json = gson.toJson(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Authorization", authToken)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Invalid request to create a game");
            } else {
                CreateGameResult result = gson.fromJson(response.body(), CreateGameResult.class);
                return result;
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ListGamesResult listGames(String authToken) {
        try {
            URI uri = URI.create(serverAddress + "/game");
            Gson gson = new Gson();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Authorization", authToken)
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Invalid request to list games");
            } else {
                ListGamesResult result = gson.fromJson(response.body(), ListGamesResult.class);
                return result;
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void playGame(String playerColor, Integer gameID, String authToken) {
        try {
            URI uri = URI.create(serverAddress + "/game");
            JsonObject body = new JsonObject();
            body.addProperty("gameID", gameID);
            body.addProperty("playerColor", playerColor);
            Gson gson = new Gson();
            String json = gson.toJson(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Authorization", authToken)
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {

            } else if (response.statusCode() == 400 || response.statusCode() == 401) {
                throw new RuntimeException("Invalid request to join a game");
            } else if (response.statusCode() == 403) {
                throw new RuntimeException("Color already taken");
            } else {
                throw new RuntimeException("server error");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void observeGame(int gameID) {
    }


}
