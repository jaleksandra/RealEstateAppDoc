package com.metropolitan.cs230;

import com.metropolitan.cs230.Oglas;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-02T19:22:14")
@StaticMetamodel(Kategorija.class)
public class Kategorija_ { 

    public static volatile SingularAttribute<Kategorija, Integer> idKategorije;
    public static volatile SingularAttribute<Kategorija, String> nazivKategorije;
    public static volatile CollectionAttribute<Kategorija, Oglas> oglasCollection;

}