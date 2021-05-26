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
@Table(name = "oglas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Oglas.findAll", query = "SELECT o FROM Oglas o")
    , @NamedQuery(name = "Oglas.findByIdOglasa", query = "SELECT o FROM Oglas o WHERE o.idOglasa = :idOglasa")
    , @NamedQuery(name = "Oglas.findByNazivOglasa", query = "SELECT o FROM Oglas o WHERE o.nazivOglasa = :nazivOglasa")
    , @NamedQuery(name = "Oglas.findByOpis", query = "SELECT o FROM Oglas o WHERE o.opis = :opis")
    , @NamedQuery(name = "Oglas.findByLokacija", query = "SELECT o FROM Oglas o WHERE o.lokacija = :lokacija")
    , @NamedQuery(name = "Oglas.findByCena", query = "SELECT o FROM Oglas o WHERE o.cena = :cena")
    , @NamedQuery(name = "Oglas.findByPovrsina", query = "SELECT o FROM Oglas o WHERE o.povrsina = :povrsina")
    , @NamedQuery(name = "Oglas.findByAdresa", query = "SELECT o FROM Oglas o WHERE o.adresa = :adresa")})
public class Oglas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_OGLASA")
    private Integer idOglasa;
    @Size(max = 1024)
    @Column(name = "NAZIV_OGLASA")
    private String nazivOglasa;
    @Size(max = 1024)
    @Column(name = "OPIS")
    private String opis;
    @Size(max = 1024)
    @Column(name = "LOKACIJA")
    private String lokacija;
    @Column(name = "CENA")
    private Integer cena;
    @Column(name = "POVRSINA")
    private Integer povrsina;
    @Size(max = 1024)
    @Column(name = "ADRESA")
    private String adresa;
    @OneToMany(mappedBy = "idOglasa")
    private Collection<Informacije> informacijeCollection;
    @JoinColumn(name = "ID_KATEGORIJE", referencedColumnName = "ID_KATEGORIJE")
    @ManyToOne
    private Kategorija idKategorije;
    @JoinColumn(name = "ID_PODRUCJA", referencedColumnName = "ID_PODRUCJA")
    @ManyToOne
    private Podrucje idPodrucja;
    @JoinColumn(name = "ID_TIPA", referencedColumnName = "ID_TIPA")
    @ManyToOne
    private Tip idTipa;
    @JoinColumn(name = "ID_NALOGA", referencedColumnName = "ID_NALOGA")
    @ManyToOne
    private Nalog idNaloga;

    public Oglas() {
    }

    public Oglas(Integer idOglasa) {
        this.idOglasa = idOglasa;
    }

    public Integer getIdOglasa() {
        return idOglasa;
    }

    public void setIdOglasa(Integer idOglasa) {
        this.idOglasa = idOglasa;
    }

    public String getNazivOglasa() {
        return nazivOglasa;
    }

    public void setNazivOglasa(String nazivOglasa) {
        this.nazivOglasa = nazivOglasa;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public Integer getCena() {
        return cena;
    }

    public void setCena(Integer cena) {
        this.cena = cena;
    }

    public Integer getPovrsina() {
        return povrsina;
    }

    public void setPovrsina(Integer povrsina) {
        this.povrsina = povrsina;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    @XmlTransient
    public Collection<Informacije> getInformacijeCollection() {
        return informacijeCollection;
    }

    public void setInformacijeCollection(Collection<Informacije> informacijeCollection) {
        this.informacijeCollection = informacijeCollection;
    }

    public Kategorija getIdKategorije() {
        return idKategorije;
    }

    public void setIdKategorije(Kategorija idKategorije) {
        this.idKategorije = idKategorije;
    }

    public Podrucje getIdPodrucja() {
        return idPodrucja;
    }

    public void setIdPodrucja(Podrucje idPodrucja) {
        this.idPodrucja = idPodrucja;
    }

    public Tip getIdTipa() {
        return idTipa;
    }

    public void setIdTipa(Tip idTipa) {
        this.idTipa = idTipa;
    }

    public Nalog getIdNaloga() {
        return idNaloga;
    }

    public void setIdNaloga(Nalog idNaloga) {
        this.idNaloga = idNaloga;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOglasa != null ? idOglasa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Oglas)) {
            return false;
        }
        Oglas other = (Oglas) object;
        if ((this.idOglasa == null && other.idOglasa != null) || (this.idOglasa != null && !this.idOglasa.equals(other.idOglasa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.metropolitan.cs230.Oglas[ idOglasa=" + idOglasa + " ]";
    }
    
}
