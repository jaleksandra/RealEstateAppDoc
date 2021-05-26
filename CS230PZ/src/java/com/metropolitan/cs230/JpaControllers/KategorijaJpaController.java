/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.cs230.JpaControllers;

import com.metropolitan.cs230.JpaControllers.exceptions.NonexistentEntityException;
import com.metropolitan.cs230.JpaControllers.exceptions.PreexistingEntityException;
import com.metropolitan.cs230.JpaControllers.exceptions.RollbackFailureException;
import com.metropolitan.cs230.Kategorija;
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
public class KategorijaJpaController implements Serializable {

    public KategorijaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Kategorija kategorija) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (kategorija.getOglasCollection() == null) {
            kategorija.setOglasCollection(new ArrayList<Oglas>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Oglas> attachedOglasCollection = new ArrayList<Oglas>();
            for (Oglas oglasCollectionOglasToAttach : kategorija.getOglasCollection()) {
                oglasCollectionOglasToAttach = em.getReference(oglasCollectionOglasToAttach.getClass(), oglasCollectionOglasToAttach.getIdOglasa());
                attachedOglasCollection.add(oglasCollectionOglasToAttach);
            }
            kategorija.setOglasCollection(attachedOglasCollection);
            em.persist(kategorija);
            for (Oglas oglasCollectionOglas : kategorija.getOglasCollection()) {
                Kategorija oldIdKategorijeOfOglasCollectionOglas = oglasCollectionOglas.getIdKategorije();
                oglasCollectionOglas.setIdKategorije(kategorija);
                oglasCollectionOglas = em.merge(oglasCollectionOglas);
                if (oldIdKategorijeOfOglasCollectionOglas != null) {
                    oldIdKategorijeOfOglasCollectionOglas.getOglasCollection().remove(oglasCollectionOglas);
                    oldIdKategorijeOfOglasCollectionOglas = em.merge(oldIdKategorijeOfOglasCollectionOglas);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findKategorija(kategorija.getIdKategorije()) != null) {
                throw new PreexistingEntityException("Kategorija " + kategorija + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Kategorija kategorija) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Kategorija persistentKategorija = em.find(Kategorija.class, kategorija.getIdKategorije());
            Collection<Oglas> oglasCollectionOld = persistentKategorija.getOglasCollection();
            Collection<Oglas> oglasCollectionNew = kategorija.getOglasCollection();
            Collection<Oglas> attachedOglasCollectionNew = new ArrayList<Oglas>();
            for (Oglas oglasCollectionNewOglasToAttach : oglasCollectionNew) {
                oglasCollectionNewOglasToAttach = em.getReference(oglasCollectionNewOglasToAttach.getClass(), oglasCollectionNewOglasToAttach.getIdOglasa());
                attachedOglasCollectionNew.add(oglasCollectionNewOglasToAttach);
            }
            oglasCollectionNew = attachedOglasCollectionNew;
            kategorija.setOglasCollection(oglasCollectionNew);
            kategorija = em.merge(kategorija);
            for (Oglas oglasCollectionOldOglas : oglasCollectionOld) {
                if (!oglasCollectionNew.contains(oglasCollectionOldOglas)) {
                    oglasCollectionOldOglas.setIdKategorije(null);
                    oglasCollectionOldOglas = em.merge(oglasCollectionOldOglas);
                }
            }
            for (Oglas oglasCollectionNewOglas : oglasCollectionNew) {
                if (!oglasCollectionOld.contains(oglasCollectionNewOglas)) {
                    Kategorija oldIdKategorijeOfOglasCollectionNewOglas = oglasCollectionNewOglas.getIdKategorije();
                    oglasCollectionNewOglas.setIdKategorije(kategorija);
                    oglasCollectionNewOglas = em.merge(oglasCollectionNewOglas);
                    if (oldIdKategorijeOfOglasCollectionNewOglas != null && !oldIdKategorijeOfOglasCollectionNewOglas.equals(kategorija)) {
                        oldIdKategorijeOfOglasCollectionNewOglas.getOglasCollection().remove(oglasCollectionNewOglas);
                        oldIdKategorijeOfOglasCollectionNewOglas = em.merge(oldIdKategorijeOfOglasCollectionNewOglas);
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
                Integer id = kategorija.getIdKategorije();
                if (findKategorija(id) == null) {
                    throw new NonexistentEntityException("The kategorija with id " + id + " no longer exists.");
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
            Kategorija kategorija;
            try {
                kategorija = em.getReference(Kategorija.class, id);
                kategorija.getIdKategorije();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The kategorija with id " + id + " no longer exists.", enfe);
            }
            Collection<Oglas> oglasCollection = kategorija.getOglasCollection();
            for (Oglas oglasCollectionOglas : oglasCollection) {
                oglasCollectionOglas.setIdKategorije(null);
                oglasCollectionOglas = em.merge(oglasCollectionOglas);
            }
            em.remove(kategorija);
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

    public List<Kategorija> findKategorijaEntities() {
        return findKategorijaEntities(true, -1, -1);
    }

    public List<Kategorija> findKategorijaEntities(int maxResults, int firstResult) {
        return findKategorijaEntities(false, maxResults, firstResult);
    }

    private List<Kategorija> findKategorijaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Kategorija.class));
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

    public Kategorija findKategorija(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Kategorija.class, id);
        } finally {
            em.close();
        }
    }

    public int getKategorijaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Kategorija> rt = cq.from(Kategorija.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
