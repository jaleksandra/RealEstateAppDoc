/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.cs230.JpaControllers;

import com.metropolitan.cs230.JpaControllers.exceptions.NonexistentEntityException;
import com.metropolitan.cs230.JpaControllers.exceptions.PreexistingEntityException;
import com.metropolitan.cs230.JpaControllers.exceptions.RollbackFailureException;
import com.metropolitan.cs230.Nalog;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.metropolitan.cs230.Oglas;
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
public class NalogJpaController implements Serializable {

    public NalogJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Nalog nalog) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (nalog.getOglasCollection() == null) {
            nalog.setOglasCollection(new ArrayList<Oglas>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Oglas> attachedOglasCollection = new ArrayList<Oglas>();
            for (Oglas oglasCollectionOglasToAttach : nalog.getOglasCollection()) {
                oglasCollectionOglasToAttach = em.getReference(oglasCollectionOglasToAttach.getClass(), oglasCollectionOglasToAttach.getIdOglasa());
                attachedOglasCollection.add(oglasCollectionOglasToAttach);
            }
            nalog.setOglasCollection(attachedOglasCollection);
            em.persist(nalog);
            for (Oglas oglasCollectionOglas : nalog.getOglasCollection()) {
                Nalog oldIdNalogaOfOglasCollectionOglas = oglasCollectionOglas.getIdNaloga();
                oglasCollectionOglas.setIdNaloga(nalog);
                oglasCollectionOglas = em.merge(oglasCollectionOglas);
                if (oldIdNalogaOfOglasCollectionOglas != null) {
                    oldIdNalogaOfOglasCollectionOglas.getOglasCollection().remove(oglasCollectionOglas);
                    oldIdNalogaOfOglasCollectionOglas = em.merge(oldIdNalogaOfOglasCollectionOglas);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findNalog(nalog.getIdNaloga()) != null) {
                throw new PreexistingEntityException("Nalog " + nalog + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Nalog nalog) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Nalog persistentNalog = em.find(Nalog.class, nalog.getIdNaloga());
            Collection<Oglas> oglasCollectionOld = persistentNalog.getOglasCollection();
            Collection<Oglas> oglasCollectionNew = nalog.getOglasCollection();
            Collection<Oglas> attachedOglasCollectionNew = new ArrayList<Oglas>();
            for (Oglas oglasCollectionNewOglasToAttach : oglasCollectionNew) {
                oglasCollectionNewOglasToAttach = em.getReference(oglasCollectionNewOglasToAttach.getClass(), oglasCollectionNewOglasToAttach.getIdOglasa());
                attachedOglasCollectionNew.add(oglasCollectionNewOglasToAttach);
            }
            oglasCollectionNew = attachedOglasCollectionNew;
            nalog.setOglasCollection(oglasCollectionNew);
            nalog = em.merge(nalog);
            for (Oglas oglasCollectionOldOglas : oglasCollectionOld) {
                if (!oglasCollectionNew.contains(oglasCollectionOldOglas)) {
                    oglasCollectionOldOglas.setIdNaloga(null);
                    oglasCollectionOldOglas = em.merge(oglasCollectionOldOglas);
                }
            }
            for (Oglas oglasCollectionNewOglas : oglasCollectionNew) {
                if (!oglasCollectionOld.contains(oglasCollectionNewOglas)) {
                    Nalog oldIdNalogaOfOglasCollectionNewOglas = oglasCollectionNewOglas.getIdNaloga();
                    oglasCollectionNewOglas.setIdNaloga(nalog);
                    oglasCollectionNewOglas = em.merge(oglasCollectionNewOglas);
                    if (oldIdNalogaOfOglasCollectionNewOglas != null && !oldIdNalogaOfOglasCollectionNewOglas.equals(nalog)) {
                        oldIdNalogaOfOglasCollectionNewOglas.getOglasCollection().remove(oglasCollectionNewOglas);
                        oldIdNalogaOfOglasCollectionNewOglas = em.merge(oldIdNalogaOfOglasCollectionNewOglas);
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
                Integer id = nalog.getIdNaloga();
                if (findNalog(id) == null) {
                    throw new NonexistentEntityException("The nalog with id " + id + " no longer exists.");
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
            Nalog nalog;
            try {
                nalog = em.getReference(Nalog.class, id);
                nalog.getIdNaloga();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nalog with id " + id + " no longer exists.", enfe);
            }
            Collection<Oglas> oglasCollection = nalog.getOglasCollection();
            for (Oglas oglasCollectionOglas : oglasCollection) {
                oglasCollectionOglas.setIdNaloga(null);
                oglasCollectionOglas = em.merge(oglasCollectionOglas);
            }
            em.remove(nalog);
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

    public List<Nalog> findNalogEntities() {
        return findNalogEntities(true, -1, -1);
    }

    public List<Nalog> findNalogEntities(int maxResults, int firstResult) {
        return findNalogEntities(false, maxResults, firstResult);
    }

    private List<Nalog> findNalogEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Nalog.class));
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

    public Nalog findNalog(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Nalog.class, id);
        } finally {
            em.close();
        }
    }

    public int getNalogCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Nalog> rt = cq.from(Nalog.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
