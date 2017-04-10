
package metier.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import metier.modele.Adherent;
import metier.modele.Evnmt;
import static metier.service.ServiceMetier.consulterListeParticipant;


public class ServiceTechnique {

    final static GeoApiContext MON_CONTEXTE_GEOAPI = new GeoApiContext().setApiKey("AIzaSyDcVVJjfmxsNdbdUYeg9MjQoJJ6THPuap4");

    public static LatLng getLatLng(String adresse) {
        try {
            GeocodingResult[] results = GeocodingApi.geocode(MON_CONTEXTE_GEOAPI, adresse).await();
            return results[0].geometry.location;
        } catch (Exception ex) {
            return null;
        }
    }

    public static void sendMailInscription(Adherent Adh, boolean reussite) {
        if (reussite) {
            System.out.println("Simulation d'envoi d'email d'inscription à " + Adh.getMail());
            System.out.println("Expéditeur : collectif@collectif.org\nPour : " + Adh.getMail() + "\nSujet : Bienvenue chez Collect'IF\n"
                    + "Corps :\n"
                    + "Bonjour " + Adh.getPrenom() + ",\n"
                    + "Nous vous confirmons votre adhésion à l'association COLLECT’IF. Votre numéro d'adhérent est : " + Adh.getId());

            System.out.println("Simulation d'envoi d'email d'inscription à mailAdmin (supposee connue)");
        } else {
            System.out.println("Bonjour "+ Adh.getPrenom() +",\n"
                    + "Votre adhésion à l'association COLLECT’IF a malencontreusement échoué... Merci de recommencer\n"
                    + "ultérieurement.");
        }

    }

    public static void sendMailPlanifier(Adherent adh, Evnmt EVT) throws Exception {
        System.out.println("Expéditeur : collectif@collectif.org\n"
                + "Pour : " + adh.getMail() + "\n"
                + "Sujet : Nouvel Évènement Collect'IF\n"
                + "Corps :\n"
                + "Bonjour " + adh.getPrenom() + ",\n"
                + "Comme vous l'aviez souhaité, COLLECT’IF organise un évènement de " + EVT.getMaListeDemandesEVT().get(0).getMonActMTO() + "le " + EVT.getDate() + "Vous"
                + "trouverez ci-dessous les détails de cet évènement.\n\nAssociativement vôtre,\n"
                + "Le Responsable de l'Association");
        System.out.println(EVT);

        LatLng origin = new LatLng(adh.getLatitude(), adh.getLongitude());
        LatLng destination = new LatLng(EVT.getMonLieuMTO().getLatitude(), EVT.getMonLieuMTO().getLongitude());

        System.out.println("C'est à " + getFlightDistanceInKm(origin, destination) + " km de chez vous)");

        System.out.println("Vous jouerez avec :");
        for (Adherent toto : consulterListeParticipant(EVT)) {
            if (!toto.equals(adh)) {
                System.out.println(toto);
            }
        }
    }

    public static double getFlightDistanceInKm(LatLng origin, LatLng destination) {

        // From: http://www.movable-type.co.uk/scripts/latlong.html
        double R = 6371.0; // Average radius of Earth (km)
        double dLat = toRad(destination.lat - origin.lat);
        double dLon = toRad(destination.lng - origin.lng);
        double lat1 = toRad(origin.lat);
        double lat2 = toRad(destination.lat);

        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0)
                + Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        double d = R * c;

        return Math.round(d * 1000.0) / 1000.0;
    }

    public static double toRad(double angleInDegree) {
        return angleInDegree * Math.PI / 180.0;
    }
}
