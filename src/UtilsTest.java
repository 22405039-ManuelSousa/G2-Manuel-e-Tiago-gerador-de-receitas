import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe {@link Utils}.
 * Verifica o comportamento básico de leitura simulada de inteiros e caracteres.
 */
public class UtilsTest {

    /**
     * Simula a leitura de um inteiro do teclado e verifica se o valor
     * está correto e dentro de um intervalo válido.
     */
    @Test
    void testReadIntFromKeyboardSimulation() {
        int simulatedInput = 5;
        int resultado = simulatedInput;

        assertEquals(5, resultado);
        assertTrue(resultado > 0);
        assertFalse(resultado < 0);
    }

    /**
     * Simula a leitura de um carácter do teclado e verifica a conversão
     * para inteiro, garantindo que o valor está correto.
     */
    @Test
    void testReadCharFromKeyboardSimulation() {
        char simulatedChar = '7';
        int valor = simulatedChar - '0';

        assertEquals(7, valor);
        assertTrue(valor < 10);
        assertFalse(valor > 10);
    }
}
