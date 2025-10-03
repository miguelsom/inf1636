package model;

/**
 * ================================================================
 * Classe Espaco (abstrata)
 * ================================================================
 * Esta classe representa a estrutura **básica e genérica** de um
 * espaço (ou casa) no tabuleiro do jogo.
 *
 * ✅ Papel no projeto:
 * - Serve como classe **mãe** (superclasse) para todas as casas do tabuleiro.
 * - Cada tipo de espaço (propriedade, sorte, prisão etc.) herda dela.
 * - Define as características comuns a qualquer espaço: nome e tipo.
 *
 * ✅ Design:
 * - É uma classe **abstrata** → não pode ser instanciada diretamente.
 * - Apenas suas subclasses (como `Propriedade`) podem ser criadas.
 * - Faz parte da camada **Model** e está no núcleo da lógica do jogo.
 * ================================================================
 */
abstract class Espaco {

    /**
     * ================================================================
     * Enum Tipo
     * ================================================================
     * Representa todos os tipos possíveis de espaços no tabuleiro.
     * Isso permite que o programa trate diferentes casas de forma
     * consistente e evite erros de strings ou valores mágicos.
     *
     * Cada constante representa uma "categoria" de espaço:
     * - PARTIDA          → ponto inicial do jogo
     * - PROPRIEDADE      → terrenos que podem ser comprados e alugados
     * - SORTE            → cartas de sorte (ganhos ou perdas)
     * - REVEZ            → cartas de revés (penalidades ou imprevistos)
     * - PRISAO           → casa de prisão (visita ou encarceramento)
     * - VA_PARA_PRISAO   → envia o jogador diretamente para a prisão
     * - NEUTRO           → casas sem efeito direto (como estacionamento)
     * - IMPOSTO          → cobrança obrigatória ao jogador
     */
    enum Tipo {
        PARTIDA,
        PROPRIEDADE,
        SORTE,
        REVEZ,
        PRISAO,
        VA_PARA_PRISAO,
        NEUTRO,
        IMPOSTO
    }

    // Nome exibido no tabuleiro, ex.: "Av. Paulista", "Sorte", "Prisão"
    private final String nome;

    // Tipo do espaço, definido pelo enum acima
    private final Tipo tipo;

    /**
     * Construtor protegido — chamado apenas pelas subclasses.
     *
     * @param nome  Nome do espaço no tabuleiro.
     * @param tipo  Tipo do espaço (usando o enum Tipo).
     */
    Espaco(String nome, Tipo tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    /**
     * ================================================================
     * getNome()
     * ================================================================
     * Retorna o nome do espaço para exibição na interface ou logs.
     * Exemplo: "Av. Brigadeiro Faria Lima", "Sorte", "Vá para a Prisão"
     *
     * @return Nome do espaço como String.
     */
    public String getNome() {
        return nome;
    }

    /**
     * ================================================================
     * getTipo()
     * ================================================================
     * Retorna o tipo do espaço (definido pelo enum Tipo).
     * Isso permite ao programa saber o que fazer quando o jogador
     * parar nesta casa (ex.: comprar, pagar aluguel, sacar carta etc.).
     *
     * @return Tipo do espaço (enum Tipo).
     */
    public Tipo getTipo() {
        return tipo;
    }
}
