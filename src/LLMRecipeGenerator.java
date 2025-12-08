import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Classe responsável por gerenciar a geração de receitas,
 * interagindo com o utilizador e com o LLM para fornecer
 * títulos de receitas e receitas completas.
 */
public class LLMRecipeGenerator {

    /** Engine de interação com o LLM */
    LLMInteractionEngine engine;

    /**
     * Construtor que inicializa a engine de interação.
     *
     * @param engine Instância de {@link LLMInteractionEngine} para comunicação com o LLM
     */
    LLMRecipeGenerator(LLMInteractionEngine engine) {
        this.engine = engine;
    }

    /**
     * Pede ao utilizador os ingredientes disponíveis.
     *
     * @return String com os ingredientes fornecidos pelo utilizador
     */
    String pedirIngredientesAoUtilizador() {
        System.out.println("=== GERADOR DE RECEITAS ===");
        System.out.println("Por favor, introduza os ingredientes que tem disponíveis (separados por vírgula):");

        return Utils.readLineFromKeyboard();
    }

    /**
     * Solicita ao LLM 5 títulos de receitas possíveis com os ingredientes fornecidos.
     *
     * @param ingredientes Ingredientes disponíveis
     * @return String com a lista numerada de títulos de receitas (ou null em caso de erro)
     * @throws IOException Em caso de erro de I/O
     * @throws NoSuchAlgorithmException Se não houver suporte para TLS
     * @throws InterruptedException Se a requisição for interrompida
     * @throws KeyManagementException Se houver problema ao inicializar SSL
     */
    String pedirTitulosReceitasAoLLM(String ingredientes) throws IOException, NoSuchAlgorithmException, InterruptedException, KeyManagementException {
        String prompt = "Eu tenho estes ingredientes: " + ingredientes + ". ";
        prompt += "Sugere apenas 5 títulos de receitas que posso fazer com eles. ";
        prompt += "Responde com uma lista numerada (1 a 5). Não escrevas introduções, apenas a lista.";

        System.out.println("<A consultar o Chef GPT...>");
        String jsonResponse = engine.sendPrompt(prompt);

        System.out.println("DEBUG (JSON RAW): " + jsonResponse);

        return JSONUtils.getJsonString(jsonResponse, "text");
    }

    /**
     * Pede ao utilizador para escolher uma das receitas disponíveis.
     *
     * @return Número da escolha do utilizador (1 a 5)
     */
    int pedirEscolhaAoUtilizador() {
        int opcao;
        do {
            System.out.println("\nQual destas receitas quer cozinhar? (Escolha o número 1-5)");
            opcao = Utils.readIntFromKeyboard();

            if (opcao < 1 || opcao > 5) {
                System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao < 1 || opcao > 5);

        return opcao;
    }

    /**
     * Solicita ao LLM a receita completa baseada na escolha do utilizador.
     *
     * @param ingredientes Ingredientes fornecidos pelo utilizador
     * @param titulosReceitas Lista de títulos de receitas
     * @param escolhaUtilizador Opção escolhida pelo utilizador
     * @return Receita completa como string (ou null se não for possível extrair)
     * @throws IOException Em caso de erro de I/O
     * @throws NoSuchAlgorithmException Se não houver suporte para TLS
     * @throws InterruptedException Se a requisição for interrompida
     * @throws KeyManagementException Se houver problema ao inicializar SSL
     */
    String pedirReceitaCompletaAoLLM(String ingredientes, String titulosReceitas, int escolhaUtilizador) throws IOException, NoSuchAlgorithmException, InterruptedException, KeyManagementException {
        String prompt = "Contexto - O utilizador tem estes ingredientes: " + ingredientes + ". ";
        prompt += "As opções dadas foram: \n" + titulosReceitas + "\n";
        prompt += "O utilizador escolheu a opção número " + escolhaUtilizador + ". ";
        prompt += "Escreve a receita completa para essa escolha (Ingredientes detalhados e Modo de Preparo).";

        System.out.println("<A escrever a receita...>");
        String jsonResponse = engine.sendPrompt(prompt);

        System.out.println("DEBUG (JSON RAW): " + jsonResponse);

        String resultado = JSONUtils.getJsonString(jsonResponse, "text");

        if (resultado == null) {
            resultado = JSONUtils.getJsonString(jsonResponse, "content");
        }

        if (resultado == null) {
            System.err.println("ERRO: Não foi possível extrair a receita.");
            System.err.println("Resposta RAW do servidor: " + jsonResponse);
        }

        return  resultado;
    }

    /**
     * Executa o fluxo completo de interação:
     * pede ingredientes, obtém títulos do LLM, permite escolha do utilizador
     * e apresenta a receita completa.
     *
     * @throws IOException Em caso de erro de I/O
     * @throws NoSuchAlgorithmException Se não houver suporte para TLS
     * @throws InterruptedException Se a requisição for interrompida
     * @throws KeyManagementException Se houver problema ao inicializar SSL
     */
    void executar() throws IOException, NoSuchAlgorithmException, InterruptedException, KeyManagementException {

        String ingredientes = pedirIngredientesAoUtilizador();

        String respostaTitulos = pedirTitulosReceitasAoLLM(ingredientes);

        if (respostaTitulos != null) {
            String titulosFormatados = respostaTitulos.replace("\\n", "\n");
            System.out.println("\nOpções de Receitas:");
            System.out.println(titulosFormatados);
        } else {
            System.out.println("Erro ao obter receitas.");
            return;
        }

        int escolha = pedirEscolhaAoUtilizador();

        String receitaCompleta = pedirReceitaCompletaAoLLM(ingredientes, respostaTitulos, escolha);

        if (receitaCompleta != null) {
            String receitaFormatada = receitaCompleta.replace("\\n", "\n");
            System.out.println("\n------------------------------------------------");
            System.out.println("AQUI ESTÁ A SUA RECEITA:");
            System.out.println("------------------------------------------------");
            System.out.println(receitaFormatada);
        } else {
            System.out.println("Erro ao obter a receita completa.");
        }
    }
}
