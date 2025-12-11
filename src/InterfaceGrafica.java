import javax.swing.*;
import java.awt.*;

public class InterfaceGrafica extends JFrame {

    private final LLMRecipeGenerator gerador;
    private JTextArea areaResultado;
    private JTextField campoIngredientes;
    private JButton botaoPedir;

    // Variável para guardar os títulos gerados para usar no segundo passo
    private String ultimosTitulosGerados = "";

    public InterfaceGrafica(LLMRecipeGenerator gerador) {
        this.gerador = gerador;
        configurarJanela();
    }

    private void configurarJanela() {
        setTitle("Gerador de Receitas com IA");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar no ecrã
        setLayout(new BorderLayout(10, 10));

        // --- PAINEL DE TOPO (INPUT) ---
        JPanel painelTopo = new JPanel(new BorderLayout(5, 5));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel labelInstrucao = new JLabel("Ingredientes (ex: ovo, leite): ");
        campoIngredientes = new JTextField();
        botaoPedir = new JButton("Sugerir Receitas");

        painelTopo.add(labelInstrucao, BorderLayout.WEST);
        painelTopo.add(campoIngredientes, BorderLayout.CENTER);
        painelTopo.add(botaoPedir, BorderLayout.EAST);

        // --- ÁREA CENTRAL (OUTPUT) ---
        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaResultado.setMargin(new Insets(10, 10, 10, 10));
        // Adiciona scroll caso o texto seja grande
        JScrollPane scrollPane = new JScrollPane(areaResultado);

        add(painelTopo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // --- AÇÃO DO BOTÃO ---
        botaoPedir.addActionListener(e -> processarPedidoSugestoes());
    }

    // Passo 1: Pedir sugestões
    private void processarPedidoSugestoes() {
        String ingredientes = campoIngredientes.getText().trim();

        if (ingredientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, escreva alguns ingredientes.");
            return;
        }

        areaResultado.setText("A consultar o Chef GPT... Por favor aguarde.\nIsto pode demorar alguns segundos.");
        botaoPedir.setEnabled(false); // Desativar botão para evitar múltiplos cliques

        // Executar numa thread separada (Worker Thread) para não bloquear a GUI
        new Thread(() -> {
            try {
                // Chama a lógica original
                String titulos = gerador.pedirTitulosReceitasAoLLM(ingredientes);

                // Trata a formatação (o JSON vem com \\n literais)
                String titulosFormatados = (titulos != null) ? titulos.replace("\\n", "\n") : "Erro ao obter títulos.";
                ultimosTitulosGerados = titulosFormatados;

                // Atualiza a interface gráfica (tem de ser no Event Dispatch Thread)
                SwingUtilities.invokeLater(() -> {
                    areaResultado.setText("OPÇÕES DISPONÍVEIS:\n\n" + titulosFormatados);
                    // Após mostrar os títulos, pede a escolha
                    pedirEscolhaAoUtilizador(ingredientes);
                });

            } catch (Exception ex) {
                ex.printStackTrace(); // Log na consola para debug
                SwingUtilities.invokeLater(() -> {
                    areaResultado.setText("Ocorreu um erro técnico: " + ex.getMessage());
                    botaoPedir.setEnabled(true);
                });
            }
        }).start();
    }

    // Passo 2: Pop-up para escolha
    private void pedirEscolhaAoUtilizador(String ingredientes) {
        String input = JOptionPane.showInputDialog(this,
                "Escreva o número da receita que quer (1-5):",
                "Selecionar Receita",
                JOptionPane.QUESTION_MESSAGE);

        // Se o utilizador clicou em Cancelar ou fechou a janela
        if (input == null) {
            botaoPedir.setEnabled(true);
            return;
        }

        try {
            int escolha = Integer.parseInt(input.trim());
            if (escolha >= 1 && escolha <= 5) {
                obterReceitaCompleta(ingredientes, escolha);
            } else {
                JOptionPane.showMessageDialog(this, "Número inválido (deve ser entre 1 e 5).");
                botaoPedir.setEnabled(true);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor insira apenas números.");
            botaoPedir.setEnabled(true);
        }
    }

    // Passo 3: Pedir receita completa
    private void obterReceitaCompleta(String ingredientes, int escolha) {
        areaResultado.append("\n\n-----------------------------------\n");
        areaResultado.append("A gerar a receita completa para a opção " + escolha + "...");

        new Thread(() -> {
            try {
                String receita = gerador.pedirReceitaCompletaAoLLM(ingredientes, ultimosTitulosGerados, escolha);

                String receitaFormatada = (receita != null) ? receita.replace("\\n", "\n") : "Erro ao obter detalhes.";

                SwingUtilities.invokeLater(() -> {
                    areaResultado.setText("RECEITA COMPLETA:\n\n" + receitaFormatada);
                    botaoPedir.setEnabled(true); // Reativar o botão para nova pesquisa
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    areaResultado.append("\nErro ao obter detalhe: " + ex.getMessage());
                    botaoPedir.setEnabled(true);
                });
            }
        }).start();
    }
}