package controller;

import model.ModelFacade;
import model.ModelFacade.PlayerSnapshot;
import model.ModelFacade.MovementResult;
import model.ModelFacade.PropriedadeSnapshot;
import view.VisaoConsole;

import java.util.List;
import java.util.Scanner;

/**
 * ================================================================
 * Classe ControladorJogo
 * ================================================================
 * É o controlador principal do sistema e o ponto de entrada do jogo.
 * 
 * Sua função é orquestrar toda a partida:
 * - Inicializar jogadores.
 * - Controlar a ordem dos turnos.
 * - Interpretar decisões do jogador (comprar, construir, vender).
 * - Comunicar-se com a camada de "Model" através da fachada.
 * - Exibir as informações e menus por meio da camada "View".
 *
 * Em resumo: ela representa a "regra geral de execução do jogo".
 * ================================================================
 */
public class ControladorJogo {

    // Fachada para acessar a lógica do jogo (Model)
    private final ModelFacade facade;

    // Controlador auxiliar que encapsula ações comuns (comprar, construir, vender)
    private final ControladorAcoes acoes;

    /**
     * Construtor: inicializa a fachada e o controlador de ações.
     * A fachada é injetada aqui para manter baixo acoplamento.
     */
    public ControladorJogo(ModelFacade facade) {
        this.facade = facade;
        this.acoes = new ControladorAcoes(facade);
    }

