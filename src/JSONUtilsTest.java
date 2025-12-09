import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JSONUtilsTest {

    @Test
    void testQuickJSONFormaterBasico() {
        String raw = "{\"nome\":\"Ana\",\"idade\":30}";
        String formatted = JSONUtils.quickJSONFormater(raw);

        assertTrue(formatted.contains("nome"));
        assertTrue(formatted.contains("idade"));
    }

    @Test
    void testQuickJSONFormaterComArray() {
        String raw = "{\"lista\":[1,2,3]}";
        String formatted = JSONUtils.quickJSONFormater(raw);

        assertTrue(formatted.contains("["));
        assertTrue(formatted.contains("1"));
        assertTrue(formatted.contains("3"));
    }

    @Test
    void testGetJsonStringExistente() {
        String json = "{\"text\":\"Olá mundo\"}";
        String value = JSONUtils.getJsonString(json, "text");

        assertEquals("Olá mundo", value);
    }

    @Test
    void testGetJsonStringInexistente() {
        String json = "{\"text\":\"Olá mundo\"}";
        assertNull(JSONUtils.getJsonString(json, "naoExiste"));
    }

    @Test
    void testGetJsonStringComEscape() {
        String json = "{\"text\":\"Oi \\\"amigo\\\"\"}";
        String value = JSONUtils.getJsonString(json, "text");

        assertEquals("Oi \\\"amigo\\\"", value);
    }
}
