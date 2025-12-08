/**
 * Classe principal do projeto.
 * Inicializa o motor de interação com o LLM e o gerador de receitas.
 */
public class Main {

    /** Chave da API fornecida pelo servidor */
    static String apiKey = "sk-pt9ku6hG-fCgiNUgotSVTw";

    /** URL da API de LLM */
    static String url = "https://modelos.ai.ulusofona.pt/v1/completions";

    /** Modelo de LLM a ser usado */
    static String model = "gpt-4-turbo";

    /** Indica se deve usar o hack para certificados SSL */
    static boolean useHack = true;

    /**
     * Método principal que inicializa o motor de LLM e executa o gerador de receitas.
     */
    public static void main(String[] args) throws Exception {
        LLMInteractionEngine engine = new LLMInteractionEngine(url, apiKey, model, useHack);
        LLMRecipeGenerator gerador = new LLMRecipeGenerator(engine);
        gerador.executar();
    }
}
