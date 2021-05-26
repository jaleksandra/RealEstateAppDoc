/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.cs230;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Aleksandra
 */
@Stateless
public class OglasFacade extends AbstractFacade<Oglas> {

    @PersistenceContext(unitName = "CS230PZPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OglasFacade() {
        super(Oglas.class);
    }
    
}
