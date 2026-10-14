package dataaccess;

import model.UserData;
import java.util.HashMap;
import java.util.Map;

public class UserDAO {
    private final Map<String, UserData> users = new HashMap<>();

    public boolean createUser(UserData user) {
        if (users.containsKey(user.username())) {
            return false;
        } else {
            users.put(user.username(), user);
        }
        return true;

    }

    public UserData getUser(String username) {
        return users.get(username);
    }

    public void clear() {
        users.clear();
    }


}
