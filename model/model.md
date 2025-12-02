# Documentação do Modelo LLM

Este ficheiro documenta as especificações técnicas e a estratégia de interação com o Grande Modelo de Linguagem (LLM) utilizado no projeto "Gerador de Receitas".

## 1. Especificações Técnicas

* **Modelo Utilizado:** `gpt-4-turbo`
* **Fornecedor:** OpenAI (via Proxy da Universidade Lusófona)
* **Endpoint da API:** `https://modelos.ai.ulusofona.pt/v1/completions`
* **Formato de Comunicação:** JSON (pedido e resposta)
* **Autenticação:** Bearer Token (API Key)

## 2. Estratégia de Prompting

Para garantir respostas estruturadas e evitar erros de formatação, o projeto utiliza uma abordagem em **duas fases** (Chain of Thought simplificado):

### Fase 1: Ideação e Seleção
* **Objetivo:** Obter uma lista curta e numerada de ideias baseada nos ingredientes, sem sobrecarregar o utilizador com texto desnecessário.
* **Estratégia:** O prompt restringe explicitamente o output a "apenas uma lista numerada", facilitando o parsing e a leitura pelo utilizador.
* **Input:** Lista de ingredientes (String).
* **Output Esperado:** 5 títulos de pratos.

### Fase 2: Geração de Conteúdo Detalhado
* **Objetivo:** Gerar a receita completa apenas para a opção que o utilizador realmente quer.
* **Estratégia:** Envia-se o contexto anterior (ingredientes + opções geradas) e a escolha do utilizador. Isso garante que o modelo mantém a coerência com a sugestão que fez anteriormente.
* **Input:** Contexto anterior + Escolha (Inteiro).
* **Output Esperado:** Texto formatado com ingredientes detalhados e passos de preparação.

## 3. Tratamento de Dados e Erros

### Sanitização de JSON
O modelo `gpt-4-turbo` é sensível a caracteres de controlo (como quebras de linha reais) dentro de strings JSON. Foi implementada uma lógica de "escape" no código Java (`buildJSON`) para transformar quebras de linha literais em `\n` antes de enviar os prompts para a API, evitando erros `400 Bad Request`.

### Parsing da Resposta
A aplicação está preparada para ler a resposta do modelo tanto no campo `text` (padrão de alguns proxies) como no campo `content` (padrão nativo do GPT-4), garantindo maior robustez.