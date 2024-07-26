package org.example;

import net.sf.json.JSONObject;

import java.util.List;
import java.util.Set;

/**
 * Classe utilitaire pour valider les objets JSON et les entités du domaine.
 */
public class Validation {

    protected static final Set<String> REQUIRED_TERRAIN_PROPERTIES = Set.of("type_terrain", "prix_m2_min", "prix_m2_max", "lotissements");
    protected static final Set<String> REQUIRED_LOT_PROPERTIES = Set.of("description", "nombre_droits_passage", "nombre_services", "superficie", "date_mesure");

    public static void validerPropietes(JSONObject jsonObject, Set<String> requiredProperties, String entityName) {
        for (String property : requiredProperties) {
            if (!jsonObject.has(property)) {
                throw new IllegalArgumentException("La propriété '" + property + "' est manquante pour l'entité " + entityName + ".");
            }
        }
    }

    public static void validerTerrain(Terrain terrain) {
        String validationMessage = terrain.valider();
        if (validationMessage != null) {
            throw new IllegalArgumentException(validationMessage);
        }
    }

    public static void validerLots(List<Lot> lots) {
        Lot.validerDescription(lots);
        for (Lot lot : lots) {
            String validationMessage = lot.valider();
            if (validationMessage != null) {
                throw new IllegalArgumentException(validationMessage);
            }
        }
    }
}

