package org.example;
import net.sf.json.JSONArray;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import net.sf.json.JSONObject;

/**
 * Ajoute une ou plusieurs observations dans le fichier JSON de sortie.
 */
public class Observations {
    private static ArrayList<String> observations = new ArrayList<>();



    public static ArrayList<String> getObservations() {
        return observations;
    }

    private  List<String> observations() {
      return observations;
    }
    private double formatterMontant (String montant) {
     montant = montant.trim().replace("$", "");

     return Double.parseDouble(montant);
    }
    private void valeurLot(JSONObject resultat) {
      JSONArray valeurParLot = resultat.getJSONArray("lotissements") ;
      for (int i = 0; i < valeurParLot.size(); i ++) {
          JSONObject lot = valeurParLot.getJSONObject(i);
          double valeurMax = formatterMontant(lot.getString("valeur_par_lot"));
          if (valeurMax > 45000) {
              String lotCourant = lot.getString("description");
              observations.add(String.format("La valeur par lot du %s est trop dispendieuse.", lotCourant));
          }
      }
    }
    private List<LocalDate> trierDate (Terrain terrain) {
        List<Lot> lotissements = terrain.getLotissements();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<LocalDate> dates = new ArrayList<>();
        for (int i = 0; i < lotissements.size() ; i ++) {
            dates.add(LocalDate.parse(lotissements.get(i).getDateMesure(), format));
        }
        Collections.sort(dates);
        return dates;
    }

    private void ecartDate (Terrain terrain) {
        List<LocalDate> dates = trierDate(terrain);
        for (int i = 0; i < dates.size() ; i ++) {
            for( int j = 1; j < dates.size() ; j ++) {
                LocalDate date1 = dates.get(i);
                LocalDate date2 = dates.get(j);
                Period period = Period.between(date1, date2);
                int mois = Math.abs(period.getMonths()) + Math.abs(period.getYears() * 12);
                if (mois >= 6) {
                    i = dates.size() - 1;
                    observations.add("L'ecart maximal entre les dates de mesure des lots d'un même terrain " +
                            "devrait être de moins de 6 mois.");
                }
            }
        }
    }
    private void taxes (JSONObject resultat) {
        double taxeMunicipale = formatterMontant(resultat.getString("taxe_municipale"));
        double taxeScolaire = formatterMontant(resultat.getString("taxe_scolaire"));
        if (taxeScolaire > 500) {
            observations.add("La taxe scolaire payable par le proprietaire necessite deux versements.");
        } else if (taxeMunicipale > 1000) {
            observations.add("La taxe municipale payable par le proprietaire necessite deux versements.");
        }
    }

    private void prixMaximal (Terrain terrain) {
        if ((terrain.getPrixM2Min() * 2) < terrain.getPrixM2Max()) {
            observations.add("Le prix maximum du m2 ne peut pas depasser deux fois le prix minimum du m2.");
        }
    }
    private void valeurFonciereTotale (JSONObject resultat) {
        double valeur = formatterMontant(resultat.getString("valeur_fonciere_totale"));
        if (valeur > 300000) {
            observations.add("La valeur fonciere totale ne doit pas depasser 300000.00$");
        }
    }
    private void superficie(Terrain terrain) {
        List<Lot> lotissements = terrain.getLotissements();
        Lot lot = new Lot();
        for (Lot lotissement : lotissements) {
            int superficie = lotissement.getSuperficie();
            if (superficie > 45000) {
                String lotCourant = lotissement.getDescription();
                observations.add(String.format("La superficie du %s est trop grande.", lotCourant));
            }
        }
    }
    public void verifierObservations(Terrain terrain, JSONObject resultat) {
         Observations observation = new Observations();
         observation.ecartDate(terrain);
         observation.valeurLot(resultat);
         observation.taxes(resultat);
         observation.prixMaximal(terrain);
         observation.valeurFonciereTotale(resultat);
         observation.superficie(terrain);
    }
    }

