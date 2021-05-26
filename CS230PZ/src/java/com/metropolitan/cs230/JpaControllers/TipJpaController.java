/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.cs230.JpaControllers;

import com.metropolitan.cs230.JpaControllers.exceptions.NonexistentEntityException;
import com.metropolitan.cs230.JpaControllers.exceptions.PreexistingEntityException;
import com.metropolitan.cs230.JpaControllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.metropolitan.cs230.Oglas;
import com.metropolitan.cs230.Tip;
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
public class TipJpaController implements Serializable {

    public TipJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tip tip) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tip.getOglasCollection() == null) {
            tip.setOglasCollection(new ArrayList<Oglas>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Oglas> attachedOglasCollection = new ArrayList<Oglas>();
            for (Oglas oglasCollectionOglasToAttach : tip.getOglasCollection()) {
                oglasCollectionOglasToAttach = em.getReference(oglasCollectionOglasToAttach.getClass(), oglasCollectionOglasToAttach.getIdOglasa());
                attachedOglasCollection.add(oglasCollectionOglasToAttach);
            }
            tip.setOglasCollection(attachedOglasCollection);
            em.persist(tip);
            for (Oglas oglasCollectionOglas : tip.getOglasCollection()) {
                Tip oldIdTipaOfOglasCollectionOglas = oglasCollectionOglas.getIdTipa();
                oglasCollectionOglas.setIdTipa(tip);
                oglasCollectionOglas = em.merge(oglasCollectionOglas);
                if (oldIdTipaOfOglasCollectionOglas != null) {
                    oldIdTipaOfOglasCollectionOglas.getOglasCollection().remove(oglasCollectionOglas);
                    oldIdTipaOfOglasCollectionOglas = em.merge(oldIdTipaOfOglasCollectionOglas);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTip(tip.getIdTipa()) != null) {
                throw new PreexistingEntityException("Tip " + tip + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tip tip) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tip persistentTip = em.find(Tip.class, tip.getIdTipa());
            Collection<Oglas> oglasCollectionOld = persistentTip.getOglasCollection();
            Collection<Oglas> oglasCollectionNew = tip.getOglasCollection();
            Collection<Oglas> attachedOglasCollectionNew = new ArrayList<Oglas>();
            for (Oglas oglasCollectionNewOglasToAttach : oglasCollectionNew) {
                oglasCollectionNewOglasToAttach = em.getReference(oglasCollectionNewOglasToAttach.getClass(), oglasCollectionNewOglasToAttach.getIdOglasa());
                attachedOglasCollectionNew.add(oglasCollectionNewOglasToAttach);
            }
            oglasCollectionNew = attachedOglasCollectionNew;
            tip.setOglasCollection(oglasCollectionNew);
            tip = em.merge(tip);
            for (Oglas oglasCollectionOldOglas : oglasCollectionOld) {
                if (!oglasCollectionNew.contains(oglasCollectionOldOglas)) {
                    oglasCollectionOldOglas.setIdTipa(null);
                    oglasCollectionOldOglas = em.merge(oglasCollectionOldOglas);
                }
            }
            for (Oglas oglasCollectionNewOglas : oglasCollectionNew) {
                if (!oglasCollectionOld.contains(oglasCollectionNewOglas)) {
                    Tip oldIdTipaOfOglasCollectionNewOglas = oglasCollectionNewOglas.getIdTipa();
                    oglasCollectionNewOglas.setIdTipa(tip);
                    oglasCollectionNewOglas = em.merge(oglasCollectionNewOglas);
                    if (oldIdTipaOfOglasCollectionNewOglas != null && !oldIdTipaOfOglasCollectionNewOglas.equals(tip)) {
                        oldIdTipaOfOglasCollectionNewOglas.getOglasCollection().remove(oglasCollectionNewOglas);
                        oldIdTipaOfOglasCollectionNewOglas = em.merge(oldIdTipaOfOglasCollectionNewOglas);
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
                Integer id = tip.getIdTipa();
                if (findTip(id) == null) {
                    throw new NonexistentEntityException("The tip with id " + id + " no longer exists.");
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
            Tip tip;
            try {
                tip = em.getReference(Tip.class, id);
                tip.getIdTipa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tip with id " + id + " no longer exists.", enfe);
            }
            Collection<Oglas> oglasCollection = tip.getOglasCollection();
            for (Oglas oglasCollectionOglas : oglasCollection) {
                oglasCollectionOglas.setIdTipa(null);
                oglasCollectionOglas = em.merge(oglasCollectionOglas);
            }
            em.remove(tip);
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

    public List<Tip> findTipEntities() {
        return findTipEntities(true, -1, -1);
    }

    public List<Tip> findTipEntities(int maxResults, int firstResult) {
        return findTipEntities(false, maxResults, firstResult);
    }

    private List<Tip> findTipEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tip.class));
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

    public Tip findTip(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tip.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tip> rt = cq.from(Tip.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
