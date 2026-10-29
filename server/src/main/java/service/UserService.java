package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;

public class UserService {
    private final UserDAO userObject;
    private final AuthDAO authObject;

    public UserService(UserDAO userObject, AuthDAO authObject) {
        this.userObject = userObject;
        this.authObject = authObject;
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            throw new BadRequestException("Error: bad request");
        }


        if (userObject.getUser(registerRequest.username()) == null) {
            UserData newUserObject = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            System.out.println(userObject.getClass().getName());
            userObject.createUser(newUserObject);

            AuthData myAuth = authObject.createAuth(registerRequest.username());
            return new RegisterResult(registerRequest.username(), myAuth.authToken());
        } else {
            throw new AlreadyTakenException("Error: already taken");
        }
    }


    public LoginResult login(LoginRequest loginRequest) {
        if (loginRequest.username() == null || loginRequest.password() == null) {
            throw new BadRequestException("Error: bad request");
        }

        UserData newUserObject = userObject.getUser(loginRequest.username());
        if (newUserObject != null) {
            if (BCrypt.checkpw(loginRequest.password(), newUserObject.password())) {
                AuthData myAuth = authObject.createAuth(loginRequest.username());
                return new LoginResult(loginRequest.username(), myAuth.authToken());
            } else {
               throw new UnauthorizedException("Error: unauthorized");
            }
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }


    public void logout(LogoutRequest logoutRequest) {
        if (authObject.getAuth(logoutRequest.authToken()) != null) {
            authObject.deleteAuth(logoutRequest.authToken());
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }





}
