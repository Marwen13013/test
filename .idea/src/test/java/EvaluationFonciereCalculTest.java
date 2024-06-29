import net.sf.json.JSONObject;
import net.sf.json.JSONObject;
import org.example.model.Lot;
import org.example.model.Terrain;
import org.example.service.EvaluationFonciereCalcul;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class EvaluationFonciereCalculTest {

    @Test
    public void testCalculerEvaluationFonciere() {
        Terrain terrain = new Terrain();
        terrain.setTypeTerrain(1);
        terrain.setPrixM2Min(5);
        terrain.setPrixM2Max(10);
        Lot lot = new Lot("Lot 1", 1, 2, 1000,
                "2023-01-01");
        terrain.setLotissements(List.of(lot));

        EvaluationFonciereCalcul service = new EvaluationFonciereCalcul();
        JSONObject result = service.calculerEvaluationFonciere(terrain);

        assertNotNull(result);
        assertEquals("10233,80 $", result.getString("valeur_fonciere_totale"));
        assertEquals("122,85 $", result.getString("taxe_scolaire"));
        assertEquals("255,85 $", result.getString("taxe_municipale"));
        assertTrue(result.getJSONArray("lotissements").size() > 0);
    }
}

