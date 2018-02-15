package comp3350.g16.inventorytracking.objects;

//------------------------------------------------------------------------------
//  User
//      A basic User class that hold a username and password.
//      More to be added on in the future.
//------------------------------------------------------------------------------
public class User {

    private String username;
    private String password;

    public User() {
    }

    public User(String username, String password) {

        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
