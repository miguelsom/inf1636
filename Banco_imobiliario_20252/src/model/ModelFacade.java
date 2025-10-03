package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * ================================================================
 * Classe ModelFacade - Fachada do modelo do jogo
 * ================================================================
 * ✅ Papel no projeto:
 * Esta classe atua como uma **fachada** (Design Pattern *Facade*),
 * ou seja, ela expõe uma API simples para o Controller e para a View
 * interagirem com a lógica do jogo (Model), sem precisar conhecer os
 * detalhes internos das classes `Jogador`, `Tabuleiro`, `Propriedade`,
 * `Espaco`, etc.
 *
 * ✅ Objetivos principais:
 * - Reduzir o acoplamento entre as camadas do sistema.
 * - Fornecer métodos claros e seguros para manipular o estado do jogo.
 * - Centralizar a lógica de alto nível (fluxo do jogo, turnos, compras,
 *   movimentação, aluguel, prisão, venda etc.).
 *
 * ✅ Benefícios do uso de *Facade*:
 * - Controller e View não acessam mais diretamente as classes do modelo.
 * - Facilita manutenção e evolução do código.
 * - Garante maior encapsulamento e consistência de regras.
 * ================================================================
 */
public class ModelFacade {

    /* ===================== Estado principal do jogo ===================== */

    // Representa o tabuleiro (todas as casas e propriedades)
    private final Tabuleiro tabuleiro;

    // Lista com todos os jogadores ativos na partida
    private final List<Jogador> jogadores;

    // Gerador de números aleatórios usado nas rolagens de dados
    private final Random rng;

    // Índice do jogador atual no ciclo de turnos
    private int indiceJogadorAtual;

    /**
     * Construtor: inicializa o estado base do jogo.
     */
    public ModelFacade() {
        this.tabuleiro = new Tabuleiro();
        this.jogadores = new ArrayList<>();
        this.rng = new Random();
        this.indiceJogadorAtual = 0;
    }

    /* ===================== Ciclo do jogo ===================== */

    /** Reinicia o estado do jogo (zera jogadores e volta ao início). */
    public void reset() {
        jogadores.clear();
        indiceJogadorAtual = 0;
    }

    /** Adiciona um novo jogador ao jogo com o saldo inicial padrão. */
    public void adicionarJogador(String nome) {
        jogadores.add(new Jogador(nome, Regras.SALDO_INICIAL));
    }

    /**
     * Verifica se o jogo terminou.
     * Critério: apenas um jogador ativo (saldo > 0) restante.
     */
    public boolean jogoEncerrado() {
        return jogadores.stream().filter(Jogador::estaAtivo).count() <= 1;
    }

    /** Passa a vez para o próximo jogador no ciclo. */
    public void finalizarTurno() {
        if (jogadores.isEmpty()) return;
        indiceJogadorAtual = (indiceJogadorAtual + 1) % jogadores.size();
    }

    /* ===================== Acesso ao jogador atual ===================== */

    /** Retorna o objeto do jogador da vez (método privado interno). */
    private Jogador jogadorDaVez() {
        return jogadores.get(indiceJogadorAtual);
    }

    /**
     * Retorna uma "foto" imutável do jogador atual.
     * Isso garante que Controller/View consigam ler dados sem alterar o estado real.
     */
    public PlayerSnapshot getJogadorDaVezSnapshot() {
        Jogador j = jogadorDaVez();
        return new PlayerSnapshot(
                j.getNome(),
                j.getSaldo(),
                j.getPosicao(),
                j.isPreso(),
                j.getPropriedades().size()
        );
    }

    /**
     * Retorna snapshots de todos os jogadores, útil para telas de status e relatórios.
     */
    public List<PlayerSnapshot> getJogadoresSnapshot() {
        List<PlayerSnapshot> snapshotList = new ArrayList<>();
        for (Jogador j : jogadores) {
            snapshotList.add(new PlayerSnapshot(
                    j.getNome(),
                    j.getSaldo(),
                    j.getPosicao(),
                    j.isPreso(),
                    j.getPropriedades().size()
            ));
        }
        return Collections.unmodifiableList(snapshotList);
    }

