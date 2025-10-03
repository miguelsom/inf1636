package model;

/**
 * ================================================================
 * Classe Propriedade
 * ================================================================
 * ✅ Papel no sistema:
 * Representa um espaço do tabuleiro que pode ser comprado, vendido e
 * gerar renda por meio de aluguel. É o principal ativo que os jogadores
 * adquirem ao longo do jogo.
 *
 * ✅ Responsabilidades principais:
 * - Armazenar informações de valor, aluguel e dono.
 * - Controlar a construção de casas e hotel.
 * - Calcular o aluguel correto conforme o nível de construção.
 * - Permitir a venda da propriedade de volta ao banco.
 *
 * ✅ Relações:
 * - Herda de {@link Espaco}, pois é um tipo específico de espaço do tabuleiro.
 * - Está associada a um {@link Jogador} (dono) que recebe aluguel e pode vendê-la.
 * ================================================================
 */
class Propriedade extends Espaco {

    /* ===================== Atributos financeiros ===================== */

    // Preço base para comprar o terreno
    private final int preco;

    // Aluguéis em cada estágio de desenvolvimento
    private final int aluguelBase;
    private final int aluguel1Casa;
    private final int aluguel2Casas;
    private final int aluguel3Casas;
    private final int aluguel4Casas;
    private final int aluguelHotel;

    // Custo para construir uma casa ou hotel
    private final int precoConstrucao;

    /* ===================== Estado da propriedade ===================== */

    // Jogador dono da propriedade (null se estiver no banco)
    private Jogador dono;

    // Número de casas construídas (0 a 4)
    private int casas;

    // Indica se existe um hotel construído
    private boolean hotel;

    /**
     * Construtor: define todos os valores da propriedade.
     *
     * @param nome            nome da propriedade
     * @param preco           preço do terreno
     * @param aluguelBase     aluguel sem construções
     * @param aluguel1Casa    aluguel com 1 casa
     * @param aluguel2Casas   aluguel com 2 casas
     * @param aluguel3Casas   aluguel com 3 casas
     * @param aluguel4Casas   aluguel com 4 casas
     * @param aluguelHotel    aluguel com hotel
     * @param precoConstrucao custo para construir cada casa ou hotel
     */
    Propriedade(
            String nome,
            int preco,
            int aluguelBase,
            int aluguel1Casa,
            int aluguel2Casas,
            int aluguel3Casas,
            int aluguel4Casas,
            int aluguelHotel,
            int precoConstrucao
    ) {
        super(nome, Tipo.PROPRIEDADE);
        this.preco = preco;
        this.aluguelBase = aluguelBase;
        this.aluguel1Casa = aluguel1Casa;
        this.aluguel2Casas = aluguel2Casas;
        this.aluguel3Casas = aluguel3Casas;
        this.aluguel4Casas = aluguel4Casas;
        this.aluguelHotel = aluguelHotel;
        this.precoConstrucao = precoConstrucao;
        this.dono = null;
        this.casas = 0;
        this.hotel = false;
    }

    /* ===================== Getters básicos ===================== */

    public int getPreco() { return preco; }
    public int getPrecoConstrucao() { return precoConstrucao; }
    public int getCasas() { return casas; }
    public boolean isHotel() { return hotel; }
    public Jogador getDono() { return dono; }
    public void setDono(Jogador dono) { this.dono = dono; }

    /* ===================== Construção ===================== */

    /**
     * Constrói uma casa ou um hotel (se já houver 4 casas).
     * - Só pode construir até 4 casas.
     * - Após isso, a próxima construção se torna um hotel.
     * - Caso já tenha hotel, não faz nada.
     */
    public void construirCasaOuHotel() {
        if (hotel) return;       // Já tem hotel → não constrói
        if (casas < 4) {
            casas++;            // Constrói mais uma casa
        } else {
            hotel = true;       // Após 4 casas → vira hotel
        }
    }

    /* ===================== Cálculo de aluguel ===================== */

    /**
     * Calcula o valor do aluguel com base no número de construções.
     * - Sem casas → aluguel base.
     * - 1 a 4 casas → aluguel correspondente.
     * - Hotel → aluguel máximo.
     */
    public int calcularAluguel() {
        if (hotel) return aluguelHotel;
        switch (casas) {
            case 4: return aluguel4Casas;
            case 3: return aluguel3Casas;
            case 2: return aluguel2Casas;
            case 1: return aluguel1Casa;
            default: return aluguelBase;
        }
    }

    /* ===================== Reset e falência ===================== */

    /**
     * Remove o dono e reinicia o estado da propriedade.
     * Usado em casos de falência ou venda ao banco.
     */
    public void resetarParaBanco() {
        this.dono = null;
        this.casas = 0;
        this.hotel = false;
    }

    /* ===================== Venda ao banco ===================== */

    /**
     * Vende a propriedade de volta ao banco.
     * O jogador recebe 90% do total investido:
     *  - preço do terreno
     *  - + valor de cada casa construída
     *  - + valor do hotel (se existir)
     *
     * @param vendedor jogador que está vendendo
     * @return true se a venda foi realizada com sucesso
     */
    public boolean venderParaBanco(Jogador vendedor) {
        // Valida se a propriedade realmente pertence ao vendedor
        if (dono == null || !dono.equals(vendedor)) {
            return false;
        }

        // Calcula o valor total investido
        int valorTotal = preco;
        valorTotal += casas * precoConstrucao;
        if (hotel) {
            valorTotal += precoConstrucao;
        }

        // Jogador recebe 90% do valor investido
        int valorVenda = (int) (valorTotal * 0.9);
        vendedor.receber(valorVenda);

        // Propriedade volta ao banco
        resetarParaBanco();
        return true;
    }
}
