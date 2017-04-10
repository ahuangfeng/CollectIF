package vue;

import dao.JpaUtil;
import static java.lang.System.exit;
import java.util.Date;
import java.util.List;

import metier.modele.Activite;
import metier.modele.Adherent;
import metier.modele.Demande;
import metier.modele.Evnmt;
import metier.modele.Lieu;
import static metier.service.ServiceMetier.Connexion;
import static metier.service.ServiceMetier.affecterLieu;
import static metier.service.ServiceMetier.affecterPaf;
import static metier.service.ServiceMetier.consulterListeActivite;
import static metier.service.ServiceMetier.consulterListeDemandeById;
import static metier.service.ServiceMetier.consulterListeEvt;
import static metier.service.ServiceMetier.consulterListeEvtAValider;
import static metier.service.ServiceMetier.consulterListeLieu;
import static metier.service.ServiceMetier.consulterListeParticipant;
import static metier.service.ServiceMetier.saveAdherent;
import static metier.service.ServiceMetier.saveDemande;
import static util.Saisie.lireChaine;
import static util.Saisie.lireInteger;

/**
 *
 * @author mpourbaix & acarpentier
 */
public class Main {

    public static void main(String[] args) throws Exception {
        JpaUtil.init();
        long idConnecte = 0;

        int choix = 0;

        while (choix != -1) {
            choix = lireInteger("\n\n==============Bienvenue à Collect'IF==============\n\nFaites votre choix :\n\n1)Enregistrer un utilisateur\n\n2)Se connecter\n\n3)Vision admin validation d'événements\n\n4)Vision admin EVT non validés\n\n5)AffecterPAF\n\n6)Afficher Lieu\n\n7)Affecter Lieu\n\n8)Quitter\n "); // rajouter cond sur les valeurs admises
            // CREATION ADH
            switch (choix) {
                //LOGIN
                case 1:
                    Adherent monAdh = new Adherent(lireChaine("nom : "), lireChaine("prenom : "), lireChaine("mail : "), lireChaine("adresse : "));
                    if (saveAdherent(monAdh)) {

                    } else {
                        System.out.println("\nImpossible de créer l'adh (existe déjà)\n");
                    }
                    break;
                case 2:
                    Adherent AdhConnect = Connexion(lireChaine("Votre email : "));
                    if (AdhConnect != null) {
                        System.out.println("Bienvenue " + AdhConnect);
                        idConnecte = AdhConnect.getId();

                        int choix2 = lireInteger("\n\nQue voulez-vous faire ?\n\n1)Poster une demande\n\n2)Consulter vos demandes ?"); // rajouter cond sur les valeurs admises

                        // CREATION DEMANDE
                        if (choix2 == 1) {

                            String moment = null;
                            while (!"matin".equals(moment) && !"aprem".equals(moment) && !"soir".equals(moment)) {
                                moment = lireChaine("moment (matin, aprem, soir) : ");
                                System.out.println(moment);
                            }

                            int jour = lireInteger("Jour ? :");
                            int mois = lireInteger("Mois ? :");
                            int annee = lireInteger("Année ? :");
                            mois -= 1;
                            annee -= 1900;

                            Date maDate = new Date(annee, mois, jour);

                            Demande maDmd = new Demande(maDate, moment);

                            List<Activite> listAct = consulterListeActivite();

                            for (int i = 0; i < listAct.size(); i++) {
                                System.out.println(listAct.get(i));
                            }

                            int idAct = lireInteger("\nid de l'activité ? ");

                            if (saveDemande(AdhConnect, maDmd, idAct)) {
                                System.out.println("Demande crée");
                            } else {
                                System.out.println("Creation demande impossible");
                                exit(0);
                            }

                        }
                        if (choix == 2) {
                            List<Demande> ListeDemandes = consulterListeDemandeById((int) idConnecte);

                            for (int i = 0; i < ListeDemandes.size(); i++) {
                                System.out.println(ListeDemandes.get(i));
                            }
                        }
                    } else {
                        System.out.println("INCONNU");
                    }

                    break;
                case 3:

                    List<Evnmt> ListeEVT = consulterListeEvt();

                    for (int i = 0; i < ListeEVT.size(); i++) {
                        System.out.println(ListeEVT.get(i));

                        for (Adherent toto : consulterListeParticipant(ListeEVT.get(i))) {
                            System.out.println(toto);

                        }
                    }

                    break;
                case 4:
                    for (Evnmt monEvt : consulterListeEvtAValider()) {
                        System.out.println(monEvt);
                    }
                    break;
                case 5:
                    affecterPaf(lireInteger("\nid de l'evt ? "), lireInteger("\nMontant PAF ? "));
                    break;
                case 6:
                    for (Lieu monLieu : consulterListeLieu()) {
                        System.out.println(monLieu);
                    }
                    break;
                case 7:
                    affecterLieu(lireInteger("\nid de l'evt ? "), lireInteger("\nid du lieu ? "));
                    break;
                case 8:
                    choix = -1;
                    break;
                default:
                    break;
            }
        }
        JpaUtil.destroy();
    }
}
