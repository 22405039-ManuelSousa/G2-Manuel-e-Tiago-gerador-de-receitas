
public class Main {

    static String apiKey = "sk-pt9ku6hG-fCgiNUgotSVTw";
    static String url = "https://modelos.ai.ulusofona.pt/v1/completions";
    static String model = "gpt-4-turbo";
    static boolean useHack = true;

    public static void main(String[] args) throws Exception {

        LLMInteractionEngine engine = new LLMInteractionEngine(url, apiKey, model, useHack);

        LLMRecipeGenerator gerador = new LLMRecipeGenerator(engine);

        gerador.executar();
    }
}