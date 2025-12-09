import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {

    private final InputStream originalIn = System.in;

    @AfterEach
    void restoreSystemIn() {
        System.setIn(originalIn);
    }

    /**
     * Simula a leitura de um inteiro válido.
     */
    @Test
    void testReadIntValido() {
        String input = "42\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Utils.sc = new Scanner(System.in); // reset Scanner
        int result = Utils.readIntFromKeyboard();

        assertEquals(42, result);
        assertTrue(result > 0);
        assertFalse(result < 0);
    }

    /**
     * Simula a leitura de um inteiro inválido.
     */
    @Test
    void testReadIntInvalido() {
        String input = "abc\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Utils.sc = new Scanner(System.in);
        int result = Utils.readIntFromKeyboard();

        assertEquals(-1, result);
        assertTrue(result < 0);
        assertFalse(result > 0);
    }

    /**
     * Simula a leitura de um carácter válido.
     */
    @Test
    void testReadCharValido() {
        String input = "7\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Utils.sc = new Scanner(System.in);
        int valor = Utils.readCharFromKeyboard();

        assertEquals(7, valor);
        assertTrue(valor >= 0);
        assertFalse(valor < 0);
    }

    /**
     * Simula a leitura de um carácter vazio (retorno -1).
     */
    @Test
    void testReadCharVazio() {
        String input = "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Utils.sc = new Scanner(System.in);
        int valor = Utils.readCharFromKeyboard();

        assertEquals(-1, valor);
        assertTrue(valor < 0);
    }

    /**
     * Simula leitura de linha de texto.
     */
    @Test
    void testReadLine() {
        String input = "ovo, leite, farinha\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Utils.sc = new Scanner(System.in);
        String line = Utils.readLineFromKeyboard();

        assertEquals("ovo, leite, farinha", line);
        assertTrue(line.contains("leite"));
        assertFalse(line.contains("chocolate"));
    }
}
