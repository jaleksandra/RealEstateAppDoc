package com.metropolitan.cs230;

import com.metropolitan.cs230.Oglas;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-02T19:22:14")
@StaticMetamodel(Nalog.class)
public class Nalog_ { 

    public static volatile SingularAttribute<Nalog, String> ime;
    public static volatile SingularAttribute<Nalog, String> prezime;
    public static volatile SingularAttribute<Nalog, String> lozinka;
    public static volatile SingularAttribute<Nalog, Integer> idNaloga;
    public static volatile SingularAttribute<Nalog, String> korisnickoIme;
    public static volatile CollectionAttribute<Nalog, Oglas> oglasCollection;

}