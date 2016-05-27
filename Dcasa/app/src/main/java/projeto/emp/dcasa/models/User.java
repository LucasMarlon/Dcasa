package projeto.emp.dcasa.models;


import android.location.Location;

public class User {

    private Location location;
    private String name;
    private String lastName;
    private String login;
    private String password;
    private byte[] photo;

    public User(Location location, String name) {
        this.location = location;
        this.name = name;
    }

    public User(Location location, String name, String lastName, String login, String password, byte[] photo) {
        this.location = location;
        this.name = name;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
        this.photo = photo;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
