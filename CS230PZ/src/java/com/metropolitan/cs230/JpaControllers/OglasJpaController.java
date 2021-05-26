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
import com.metropolitan.cs230.Kategorija;
import com.metropolitan.cs230.Podrucje;
import com.metropolitan.cs230.Tip;
import com.metropolitan.cs230.Nalog;
import com.metropolitan.cs230.Informacije;
import com.metropolitan.cs230.JpaControllers.exceptions.NonexistentEntityException;
import com.metropolitan.cs230.JpaControllers.exceptions.PreexistingEntityException;
import com.metropolitan.cs230.JpaControllers.exceptions.RollbackFailureException;
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
public class OglasJpaController implements Serializable {

    public OglasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Oglas oglas) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (oglas.getInformacijeCollection() == null) {
            oglas.setInformacijeCollection(new ArrayList<Informacije>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Kategorija idKategorije = oglas.getIdKategorije();
            if (idKategorije != null) {
                idKategorije = em.getReference(idKategorije.getClass(), idKategorije.getIdKategorije());
                oglas.setIdKategorije(idKategorije);
            }
            Podrucje idPodrucja = oglas.getIdPodrucja();
            if (idPodrucja != null) {
                idPodrucja = em.getReference(idPodrucja.getClass(), idPodrucja.getIdPodrucja());
                oglas.setIdPodrucja(idPodrucja);
            }
            Tip idTipa = oglas.getIdTipa();
            if (idTipa != null) {
                idTipa = em.getReference(idTipa.getClass(), idTipa.getIdTipa());
                oglas.setIdTipa(idTipa);
            }
            Nalog idNaloga = oglas.getIdNaloga();
            if (idNaloga != null) {
                idNaloga = em.getReference(idNaloga.getClass(), idNaloga.getIdNaloga());
                oglas.setIdNaloga(idNaloga);
            }
            Collection<Informacije> attachedInformacijeCollection = new ArrayList<Informacije>();
            for (Informacije informacijeCollectionInformacijeToAttach : oglas.getInformacijeCollection()) {
                informacijeCollectionInformacijeToAttach = em.getReference(informacijeCollectionInformacijeToAttach.getClass(), informacijeCollectionInformacijeToAttach.getIdInformacije());
                attachedInformacijeCollection.add(informacijeCollectionInformacijeToAttach);
            }
            oglas.setInformacijeCollection(attachedInformacijeCollection);
            em.persist(oglas);
            if (idKategorije != null) {
                idKategorije.getOglasCollection().add(oglas);
                idKategorije = em.merge(idKategorije);
            }
            if (idPodrucja != null) {
                idPodrucja.getOglasCollection().add(oglas);
                idPodrucja = em.merge(idPodrucja);
            }
            if (idTipa != null) {
                idTipa.getOglasCollection().add(oglas);
                idTipa = em.merge(idTipa);
            }
            if (idNaloga != null) {
                idNaloga.getOglasCollection().add(oglas);
                idNaloga = em.merge(idNaloga);
            }
            for (Informacije informacijeCollectionInformacije : oglas.getInformacijeCollection()) {
                Oglas oldIdOglasaOfInformacijeCollectionInformacije = informacijeCollectionInformacije.getIdOglasa();
                informacijeCollectionInformacije.setIdOglasa(oglas);
                informacijeCollectionInformacije = em.merge(informacijeCollectionInformacije);
                if (oldIdOglasaOfInformacijeCollectionInformacije != null) {
                    oldIdOglasaOfInformacijeCollectionInformacije.getInformacijeCollection().remove(informacijeCollectionInformacije);
                    oldIdOglasaOfInformacijeCollectionInformacije = em.merge(oldIdOglasaOfInformacijeCollectionInformacije);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findOglas(oglas.getIdOglasa()) != null) {
                throw new PreexistingEntityException("Oglas " + oglas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Oglas oglas) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Oglas persistentOglas = em.find(Oglas.class, oglas.getIdOglasa());
            Kategorija idKategorijeOld = persistentOglas.getIdKategorije();
            Kategorija idKategorijeNew = oglas.getIdKategorije();
            Podrucje idPodrucjaOld = persistentOglas.getIdPodrucja();
            Podrucje idPodrucjaNew = oglas.getIdPodrucja();
            Tip idTipaOld = persistentOglas.getIdTipa();
            Tip idTipaNew = oglas.getIdTipa();
            Nalog idNalogaOld = persistentOglas.getIdNaloga();
            Nalog idNalogaNew = oglas.getIdNaloga();
            Collection<Informacije> informacijeCollectionOld = persistentOglas.getInformacijeCollection();
            Collection<Informacije> informacijeCollectionNew = oglas.getInformacijeCollection();
            if (idKategorijeNew != null) {
                idKategorijeNew = em.getReference(idKategorijeNew.getClass(), idKategorijeNew.getIdKategorije());
                oglas.setIdKategorije(idKategorijeNew);
            }
            if (idPodrucjaNew != null) {
                idPodrucjaNew = em.getReference(idPodrucjaNew.getClass(), idPodrucjaNew.getIdPodrucja());
                oglas.setIdPodrucja(idPodrucjaNew);
            }
            if (idTipaNew != null) {
                idTipaNew = em.getReference(idTipaNew.getClass(), idTipaNew.getIdTipa());
                oglas.setIdTipa(idTipaNew);
            }
            if (idNalogaNew != null) {
                idNalogaNew = em.getReference(idNalogaNew.getClass(), idNalogaNew.getIdNaloga());
                oglas.setIdNaloga(idNalogaNew);
            }
            Collection<Informacije> attachedInformacijeCollectionNew = new ArrayList<Informacije>();
            for (Informacije informacijeCollectionNewInformacijeToAttach : informacijeCollectionNew) {
                informacijeCollectionNewInformacijeToAttach = em.getReference(informacijeCollectionNewInformacijeToAttach.getClass(), informacijeCollectionNewInformacijeToAttach.getIdInformacije());
                attachedInformacijeCollectionNew.add(informacijeCollectionNewInformacijeToAttach);
            }
            informacijeCollectionNew = attachedInformacijeCollectionNew;
            oglas.setInformacijeCollection(informacijeCollectionNew);
            oglas = em.merge(oglas);
            if (idKategorijeOld != null && !idKategorijeOld.equals(idKategorijeNew)) {
                idKategorijeOld.getOglasCollection().remove(oglas);
                idKategorijeOld = em.merge(idKategorijeOld);
            }
            if (idKategorijeNew != null && !idKategorijeNew.equals(idKategorijeOld)) {
                idKategorijeNew.getOglasCollection().add(oglas);
                idKategorijeNew = em.merge(idKategorijeNew);
            }
            if (idPodrucjaOld != null && !idPodrucjaOld.equals(idPodrucjaNew)) {
                idPodrucjaOld.getOglasCollection().remove(oglas);
                idPodrucjaOld = em.merge(idPodrucjaOld);
            }
            if (idPodrucjaNew != null && !idPodrucjaNew.equals(idPodrucjaOld)) {
                idPodrucjaNew.getOglasCollection().add(oglas);
                idPodrucjaNew = em.merge(idPodrucjaNew);
            }
            if (idTipaOld != null && !idTipaOld.equals(idTipaNew)) {
                idTipaOld.getOglasCollection().remove(oglas);
                idTipaOld = em.merge(idTipaOld);
            }
            if (idTipaNew != null && !idTipaNew.equals(idTipaOld)) {
                idTipaNew.getOglasCollection().add(oglas);
                idTipaNew = em.merge(idTipaNew);
            }
            if (idNalogaOld != null && !idNalogaOld.equals(idNalogaNew)) {
                idNalogaOld.getOglasCollection().remove(oglas);
                idNalogaOld = em.merge(idNalogaOld);
            }
            if (idNalogaNew != null && !idNalogaNew.equals(idNalogaOld)) {
                idNalogaNew.getOglasCollection().add(oglas);
                idNalogaNew = em.merge(idNalogaNew);
            }
            for (Informacije informacijeCollectionOldInformacije : informacijeCollectionOld) {
                if (!informacijeCollectionNew.contains(informacijeCollectionOldInformacije)) {
                    informacijeCollectionOldInformacije.setIdOglasa(null);
                    informacijeCollectionOldInformacije = em.merge(informacijeCollectionOldInformacije);
                }
            }
            for (Informacije informacijeCollectionNewInformacije : informacijeCollectionNew) {
                if (!informacijeCollectionOld.contains(informacijeCollectionNewInformacije)) {
                    Oglas oldIdOglasaOfInformacijeCollectionNewInformacije = informacijeCollectionNewInformacije.getIdOglasa();
                    informacijeCollectionNewInformacije.setIdOglasa(oglas);
                    informacijeCollectionNewInformacije = em.merge(informacijeCollectionNewInformacije);
                    if (oldIdOglasaOfInformacijeCollectionNewInformacije != null && !oldIdOglasaOfInformacijeCollectionNewInformacije.equals(oglas)) {
                        oldIdOglasaOfInformacijeCollectionNewInformacije.getInformacijeCollection().remove(informacijeCollectionNewInformacije);
                        oldIdOglasaOfInformacijeCollectionNewInformacije = em.merge(oldIdOglasaOfInformacijeCollectionNewInformacije);
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
                Integer id = oglas.getIdOglasa();
                if (findOglas(id) == null) {
                    throw new NonexistentEntityException("The oglas with id " + id + " no longer exists.");
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
            Oglas oglas;
            try {
                oglas = em.getReference(Oglas.class, id);
                oglas.getIdOglasa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The oglas with id " + id + " no longer exists.", enfe);
            }
            Kategorija idKategorije = oglas.getIdKategorije();
            if (idKategorije != null) {
                idKategorije.getOglasCollection().remove(oglas);
                idKategorije = em.merge(idKategorije);
            }
            Podrucje idPodrucja = oglas.getIdPodrucja();
            if (idPodrucja != null) {
                idPodrucja.getOglasCollection().remove(oglas);
                idPodrucja = em.merge(idPodrucja);
            }
            Tip idTipa = oglas.getIdTipa();
            if (idTipa != null) {
                idTipa.getOglasCollection().remove(oglas);
                idTipa = em.merge(idTipa);
            }
            Nalog idNaloga = oglas.getIdNaloga();
            if (idNaloga != null) {
                idNaloga.getOglasCollection().remove(oglas);
                idNaloga = em.merge(idNaloga);
            }
            Collection<Informacije> informacijeCollection = oglas.getInformacijeCollection();
            for (Informacije informacijeCollectionInformacije : informacijeCollection) {
                informacijeCollectionInformacije.setIdOglasa(null);
                informacijeCollectionInformacije = em.merge(informacijeCollectionInformacije);
            }
            em.remove(oglas);
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

    public List<Oglas> findOglasEntities() {
        return findOglasEntities(true, -1, -1);
    }

    public List<Oglas> findOglasEntities(int maxResults, int firstResult) {
        return findOglasEntities(false, maxResults, firstResult);
    }

    private List<Oglas> findOglasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Oglas.class));
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

    public Oglas findOglas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Oglas.class, id);
        } finally {
            em.close();
        }
    }

    public int getOglasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Oglas> rt = cq.from(Oglas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
