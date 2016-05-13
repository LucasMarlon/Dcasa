package projeto.emp.dcasa.models;

import android.location.Location;

import java.util.List;
import java.util.Map;

public class Professional {

    private Location location;
    private String phone_number;
    private String nome;
    private Map<User, String> comments;
    private Map<User, Integer> evaluations;
    private PROFESSIONAL_TYPE type;

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Location getLocation() {
        return location;
    }

    public Double calculatesAverageEvaluations () {
        //TODO
        return null;
    }

    public Map<User, Integer> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(Map<User, Integer> evaluations) {
        this.evaluations = evaluations;
    }

    public Map<User, String> getComments() {
        return comments;
    }

    public void setComments(Map<User, String> comments) {
        this.comments = comments;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public PROFESSIONAL_TYPE getType() {
        return type;
    }

    public void setType(PROFESSIONAL_TYPE type) {
        this.type = type;
    }
}
