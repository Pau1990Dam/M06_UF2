package Pojos;

import java.io.Serializable;
import java.util.Date;

public class PrestecPK implements Serializable {

    private Llibre llibre;
    private Soci soci;
    private Date data_Inici;
    private Date data_Final;

    public PrestecPK(){}

    public PrestecPK(Llibre llibre, Soci soci, Date data_Inici, Date data_Final) {
        this.llibre = llibre;
        this.soci = soci;
        this.data_Inici = data_Inici;
        this.data_Final = data_Final;
    }

    public Llibre getLlibre() {
        return llibre;
    }

    public Soci getSoci() {
        return soci;
    }

    public Date getData_Inici() {
        return data_Inici;
    }

    public Date getData_Final() { return data_Final;}

    public void setLlibre(Llibre llibre) {this.llibre = llibre;}

    public void setSoci(Soci soci) {this.soci = soci;}

    public void setData_Inici(Date data_Inici) {this.data_Inici = data_Inici;}

    public void setData_Final(Date data_Final) {this.data_Final = data_Final;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrestecPK prestecPK = (PrestecPK) o;

        if (!getLlibre().equals(prestecPK.getLlibre())) return false;
        if (!getSoci().equals(prestecPK.getSoci())) return false;
        if (!getData_Inici().equals(prestecPK.getData_Inici())) return false;
        return getData_Final().equals(prestecPK.getData_Final());
    }

    @Override
    public int hashCode() {
        int result = getLlibre().hashCode();
        result = 31 * result + getSoci().hashCode();
        result = 31 * result + getData_Inici().hashCode();
        result = 31 * result + getData_Final().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PrestecPK{" +
                "llibre=" + llibre +
                ", soci=" + soci +
                ", data_Inici=" + data_Inici +
                ", data_Final=" + data_Final +
                '}';
    }
}
