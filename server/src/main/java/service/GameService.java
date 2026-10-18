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
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) {
        if (createGameRequest.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (createGameRequest.authToken() == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }


        AuthData newAuthObject = authObject.getAuth(createGameRequest.authToken());

        if (newAuthObject != null) {
            if (!createGameRequest.gameName().isBlank()) {
                GameData newGame = gameObject.createGame(createGameRequest.gameName());
                return new CreateGameResult(newGame.gameID());
            } else {
                throw new BadRequestException("Error: bad request");
            }
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public void joinGame(JoinGameRequest joinGameRequest) {
        if (joinGameRequest.playerColor() == null || joinGameRequest.gameID() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (joinGameRequest.authToken() == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        AuthData newAuthObject = authObject.getAuth(joinGameRequest.authToken());

        if (newAuthObject != null) {
            GameData myGame = gameObject.getGame(joinGameRequest.gameID());

            if (myGame != null) {
                if ("WHITE".equals(joinGameRequest.playerColor())) {
                    if (myGame.whiteUsername() == null) {
                        GameData newGame = new GameData(myGame.gameID(), newAuthObject.username(), myGame.blackUsername(), myGame.gameName(), myGame.game());
                        gameObject.updateGame(newGame);
                    } else {
                        throw new AlreadyTakenException("Error: already taken");
                    }
                } else if ("BLACK".equals(joinGameRequest.playerColor())){
                    if (myGame.blackUsername() == null) {
                        GameData newGame = new GameData(myGame.gameID(), myGame.whiteUsername(), newAuthObject.username(), myGame.gameName(), myGame.game());
                        gameObject.updateGame(newGame);
                    } else {
                        throw new AlreadyTakenException("Error: already taken");
                    }
                } else {
                    throw new BadRequestException("Error: bad request");
                }
            } else {
                throw new BadRequestException("Error: bad request");
            }
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

}
