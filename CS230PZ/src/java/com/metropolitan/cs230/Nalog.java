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
@Table(name = "nalog")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nalog.findAll", query = "SELECT n FROM Nalog n")
    , @NamedQuery(name = "Nalog.findByIdNaloga", query = "SELECT n FROM Nalog n WHERE n.idNaloga = :idNaloga")
    , @NamedQuery(name = "Nalog.findByKorisnickoIme", query = "SELECT n FROM Nalog n WHERE n.korisnickoIme = :korisnickoIme")
    , @NamedQuery(name = "Nalog.findByLozinka", query = "SELECT n FROM Nalog n WHERE n.lozinka = :lozinka")
    , @NamedQuery(name = "Nalog.findByIme", query = "SELECT n FROM Nalog n WHERE n.ime = :ime")
    , @NamedQuery(name = "Nalog.findByPrezime", query = "SELECT n FROM Nalog n WHERE n.prezime = :prezime")})
public class Nalog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_NALOGA")
    private Integer idNaloga;
    @Size(max = 1024)
    @Column(name = "KORISNICKO_IME")
    private String korisnickoIme;
    @Size(max = 1024)
    @Column(name = "LOZINKA")
    private String lozinka;
    @Size(max = 1024)
    @Column(name = "IME")
    private String ime;
    @Size(max = 1024)
    @Column(name = "PREZIME")
    private String prezime;
    @OneToMany(mappedBy = "idNaloga")
    private Collection<Oglas> oglasCollection;

    public Nalog() {
    }

    public Nalog(Integer idNaloga) {
        this.idNaloga = idNaloga;
    }

    public Integer getIdNaloga() {
        return idNaloga;
    }

    public void setIdNaloga(Integer idNaloga) {
        this.idNaloga = idNaloga;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
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
        hash += (idNaloga != null ? idNaloga.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Nalog)) {
            return false;
        }
        Nalog other = (Nalog) object;
        if ((this.idNaloga == null && other.idNaloga != null) || (this.idNaloga != null && !this.idNaloga.equals(other.idNaloga))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.metropolitan.cs230.Nalog[ idNaloga=" + idNaloga + " ]";
    }
    
}