    /**
     * ================================================================
     * iniciarJogo()
     * ================================================================
     * Método principal do jogo. Ele inicia o tabuleiro e entra
     * no loop principal que roda enquanto a partida estiver ativa.
     */
    public void iniciarJogo() {
        // Reinicia o estado do jogo (zera jogadores, posições, tabuleiro etc.)
        facade.reset();

        try (Scanner in = new Scanner(System.in)) {

            // ===== Configuração inicial =====
            // Pergunta ao usuário quantos jogadores irão participar
            int qtd = VisaoConsole.perguntarQuantidadeJogadores(in);

            // Cadastra cada jogador com seu nome
            for (int i = 1; i <= qtd; i++) {
                String nome = VisaoConsole.perguntarNomeJogador(in, i);
                facade.adicionarJogador(nome);
            }

            // ===== Loop principal do jogo =====
            while (!facade.jogoEncerrado()) {

                // Obtem o "snapshot" do jogador atual (informações seguras e imutáveis)
                PlayerSnapshot ps = facade.getJogadorDaVezSnapshot();
                VisaoConsole.mostrarCabecalhoTurno(ps);

                // === Verifica se o jogador está preso ===
                if (ps.isPreso()) {
                    // Exibe menu com opções enquanto estiver preso
                    int opc = VisaoConsole.menuPrisao(in);
                    
                    if (opc == 1) { // Usar carta de liberdade
                        boolean saiu = facade.usarCartaSairLivreDaVez();
                        if (!saiu) {
                            VisaoConsole.mostrarInfo("Você não possui carta de sair da prisão.");
                            facade.finalizarTurno();
                            continue;
                        } else {
                            VisaoConsole.mostrarSaidaPrisao();
                        }

                    } else if (opc == 2) { // Tentar sair com dupla nos dados
                        VisaoConsole.aguardarEnter(in, "Pressione ENTER para lançar os dados (tentativa de sair com dupla)...");
                        int[] dadosPrisao = facade.lancarDados();
                        VisaoConsole.mostrarRolagem(dadosPrisao[0], dadosPrisao[1]);
                        boolean saiu = facade.tentarSairDaPrisaoComDadosDaVez(dadosPrisao[0], dadosPrisao[1]);
                        if (!saiu) {
                            VisaoConsole.mostrarFalhaPrisao();
                            facade.finalizarTurno();
                            continue;
                        } else {
                            VisaoConsole.mostrarSaidaPrisao();
                        }

                    } else { // Passar o turno (continuar preso)
                        VisaoConsole.mostrarInfo("Você optou por permanecer preso neste turno.");
                        facade.finalizarTurno();
                        continue;
                    }
                }

                // === Jogada normal ===
                // Lança os dados e move o jogador
                VisaoConsole.aguardarEnter(in, "Pressione ENTER para lançar os dados...");
                int[] dados = facade.lancarDados();
                VisaoConsole.mostrarRolagem(dados[0], dados[1]);

                // Realiza o deslocamento no tabuleiro com base na soma dos dados
                MovementResult move = facade.deslocarJogadorDaVez(dados[0] + dados[1]);
                VisaoConsole.mostrarMovimento(ps.getNome(), move.getNovaPosicao(), move.getNomeEspaco());

                // Processa os efeitos da casa onde o jogador parou
                acoes.processarCasaAtual();

                // Se o jogador foi preso durante o processamento, termina o turno
                if (facade.getJogadorDaVezSnapshot().isPreso()) {
                    facade.finalizarTurno();
                    continue;
                }

                // === Pergunta se deseja comprar a propriedade atual ===
                boolean acabouDeComprar = false;
                if (VisaoConsole.confirmar(in, "Tentar comprar esta propriedade (se disponível)? [s/n]: ")) {
                    boolean comprou = acoes.tentarComprarAtual();
                    acabouDeComprar = comprou; // Marca se houve compra neste turno
                    VisaoConsole.mostrarInfo(comprou ? "✅ Compra realizada." : "❌ Não foi possível comprar.");
                }

                // === Construção de casas/hotel ===
                // Só é permitida se o jogador já era dono da propriedade antes do turno.
                if (!acabouDeComprar) {
                    if (VisaoConsole.confirmar(in, "Tentar construir nesta propriedade (se for sua)? [s/n]: ")) {
                        boolean construiu = acoes.construirNaAtual();
                        VisaoConsole.mostrarInfo(construiu ? "✅ Construção realizada." : "❌ Não foi possível construir.");
                    }
                } else {
                    VisaoConsole.mostrarInfo("🏗️ Você acabou de comprar esta propriedade. Construções estarão disponíveis em turnos futuros.");
                }

                // === Venda de propriedades ao banco ===
                if (VisaoConsole.confirmar(in, "Deseja vender alguma propriedade ao banco? [s/n]: ")) {
                    List<PropriedadeSnapshot> propriedades = facade.getPropriedadesDoJogadorDaVez();

                    // Caso não possua nenhuma
                    if (propriedades.isEmpty()) {
                        VisaoConsole.mostrarInfo("Você não possui propriedades para vender.");
                    } else {
                        // Lista todas as propriedades que o jogador possui
                        VisaoConsole.mostrarInfo("Selecione o número da propriedade para vender:");
                        for (int i = 0; i < propriedades.size(); i++) {
                            PropriedadeSnapshot p = propriedades.get(i);
                            VisaoConsole.mostrarInfo((i + 1) + " - " + p.getNome() +
                                    " (Casas: " + p.getCasas() + (p.isHotel() ? " + Hotel" : "") + ")");
                        }

                        // Pede ao jogador para escolher qual vender
                        int escolha = VisaoConsole.perguntarNumero(in, "Número da propriedade: ") - 1;
                        if (escolha >= 0 && escolha < propriedades.size()) {
                            PropriedadeSnapshot escolhida = propriedades.get(escolha);
                            boolean sucesso = facade.venderPropriedadePorNome(escolhida.getNome());
                            if (sucesso) {
                                VisaoConsole.mostrarInfo("✅ " + escolhida.getNome() + " foi vendida ao banco e voltou a estar disponível para compra.");
                            } else {
                                VisaoConsole.mostrarInfo("❌ Não foi possível vender essa propriedade.");
                            }
                        }
                    }
                }

                // === Finaliza turno e passa para o próximo jogador ===
                facade.finalizarTurno();
            }

            // Quando o jogo termina, exibe o resumo dos jogadores
            VisaoConsole.mostrarEncerramento(facade.getJogadoresSnapshot());
        }
    }
}
