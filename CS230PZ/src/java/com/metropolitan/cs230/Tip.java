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
@Table(name = "tip")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tip.findAll", query = "SELECT t FROM Tip t")
    , @NamedQuery(name = "Tip.findByIdTipa", query = "SELECT t FROM Tip t WHERE t.idTipa = :idTipa")
    , @NamedQuery(name = "Tip.findByNazivTipa", query = "SELECT t FROM Tip t WHERE t.nazivTipa = :nazivTipa")})
public class Tip implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIPA")
    private Integer idTipa;
    @Size(max = 1024)
    @Column(name = "NAZIV_TIPA")
    private String nazivTipa;
    @OneToMany(mappedBy = "idTipa")
    private Collection<Oglas> oglasCollection;

    public Tip() {
    }

    public Tip(Integer idTipa) {
        this.idTipa = idTipa;
    }

    public Integer getIdTipa() {
        return idTipa;
    }

    public void setIdTipa(Integer idTipa) {
        this.idTipa = idTipa;
    }

    public String getNazivTipa() {
        return nazivTipa;
    }

    public void setNazivTipa(String nazivTipa) {
        this.nazivTipa = nazivTipa;
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
        hash += (idTipa != null ? idTipa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tip)) {
            return false;
        }
        Tip other = (Tip) object;
        if ((this.idTipa == null && other.idTipa != null) || (this.idTipa != null && !this.idTipa.equals(other.idTipa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.metropolitan.cs230.Tip[ idTipa=" + idTipa + " ]";
    }
    
}