    /* ===================== Dados e movimento ===================== */

    /** Lança dois dados e retorna seus valores individuais. */
    public int[] lancarDados() {
        return new int[]{Dado.rolar(rng), Dado.rolar(rng)};
    }

    /**
     * Move o jogador da vez pelo tabuleiro com base nos dados lançados.
     * - Atualiza a posição.
     * - Dá bônus ao passar pela partida.
     * - Retorna informações da nova casa.
     */
    public MovementResult deslocarJogadorDaVez(int passos) {
        Jogador j = jogadorDaVez();
        int posAnterior = j.getPosicao();
        int novaPosicao = (posAnterior + passos) % tabuleiro.tamanho();

        // Bônus ao passar pela casa de Partida
        if ((posAnterior + passos) >= tabuleiro.tamanho()) {
            j.creditar(Regras.BONUS_PARTIDA);
        }

        j.setPosicao(novaPosicao);
        Espaco e = tabuleiro.getEspaco(novaPosicao);

        return new MovementResult(novaPosicao, e.getNome(), e.getTipo());
    }

    /* ===================== Prisão ===================== */

    /** Usa uma carta de “Sair da Prisão” se disponível. */
    public boolean usarCartaSairLivreDaVez() {
        Jogador j = jogadorDaVez();
        if (j.consumirCartaSaidaLivreSeDisponivel()) {
            j.sairDaPrisao();
            return true;
        }
        return false;
    }

    /**
     * Tenta sair da prisão com base no lançamento dos dados.
     * - Sai se tirar dupla.
     * - Sai pagando multa após 3 turnos preso.
     */
    public boolean tentarSairDaPrisaoComDadosDaVez(int d1, int d2) {
        Jogador j = jogadorDaVez();
        if (!j.isPreso()) return true;

        if (d1 == d2) { // dupla: sai imediatamente
            j.sairDaPrisao();
            return true;
        }

        j.incrementarTurnosNaPrisao();
        if (j.getTurnosNaPrisao() >= 3 && j.getSaldo() >= Regras.MULTA_SAIDA_PRISAO) {
            j.debitar(Regras.MULTA_SAIDA_PRISAO);
            j.sairDaPrisao();
            return true;
        }
        return false;
    }

    /* ===================== Processamento de casas ===================== */

    /**
     * Aplica o efeito da casa onde o jogador parou.
     * - Vá para prisão → move jogador.
     * - Propriedade de outro jogador → cobra aluguel.
     */
    public void processarCasaAtualDaVez() {
        Jogador j = jogadorDaVez();
        Espaco e = tabuleiro.getEspaco(j.getPosicao());

        switch (e.getTipo()) {
            case VA_PARA_PRISAO:
                j.enviarParaPrisao(tabuleiro.getIndicePrisao());
                break;

            case PROPRIEDADE:
                Propriedade p = (Propriedade) e;
                Jogador dono = p.getDono();
                if (dono != null && dono != j) {
                    if (p.getCasas() > 0 || p.isHotel()) {
                        int aluguel = p.calcularAluguel();
                        j.debitar(aluguel);
                        dono.creditar(aluguel);
                    }
                }
                break;

            default:
                break;
        }
    }

    /* ===================== Ações: compra / construção ===================== */

    /**
     * Tenta comprar a propriedade atual, se disponível.
     * - Verifica se não tem dono e se o jogador tem saldo.
     */
    public boolean comprarPropriedadeAtualDaVez() {
        Jogador j = jogadorDaVez();
        Espaco e = tabuleiro.getEspaco(j.getPosicao());
        if (!(e instanceof Propriedade)) return false;

        Propriedade p = (Propriedade) e;
        if (p.getDono() != null) return false;
        if (j.getSaldo() < p.getPreco()) return false;

        j.debitar(p.getPreco());
        p.setDono(j);
        j.adicionarPropriedade(p);
        return true;
    }

