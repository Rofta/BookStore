import java.util.HashMap;
import java.util.Map;

public class UserInfo
{
    private HashMap<String, User> loginInfo = new HashMap<>();

    UserInfo()
    {
        loginInfo.put("Mihaela123", new User(1, "Mihaela123","mija35", "user"));
        loginInfo.put("Mytro", new User(2, "Mytro", "lukeskywalker2004", "user"));
        loginInfo.put("damian", new User(3, "damian", "chitara56", "user"));
        loginInfo.put("cosminBoss", new User(4, "cosminBoss", "cosmin2004", "user"));

        loginInfo.put("admin", new User(0, "admin", "admin123", "admin"));
    }
    public User getUser(String username)
    {
        return loginInfo.get(username);
    }
    public void addUser(String username, User user)
    {
        loginInfo.put(username, user);
    }
}
