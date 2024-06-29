package org.example.model;

import java.util.List;

/**
 * Représente un terrain avec plusieurs lots.
 */
public class Terrain {
    private int typeTerrain;
    private double prixM2Min;
    private double prixM2Max;
    private List<Lot> lotissements;

    public Terrain() {
    }

    public int getTypeTerrain() {
        return typeTerrain;
    }

    public void setTypeTerrain(int typeTerrain) {
        this.typeTerrain = typeTerrain;
    }

    public double getPrixM2Min() {
        return prixM2Min;
    }

    public void setPrixM2Min(double prixM2Min) {
        this.prixM2Min = prixM2Min;
    }

    public double getPrixM2Max() {
        return prixM2Max;
    }

    public void setPrixM2Max(double prixM2Max) {
        this.prixM2Max = prixM2Max;
    }

    public List<Lot> getLotissements() {
        return lotissements;
    }

    public void setLotissements(List<Lot> lotissements) {
        this.lotissements = lotissements;
    }

    public String valider() {
        if (typeTerrain < 0 || typeTerrain > 2) {
            return "Le type de terrain est invalide.";
        }
        if (lotissements == null || lotissements.isEmpty()) {
            return "Le terrain doit avoir au moins un lot.";
        }
        if (lotissements.size() > 10) {
            return "Le nombre de lots ne doit jamais dépasser 10.";
        }
        if (prixM2Max < 0 || prixM2Min < 0) {
            return "Le prix ne doit pas être négatif.";
        }
        for (Lot lot : lotissements) {
            String validationMessage = lot.validerDate();
            if (validationMessage != null) {
                return validationMessage;
            }
        }
        return null;
    }

}


