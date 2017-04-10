/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Adherent;
import metier.modele.Demande;
import metier.modele.EvNonPayant;
import metier.modele.EvPayant;
import metier.modele.Evnmt;
import static metier.service.ServiceMetier.consulterListeParticipant;

/**
 * @author Alex
 */
public class EvenementDAO {

    public void create(Demande dem) throws Exception {

        EntityManager em = JpaUtil.obtenirEntityManager();

        //CLES ETRANGERES
        Evnmt EVT = null;
        if (dem.getMonActMTO().getPayant()) {
            EVT = new EvPayant(0, dem.getDate(), dem.getMoment(), false);
        } else if (!dem.getMonActMTO().getPayant()) {
            EVT = new EvNonPayant(dem.getDate(), dem.getMoment(), false);
        } else {
            System.out.println("Evenement ni payant ni gratuit");
        }

        //récupérer toutes les demandes mê heure mê date mê acti que dem et les ajouter à EVT
        DemandeDAO dDAO = new DemandeDAO();
        List<Demande> listeDemandesPareil = dDAO.findByDemandeNotTraiter(dem);

        if (listeDemandesPareil.size() >= dem.getMonActMTO().getNbParticipants()) {
            for (int i = 0; i < dem.getMonActMTO().getNbParticipants(); i++) {
                EVT.addDemande(listeDemandesPareil.get(i));
                listeDemandesPareil.get(i).setTraiter(Boolean.TRUE);
            }

            try {
                System.out.println("Persistance de :" + EVT);
                em.persist(EVT);
            } catch (Exception e) {
                em.getTransaction().rollback();
                throw e;
            }
        }
    }

    public Evnmt findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Evnmt evt = null;
        try {
            evt = em.find(Evnmt.class, id);
        } catch (Exception e) {
            throw e;
        }
        return evt;
    }

    public List<Evnmt> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Evnmt> evt = null;
        try {
            Query q = em.createQuery("SELECT a FROM Evnmt a");
            evt = (List<Evnmt>) q.getResultList();
        } catch (Exception e) {
            throw e;
        }
        return evt;
    }
}
