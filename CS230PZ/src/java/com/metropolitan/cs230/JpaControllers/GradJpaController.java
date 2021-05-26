/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.cs230.JpaControllers;

import com.metropolitan.cs230.Grad;
import com.metropolitan.cs230.JpaControllers.exceptions.NonexistentEntityException;
import com.metropolitan.cs230.JpaControllers.exceptions.PreexistingEntityException;
import com.metropolitan.cs230.JpaControllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
public class GradJpaController implements Serializable {

    public GradJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grad grad) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (grad.getPodrucjeCollection() == null) {
            grad.setPodrucjeCollection(new ArrayList<Podrucje>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Podrucje> attachedPodrucjeCollection = new ArrayList<Podrucje>();
            for (Podrucje podrucjeCollectionPodrucjeToAttach : grad.getPodrucjeCollection()) {
                podrucjeCollectionPodrucjeToAttach = em.getReference(podrucjeCollectionPodrucjeToAttach.getClass(), podrucjeCollectionPodrucjeToAttach.getIdPodrucja());
                attachedPodrucjeCollection.add(podrucjeCollectionPodrucjeToAttach);
            }
            grad.setPodrucjeCollection(attachedPodrucjeCollection);
            em.persist(grad);
            for (Podrucje podrucjeCollectionPodrucje : grad.getPodrucjeCollection()) {
                Grad oldIdGradaOfPodrucjeCollectionPodrucje = podrucjeCollectionPodrucje.getIdGrada();
                podrucjeCollectionPodrucje.setIdGrada(grad);
                podrucjeCollectionPodrucje = em.merge(podrucjeCollectionPodrucje);
                if (oldIdGradaOfPodrucjeCollectionPodrucje != null) {
                    oldIdGradaOfPodrucjeCollectionPodrucje.getPodrucjeCollection().remove(podrucjeCollectionPodrucje);
                    oldIdGradaOfPodrucjeCollectionPodrucje = em.merge(oldIdGradaOfPodrucjeCollectionPodrucje);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGrad(grad.getIdGrada()) != null) {
                throw new PreexistingEntityException("Grad " + grad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grad grad) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Grad persistentGrad = em.find(Grad.class, grad.getIdGrada());
            Collection<Podrucje> podrucjeCollectionOld = persistentGrad.getPodrucjeCollection();
            Collection<Podrucje> podrucjeCollectionNew = grad.getPodrucjeCollection();
            Collection<Podrucje> attachedPodrucjeCollectionNew = new ArrayList<Podrucje>();
            for (Podrucje podrucjeCollectionNewPodrucjeToAttach : podrucjeCollectionNew) {
                podrucjeCollectionNewPodrucjeToAttach = em.getReference(podrucjeCollectionNewPodrucjeToAttach.getClass(), podrucjeCollectionNewPodrucjeToAttach.getIdPodrucja());
                attachedPodrucjeCollectionNew.add(podrucjeCollectionNewPodrucjeToAttach);
            }
            podrucjeCollectionNew = attachedPodrucjeCollectionNew;
            grad.setPodrucjeCollection(podrucjeCollectionNew);
            grad = em.merge(grad);
            for (Podrucje podrucjeCollectionOldPodrucje : podrucjeCollectionOld) {
                if (!podrucjeCollectionNew.contains(podrucjeCollectionOldPodrucje)) {
                    podrucjeCollectionOldPodrucje.setIdGrada(null);
                    podrucjeCollectionOldPodrucje = em.merge(podrucjeCollectionOldPodrucje);
                }
            }
            for (Podrucje podrucjeCollectionNewPodrucje : podrucjeCollectionNew) {
                if (!podrucjeCollectionOld.contains(podrucjeCollectionNewPodrucje)) {
                    Grad oldIdGradaOfPodrucjeCollectionNewPodrucje = podrucjeCollectionNewPodrucje.getIdGrada();
                    podrucjeCollectionNewPodrucje.setIdGrada(grad);
                    podrucjeCollectionNewPodrucje = em.merge(podrucjeCollectionNewPodrucje);
                    if (oldIdGradaOfPodrucjeCollectionNewPodrucje != null && !oldIdGradaOfPodrucjeCollectionNewPodrucje.equals(grad)) {
                        oldIdGradaOfPodrucjeCollectionNewPodrucje.getPodrucjeCollection().remove(podrucjeCollectionNewPodrucje);
                        oldIdGradaOfPodrucjeCollectionNewPodrucje = em.merge(oldIdGradaOfPodrucjeCollectionNewPodrucje);
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
                Integer id = grad.getIdGrada();
                if (findGrad(id) == null) {
                    throw new NonexistentEntityException("The grad with id " + id + " no longer exists.");
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
            Grad grad;
            try {
                grad = em.getReference(Grad.class, id);
                grad.getIdGrada();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grad with id " + id + " no longer exists.", enfe);
            }
            Collection<Podrucje> podrucjeCollection = grad.getPodrucjeCollection();
            for (Podrucje podrucjeCollectionPodrucje : podrucjeCollection) {
                podrucjeCollectionPodrucje.setIdGrada(null);
                podrucjeCollectionPodrucje = em.merge(podrucjeCollectionPodrucje);
            }
            em.remove(grad);
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

    public List<Grad> findGradEntities() {
        return findGradEntities(true, -1, -1);
    }

    public List<Grad> findGradEntities(int maxResults, int firstResult) {
        return findGradEntities(false, maxResults, firstResult);
    }

    private List<Grad> findGradEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grad.class));
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

    public Grad findGrad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grad.class, id);
        } finally {
            em.close();
        }
    }

    public int getGradCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grad> rt = cq.from(Grad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
