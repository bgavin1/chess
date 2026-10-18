package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class GameService {
    private final GameDAO gameObject;
    private final AuthDAO authObject;

    public GameService(GameDAO gameObject, AuthDAO authObject) {
        this.gameObject = gameObject;
        this.authObject = authObject;
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) {
        AuthData newAuthObject = authObject.getAuth(listGamesRequest.authToken());

        if (newAuthObject != null) {
            Collection<GameResultData> myList = new ArrayList<>();
            Collection<GameData> listOfGames = gameObject.listGames();
            for (GameData object : listOfGames) {
                GameResultData newObject = new GameResultData(object.gameID(), object.whiteUsername(), object.blackUsername(), object.gameName());
                myList.add(newObject);

            }
            return new ListGamesResult(myList);
        } else {
            //throw exception
        }
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) {
        AuthData newAuthObject = authObject.getAuth(createGameRequest.authToken());

        if (newAuthObject != null) {
            if (createGameRequest.gameName() != null && createGameRequest.gameName().isBlank()) {
                GameData newGame = gameObject.createGame(createGameRequest.gameName());
                return new CreateGameResult(newGame.gameID());
            } else {
                //throw exception
            }
        } else {
            //throw exception
        }
    }

    public void joinGame(JoinGameRequest joinGameRequest) {
        AuthData newAuthObject = authObject.getAuth(joinGameRequest.authToken());

        if (newAuthObject != null) {
            GameData myGame = gameObject.getGame(joinGameRequest.gameID());

            if (myGame != null) {
                if ("WHITE".equals(joinGameRequest.playerColor())) {
                    if (myGame.whiteUsername() == null) {
                        GameData newGame = new GameData(myGame.gameID(), newAuthObject.username(), myGame.blackUsername(), myGame.gameName(), myGame.game());
                        gameObject.updateGame(newGame);
                    } else {
                        //throw already taken exception
                    }
                } else if ("BLACK".equals(joinGameRequest.playerColor())){
                    if (myGame.blackUsername() == null) {
                        GameData newGame = new GameData(myGame.gameID(), myGame.whiteUsername(), newAuthObject.username(), myGame.gameName(), myGame.game());
                        gameObject.updateGame(newGame);
                    } else {
                        //throw already taken exception
                    }
                } else {
                    //throw incorrect color exception
                }
            } else {
                //throw game doesn't exist exception
            }
        } else {
            //throw unauthorized exception
        }
    }

}
