
public class Main {

    static String apiKey = "sk-pt9ku6hG-fCgiNUgotSVTw"; // A chave fornecida no exemplo
    static String url = "https://modelos.ai.ulusofona.pt/v1/completions";
    static String model = "gpt-4-turbo";
    static boolean useHack = true;

    public static void main(String[] args) throws Exception {
        // 1. Inicializar o motor de conexão
        LLMInteractionEngine engine = new LLMInteractionEngine(url, apiKey, model, useHack);

        // 2. Inicializar o Gerador de Receitas (com o novo nome)
        LLMRecipeGenerator gerador = new LLMRecipeGenerator(engine);

        // 3. Executar o programa
        gerador.executar();
    }
}