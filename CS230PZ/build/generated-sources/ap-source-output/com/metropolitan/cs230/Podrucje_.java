package com.metropolitan.cs230;

import com.metropolitan.cs230.Grad;
import com.metropolitan.cs230.Oglas;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-02T19:22:14")
@StaticMetamodel(Podrucje.class)
public class Podrucje_ { 

    public static volatile SingularAttribute<Podrucje, Integer> idPodrucja;
    public static volatile SingularAttribute<Podrucje, String> imePodrucja;
    public static volatile SingularAttribute<Podrucje, Grad> idGrada;
    public static volatile CollectionAttribute<Podrucje, Oglas> oglasCollection;

}