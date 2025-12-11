import javax.swing.SwingUtilities;

public class Main {

    static String apiKey = "sk-pt9ku6hG-fCgiNUgotSVTw";
    static String url = "https://modelos.ai.ulusofona.pt/v1/completions";
    static String model = "gpt-4-turbo";
    static boolean useHack = true;

    public static void main(String[] args) {
        // Inicializa o motor
        LLMInteractionEngine engine = new LLMInteractionEngine(url, apiKey, model, useHack);

        // Inicializa a lógica
        LLMRecipeGenerator gerador = new LLMRecipeGenerator(engine);

        // Interfaces Swing devem ser iniciadas na thread correta
        SwingUtilities.invokeLater(() -> {
            InterfaceGrafica gui = new InterfaceGrafica(gerador);
            gui.setVisible(true);
        });
    }
}