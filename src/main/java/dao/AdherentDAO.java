package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Adherent;

public class AdherentDAO {

    public Adherent findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Adherent adherent = null;
        try {
            adherent = em.find(Adherent.class, id);
        } catch (Exception e) {
            throw e;
        }
        return adherent;
    }

    public List<Adherent> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Adherent> adherents = null;
        try {
            Query q = em.createQuery("SELECT a FROM Adherent a");
            adherents = (List<Adherent>) q.getResultList();
        } catch (Exception e) {
            throw e;
        }

        return adherents;
    }

    public boolean checkAdhExist(Adherent monAdh) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            System.out.println("coucou");
            Query q = em.createQuery("SELECT count(a) FROM Adherent a WHERE a.mail ='" + monAdh.getMail() + "'");
            long count = (long) q.getSingleResult();
            if (count > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    public void create(Adherent monAdherent) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();

        try {
            em.persist(monAdherent);
        } catch (Exception e) {
            throw e;
        }
    }

    public Adherent findByMail(String mail) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Adherent adherent = null;
        try {
            Query q = em.createQuery("SELECT a FROM Adherent a WHERE a.mail ='" + mail + "'");

            List results = q.getResultList();
            if (results.isEmpty()) {
                return null;
            }

            adherent = (Adherent) q.getSingleResult();

            return adherent;

        } catch (Exception e) {

            System.out.println("ERRRRREEEEEUUUR");
            throw e;
        }

    }

}
