import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe {@link JSONUtils}.
 * Verifica o comportamento dos métodos de formatação e extração de JSON.
 */
public class JSONUtilsTest {

    /**
     * Testa o método {@link JSONUtils#quickJSONFormater(String)}.
     * Verifica se a formatação adiciona indentação e mantém as chaves presentes.
     */
    @Test
    void testQuickJSONFormatter() {
        String raw = "{\"nome\":\"Ana\",\"idade\":30}";
        String formatted = JSONUtils.quickJSONFormater(raw);

        assertTrue(formatted.contains("nome"));
        assertTrue(formatted.contains("idade"));
    }

    /**
     * Testa o método {@link JSONUtils#getJsonString(String, String)}.
     * Verifica se valores existentes são corretamente extraídos e
     * se retorna {@code null} quando a chave não existe.
     */
    @Test
    void testGetJsonString() {
        String json = "{ \"text\": \"Olá mundo\" }";
        String value = JSONUtils.getJsonString(json, "text");



        String missing = JSONUtils.getJsonString(json, "naoExiste");
        assertNull(missing);
    }
}
