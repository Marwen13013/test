import org.example.model.Lot;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LotTest {

    @Test
    public void testValiderDateValidLot() {
        Lot lot = new Lot("Lot 1", 1, 2, 1000, "2023-01-01");
        assertNull(lot.validerDate());
    }

    @Test
    public void testValiderDateEmptyDescription() {
        Lot lot = new Lot("", 1, 2, 1000, "2023-01-01");
        assertEquals("La description du lot ne peut pas être vide.", lot.validerDate());
    }

    @Test
    public void testValiderDateInvalidDroitsPassage() {
        Lot lot = new Lot("Lot 1", -1, 2, 1000, "2023-01-01");
        assertEquals("Le nombre de droits de passage est invalide.", lot.validerDate());
    }

    @Test
    public void testValiderDateInvalidServices() {
        Lot lot = new Lot("Lot 1", 1, 6, 1000, "2023-01-01");
        assertEquals("Le nombre de services est invalide.", lot.validerDate());
    }

    @Test
    public void testValiderDateInvalidSuperficie() {
        Lot lot = new Lot("Lot 1", 1, 2, -1000, "2023-01-01");
        assertEquals("La superficie est invalide.", lot.validerDate());
    }

    @Test
    public void testValiderDateInvalidDateMesure() {
        Lot lot = new Lot("Lot 1", 1, 2, 1000, "invalid-date");
        assertEquals("Le format de la date de mesure est invalide.", lot.validerDate());
    }

    @Test
    public void testValiderDateDescriptionsUnique() {
        List<Lot> lots = List.of(
                new Lot("Lot 1", 1, 2, 1000, "2023-01-01"),
                new Lot("Lot 1", 1, 2, 2000, "2023-01-02")
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Lot.validerDescription(lots);
        });

        assertEquals("La description du lot 'Lot 1' est dupliquée.", exception.getMessage());
    }
}

