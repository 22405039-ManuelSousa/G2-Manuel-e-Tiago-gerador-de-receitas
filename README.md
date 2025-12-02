# Gerador de Receitas com LLM

Este projeto é uma aplicação Java que utiliza um Grande Modelo de Linguagem (LLM) para sugerir receitas culinárias baseadas em ingredientes fornecidos pelo utilizador.

## Tabela de Resumo do Projeto

| Campo | Descrição |
| :--- | :--- |
| **Tema** | Gerador de Receitas Culinárias |
| **Grupo** | Manuel Sousa (a22405039) e Tiago Santos (a22403423) |
| **Funcionalidade baseada em LLM** | O utilizador insere ingredientes, o LLM sugere pratos e gera a receita completa do prato escolhido. |
| **Prompts de integração esperadas** | 1. "Tenho estes ingredientes: [X]. Dá-me receitas."<br>2. "Dá-me a receita de [Prato]." |
| **Prompts de integração usadas** | **Prompt 1 (Sugestões):** "Eu tenho estes ingredientes: [ingredientes]. Sugere apenas 5 títulos de receitas que posso fazer com eles. Responde com uma lista numerada (1 a 5). Não escrevas introduções, apenas a lista."<br><br>**Prompt 2 (Receita Completa):** "Contexto - O utilizador tem estes ingredientes: [ingredientes]. As opções dadas foram: [lista_titulos]. O utilizador escolheu a opção número [escolha]. Escreve a receita completa para essa escolha (Ingredientes detalhados e Modo de Preparo)." |
| **Classes esperadas e responsabilidade** | **Main:** Inicializa o sistema.<br>**GeradorReceitasLLM:** Gere o fluxo lógico (pede ingredientes, gera prompt, processa resposta).<br>**LLMInteractionEngine:** Envia pedidos HTTP para a API.<br>**JSONUtils:** Faz o parsing das respostas JSON.<br>**Utils:** Lê inputs do utilizador. |

## Funcionalidades

1. **Input de Ingredientes:** O utilizador insere uma lista de ingredientes que possui.
2. **Geração de Sugestões:** O sistema consulta o LLM e devolve 5 títulos de receitas possíveis.
3. **Seleção:** O utilizador escolhe uma das opções (1-5).
4. **Receita Completa:** O sistema consulta novamente o LLM para obter os detalhes (preparação e quantidades) da receita escolhida.

## Autores

* **Manuel Sousa** - a22405039
* **Tiago Santos** - a22403423
  ![](docs/diagrama-uml-gerador-de-receitas.png?raw=true "Diagrama UML")
