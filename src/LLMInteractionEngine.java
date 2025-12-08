import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * Classe responsável por gerenciar a interação com o LLM (Language Model),
 * incluindo envio de prompts e recebimento de respostas.
 */
public class LLMInteractionEngine {

    /** URL do endpoint da API */
    String url;

    /** Chave de autenticação da API */
    String apiKey;

    /** Modelo de LLM a ser usado */
    String model;

    /** Indica se deve usar hack para ignorar problemas de SSL */
    boolean useHack;

    /**
     * Construtor completo que permite configurar o hack de SSL.
     *
     * @param url Endpoint da API
     * @param apiKey Chave de autenticação
     * @param model Modelo a usar
     * @param useHack Ativa ou desativa hack de SSL
     */
    LLMInteractionEngine(String url, String apiKey, String model, boolean useHack) {
        this.url = url;
        this.apiKey = apiKey;
        this.model = model;
        this.useHack = useHack;
    }

    /**
     * Construtor padrão sem hack de SSL.
     *
     * @param url Endpoint da API
     * @param apiKey Chave de autenticação
     * @param model Modelo a usar
     */
    LLMInteractionEngine(String url, String apiKey, String model) {
        this.url = url;
        this.apiKey = apiKey;
        this.model = model;
        this.useHack = false;
    }

    /**
     * Constrói o JSON a ser enviado para o LLM, escapando caracteres especiais.
     *
     * @param model Modelo do LLM
     * @param prompt Prompt do utilizador
     * @return JSON pronto para envio
     */
    String buildJSON(String model, String prompt) {
        // --- CORREÇÃO DO ERRO 400 ---
        // É necessário escapar caracteres especiais (quebras de linha e aspas)
        // para que o JSON enviado ao servidor seja válido.
        String escapedPrompt = prompt
                .replace("\\", "\\\\")  // Escapar barras invertidas primeiro
                .replace("\"", "\\\"")  // Escapar aspas duplas
                .replace("\n", "\\n")  // Escapar quebra de linha
                .replace("\r", "");    // Remover carriage return (Windows)

        String json = "";
        json += "{";
        json += "\"" + "model" + "\": " + "\"" + model + "\",";
        json += "\"" + "prompt" + "\" :" + "\"" + escapedPrompt + "\"";
        json += "}";
        return json;
    }

    /**
     * Envia um prompt para o LLM e retorna a resposta.
     * Se useHack estiver ativo, utiliza o método alternativo que ignora SSL.
     *
     * @param prompt Prompt do utilizador
     * @return Resposta do LLM em formato JSON
     * @throws IOException Em caso de erro de I/O
     * @throws InterruptedException Se a requisição for interrompida
     * @throws NoSuchAlgorithmException Se não houver suporte para TLS
     * @throws KeyManagementException Se houver problema ao inicializar SSL
     */
    String sendPrompt(String prompt) throws IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException {
        if(useHack) {
            return sendPrompt_Hack(prompt);
        }
        HttpClient client = HttpClient.newHttpClient();
        String json = buildJSON(model, prompt);
        return sendRequestToClientAndGetReply(client, url, apiKey, json);
    }

    /**
     * Envia o prompt para o LLM utilizando hack para ignorar certificados SSL.
     *
     * @param prompt Prompt do utilizador
     * @return Resposta do LLM em JSON
     * @throws IOException Em caso de erro de I/O
     * @throws InterruptedException Se a requisição for interrompida
     * @throws NoSuchAlgorithmException Se não houver suporte para TLS
     * @throws KeyManagementException Se houver problema ao inicializar SSL
     */
    String sendPrompt_Hack(String prompt) throws IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException {

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[]{ new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] c, String a) {}
            public void checkServerTrusted(X509Certificate[] c, String a) {}
            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
        }}, new SecureRandom());

        HttpClient insecureClient = HttpClient.newBuilder().sslContext(sc).build();

        String json = buildJSON(model, prompt);

        return sendRequestToClientAndGetReply(insecureClient, url, apiKey, json);
    }

    /**
     * Envia a requisição HTTP ao endpoint fornecido e retorna a resposta.
     *
     * @param client HttpClient a ser usado
     * @param url Endpoint da API
     * @param apiKey Chave de autenticação
     * @param json JSON a ser enviado
     * @return Resposta do servidor como string
     * @throws IOException Em caso de erro de I/O
     * @throws InterruptedException Se a requisição for interrompida
     */
    String sendRequestToClientAndGetReply(HttpClient client, String url, String apiKey, String json) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

        return resp.body();
    }
}
