import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LLMRecipeGeneratorTest {

    static class FakeEngine extends LLMInteractionEngine {
        String resposta;
        FakeEngine(String r) { super("URL","KEY","MODEL"); this.resposta = r; }
        @Override String sendPrompt(String prompt) { return resposta; }
    }

    @Test
    void testEscolhaValida() {
        int escolha = 3;
        assertTrue(escolha >= 1 && escolha <= 5);
    }

    @Test
    void testIngredientesString() {
        String ingredientes = "ovo, leite, farinha";
        assertTrue(ingredientes.contains("ovo"));
        assertFalse(ingredientes.contains("chocolate"));
    }

    @Test
    void testPedirTitulosReceitasAoLLM() throws Exception {
        String respJson = "{\"text\":\"1 - Omelete\\n2 - Massa\"}";
        LLMRecipeGenerator gen = new LLMRecipeGenerator(new FakeEngine(respJson));

        String r = gen.pedirTitulosReceitasAoLLM("ovo, leite");
        assertNotNull(r);
        assertTrue(r.contains("1 - Omelete"));
    }

    @Test
    void testPedirReceitaCompletaAoLLM_Text() throws Exception {
        String respJson = "{\"text\":\"Passo 1: Misturar\"}";
        LLMRecipeGenerator gen = new LLMRecipeGenerator(new FakeEngine(respJson));
        String receita = gen.pedirReceitaCompletaAoLLM("ovos","1 - Omelete",1);
        assertNotNull(receita);
        assertTrue(receita.contains("Passo 1"));
    }

    @Test
    void testPedirReceitaCompletaAoLLM_Content() throws Exception {
        String respJson = "{\"content\":\"Linha A\"}";
        LLMRecipeGenerator gen = new LLMRecipeGenerator(new FakeEngine(respJson));
        String receita = gen.pedirReceitaCompletaAoLLM("ovos","1 - Omelete",1);
        assertNotNull(receita);
        assertTrue(receita.contains("Linha A"));
    }

    @Test
    void testPedirReceitaCompletaAoLLM_Erro() throws Exception {
        String respJson = "{\"nada\":\"xxx\"}";
        LLMRecipeGenerator gen = new LLMRecipeGenerator(new FakeEngine(respJson));
        String receita = gen.pedirReceitaCompletaAoLLM("ovos","1 - Omelete",1);
        assertNull(receita);
    }
}
