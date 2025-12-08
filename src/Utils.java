import java.util.Scanner;

/**
 * Utilitários para leitura de entrada do utilizador via teclado.
 * Fornece métodos para ler linhas de texto, números inteiros e caracteres.
 */
public class Utils {

    /** Scanner para leitura de entrada padrão */
    static Scanner sc = new Scanner(System.in);

    /**
     * Lê uma linha de texto do teclado.
     * Normalmente usado para capturar ingredientes ou outros textos.
     *
     * @return Texto digitado pelo utilizador
     */
    static String readLineFromKeyboard() {
        System.out.print("> ");
        String input = sc.nextLine();
        return input;
    }

    /**
     * Lê um número inteiro do teclado de forma segura.
     * Se a entrada não for válida, retorna -1.
     *
     * @return Número inteiro digitado pelo utilizador ou -1 se inválido
     */
    static int readIntFromKeyboard() {
        System.out.print("> ");
        String input = sc.nextLine();
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Lê um carácter do teclado e retorna seu valor como inteiro.
     * Apenas o primeiro carácter é considerado.
     * Se não houver entrada, retorna -1.
     *
     * @return Valor inteiro do primeiro carácter digitado, ou -1 se inválido
     */
    static int readCharFromKeyboard() {
        System.out.print("> ");
        String input = sc.nextLine();

        if (!input.isEmpty()) {
            char c = input.charAt(0);
            return c - '0';
        }

        return -1;
    }
}
