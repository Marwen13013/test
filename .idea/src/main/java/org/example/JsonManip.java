package org.example;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitaire pour lire et écrire des fichiers JSON.
 */
public class JsonManip {

    public static Terrain lireFichierJSON(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(content);

        JSONObject transformedJson = transformerCleEtValeur(jsonObject);
        Validation.validerPropietes(transformedJson, Validation.REQUIRED_TERRAIN_PROPERTIES, "Terrain");

        Terrain terrain = creerTerrain(transformedJson);
        List<Lot> lots = creerLotsArray(transformedJson.getJSONArray("lotissements"));
        Validation.validerLots(lots);

        terrain.setLotissements(lots);
        Validation.validerTerrain(terrain);

        return terrain;
    }

    private static Terrain creerTerrain(JSONObject transformedJson) {
        Terrain terrain = new Terrain();
        terrain.setTypeTerrain(transformedJson.getInt("type_terrain"));
        terrain.setPrixM2Min(Double.parseDouble(transformedJson.getString("prix_m2_min").replace(",", ".").replace(" $", "")));
        terrain.setPrixM2Max(Double.parseDouble(transformedJson.getString("prix_m2_max").replace(",", ".").replace(" $", "")));
        return terrain;
    }

    private static List<Lot> creerLotsArray(JSONArray lotsArray) {
        List<Lot> lots = new ArrayList<>();
        for (int i = 0; i < lotsArray.size(); i++) {
            JSONObject lotObject = lotsArray.getJSONObject(i);
            Validation.validerPropietes(lotObject, Validation.REQUIRED_LOT_PROPERTIES, "Lot");
            lots.add(creerLot(lotObject));
        }
        return lots;
    }

    private static Lot creerLot(JSONObject lotObject) {
        Lot lot = new Lot();
        lot.setDescription(lotObject.getString("description"));
        lot.setNombreDroitsPassage(lotObject.getInt("nombre_droits_passage"));
        lot.setNombreServices(lotObject.getInt("nombre_services"));
        lot.setSuperficie(lotObject.getInt("superficie"));
        lot.setDateMesure(lotObject.getString("date_mesure"));
        return lot;
    }

    public static void ecrireFichierJSON(String filePath, JSONObject jsonObject) throws IOException {
        JSONObject transformedJson = transformerCleEtValeur(jsonObject);
        Files.write(Paths.get(filePath), transformedJson.toString(2).getBytes());
    }

    private static JSONObject transformerCleEtValeur(JSONObject jsonObject) {
        JSONObject transformedJson = new JSONObject();
        for (Object key : jsonObject.keySet()) {
            String transformedKey = key.toString().toLowerCase().replaceAll("\\s+", "");
            Object value = jsonObject.get(key);

            if (value instanceof JSONObject) {
                value = transformerCleEtValeur((JSONObject) value);
            } else if (value instanceof JSONArray) {
                value = transformerArray((JSONArray) value);
            } else if (value instanceof String) {
                value = transformerValeur((String) value);
            }
            transformedJson.put(transformedKey, value);
        }
        return transformedJson;
    }

    private static String transformerValeur(String value) {
        if (value.contains(",") && !value.contains(".")) {
            return value.replace(",", ".");
        }
        return value;
    }

    private static JSONArray transformerArray(JSONArray jsonArray) {
        JSONArray transformedArray = new JSONArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONObject) {
                value = transformerCleEtValeur((JSONObject) value);
            } else if (value instanceof String) {
                value = transformerValeur((String) value);
            }
            transformedArray.add(value);
        }
        return transformedArray;
    }
}


