package Pojos;

import org.hibernate.validator.constraints.NotBlank;
import MainCode.Main;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;


@Entity
@Table(name="LLIBRES")
public class Llibre implements Serializable{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="Llibre_ID", updatable = false, nullable = false)
    private long id;
    @NotBlank(message = "el campo TITOL no debe ser nulo ni estar compuesto por espacios en blanco")
    private String titol;
    private int nombre_exemplars;
    private String editorial;
    @Min(1)
    @Max(10000)
    private int nombre_pagines;
    @Temporal(TemporalType.DATE)
    private Date any_edicio;
    @NotBlank(message = "el campo AUTOR no debe ser nulo ni estar compuesto por espacios en blanco")
    private String autor;
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy="llibre", orphanRemoval = true)//, cascade=CascadeType.ALL, orphanRemoval=true
    //@org.hibernate.annotations.Cascade( {org.hibernate.annotations.CascadeType.ALL})
    private Collection<Prestec> prestecs;

    public Llibre(){}

    public Llibre(String titol, String editorial, int nombre_pagines, Date any_edicio, String autor) {
        this.titol = titol;
        this.editorial=editorial;
        this.nombre_pagines = nombre_pagines;
        this.any_edicio = any_edicio;
        this.autor = autor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public int getNombre_exemplars() {
        return nombre_exemplars;
    }

    public void setNombre_exemplars(int nombre_exemplars) {
        this.nombre_exemplars = nombre_exemplars;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getNombre_pagines() {
        return nombre_pagines;
    }

    public void setNombre_pagines(int nombre_pagines) {
        this.nombre_pagines = nombre_pagines;
    }

    public Date getAny_edicio() {
        return any_edicio;
    }

    public void setAny_edicio(Date any_edicio) {
        this.any_edicio = any_edicio;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Collection<Prestec> getPrestecs() {return prestecs;}

    public void setPrestecs(Collection<Prestec> prestecs) {this.prestecs = prestecs;}

    @Transient
    @Override
    public String toString() {
        return "Llibre{" +
                "id=" + id +
                ", titol='" + titol + '\'' +
                ", nombre_exemplars=" + nombre_exemplars +
                ", editorial='" + editorial + '\'' +
                ", nombre_pagines=" + nombre_pagines +
                ", any_edicio=" + any_edicio +
                ", autor='" + autor + '\''+
                ", Prestecs='"+ Arrays.toString(this.getPrestecs().toArray());
    }

    @Transient
    public String toStringForTableView() {
        return  titol +";"+ editorial +";"+ nombre_pagines+";"+ Main.convertToLocalDate(any_edicio)+";"+ autor;

    }
}
