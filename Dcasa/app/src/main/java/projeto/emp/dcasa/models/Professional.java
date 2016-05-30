package projeto.emp.dcasa.models;

import android.location.Location;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Professional implements Serializable{

    private Location location;
    private String phone_number;
    private String name;
    private String cpf;
    private Map<User, String> comments;
    private Map<User, Integer> evaluations;
    private PROFESSIONAL_TYPE type;

    public Professional() {
        this.comments = new HashMap<User, String>();
        this.evaluations = new HashMap<User, Integer>();
    }

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

    public Float getAverageEvaluations () {
        Integer sum = 0;
        for (Integer eval : evaluations.values()) {
            sum += eval;
        }
        float media = 0;
        if (evaluations.size() > 0) {
            media = sum/evaluations.size();
        }
        return media;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Map<User, Integer> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(Map<User, Integer> evaluations) {
        this.evaluations = evaluations;
    }

    public void addEvaluation(User user, Integer evaluation) {
        evaluations.put(user, evaluation);
    }

    public Map<User, String> getComments() {
        return comments;
    }

    public void setComments(Map<User, String> comments) {
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PROFESSIONAL_TYPE getType() {
        return type;
    }

    public void setType(PROFESSIONAL_TYPE type) {
        this.type = type;
    }
}
