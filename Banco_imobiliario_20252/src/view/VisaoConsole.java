package view;

import java.util.List;
import java.util.Scanner;
import model.ModelFacade.PlayerSnapshot;

/**
 * ================================================================
 * Classe VisaoConsole
 * ================================================================
 * ✅ Responsabilidade:
 * É a camada de **View (Interface do Usuário)** do padrão MVC.
 * Toda a interação com o jogador via console acontece aqui.
 * 
 * ✅ O que ela faz:
 * - Mostra mensagens e informações do estado atual do jogo.
 * - Recebe entradas do usuário pelo teclado (Scanner).
 * - Exibe menus e perguntas durante o jogo.
 *
 * ✅ Por que ela existe:
 * O objetivo é manter a responsabilidade da interface separada da lógica.
 * Nenhum "System.out" deve estar espalhado pelo Model ou Controller.
 * Assim, se no futuro quisermos mudar para uma interface gráfica,
 * basta alterar esta classe.
 * ================================================================
 */
public class VisaoConsole {

    // ================================================================
    // 🧭 Exibição de informações gerais do turno
    // ================================================================

    /**
     * Mostra o cabeçalho de cada turno, com nome, saldo e status do jogador.
     */
    public static void mostrarCabecalhoTurno(PlayerSnapshot ps) {
        System.out.println("\n==============================");
        System.out.println("Vez de: " + ps.getNome() + " | Saldo: $" + ps.getSaldo()
                + (ps.isPreso() ? " | (Preso)" : ""));
    }

    /**
     * Mostra o resultado do lançamento dos dados.
     */
    public static void mostrarRolagem(int d1, int d2) {
        System.out.println("Dados: " + d1 + " + " + d2 + " = " + (d1 + d2));
    }

    /**
     * Mostra a movimentação do jogador no tabuleiro após rolar os dados.
     */
    public static void mostrarMovimento(String nomeJogador, int pos, String nomeEspaco) {
        System.out.println(nomeJogador + " moveu para " + pos + " (" + nomeEspaco + ")");
    }

    /**
     * Mostra mensagem quando o jogador falha ao tentar sair da prisão.
     */
    public static void mostrarFalhaPrisao() {
        System.out.println("Não foi dupla: você permanece na prisão.");
    }

    /**
     * Mostra mensagem quando o jogador sai da prisão com sucesso.
     */
    public static void mostrarSaidaPrisao() {
        System.out.println("Você saiu da prisão.");
    }

    /**
     * Mostra mensagem final ao encerrar o jogo.
     * Se restar apenas um jogador, ele é declarado vencedor.
     */
    public static void mostrarEncerramento(List<PlayerSnapshot> jogadoresRestantes) {
        System.out.println("\n=== FIM DE JOGO ===");
        if (jogadoresRestantes.size() == 1) {
            System.out.println("🏆 Vencedor: " + jogadoresRestantes.get(0).getNome());
        } else {
            System.out.println("Jogo encerrado sem vencedor definido.");
        }
    }

    /**
     * Mostra uma mensagem genérica de informação no console.
     */
    public static void mostrarInfo(String msg) {
        System.out.println(msg);
    }

    // ================================================================
    // 🎮 Entrada de dados dos jogadores
    // ================================================================

    /**
     * Pergunta ao usuário quantos jogadores participarão da partida.
     * Aceita apenas valores entre 2 e 6 (valores clássicos do jogo).
     */
    public static int perguntarQuantidadeJogadores(Scanner in) {
        while (true) {
            System.out.print("Quantos jogadores? (2–6): ");
            String s = in.nextLine().trim();
            try {
                int n = Integer.parseInt(s);
                if (n >= 2 && n <= 6) return n;
            } catch (NumberFormatException ignored) {}
            System.out.println("Valor inválido. Tente novamente.");
        }
    }

    /**
     * Pergunta o nome de um jogador durante a configuração inicial.
     * Não permite nomes vazios.
     */
    public static String perguntarNomeJogador(Scanner in, int idx) {
        while (true) {
            System.out.print("Nome do jogador " + idx + ": ");
            String nome = in.nextLine().trim();
            if (!nome.isEmpty()) return nome;
            System.out.println("Nome não pode ser vazio.");
        }
    }

    /**
     * Aguarda o jogador pressionar ENTER para continuar.
     * Usado, por exemplo, antes de rolar os dados.
     */
    public static void aguardarEnter(Scanner in, String mensagem) {
        System.out.print(mensagem);
        in.nextLine();
    }

    /**
     * Faz uma pergunta do tipo "sim ou não" e retorna true se a resposta começar com "s".
     * Ex.: "s", "sim", "S" etc. são considerados "Sim".
     */
    public static boolean confirmar(Scanner in, String pergunta) {
        System.out.print(pergunta);
        String s = in.nextLine().trim().toLowerCase();
        return s.startsWith("s"); // aceita s ou sim
    }

    /**
     * Mostra o menu de opções quando o jogador está preso e lê a escolha do usuário.
     * Opções:
     * 1 - Usar carta "Sair da Prisão" (se tiver)
     * 2 - Tentar sair rolando dados (precisa tirar dupla)
     * 3 - Passar a vez
     */
    public static int menuPrisao(Scanner in) {
        while (true) {
            System.out.println("Você está preso. Escolha uma opção:");
            System.out.println("1) Usar carta 'Sair da Prisão' (se houver)");
            System.out.println("2) Tentar sair com dupla (rolar dados)");
            System.out.println("3) Passar o turno");
            System.out.print("Opção: ");
            String s = in.nextLine().trim();
            if (s.equals("1") || s.equals("2") || s.equals("3")) return Integer.parseInt(s);
            System.out.println("Opção inválida. Tente de novo.");
        }
    }

    /**
     * Pergunta um número inteiro ao usuário e só retorna quando a entrada for válida.
     * Usado, por exemplo, para selecionar qual propriedade vender.
     */
    public static int perguntarNumero(Scanner in, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String s = in.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
            }
        }
    }

}
