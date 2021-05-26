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
@Table(name = "grad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grad.findAll", query = "SELECT g FROM Grad g")
    , @NamedQuery(name = "Grad.findByIdGrada", query = "SELECT g FROM Grad g WHERE g.idGrada = :idGrada")
    , @NamedQuery(name = "Grad.findByNazivGrada", query = "SELECT g FROM Grad g WHERE g.nazivGrada = :nazivGrada")})
public class Grad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_GRADA")
    private Integer idGrada;
    @Size(max = 1024)
    @Column(name = "NAZIV_GRADA")
    private String nazivGrada;
    @OneToMany(mappedBy = "idGrada")
    private Collection<Podrucje> podrucjeCollection;

    public Grad() {
    }

    public Grad(Integer idGrada) {
        this.idGrada = idGrada;
    }

    public Integer getIdGrada() {
        return idGrada;
    }

    public void setIdGrada(Integer idGrada) {
        this.idGrada = idGrada;
    }

    public String getNazivGrada() {
        return nazivGrada;
    }

    public void setNazivGrada(String nazivGrada) {
        this.nazivGrada = nazivGrada;
    }

    @XmlTransient
    public Collection<Podrucje> getPodrucjeCollection() {
        return podrucjeCollection;
    }

    public void setPodrucjeCollection(Collection<Podrucje> podrucjeCollection) {
        this.podrucjeCollection = podrucjeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGrada != null ? idGrada.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grad)) {
            return false;
        }
        Grad other = (Grad) object;
        if ((this.idGrada == null && other.idGrada != null) || (this.idGrada != null && !this.idGrada.equals(other.idGrada))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.metropolitan.cs230.Grad[ idGrada=" + idGrada + " ]";
    }
    
}
