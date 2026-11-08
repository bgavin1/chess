package ui;

import com.google.gson.Gson;
import model.AuthData;
import model.LoginRequest;
import model.LoginResult;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {

    private String serverAddress;

    ServerFacade(String serverAddress) {
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
            LoginResult result = new Gson().fromJson(response.body(), LoginResult.class);
            return result;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

}
