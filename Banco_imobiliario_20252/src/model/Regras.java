package model;

/**
 * ================================================================
 * Classe Regras
 * ================================================================
 * ✅ Papel no sistema:
 * Esta classe funciona como um "manual de configuração" centralizado
 * para o jogo Banco Imobiliário. Aqui ficam armazenadas todas as
 * constantes e parâmetros fixos que controlam a lógica do jogo.
 *
 * ✅ Por que isso é importante:
 * - Facilita ajustes futuros nas regras sem alterar o código principal.
 * - Evita números mágicos espalhados pelo projeto.
 * - Melhora a legibilidade e manutenção do código.
 *
 * ✅ Características:
 * - É `final` para impedir herança.
 * - Possui construtor privado para evitar instâncias.
 * - Todos os atributos são `public static final` (constantes globais).
 * ================================================================
 */
final class Regras {

    // ==========================================================
    // 🏁 CONFIGURAÇÕES GERAIS DO JOGO
    // ==========================================================

    /** 💰 Saldo inicial que cada jogador recebe ao início da partida. */
    public static final int SALDO_INICIAL = 4000;

    /** 🎁 Bônus recebido ao passar ou cair na casa "Partida". */
    public static final int BONUS_INICIO = 200;
    public static final int BONUS_PARTIDA = 200;

    /** 🏦 Saldo inicial do banco — usado em transações como compras e pagamentos. */
    public static final int SALDO_BANCO = 200_000;

    // ==========================================================
    // 🚔 REGRAS RELACIONADAS À PRISÃO
    // ==========================================================

    /** ⏱️ Número máximo de turnos que um jogador pode permanecer preso. */
    public static final int TURNOS_MAX_PRISAO = 3;

    /** 💸 Multa cobrada automaticamente ao sair da prisão no 4º turno, se aplicável. */
    public static final int MULTA_SAIDA_PRISAO = 50;

    // ==========================================================
    // 🏗️ CUSTOS DE CONSTRUÇÃO
    // ==========================================================

    /** 🏠 Custo padrão para construir uma casa em uma propriedade. */
    public static final int CUSTO_CASA = 150;

    /** 🏨 Custo padrão para construir um hotel (após as 4 casas). */
    public static final int CUSTO_HOTEL = 500;

    // ==========================================================
    // 💼 TAXAS, PENALIDADES E PRÊMIOS
    // ==========================================================

    /** 💵 Valor padrão de taxa cobrada em casas especiais (ex.: imposto). */
    public static final int TAXA_PADRAO = 200;

    /** 🚫 Valor padrão de multa aplicada por cartas de "Revés". */
    public static final int MULTA_REVEZ = 150;

    /** 🪄 Valor padrão de prêmio concedido por cartas de "Sorte". */
    public static final int PREMIO_SORTE = 200;

    // ==========================================================
    // 📏 LIMITES E REGRAS AVANÇADAS
    // ==========================================================

    /** 🏘️ Número máximo de casas permitidas por propriedade. */
    public static final int MAX_CASAS = 4;

    /** 🏨 Número máximo de hotéis por propriedade. */
    public static final int MAX_HOTEIS = 1;

    /** 🎲 Número máximo de duplas consecutivas permitidas antes de ir para a prisão. */
    public static final int MAX_DUPLAS_CONSECUTIVAS = 3;

    /**
     * ⏹️ Limite opcional de turnos consecutivos sem falência para encerrar
     * automaticamente o jogo por patrimônio.
     * Use 0 para desativar essa regra e terminar o jogo apenas por falência.
     */
    public static final int LIMITE_TURNOS_SEM_FALENCIA = 20_000_000;

    /** 🏆 Patrimônio alvo necessário para vencer o jogo por riqueza. */
    public static final int PATRIMONIO_ALVO = 1_000_000;

    // ==========================================================
    // 🚫 Construtor privado
    // ==========================================================

    /**
     * Construtor privado para impedir a criação de instâncias da classe.
     * Como todas as variáveis são constantes estáticas, essa classe
     * nunca precisa ser instanciada.
     */
    private Regras() {}
}
