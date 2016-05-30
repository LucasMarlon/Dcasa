package projeto.emp.dcasa.models;


import java.io.Serializable;

public enum PROFESSIONAL_TYPE implements Serializable {

    PLUMBER (1, "Encanador"),
    FITTER (2, "Montador"),
    ELECTRICIAN (3, "Eletricista");

    private final int cod;
    private final String type;

    PROFESSIONAL_TYPE(final int cod, final String type) {
        this.cod = cod;
        this.type =  type;

    }
    public int getCod() {
        return cod;
    }

    public String getType() {
        return type;
    }

}
