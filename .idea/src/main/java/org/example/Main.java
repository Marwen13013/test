package org.example;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Classe principale pour exécuter le programme d'évaluation foncière.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar Projet.jar entree.json sortie.json");
            return;
        }

        String inputFilePath = args[0];
        String outputFilePath = args[1];

        try {
            Terrain terrain = JsonManip.lireFichierJSON(inputFilePath);
            EvaluationFonciereCalcul service = new EvaluationFonciereCalcul();
            JSONObject result = service.calculerEvaluationFonciere(terrain);
            EvaluationFonciereCalcul.observationsSouleves(terrain, result);
            JsonManip.ecrireFichierJSON(outputFilePath, result);
            System.out.println("Évaluation foncière calculée avec succès!");
        } catch (IOException e) {
            gererErreur(outputFilePath, "Erreur lors de la lecture ou de l'écriture des fichiers : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            gererErreur(outputFilePath, e.getMessage());
        }
    }

    private static void gererErreur(String outputFilePath, String message) {
        JSONObject errorJson = new JSONObject();
        errorJson.put("message", message);
        try {
            JsonManip.ecrireFichierJSON(outputFilePath, errorJson);
        } catch (IOException ioException) {
            System.out.println("Impossible de créer le fichier de sortie. Veuillez vérifier les permissions d'écriture dans le répertoire et réessayer.");
        }
    }
}