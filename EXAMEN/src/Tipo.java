
public class Tipo {

    private Integer idty;
    private String tipus;

    public Tipo(int i, String s) {
        this.idty = i;
        this.tipus = s;
    }

    public Tipo(){}

    public Integer getIdty() {
        return idty;
    }

    public void setIdty(Integer idty) {
        this.idty = idty;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    @Override
    public String toString() {
        return
                "id = " + idty +
                ", nom = " + tipus ;
    }
}
