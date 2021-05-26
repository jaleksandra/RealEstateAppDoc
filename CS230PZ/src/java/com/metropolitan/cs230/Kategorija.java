/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.cs230;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Aleksandra
 */
@Entity
@Table(name = "kategorija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Kategorija.findAll", query = "SELECT k FROM Kategorija k")
    , @NamedQuery(name = "Kategorija.findByIdKategorije", query = "SELECT k FROM Kategorija k WHERE k.idKategorije = :idKategorije")
    , @NamedQuery(name = "Kategorija.findByNazivKategorije", query = "SELECT k FROM Kategorija k WHERE k.nazivKategorije = :nazivKategorije")})
public class Kategorija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_KATEGORIJE")
    private Integer idKategorije;
    @Size(max = 1024)
    @Column(name = "NAZIV_KATEGORIJE")
    private String nazivKategorije;
    @OneToMany(mappedBy = "idKategorije")
    private Collection<Oglas> oglasCollection;

    public Kategorija() {
    }

    public Kategorija(Integer idKategorije) {
        this.idKategorije = idKategorije;
    }

    public Integer getIdKategorije() {
        return idKategorije;
    }

    public void setIdKategorije(Integer idKategorije) {
        this.idKategorije = idKategorije;
    }

    public String getNazivKategorije() {
        return nazivKategorije;
    }

    public void setNazivKategorije(String nazivKategorije) {
        this.nazivKategorije = nazivKategorije;
    }

    @XmlTransient
    public Collection<Oglas> getOglasCollection() {
        return oglasCollection;
    }

    public void setOglasCollection(Collection<Oglas> oglasCollection) {
        this.oglasCollection = oglasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idKategorije != null ? idKategorije.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kategorija)) {
            return false;
        }
        Kategorija other = (Kategorija) object;
        if ((this.idKategorije == null && other.idKategorije != null) || (this.idKategorije != null && !this.idKategorije.equals(other.idKategorije))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.metropolitan.cs230.Kategorija[ idKategorije=" + idKategorije + " ]";
    }
    
}
