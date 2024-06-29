package org.example.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Représente un lot dans un terrain.
 */
public class Lot {
    private String description;
    private int nombreDroitsPassage;
    private int nombreServices;
    private int superficie;
    private String dateMesure;

    public Lot() {
    }

    public Lot(String description, int nombreDroitsPassage, int nombreServices, int superficie, String dateMesure) {
        this.description = description;
        this.nombreDroitsPassage = nombreDroitsPassage;
        this.nombreServices = nombreServices;
        this.superficie = superficie;
        this.dateMesure = dateMesure;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNombreDroitsPassage() {
        return nombreDroitsPassage;
    }

    public void setNombreDroitsPassage(int nombreDroitsPassage) {
        this.nombreDroitsPassage = nombreDroitsPassage;
    }

    public int getNombreServices() {
        return nombreServices;
    }

    public void setNombreServices(int nombreServices) {
        this.nombreServices = nombreServices;
    }

    public int getSuperficie() {
        return superficie;
    }

    public void setSuperficie(int superficie) {
        this.superficie = superficie;
    }

    public String getDateMesure() {
        return dateMesure;
    }

    public void setDateMesure(String dateMesure) {
        this.dateMesure = dateMesure;
    }

    public String validerDate() {
        if (nombreDroitsPassage < 0 || nombreDroitsPassage > 10) {
            return "Le nombre de droits de passage est invalide.";
        }
        if (nombreServices < 0 || nombreServices > 5) {
            return "Le nombre de services est invalide.";
        }
        if (superficie < 0 || superficie > 50000) {
            return "La superficie est invalide.";
        }
        if (description == null || description.trim().isEmpty()) {
            return "La description du lot ne peut pas être vide.";
        }
        if (!estDateValide(dateMesure)) {
            return "Le format de la date de mesure est invalide.";
        }
        return null;
    }

    private boolean estDateValide(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static void validerDescription(List<Lot> lots) {
        Set<String> descriptions = new HashSet<>();
        for (Lot lot : lots) {
            String description = lot.getDescription().trim();
            if (description.isEmpty()) {
                throw new IllegalArgumentException("La description du lot ne peut pas être vide.");
            }
            if (!descriptions.add(description)) {
                throw new IllegalArgumentException("La description du lot '" + description + "' est dupliquée.");
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lot lot = (Lot) o;
        return Objects.equals(description, lot.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}

