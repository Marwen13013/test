package org.example.service;

import org.example.model.Lot;
import org.example.model.Terrain;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class EvaluationFonciereCalcul {

    public JSONObject calculerEvaluationFonciere(Terrain terrain) {
        double valeurFonciereTotale = calculerValeurFonciereTotale(terrain);
        double taxeScolaire = calculerTaxe(valeurFonciereTotale, 0.012);
        double taxeMunicipale = calculerTaxe(valeurFonciereTotale, 0.025);
        JSONArray lotsArray = creerLotsArray(terrain);

        return creerResultatJSON(valeurFonciereTotale, taxeScolaire, taxeMunicipale, lotsArray);
    }

    private double calculerValeurFonciereTotale(Terrain terrain) {
        double valeurFonciereTotale = 733.77;
        for (Lot lot : terrain.getLotissements()) {
            valeurFonciereTotale += calculerValeurParLot(lot, terrain);
        }
        return arrondirValeur(valeurFonciereTotale);
    }

    private double calculerValeurParLot(Lot lot, Terrain terrain) {
        double valeurLot = calculerValeurLot(lot, terrain);
        double valeurDroitsPassage = calculerValeurDroitsPassage(lot, valeurLot, terrain.getTypeTerrain());
        double valeurServices = calculerValeurServices(lot, terrain.getTypeTerrain());
        return valeurLot + valeurDroitsPassage + valeurServices;
    }

    private double calculerValeurLot(Lot lot, Terrain terrain) {
        double prixM2 = getPrixM2(terrain);
        return lot.getSuperficie() * prixM2;
    }

    private double getPrixM2(Terrain terrain) {
        switch (terrain.getTypeTerrain()) {
            case 0: return terrain.getPrixM2Min();
            case 1: return (terrain.getPrixM2Min() + terrain.getPrixM2Max()) / 2;
            case 2: return terrain.getPrixM2Max();
            default: throw new IllegalArgumentException("Type de terrain invalide");
        }
    }

    private double calculerValeurDroitsPassage(Lot lot, double valeurLot, int typeTerrain) {
        double base = 500;
        double pourcentage = getPourcentage(typeTerrain);
        return base - (lot.getNombreDroitsPassage() * pourcentage * valeurLot);
    }

    private double getPourcentage(int typeTerrain) {
        switch (typeTerrain) {
            case 0: return 0.05;
            case 1: return 0.10;
            case 2: return 0.15;
            default: throw new IllegalArgumentException("Type de terrain invalide");
        }
    }

    private double calculerValeurServices(Lot lot, int typeTerrain) {
        int totalServices = lot.getNombreServices() + 2;
        double valeurService = getValeurService(lot, typeTerrain);
        return Math.min(valeurService * totalServices, 5000);
    }

    private double getValeurService(Lot lot, int typeTerrain) {
        if (typeTerrain == 0) return 0;
        if (typeTerrain == 1) return getValeurServiceResidentiel(lot);
        if (typeTerrain == 2) return getValeurServiceCommercial(lot);
        throw new IllegalArgumentException("Type de terrain invalide");
    }

    private double getValeurServiceResidentiel(Lot lot) {
        if (lot.getSuperficie() <= 500) return 0;
        if (lot.getSuperficie() <= 10000) return 500;
        return 1000;
    }

    private double getValeurServiceCommercial(Lot lot) {
        if (lot.getSuperficie() <= 500) return 500;
        return 1500;
    }

    private double calculerTaxe(double valeurFonciere, double taux) {
        return Math.ceil(valeurFonciere * taux * 20.0) / 20.0;
    }

    private double arrondirValeur(double valeur) {
        return Math.ceil(valeur * 20.0) / 20.0;
    }

    private JSONArray creerLotsArray(Terrain terrain) {
        JSONArray lotsArray = new JSONArray();
        for (Lot lot : terrain.getLotissements()) {
            lotsArray.add(creerLotJson(lot, terrain));
        }
        return lotsArray;
    }

    private JSONObject creerLotJson(Lot lot, Terrain terrain) {
        JSONObject lotObject = new JSONObject();
        lotObject.put("description", lot.getDescription());
        lotObject.put("valeur_par_lot", String.format("%.2f $", calculerValeurParLot(lot, terrain)));
        return lotObject;
    }

    private JSONObject creerResultatJSON(double valeurFonciereTotale, double taxeScolaire, double taxeMunicipale, JSONArray lotsArray) {
        JSONObject result = new JSONObject();
        result.put("valeur_fonciere_totale", String.format("%.2f $", valeurFonciereTotale));
        result.put("taxe_scolaire", String.format("%.2f $", taxeScolaire));
        result.put("taxe_municipale", String.format("%.2f $", taxeMunicipale));
        result.put("lotissements", lotsArray);
        return result;
    }
}
