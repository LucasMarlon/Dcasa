package projeto.emp.dcasa.models;


public enum PROFESSIONAL_TYPE {

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
