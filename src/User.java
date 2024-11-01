public class User
{
    private int userId;
    private String userName;
    private String password;
    private String role;

    public User(int userId, String userName, String password, String role)
    {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public int getUserId()
    {
        return userId;
    }
    public String getUserName()
    {
        return userName;
    }
    public String getPassword()
    {
        return password;
    }
    public String getRole()
    {
        return role;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    @Override
    public String toString()
    {
        return getUserId() + "," + getUserName() + "," + getPassword() + "," + getRole();
    }
}
