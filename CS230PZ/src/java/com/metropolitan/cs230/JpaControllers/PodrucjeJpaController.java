/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.cs230.JpaControllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.metropolitan.cs230.Grad;
import com.metropolitan.cs230.JpaControllers.exceptions.NonexistentEntityException;
import com.metropolitan.cs230.JpaControllers.exceptions.PreexistingEntityException;
import com.metropolitan.cs230.JpaControllers.exceptions.RollbackFailureException;
import com.metropolitan.cs230.Oglas;
import com.metropolitan.cs230.Podrucje;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Aleksandra
 */
public class PodrucjeJpaController implements Serializable {

    public PodrucjeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Podrucje podrucje) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (podrucje.getOglasCollection() == null) {
            podrucje.setOglasCollection(new ArrayList<Oglas>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Grad idGrada = podrucje.getIdGrada();
            if (idGrada != null) {
                idGrada = em.getReference(idGrada.getClass(), idGrada.getIdGrada());
                podrucje.setIdGrada(idGrada);
            }
            Collection<Oglas> attachedOglasCollection = new ArrayList<Oglas>();
            for (Oglas oglasCollectionOglasToAttach : podrucje.getOglasCollection()) {
                oglasCollectionOglasToAttach = em.getReference(oglasCollectionOglasToAttach.getClass(), oglasCollectionOglasToAttach.getIdOglasa());
                attachedOglasCollection.add(oglasCollectionOglasToAttach);
            }
            podrucje.setOglasCollection(attachedOglasCollection);
            em.persist(podrucje);
            if (idGrada != null) {
                idGrada.getPodrucjeCollection().add(podrucje);
                idGrada = em.merge(idGrada);
            }
            for (Oglas oglasCollectionOglas : podrucje.getOglasCollection()) {
                Podrucje oldIdPodrucjaOfOglasCollectionOglas = oglasCollectionOglas.getIdPodrucja();
                oglasCollectionOglas.setIdPodrucja(podrucje);
                oglasCollectionOglas = em.merge(oglasCollectionOglas);
                if (oldIdPodrucjaOfOglasCollectionOglas != null) {
                    oldIdPodrucjaOfOglasCollectionOglas.getOglasCollection().remove(oglasCollectionOglas);
                    oldIdPodrucjaOfOglasCollectionOglas = em.merge(oldIdPodrucjaOfOglasCollectionOglas);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPodrucje(podrucje.getIdPodrucja()) != null) {
                throw new PreexistingEntityException("Podrucje " + podrucje + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Podrucje podrucje) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Podrucje persistentPodrucje = em.find(Podrucje.class, podrucje.getIdPodrucja());
            Grad idGradaOld = persistentPodrucje.getIdGrada();
            Grad idGradaNew = podrucje.getIdGrada();
            Collection<Oglas> oglasCollectionOld = persistentPodrucje.getOglasCollection();
            Collection<Oglas> oglasCollectionNew = podrucje.getOglasCollection();
            if (idGradaNew != null) {
                idGradaNew = em.getReference(idGradaNew.getClass(), idGradaNew.getIdGrada());
                podrucje.setIdGrada(idGradaNew);
            }
            Collection<Oglas> attachedOglasCollectionNew = new ArrayList<Oglas>();
            for (Oglas oglasCollectionNewOglasToAttach : oglasCollectionNew) {
                oglasCollectionNewOglasToAttach = em.getReference(oglasCollectionNewOglasToAttach.getClass(), oglasCollectionNewOglasToAttach.getIdOglasa());
                attachedOglasCollectionNew.add(oglasCollectionNewOglasToAttach);
            }
            oglasCollectionNew = attachedOglasCollectionNew;
            podrucje.setOglasCollection(oglasCollectionNew);
            podrucje = em.merge(podrucje);
            if (idGradaOld != null && !idGradaOld.equals(idGradaNew)) {
                idGradaOld.getPodrucjeCollection().remove(podrucje);
                idGradaOld = em.merge(idGradaOld);
            }
            if (idGradaNew != null && !idGradaNew.equals(idGradaOld)) {
                idGradaNew.getPodrucjeCollection().add(podrucje);
                idGradaNew = em.merge(idGradaNew);
            }
            for (Oglas oglasCollectionOldOglas : oglasCollectionOld) {
                if (!oglasCollectionNew.contains(oglasCollectionOldOglas)) {
                    oglasCollectionOldOglas.setIdPodrucja(null);
                    oglasCollectionOldOglas = em.merge(oglasCollectionOldOglas);
                }
            }
            for (Oglas oglasCollectionNewOglas : oglasCollectionNew) {
                if (!oglasCollectionOld.contains(oglasCollectionNewOglas)) {
                    Podrucje oldIdPodrucjaOfOglasCollectionNewOglas = oglasCollectionNewOglas.getIdPodrucja();
                    oglasCollectionNewOglas.setIdPodrucja(podrucje);
                    oglasCollectionNewOglas = em.merge(oglasCollectionNewOglas);
                    if (oldIdPodrucjaOfOglasCollectionNewOglas != null && !oldIdPodrucjaOfOglasCollectionNewOglas.equals(podrucje)) {
                        oldIdPodrucjaOfOglasCollectionNewOglas.getOglasCollection().remove(oglasCollectionNewOglas);
                        oldIdPodrucjaOfOglasCollectionNewOglas = em.merge(oldIdPodrucjaOfOglasCollectionNewOglas);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = podrucje.getIdPodrucja();
                if (findPodrucje(id) == null) {
                    throw new NonexistentEntityException("The podrucje with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Podrucje podrucje;
            try {
                podrucje = em.getReference(Podrucje.class, id);
                podrucje.getIdPodrucja();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The podrucje with id " + id + " no longer exists.", enfe);
            }
            Grad idGrada = podrucje.getIdGrada();
            if (idGrada != null) {
                idGrada.getPodrucjeCollection().remove(podrucje);
                idGrada = em.merge(idGrada);
            }
            Collection<Oglas> oglasCollection = podrucje.getOglasCollection();
            for (Oglas oglasCollectionOglas : oglasCollection) {
                oglasCollectionOglas.setIdPodrucja(null);
                oglasCollectionOglas = em.merge(oglasCollectionOglas);
            }
            em.remove(podrucje);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Podrucje> findPodrucjeEntities() {
        return findPodrucjeEntities(true, -1, -1);
    }

    public List<Podrucje> findPodrucjeEntities(int maxResults, int firstResult) {
        return findPodrucjeEntities(false, maxResults, firstResult);
    }

    private List<Podrucje> findPodrucjeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Podrucje.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Podrucje findPodrucje(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Podrucje.class, id);
        } finally {
            em.close();
        }
    }

    public int getPodrucjeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Podrucje> rt = cq.from(Podrucje.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
