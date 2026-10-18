package dataaccess;

import model.UserData;


public interface UserDAO {
    boolean createUser(UserData user);

    UserData getUser(String username);

    void clear();

}
