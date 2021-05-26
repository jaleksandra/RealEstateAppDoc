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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "podrucje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Podrucje.findAll", query = "SELECT p FROM Podrucje p")
    , @NamedQuery(name = "Podrucje.findByIdPodrucja", query = "SELECT p FROM Podrucje p WHERE p.idPodrucja = :idPodrucja")
    , @NamedQuery(name = "Podrucje.findByImePodrucja", query = "SELECT p FROM Podrucje p WHERE p.imePodrucja = :imePodrucja")})
public class Podrucje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_PODRUCJA")
    private Integer idPodrucja;
    @Size(max = 1024)
    @Column(name = "IME_PODRUCJA")
    private String imePodrucja;
    @OneToMany(mappedBy = "idPodrucja")
    private Collection<Oglas> oglasCollection;
    @JoinColumn(name = "ID_GRADA", referencedColumnName = "ID_GRADA")
    @ManyToOne
    private Grad idGrada;

    public Podrucje() {
    }

    public Podrucje(Integer idPodrucja) {
        this.idPodrucja = idPodrucja;
    }

    public Integer getIdPodrucja() {
        return idPodrucja;
    }

    public void setIdPodrucja(Integer idPodrucja) {
        this.idPodrucja = idPodrucja;
    }

    public String getImePodrucja() {
        return imePodrucja;
    }

    public void setImePodrucja(String imePodrucja) {
        this.imePodrucja = imePodrucja;
    }

    @XmlTransient
    public Collection<Oglas> getOglasCollection() {
        return oglasCollection;
    }

    public void setOglasCollection(Collection<Oglas> oglasCollection) {
        this.oglasCollection = oglasCollection;
    }

    public Grad getIdGrada() {
        return idGrada;
    }

    public void setIdGrada(Grad idGrada) {
        this.idGrada = idGrada;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPodrucja != null ? idPodrucja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Podrucje)) {
            return false;
        }
        Podrucje other = (Podrucje) object;
        if ((this.idPodrucja == null && other.idPodrucja != null) || (this.idPodrucja != null && !this.idPodrucja.equals(other.idPodrucja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.metropolitan.cs230.Podrucje[ idPodrucja=" + idPodrucja + " ]";
    }
    
}
