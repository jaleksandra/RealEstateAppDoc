/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.cs230;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Aleksandra
 */
@Entity
@Table(name = "informacije")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Informacije.findAll", query = "SELECT i FROM Informacije i")
    , @NamedQuery(name = "Informacije.findByIdInformacije", query = "SELECT i FROM Informacije i WHERE i.idInformacije = :idInformacije")
    , @NamedQuery(name = "Informacije.findBySvojina", query = "SELECT i FROM Informacije i WHERE i.svojina = :svojina")
    , @NamedQuery(name = "Informacije.findByVrednost", query = "SELECT i FROM Informacije i WHERE i.vrednost = :vrednost")
    , @NamedQuery(name = "Informacije.findByTip", query = "SELECT i FROM Informacije i WHERE i.tip = :tip")})
public class Informacije implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_INFORMACIJE")
    private Integer idInformacije;
    @Size(max = 1024)
    @Column(name = "SVOJINA")
    private String svojina;
    @Size(max = 1024)
    @Column(name = "VREDNOST")
    private String vrednost;
    @Size(max = 1024)
    @Column(name = "TIP")
    private String tip;
    @JoinColumn(name = "ID_OGLASA", referencedColumnName = "ID_OGLASA")
    @ManyToOne
    private Oglas idOglasa;

    public Informacije() {
    }

    public Informacije(Integer idInformacije) {
        this.idInformacije = idInformacije;
    }

    public Integer getIdInformacije() {
        return idInformacije;
    }

    public void setIdInformacije(Integer idInformacije) {
        this.idInformacije = idInformacije;
    }

    public String getSvojina() {
        return svojina;
    }

    public void setSvojina(String svojina) {
        this.svojina = svojina;
    }

    public String getVrednost() {
        return vrednost;
    }

    public void setVrednost(String vrednost) {
        this.vrednost = vrednost;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public Oglas getIdOglasa() {
        return idOglasa;
    }

    public void setIdOglasa(Oglas idOglasa) {
        this.idOglasa = idOglasa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInformacije != null ? idInformacije.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Informacije)) {
            return false;
        }
        Informacije other = (Informacije) object;
        if ((this.idInformacije == null && other.idInformacije != null) || (this.idInformacije != null && !this.idInformacije.equals(other.idInformacije))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.metropolitan.cs230.Informacije[ idInformacije=" + idInformacije + " ]";
    }
    
}
