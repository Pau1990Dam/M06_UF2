package Pojos;


import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;


@Entity
@Table(name = "SOCIS")
public class Soci implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="Soci_ID")
    private long id;

    @NotBlank
    private String cognom;

    @Temporal(TemporalType.DATE)
    @Past
    private Date edat;
    private String direccio;
    //Telefon esta en Logn en comptes de long xq les annotations de validació com '@Size' no funcionen bé
    // amb els tipus primitius.
    @Min(100000000)
    @Digits(integer = 12, fraction = 0)
    private long telefon;
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy="soci", orphanRemoval = true)//cascade=CascadeType.ALL, orphanRemoval=true
    //@org.hibernate.annotations.Cascade( {org.hibernate.annotations.CascadeType.ALL})
    private Collection<Prestec> prestecs;

    public Soci(){}

    public Soci(String cognom, Date edat, String direccio, long telefon) {
        this.cognom = cognom;
        this.edat = edat;
        this.direccio = direccio;
        this.telefon = telefon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCognom() {
        return cognom;
    }

    public void setCognom(String cognom) {
        this.cognom = cognom;
    }

    public Date getEdat() {
        return edat;
    }

    public void setEdat(Date edat) {
        this.edat = edat;
    }

    public String getDireccio() {
        return direccio;
    }

    public void setDireccio(String direccio) {
        this.direccio = direccio;
    }

    public long getTelefon() {
        return telefon;
    }

    public void setTelefon(long telefon) {
        this.telefon = telefon;
    }

    public Collection<Prestec> getPrestecs() {return prestecs; }

    public void setPrestecs(Collection<Prestec> prestecs) { this.prestecs = prestecs;}

    @Transient
    @Override
    public String toString() {
        return "Soci{" +
                "id=" + id +
                ", cognom='" + cognom + '\'' +
                ", edat=" + edat +
                ", direccio='" + direccio + '\'' +
                ", telefon='" + telefon + '\''+
                ", Prestecs='"+ Arrays.toString(this.getPrestecs().toArray());
    }
}
//Socis (nom, cognom, edat, direcció, telèfon)
