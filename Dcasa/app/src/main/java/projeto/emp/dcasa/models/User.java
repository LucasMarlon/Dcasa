package projeto.emp.dcasa.models;


import android.location.Location;

public class User {

    private Location location;
    private String nome;

    public User(Location location, String nome) {
        this.location = location;
        this.nome = nome;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
