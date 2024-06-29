import net.sf.json.JSONObject;
import org.example.manip.Validation;
import org.example.model.Lot;
import org.example.model.Terrain;
import org.junit.Test;

import java.util.List;
import java.util.Set;

public class ValidationTest {

    @Test
    public void testValiderPropietes() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type_terrain", 1);
        jsonObject.put("prix_m2_min", "100");
        jsonObject.put("prix_m2_max", "200");
        jsonObject.put("lotissements", new JSONObject());

        Validation.validerPropietes(jsonObject, Set.of("type_terrain", "prix_m2_min", "prix_m2_max", "lotissements"), "Terrain");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValiderPropietesMissing() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type_terrain", 1);

        Validation.validerPropietes(jsonObject, Set.of("type_terrain", "prix_m2_min", "prix_m2_max", "lotissements"), "Terrain");
    }

    @Test
    public void testValiderTerrain() {
        Terrain terrain = new Terrain();
        terrain.setTypeTerrain(1);
        terrain.setPrixM2Min(100);
        terrain.setPrixM2Max(200);
        terrain.setLotissements(List.of(new Lot("Lot 1", 1, 2, 1000, "2023-01-01")));

        Validation.validerTerrain(terrain);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateInvalidTerrain() {
        Terrain terrain = new Terrain();
        terrain.setTypeTerrain(3); // Invalid type

        Validation.validerTerrain(terrain);
    }

    @Test
    public void testValiderLots() {
        List<Lot> lots = List.of(new Lot("Lot 1", 1, 2, 1000, "2023-01-01"));
        Validation.validerLots(lots);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateInvalidLots() {
        List<Lot> lots = List.of(new Lot("", 1, 2, 1000, "2023-01-01"));
        Validation.validerLots(lots);
    }
}

