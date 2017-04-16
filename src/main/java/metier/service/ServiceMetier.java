/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

import com.google.maps.model.LatLng;
import dao.ActiviteDAO;
import dao.AdherentDAO;
import dao.DemandeDAO;
import dao.EvenementDAO;
import dao.JpaUtil;
import dao.LieuDAO;
import java.util.ArrayList;
import java.util.List;
import metier.modele.Activite;
import metier.modele.Adherent;
import metier.modele.Demande;
import metier.modele.EvPayant;
import metier.modele.Evnmt;
import metier.modele.Lieu;
import static metier.service.ServiceTechnique.getLatLng;
import static metier.service.ServiceTechnique.sendMailInscription;
import static metier.service.ServiceTechnique.sendMailPlanifier;

/**
 *
 * @author mpourbaix & acarpentier
 */
public class ServiceMetier {

    public static boolean saveAdherent(Adherent monAdherent) throws Exception
    {
        JpaUtil.creerEntityManager();

        AdherentDAO aDAO = new AdherentDAO();

        if (!aDAO.checkAdhExist(monAdherent)) {
            LatLng monLatLng = getLatLng(monAdherent.getAdresse());
            monAdherent.setLatitudeLongitude(monLatLng.lat, monLatLng.lng);

            JpaUtil.ouvrirTransaction();
            aDAO.create(monAdherent);

            JpaUtil.validerTransaction();

            sendMailInscription(monAdherent, true);
            JpaUtil.fermerEntityManager();

            return true;
        } else {
            sendMailInscription(monAdherent, false);
            return false;
        }
    }

    public static List<Adherent> consulterListeAdherent() throws Exception {
        JpaUtil.creerEntityManager();

        AdherentDAO aDAO = new AdherentDAO();

        List<Adherent> maListe = aDAO.findAll();

        JpaUtil.fermerEntityManager();

        return maListe;

    }

    public static void saveLieu(Lieu monLieu) throws Exception {
        JpaUtil.creerEntityManager();

        LieuDAO aDAO = new LieuDAO();

        JpaUtil.ouvrirTransaction();
        aDAO.create(monLieu);

        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();

    }

    public static List<Lieu> consulterListeLieu() throws Exception {
        JpaUtil.creerEntityManager();

        LieuDAO aDAO = new LieuDAO();

        List<Lieu> maListe = aDAO.findAll();

        JpaUtil.fermerEntityManager();
        return maListe;
    }

    public static void saveActivite(Activite monActivite) throws Exception {
        JpaUtil.creerEntityManager();

        ActiviteDAO aDAO = new ActiviteDAO();

        JpaUtil.ouvrirTransaction();
        aDAO.create(monActivite);

        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }

    public static Adherent Connexion(String mail) throws Exception {
        JpaUtil.creerEntityManager();

        AdherentDAO aDAO = new AdherentDAO();

        Adherent monAdh = aDAO.findByMail(mail);

        JpaUtil.fermerEntityManager();

        return monAdh;

    }

    public static boolean saveDemande(Adherent monAdh, Demande maDmd, int actId) throws Exception //return false if pas pu inséré
    {
        JpaUtil.creerEntityManager();

        DemandeDAO dDAO = new DemandeDAO();

        //SAUVEGARDE DEMANDE
        if (!dDAO.checkDmdExist(monAdh, maDmd, actId)) {
            JpaUtil.ouvrirTransaction();
            dDAO.create(monAdh, maDmd, actId);
            JpaUtil.validerTransaction();
            JpaUtil.fermerEntityManager();

            //ON CHECK SI ASSEZ DE GENS POUR FAIRE L'EVT ET ON LE CREE
            JpaUtil.creerEntityManager();
            JpaUtil.ouvrirTransaction();
            if (dDAO.checkAndCreationEvt(maDmd)) {
                System.out.println("Création d'EVENEMENT !");
                JpaUtil.validerTransaction();
            } else {
                JpaUtil.annulerTransaction();
            }

            JpaUtil.fermerEntityManager();

            return true;
        } else {
            System.out.println("Demande déjà existante");
            JpaUtil.fermerEntityManager();
            return false;
        }
    }

    public static List<Activite> consulterListeActivite() throws Exception {
        JpaUtil.creerEntityManager();

        ActiviteDAO aDAO = new ActiviteDAO();

        List<Activite> maListe = aDAO.findAll();

        JpaUtil.fermerEntityManager();
        return maListe;
    }

    public static List<Demande> consulterListeDemandeById(int id) throws Exception {
        JpaUtil.creerEntityManager();

        DemandeDAO dDAO = new DemandeDAO();

        List<Demande> maListe = dDAO.findById(id);

        JpaUtil.fermerEntityManager();
        return maListe;
    }

    public static List<Evnmt> consulterListeEvt() throws Exception {
        JpaUtil.creerEntityManager();

        EvenementDAO eDAO = new EvenementDAO();

        List<Evnmt> maListe = eDAO.findAll();

        JpaUtil.fermerEntityManager();
        return maListe;
    }

    public static List<Evnmt> consulterListeEvtAValider() throws Exception {
        JpaUtil.creerEntityManager();

        List<Evnmt> maListe = new ArrayList();
        EvenementDAO eDAO = new EvenementDAO();
        for (Evnmt Evt : eDAO.findAll()) {
            if (!Evt.isPlanifie()) {
                maListe.add(Evt);
            }
        }

        JpaUtil.fermerEntityManager();
        return maListe;
    }

    public static List<Adherent> consulterListeParticipant(Evnmt myEvnmt) throws Exception {
        JpaUtil.creerEntityManager();

        List<Adherent> maListe = new ArrayList();
        for (Demande d : myEvnmt.getMaListeDemandesEVT()) {
            maListe.add(d.getMonAdhMTO());
        }

        JpaUtil.fermerEntityManager();

        return maListe;

    }

    //new!
    public static Activite consulterActiviteById(long id) throws Exception{
        JpaUtil.creerEntityManager();
        ActiviteDAO adao = new ActiviteDAO();
        Activite act = adao.findById(id);
        JpaUtil.fermerEntityManager();
        return act;
    }
    
    public static void affecterPaf(int idEVT, int montant) throws Exception {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        EvenementDAO eDAO = new EvenementDAO();

        Evnmt monEVT = eDAO.findById((long) idEVT);

        if (monEVT != null) {
            if (monEVT instanceof EvPayant) {
                EvPayant EVT_P = (EvPayant) monEVT;
                EVT_P.setMontantPaf(montant);
            } else {
                System.out.println("Erreur c'est un EVT_nonPayant");
            }
        } else {
            System.out.println("Erreur pas d'EVT à cette id");
        }
        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();
    }

    public static void affecterLieu(int idEVT, int idLieu) throws Exception {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        EvenementDAO eDAO = new EvenementDAO();
        Evnmt monEVT = eDAO.findById((long) idEVT);

        if (monEVT == null) {
            System.out.println("Erreur pas d'EVT à cette id");
            JpaUtil.fermerEntityManager();
            return;
        }

        LieuDAO lDAO = new LieuDAO();
        Lieu monLieu = lDAO.findById((long) idLieu);

        if (monLieu == null) {
            System.out.println("Erreur pas de Lieu à cette id");
            JpaUtil.fermerEntityManager();
            return;
        }

        monEVT.setAssociatedLieu(monLieu);
        monEVT.setPlanifie(true);

        JpaUtil.validerTransaction();
        JpaUtil.fermerEntityManager();

        for (Adherent toto : consulterListeParticipant(monEVT)) {
            System.out.println(toto);
            sendMailPlanifier(toto, monEVT);
        }
    }
}
