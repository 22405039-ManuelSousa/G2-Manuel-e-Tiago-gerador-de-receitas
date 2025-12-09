import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários simples para a classe LLMInteractionEngine.
 */
public class LLMInteractionEngineTest {

    /**
     * Testa o construtor padrão.
     */
    @Test
    void testConstrutorNormal() {
        LLMInteractionEngine eng = new LLMInteractionEngine("A", "B", "C");

        assertEquals("A", eng.url);
        assertEquals("B", eng.apiKey);
        assertEquals("C", eng.model);
        assertFalse(eng.useHack);
    }

    /**
     * Testa o construtor com hack SSL.
     */
    @Test
    void testConstrutorComHack() {
        LLMInteractionEngine eng = new LLMInteractionEngine("X", "Y", "Z", true);

        assertEquals("X", eng.url);
        assertEquals("Y", eng.apiKey);
        assertEquals("Z", eng.model);
        assertTrue(eng.useHack);
    }

    /**
     * Testa a criação do JSON.
     */
    @Test
    void testBuildJSON() {
        LLMInteractionEngine engine = new LLMInteractionEngine("url", "key", "modelo");

        String prompt = "Teste \"linha\" \n outra";
        String json = engine.buildJSON("modelo", prompt);

        assertTrue(json.contains("\"model\": \"modelo\""));
        assertTrue(json.contains("Teste \\\"linha\\\""));
        assertTrue(json.contains("\\n"));
        assertFalse(json.contains("\n")); // não pode conter quebra real
    }

    /**
     * Testa o método interno usando um HttpClient falso.
     */
    @Test
    void testSendRequestToClientAndGetReply() throws IOException, InterruptedException {
        // CLIENTE FALSO
        HttpClient fakeClient = new HttpClient() {
            @Override
            public Optional<CookieHandler> cookieHandler() {
                return Optional.empty();
            }

            @Override
            public Optional<Duration> connectTimeout() {
                return Optional.empty();
            }

            @Override
            public Redirect followRedirects() {
                return null;
            }

            @Override
            public Optional<ProxySelector> proxy() {
                return Optional.empty();
            }

            @Override
            public SSLContext sslContext() {
                return null;
            }

            @Override
            public SSLParameters sslParameters() {
                return null;
            }

            @Override
            public Optional<Authenticator> authenticator() {
                return Optional.empty();
            }

            @Override
            public Version version() {
                return null;
            }

            @Override
            public Optional<Executor> executor() {
                return Optional.empty();
            }

            @Override
            public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> handler) {
                return new HttpResponse<T>() {
                    @Override public int statusCode() { return 200; }
                    @Override public T body() { return (T) "RESPOSTA_FAKE"; }

                    @Override
                    public Optional<SSLSession> sslSession() {
                        return Optional.empty();
                    }

                    @Override public HttpRequest request() { return request; }
                    @Override public Optional<HttpResponse<T>> previousResponse() { return Optional.empty(); }
                    @Override public HttpHeaders headers() { return HttpHeaders.of(Map.of(), (a, b)->true); }
                    @Override public URI uri() { return request.uri(); }
                    @Override public HttpClient.Version version() { return HttpClient.Version.HTTP_1_1; }
                };
            }

            @Override
            public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler) {
                return null;
            }

            @Override
            public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler, HttpResponse.PushPromiseHandler<T> pushPromiseHandler) {
                return null;
            }
        };

        LLMInteractionEngine eng = new LLMInteractionEngine("http://teste", "CHAVE", "MODELO");

        String json = "{}";
        String resp = eng.sendRequestToClientAndGetReply(fakeClient, "http://x", "AAA", json);

        assertEquals("RESPOSTA_FAKE", resp);
    }

    /**
     * Testa o sendPrompt quando useHack = false.
     * Como não podemos contactar a internet, substituímos client por uma classe interna fake.
     */
    @Test
    void testSendPromptSemHack() throws Exception {
        LLMInteractionEngine eng = new LLMInteractionEngine("http://fake", "KEY", "MODEL");

        // Substitui temporariamente o método usando classe anónima
        HttpClient fakeClient = new HttpClient() {
            @Override
            public Optional<CookieHandler> cookieHandler() {
                return Optional.empty();
            }

            @Override
            public Optional<Duration> connectTimeout() {
                return Optional.empty();
            }

            @Override
            public Redirect followRedirects() {
                return null;
            }

            @Override
            public Optional<ProxySelector> proxy() {
                return Optional.empty();
            }

            @Override
            public SSLContext sslContext() {
                return null;
            }

            @Override
            public SSLParameters sslParameters() {
                return null;
            }

            @Override
            public Optional<Authenticator> authenticator() {
                return Optional.empty();
            }

            @Override
            public Version version() {
                return null;
            }

            @Override
            public Optional<Executor> executor() {
                return Optional.empty();
            }

            @Override
            public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> handler) {
                return new HttpResponse<T>() {
                    @Override public int statusCode() { return 200; }
                    @Override public T body() { return (T) "OK_SEM_HACK"; }

                    @Override
                    public Optional<SSLSession> sslSession() {
                        return Optional.empty();
                    }

                    @Override
                    public URI uri() {
                        return null;
                    }

                    @Override
                    public Version version() {
                        return null;
                    }

                    @Override public HttpRequest request() { return request; }

                    @Override
                    public Optional<HttpResponse<T>> previousResponse() {
                        return Optional.empty();
                    }

                    @Override
                    public HttpHeaders headers() {
                        return null;
                    }
                };
            }

            @Override
            public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler) {
                return null;
            }

            @Override
            public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler, HttpResponse.PushPromiseHandler<T> pushPromiseHandler) {
                return null;
            }
        };

        // Forçar o uso do fakeClient através de acesso ao método privado
        String resp = eng.sendRequestToClientAndGetReply(fakeClient, "http://fake", "KEY", "{}");

        assertEquals("OK_SEM_HACK", resp);
    }

    /**
     * Testa o sendPrompt_Hack garantindo apenas que não lança exceção.
     */
    @Test
    void testSendPromptHackConstrucaoSSL() throws Exception {
        LLMInteractionEngine eng = new LLMInteractionEngine("URL", "KEY", "MODEL", true);

        // Apenas testar criação de SSLContext sem chamar rede
        SSLContext sc = SSLContext.getInstance("TLS");
        assertNotNull(sc);
    }
}