    /**
     * Constrói uma casa ou hotel na propriedade atual (se possível).
     */
    public boolean construirNaPropriedadeAtualDaVez() {
        Jogador j = jogadorDaVez();
        Espaco e = tabuleiro.getEspaco(j.getPosicao());
        if (!(e instanceof Propriedade)) return false;

        Propriedade p = (Propriedade) e;
        if (p.getDono() != j) return false;
        if (p.isHotel()) return false;
        if (j.getSaldo() < p.getPrecoConstrucao()) return false;

        j.debitar(p.getPrecoConstrucao());
        p.construirCasaOuHotel();
        return true;
    }

    /* ===================== Venda de propriedades ===================== */

    /**
     * Retorna snapshots de todas as propriedades do jogador da vez.
     * Esses objetos são imutáveis e seguros para exibição.
     */
    public List<PropriedadeSnapshot> getPropriedadesDoJogadorDaVez() {
        Jogador j = jogadorDaVez();
        List<PropriedadeSnapshot> snapshotList = new ArrayList<>();
        for (Propriedade p : j.getPropriedades()) {
            snapshotList.add(new PropriedadeSnapshot(
                    p.getNome(),
                    p.getCasas(),
                    p.isHotel(),
                    p.getPreco(),
                    p.getPrecoConstrucao()
            ));
        }
        return Collections.unmodifiableList(snapshotList);
    }

    /**
     * Vende uma propriedade ao banco pelo nome.
     * - Remove do jogador.
     * - Reseta o estado da propriedade.
     * - Retorna parte do valor ao jogador.
     */
    public boolean venderPropriedadePorNome(String nomePropriedade) {
        Jogador j = jogadorDaVez();
        for (Propriedade p : j.getPropriedades()) {
            if (p.getNome().equalsIgnoreCase(nomePropriedade)) {
                return p.venderParaBanco(j);
            }
        }
        return false;
    }

    /* ===================== DTOs (Data Transfer Objects) ===================== */
    // As classes abaixo são usadas apenas para transferir dados para o Controller/View
    // sem expor diretamente as classes internas do modelo.

    public static class PlayerSnapshot {
        private final String nome;
        private final int saldo;
        private final int posicao;
        private final boolean preso;
        private final int qtdPropriedades;

        public PlayerSnapshot(String nome, int saldo, int posicao, boolean preso, int qtdPropriedades) {
            this.nome = nome;
            this.saldo = saldo;
            this.posicao = posicao;
            this.preso = preso;
            this.qtdPropriedades = qtdPropriedades;
        }

        public String getNome() { return nome; }
        public int getSaldo() { return saldo; }
        public int getPosicao() { return posicao; }
        public boolean isPreso() { return preso; }
        public int getQtdPropriedades() { return qtdPropriedades; }
    }

    public static class MovementResult {
        private final int novaPosicao;
        private final String nomeEspaco;
        private final Espaco.Tipo tipo;

        public MovementResult(int novaPosicao, String nomeEspaco, Espaco.Tipo tipo) {
            this.novaPosicao = novaPosicao;
            this.nomeEspaco = nomeEspaco;
            this.tipo = tipo;
        }

        public int getNovaPosicao() { return novaPosicao; }
        public String getNomeEspaco() { return nomeEspaco; }
        public Espaco.Tipo getTipo() { return tipo; }
    }

    public static class PropriedadeSnapshot {
        private final String nome;
        private final int casas;
        private final boolean hotel;
        private final int preco;
        private final int precoConstrucao;

        public PropriedadeSnapshot(String nome, int casas, boolean hotel, int preco, int precoConstrucao) {
            this.nome = nome;
            this.casas = casas;
            this.hotel = hotel;
            this.preco = preco;
            this.precoConstrucao = precoConstrucao;
        }

        public String getNome() { return nome; }
        public int getCasas() { return casas; }
        public boolean isHotel() { return hotel; }
        public int getPreco() { return preco; }
        public int getPrecoConstrucao() { return precoConstrucao; }
    }
}
