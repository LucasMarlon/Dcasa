package projeto.emp.dcasa.models;


import android.location.Location;

import java.io.Serializable;

public class User implements Serializable{

    private String name;
    private String lastName;
    private String phone;
    private String login;
    private String password;

    public User(String name) {
        this.name = name;
    }

    public User(String name, String lastName, String phone, String login, String password) {
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.login = login;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
    }
