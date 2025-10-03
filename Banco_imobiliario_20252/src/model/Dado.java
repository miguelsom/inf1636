package model;

import java.util.Random;

/**
 * ================================================================
 * Classe Dado
 * ================================================================
 * Esta classe representa a lógica de rolagem dos dados do jogo.
 *
 * ✅ Papel no projeto:
 * - É uma classe **utilitária** (não precisa ser instanciada).
 * - Fornece métodos estáticos para gerar valores aleatórios entre 1 e 6.
 * - É usada sempre que algum jogador precisa "rolar os dados".
 *
 * ✅ Responsabilidades principais:
 * - Rolar um único dado (valor de 1 a 6).
 * - Rolar dois dados e retornar a soma dos resultados.
 *
 * ✅ Importante:
 * - A classe é marcada como `final` para impedir herança.
 * - O construtor é `private` para evitar que seja instanciada.
 * - Faz parte da camada **Model** do jogo.
 * ================================================================
 */
final class Dado {

    /**
     * Construtor privado — impede que a classe seja instanciada.
     * 
     * Como todos os métodos são estáticos, não faz sentido criar
     * objetos do tipo `Dado`. Essa abordagem reforça a ideia de que
     * a classe é apenas um utilitário para uso global no sistema.
     */
    private Dado() {}

    /**
     * ================================================================
     * rolar()
     * ================================================================
     * Rola um dado de 6 faces e retorna um valor inteiro aleatório
     * entre 1 e 6.
     *
     * Uso típico: quando o jogador precisa rolar apenas um dado.
     *
     * @param gerador instância de {@link Random} usada para gerar
     *                o número aleatório. (A injeção do Random de fora
     *                facilita testes e mantém consistência no jogo.)
     * @return valor inteiro entre 1 e 6 representando o resultado da rolagem.
     */
    public static int rolar(Random gerador) {
        return gerador.nextInt(6) + 1; // nextInt(6) gera 0..5 → soma 1 → 1..6
    }

    /**
     * ================================================================
     * rolarDoisDados()
     * ================================================================
     * Rola dois dados de 6 faces e retorna a soma dos dois valores.
     *
     * Uso típico: movimentação do jogador no tabuleiro (Monopoly).
     * 
     * A soma resultante será sempre entre 2 e 12.
     *
     * @param gerador instância de {@link Random} usada para gerar
     *                os números aleatórios.
     * @return soma dos dois valores gerados na rolagem dos dados.
     */
    public static int rolarDoisDados(Random gerador) {
        int dado1 = rolar(gerador);
        int dado2 = rolar(gerador);
        return dado1 + dado2;
    }
}
