package com.metropolitan.cs230;

import com.metropolitan.cs230.Informacije;
import com.metropolitan.cs230.Kategorija;
import com.metropolitan.cs230.Nalog;
import com.metropolitan.cs230.Podrucje;
import com.metropolitan.cs230.Tip;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-02T19:22:14")
@StaticMetamodel(Oglas.class)
public class Oglas_ { 

    public static volatile SingularAttribute<Oglas, Integer> idOglasa;
    public static volatile SingularAttribute<Oglas, String> nazivOglasa;
    public static volatile SingularAttribute<Oglas, Podrucje> idPodrucja;
    public static volatile SingularAttribute<Oglas, Tip> idTipa;
    public static volatile SingularAttribute<Oglas, Nalog> idNaloga;
    public static volatile CollectionAttribute<Oglas, Informacije> informacijeCollection;
    public static volatile SingularAttribute<Oglas, String> lokacija;
    public static volatile SingularAttribute<Oglas, String> adresa;
    public static volatile SingularAttribute<Oglas, Integer> povrsina;
    public static volatile SingularAttribute<Oglas, Kategorija> idKategorije;
    public static volatile SingularAttribute<Oglas, Integer> cena;
    public static volatile SingularAttribute<Oglas, String> opis;

}