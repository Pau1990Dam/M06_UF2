package Pojos;


import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by pau on 26/01/16.
 */
@Entity
@Table(name = "PRESTECS")
@IdClass(PrestecPK.class)
public class Prestec implements Serializable {

    @Id
    //@ManyToOne(cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    @ManyToOne(fetch= FetchType.EAGER)//cascade = CascadeType.MERGE,
   // @org.hibernate.annotations.Cascade( {org.hibernate.annotations.CascadeType.ALL})
    @JoinColumn(name = "Llibre_ID")
    private Llibre llibre;

    @Id
    //@ManyToOne(cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    @ManyToOne(fetch= FetchType.EAGER)//cascade = CascadeType.MERGE,
    //@org.hibernate.annotations.Cascade( {org.hibernate.annotations.CascadeType.ALL})
    @JoinColumn(name = "soci_id")
    private Soci soci;

    @Id
    @Temporal(TemporalType.TIMESTAMP)
    private Date data_Inici;

    @Id
    @Temporal(TemporalType.TIMESTAMP)

    private Date data_Final;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data_Entrega_Efectiva;

    public Prestec(){}


    public Llibre getLlibre() {
        return llibre;
    }

    public void setLlibre(Llibre llibre) {
        this.llibre = llibre;
    }


    public Soci getSoci() {
        return soci;
    }

    public void setSoci(Soci soci) {
        this.soci = soci;
    }

    public Date getData_Inici() {
        return data_Inici;
    }

    public void setData_Inici(Date data_Inici) {
        this.data_Inici = data_Inici;
    }

    public Date getData_Final() {
        return data_Final;
    }

   // @Future
    public void setData_Final(Date data_Final) {
        this.data_Final = data_Final;
    }

    public Date getData_Entrega_Efectiva() {
        return data_Entrega_Efectiva;
    }

    public void setData_Entrega_Efectiva(Date data_Entrega_Efectiva) {
        this.data_Entrega_Efectiva = data_Entrega_Efectiva;
    }

    @Transient
    public String getID(){return getLlibre().getId()+" "+getSoci().getId()+" "+getData_Inici()+" "+getData_Final();}

    @Transient
    @Override
    public String toString() {

        return "Soci ID: "+soci.getId()+" Llibre ID: "+llibre.getId()+" Inici Prestec: "+data_Inici+"" +
                " Entrega programada: "+data_Final+" Entrega efectiva: "+data_Entrega_Efectiva;
    }

    @Transient
    public String toStringFormated() {

        return String.format("||%-8.8s||%-8.8s||%-27.27s||%-27.27s||%-27.27s||",soci.getId(),llibre.getId(),data_Inici,data_Final,data_Entrega_Efectiva);
    }

    @Transient
    public String toStringChangeChecker() {

        return llibre.getId()+";"+soci.getId()+";"+data_Inici+";"+data_Final;
    }
}
//Préstec (llibre.getId(), soci.getId(), data_Inici, data_Final, data_Entrega_Efectiva )
/*
"Soci ID: "+soci.getId()+" Llibre ID: "+llibre.getId()+" Inici Prestec: "+data_Inici+"" +
                " Entrega programada: "+data_Final+" Entrega efectiva: "+data_Entrega_Efectiva;
 */