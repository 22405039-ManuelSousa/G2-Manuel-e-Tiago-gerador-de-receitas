/**
 * Utilitários simples para manipulação e extração de dados de JSON.
 */
public class JSONUtils {

    /**
     * Formata um JSON bruto em string de forma legível, adicionando indentação e quebras de linha.
     * <p>
     * Nota: Este método é básico e não valida a estrutura do JSON, apenas adiciona formatação.
     *
     * @param json JSON bruto em string
     * @return JSON formatado com identação
     */
    static String quickJSONFormater(String json) {
        StringBuilder out = new StringBuilder();
        boolean inStr = false, esc = false;
        int indent = 0;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (esc) { out.append(c); esc = false; continue; }
            if (c == '\\') { out.append(c); esc = true; continue; }
            if (c == '\"') { inStr = !inStr; out.append(c); continue; }
            if (inStr) { out.append(c); continue; }

            switch (c) {
                case '{': case '[':
                    out.append(c).append('\n').append("  ".repeat(++indent));
                    break;
                case '}': case ']':
                    out.append('\n').append("  ".repeat(--indent)).append(c);
                    break;
                case ',':
                    out.append(c).append('\n').append("  ".repeat(indent));
                    break;
                case ':':
                    out.append(": ");
                    break;

                default:
                    if (!Character.isWhitespace(c)) out.append(c);
            }
        }
        return out.toString();
    }

    /**
     * Extrai o valor de uma chave de string em JSON simples do tipo `"key": "value"`.
     * <p>
     * Nota: Método muito simplificado, não funciona corretamente se houver múltiplos subcampos com a mesma chave
     * ou se a estrutura JSON for complexa.
     *
     * @param json JSON em string
     * @param key Chave cujo valor se quer extrair
     * @return Valor da chave como string ou {@code null} se não existir
     */
    static String getJsonString(String json, String key) {
        String pattern = "\"" + key + "\"";
        int keyPos = json.indexOf(pattern);
        if (keyPos < 0) return null;

        int colonPos = json.indexOf(':', keyPos + pattern.length());
        if (colonPos < 0) return null;

        // Encontrar a aspa de abertura do valor
        int firstQuote = json.indexOf('"', colonPos + 1);
        if (firstQuote < 0) return null;

        // Encontrar a aspa de fechamento (trata escape mínimo)
        int secondQuote = json.indexOf('"', firstQuote + 1);
        while (secondQuote > 0 && json.charAt(secondQuote - 1) == '\\') {
            secondQuote = json.indexOf('"', secondQuote + 1);
        }
        if (secondQuote < 0) return null;

        return json.substring(firstQuote + 1, secondQuote);
    }
}
