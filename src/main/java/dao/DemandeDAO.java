/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Activite;
import metier.modele.Adherent;
import metier.modele.Demande;
import util.Saisie;

/**
 *
 * @author mpourbaix
 */
public class DemandeDAO {

    public boolean checkDmdExist(Adherent monAdh, Demande maDmd, int actId) throws Exception { //même demande par même personne et même activité RAJOUTER VERIF ACTI
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            Query q = em.createQuery("SELECT count(a) FROM Demande a WHERE a.moment = '" + maDmd.getMoment() + "' and a.date = :date and a.monAdhMTO = :id");
            q.setParameter("id", monAdh);
            q.setParameter("date", maDmd.getDate());
            long count = (long) q.getSingleResult();
            if (count > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    public void create(Adherent monAdh, Demande maDmd, int actId) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();

        //CLES ETRANGERES
        monAdh.addDemande(maDmd);
        maDmd.setAssociatedAdh(monAdh);

        ActiviteDAO aDAO = new ActiviteDAO();
        Activite monAct = aDAO.findById(actId);
        maDmd.setAssociatedAct(monAct);

        try {
            em.merge(monAdh);
            em.persist(maDmd);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean checkAndCreationEvt(Demande maDmd) throws Exception {
        // ON VA VERIFIER SI ASSEZ DE DEMANDES POUR CREER EVT
        EntityManager em = JpaUtil.obtenirEntityManager();

        Query q = em.createQuery("SELECT count(a) FROM Demande a WHERE a.moment = '" + maDmd.getMoment() + "' and a.date = :date and a.monActMTO = :id");
        q.setParameter("id", maDmd.getMonActMTO());
        q.setParameter("date", maDmd.getDate());

        long count = (long) q.getSingleResult();
        if (count >= maDmd.getMonActMTO().getNbParticipants())
        {
            

            //On a assez de participants donc on crée l'evt
            EvenementDAO eDAO = new EvenementDAO();

            eDAO.create(maDmd);
            return true;
        }
        return false;
    }

    public List<Demande> findByDemandeNotTraiter(Demande maDmd) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Demande> demandesPareil = null;
        try {
            Query q = em.createQuery("SELECT a FROM Demande a WHERE a.date = :date AND a.moment ='" + maDmd.getMoment() + "' AND a.monActMTO = :id AND a.traiter = '0'");
            q.setParameter("id", maDmd.getMonActMTO());
            q.setParameter("date", maDmd.getDate());

            demandesPareil = (List<Demande>) q.getResultList();

            return demandesPareil;

        } catch (Exception e) {
            throw e;
        }
    }

    public List<Demande> findById(int id) throws Exception //id de l'adh
    {
        AdherentDAO adhDAO = new AdherentDAO();
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Demande> demandesId = null;
        try {
            Query q = em.createQuery("SELECT a FROM Demande a WHERE a.monAdhMTO = :id");
            q.setParameter("id", adhDAO.findById(id));

            demandesId = (List<Demande>) q.getResultList();

            return demandesId;

        } catch (Exception e) {
            throw e;
        }
    }
}
