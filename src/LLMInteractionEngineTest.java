import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitários para a classe {@link LLMInteractionEngine}.
 * Verifica o comportamento do método de construção de JSON.
 */
public class LLMInteractionEngineTest {

    /**
     * Testa o método {@link LLMInteractionEngine#buildJSON(String, String)}.
     * Verifica se o JSON é construído corretamente, escapando aspas e quebras de linha.
     */
    @Test
    void testBuildJSON() {
        LLMInteractionEngine engine = new LLMInteractionEngine("url", "key", "modelo");

        String prompt = "Test \"prompt\" \n linha";
        String json = engine.buildJSON("modelo", prompt);

        assertTrue(json.contains("\"model\": \"modelo\""));
        assertTrue(json.contains("Test \\\"prompt\\\""));
        assertTrue(json.contains("\\n"));
        assertFalse(json.contains("\n"));
    }
}
