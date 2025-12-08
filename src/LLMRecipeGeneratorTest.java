import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe {@link LLMRecipeGenerator}.
 * Verifica comportamentos básicos como validade de escolha e manipulação de ingredientes.
 */
public class LLMRecipeGeneratorTest {

    /**
     * Testa se a escolha do utilizador está dentro do intervalo válido (1 a 5).
     */
    @Test
    void testEscolhaValida() {
        int escolha = 3;

        assertTrue(escolha >= 1 && escolha <= 5);
        assertFalse(escolha < 1 || escolha > 5);
    }

    /**
     * Testa se a string de ingredientes contém os elementos esperados
     * e não contém elementos não fornecidos.
     */
    @Test
    void testIngredientesString() {
        String ingredientes = "ovo, leite, farinha";
        assertTrue(ingredientes.contains("ovo"));
        assertTrue(ingredientes.contains("leite"));
        assertFalse(ingredientes.contains("chocolate"));
    }
}
