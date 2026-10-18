package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDAO userObject;
    private final AuthDAO authObject;

    public UserService(UserDAO userObject, AuthDAO authObject) {
        this.userObject = userObject;
        this.authObject = authObject;
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        if (userObject.getUser(registerRequest.username()) == null) {
            UserData newUserObject = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            userObject.createUser(newUserObject);

            AuthData myAuth = authObject.createAuth(registerRequest.username());
            return new RegisterResult(registerRequest.username(), myAuth.authToken());
        } else {
            //throw AlreadyTakenException;
        }
    }


    public LoginResult login(LoginRequest loginRequest) {
        UserData newUserObject = userObject.getUser(loginRequest.username());
        if (newUserObject != null) {
            if (newUserObject.password().equals(loginRequest.password())) {
                AuthData myAuth = authObject.createAuth(loginRequest.username());
                return new LoginResult(loginRequest.username(), myAuth.authToken());
            } else {
               // throw unauthorized exception or something like incorrect password
            }
        } else {
            // throw exception
        }
    }


    public void logout(LogoutRequest logoutRequest) {
        if (authObject.getAuth(logoutRequest.authToken()) != null) {
            authObject.deleteAuth(logoutRequest.authToken());
        } else {
            //throw exception
        }
    }





}
