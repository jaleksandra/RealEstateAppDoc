/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.cs230.JpaControllers;

import com.metropolitan.cs230.Informacije;
import com.metropolitan.cs230.JpaControllers.exceptions.NonexistentEntityException;
import com.metropolitan.cs230.JpaControllers.exceptions.PreexistingEntityException;
import com.metropolitan.cs230.JpaControllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.metropolitan.cs230.Oglas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Aleksandra
 */
public class InformacijeJpaController implements Serializable {

    public InformacijeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Informacije informacije) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Oglas idOglasa = informacije.getIdOglasa();
            if (idOglasa != null) {
                idOglasa = em.getReference(idOglasa.getClass(), idOglasa.getIdOglasa());
                informacije.setIdOglasa(idOglasa);
            }
            em.persist(informacije);
            if (idOglasa != null) {
                idOglasa.getInformacijeCollection().add(informacije);
                idOglasa = em.merge(idOglasa);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findInformacije(informacije.getIdInformacije()) != null) {
                throw new PreexistingEntityException("Informacije " + informacije + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Informacije informacije) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Informacije persistentInformacije = em.find(Informacije.class, informacije.getIdInformacije());
            Oglas idOglasaOld = persistentInformacije.getIdOglasa();
            Oglas idOglasaNew = informacije.getIdOglasa();
            if (idOglasaNew != null) {
                idOglasaNew = em.getReference(idOglasaNew.getClass(), idOglasaNew.getIdOglasa());
                informacije.setIdOglasa(idOglasaNew);
            }
            informacije = em.merge(informacije);
            if (idOglasaOld != null && !idOglasaOld.equals(idOglasaNew)) {
                idOglasaOld.getInformacijeCollection().remove(informacije);
                idOglasaOld = em.merge(idOglasaOld);
            }
            if (idOglasaNew != null && !idOglasaNew.equals(idOglasaOld)) {
                idOglasaNew.getInformacijeCollection().add(informacije);
                idOglasaNew = em.merge(idOglasaNew);
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
                Integer id = informacije.getIdInformacije();
                if (findInformacije(id) == null) {
                    throw new NonexistentEntityException("The informacije with id " + id + " no longer exists.");
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
            Informacije informacije;
            try {
                informacije = em.getReference(Informacije.class, id);
                informacije.getIdInformacije();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The informacije with id " + id + " no longer exists.", enfe);
            }
            Oglas idOglasa = informacije.getIdOglasa();
            if (idOglasa != null) {
                idOglasa.getInformacijeCollection().remove(informacije);
                idOglasa = em.merge(idOglasa);
            }
            em.remove(informacije);
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

    public List<Informacije> findInformacijeEntities() {
        return findInformacijeEntities(true, -1, -1);
    }

    public List<Informacije> findInformacijeEntities(int maxResults, int firstResult) {
        return findInformacijeEntities(false, maxResults, firstResult);
    }

    private List<Informacije> findInformacijeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Informacije.class));
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

    public Informacije findInformacije(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Informacije.class, id);
        } finally {
            em.close();
        }
    }

    public int getInformacijeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Informacije> rt = cq.from(Informacije.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
