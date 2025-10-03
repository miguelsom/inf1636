package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ================================================================
 * Classe Jogador
 * ================================================================
 * Representa um participante da partida e mantém todas as
 * informações relacionadas ao seu estado no jogo.
 *
 * ✅ Responsabilidades principais:
 * - Armazenar e gerenciar informações pessoais do jogador.
 * - Controlar saldo, posição no tabuleiro e propriedades adquiridas.
 * - Gerenciar status de prisão e cartas especiais.
 * - Oferecer métodos para operações comuns como pagar, receber,
 *   comprar, vender e mover-se.
 *
 * ✅ Pilares de design:
 * - Encapsulamento: todos os atributos são privados e expostos
 *   apenas por métodos públicos controlados.
 * - Coesão: cada método tem responsabilidade única e clara.
 * - Imutabilidade parcial: algumas listas são expostas apenas como
 *   cópias imutáveis para evitar modificações externas indevidas.
 * ================================================================
 */
class Jogador {

    /* ===================== Atributos principais ===================== */

    // Nome do jogador (definido ao iniciar o jogo)
    private final String nome;

    // Dinheiro atual do jogador
    private int saldo;

    // Índice da posição atual no tabuleiro
    private int posicao;

    // Lista de propriedades que pertencem ao jogador
    private final List<Propriedade> propriedades = new ArrayList<>();

    /* ===================== Prisão e cartas ===================== */

    // Indica se o jogador está preso
    private boolean preso;

    // Contador de turnos que o jogador passou preso
    private int turnosNaPrisao;

    // Quantidade de cartas "Sair da prisão" que o jogador possui
    private int cartasSaidaLivre;

    /**
     * Construtor: inicializa um novo jogador com nome e saldo inicial.
     *
     * @param nome Nome do jogador.
     * @param saldoInicial Quantia inicial de dinheiro.
     */
    Jogador(String nome, int saldoInicial) {
        this.nome = nome;
        this.saldo = saldoInicial;
        this.posicao = 0;
        this.preso = false;
        this.turnosNaPrisao = 0;
        this.cartasSaidaLivre = 0;
    }

    /* ===================== Informações básicas ===================== */

    /** Retorna o nome do jogador. */
    public String getNome() {
        return nome;
    }

    /** Retorna o saldo atual do jogador. */
    public int getSaldo() {
        return saldo;
    }

    /** Retorna a posição atual do jogador no tabuleiro. */
    public int getPosicao() {
        return posicao;
    }

    /** Retorna true se o jogador está preso. */
    public boolean isPreso() {
        return preso;
    }

    /** Retorna quantos turnos o jogador passou preso. */
    public int getTurnosNaPrisao() {
        return turnosNaPrisao;
    }

    /* ===================== Dinheiro ===================== */

    /**
     * Debita um valor do saldo do jogador.
     * Se o valor for maior que o saldo, zera o saldo.
     *
     * @param valor Valor a ser debitado.
     */
    public void debitar(int valor) {
        saldo -= valor;
        if (saldo < 0) saldo = 0; // evita saldo negativo extremo
    }

    /**
     * Credita um valor ao saldo do jogador.
     *
     * @param valor Valor a ser adicionado.
     */
    public void creditar(int valor) {
        saldo += valor;
    }

    /** Alias de debitar — usado quando o jogador paga algo. */
    public void pagar(int valor) {
        debitar(valor);
    }

    /** Alias de creditar — usado quando o jogador recebe algo. */
    public void receber(int valor) {
        creditar(valor);
    }

    /* ===================== Movimento ===================== */

    /**
     * Atualiza a posição atual do jogador no tabuleiro.
     * Este método é chamado após cada jogada de dados.
     *
     * @param posicao Nova posição (índice da casa).
     */
    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    /* ===================== Prisão ===================== */

    /**
     * Envia o jogador diretamente para a prisão.
     * 
     * @param indicePrisao Índice da casa "Prisão" no tabuleiro.
     */
    public void enviarParaPrisao(int indicePrisao) {
        this.preso = true;
        this.posicao = indicePrisao;
        this.turnosNaPrisao = 0;
    }

    /** Liberta o jogador da prisão e zera o contador de turnos preso. */
    public void sairDaPrisao() {
        this.preso = false;
        this.turnosNaPrisao = 0;
    }

    /** Incrementa o número de turnos que o jogador passou preso. */
    public void incrementarTurnosNaPrisao() {
        this.turnosNaPrisao++;
    }

    /**
     * Consome uma carta "Sair da prisão" caso o jogador possua uma.
     * 
     * @return true se uma carta foi usada, false se o jogador não tinha.
     */
    public boolean consumirCartaSaidaLivreSeDisponivel() {
        if (cartasSaidaLivre > 0) {
            cartasSaidaLivre--;
            return true;
        }
        return false;
    }

    /** Adiciona uma carta "Sair da prisão" ao jogador. */
    public void adicionarCartaSaidaLivre() {
        this.cartasSaidaLivre++;
    }

    /* ===================== Propriedades ===================== */

    /**
     * Adiciona uma nova propriedade ao jogador.
     *
     * @param propriedade A propriedade a ser adicionada.
     */
    public void adicionarPropriedade(Propriedade propriedade) {
        if (propriedade != null && !propriedades.contains(propriedade)) {
            propriedades.add(propriedade);
        }
    }

    /**
     * Remove uma propriedade da lista do jogador.
     * 
     * @param propriedade A propriedade a ser removida.
     */
    public void removerPropriedade(Propriedade propriedade) {
        propriedades.remove(propriedade);
    }

    /**
     * Retorna uma lista **imutável** das propriedades do jogador.
     * Isso impede que código externo altere a lista diretamente,
     * preservando o encapsulamento.
     *
     * @return Lista imutável de propriedades.
     */
    public List<Propriedade> getPropriedades() {
        return Collections.unmodifiableList(propriedades);
    }

    /** Remove todas as propriedades do jogador (usado em casos de falência). */
    public void limparPropriedades() {
        propriedades.clear();
    }

    /* ===================== Estado geral ===================== */

    /**
     * Retorna true se o jogador ainda está "ativo" no jogo.
     * Critério: ter saldo maior que zero.
     *
     * @return true se o jogador pode continuar jogando.
     */
    public boolean estaAtivo() {
        return saldo > 0;
    }

    /**
     * Representação textual do jogador (usada em logs e console).
     */
    @Override
    public String toString() {
        return nome + " - Saldo: $" + saldo + (preso ? " (Preso)" : "");
    }
}
