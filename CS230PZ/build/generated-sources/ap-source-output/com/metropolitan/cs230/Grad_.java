package com.metropolitan.cs230;

import com.metropolitan.cs230.Podrucje;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-02T19:22:14")
@StaticMetamodel(Grad.class)
public class Grad_ { 

    public static volatile CollectionAttribute<Grad, Podrucje> podrucjeCollection;
    public static volatile SingularAttribute<Grad, Integer> idGrada;
    public static volatile SingularAttribute<Grad, String> nazivGrada;

}