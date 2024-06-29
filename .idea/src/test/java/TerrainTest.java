import org.example.model.Lot;
import org.example.model.Terrain;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TerrainTest {

    @Test
    public void testValiderValidTerrain() {
        Terrain terrain = new Terrain();
        terrain.setTypeTerrain(1);
        terrain.setPrixM2Min(100);
        terrain.setPrixM2Max(200);

        Lot lot1 = new Lot("Lot 1", 1, 2, 1000, "2023-01-01");
        Lot lot2 = new Lot("Lot 2", 2, 3, 2000, "2023-01-02");
        terrain.setLotissements(List.of(lot1, lot2));

        assertNull(terrain.valider());
    }

    @Test
    public void testValiderInvalidTerrainType() {
        Terrain terrain = new Terrain();
        terrain.setTypeTerrain(3); // Invalid type
        terrain.setPrixM2Min(100);
        terrain.setPrixM2Max(200);

        assertEquals("Le type de terrain est invalide.", terrain.valider());
    }

    @Test
    public void testValiderEmptyLots() {
        Terrain terrain = new Terrain();
        terrain.setTypeTerrain(1);
        terrain.setPrixM2Min(100);
        terrain.setPrixM2Max(200);
        terrain.setLotissements(List.of());

        assertEquals("Le terrain doit avoir au moins un lot.", terrain.valider());
    }

    @Test
    public void testValiderTooManyLots() {
        Terrain terrain = new Terrain();
        terrain.setTypeTerrain(1);
        terrain.setPrixM2Min(100);
        terrain.setPrixM2Max(200);

        List<Lot> lots = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            lots.add(new Lot("Lot " + i, 1, 2, 1000, "2023-01-01"));
        }
        terrain.setLotissements(lots);

        assertEquals("Le nombre de lots ne doit jamais dépasser 10.", terrain.valider());
    }
}

