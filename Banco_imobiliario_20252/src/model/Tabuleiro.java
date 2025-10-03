package model;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================================
 * Classe Tabuleiro
 * ================================================================
 * ✅ Responsabilidade:
 * Representa todo o tabuleiro do jogo Banco Imobiliário, ou seja,
 * a sequência de casas por onde os jogadores se movem durante a partida.
 *
 * ✅ O que ela faz:
 * - Cria e organiza todos os espaços (propriedades, sorte, revés, prisão etc.)
 *   na ordem correta do jogo.
 * - Guarda a referência do índice da prisão (necessário para enviar o jogador).
 * - Oferece métodos para consultar o espaço atual e o tamanho do tabuleiro.
 *
 * ✅ Por que ela existe:
 * Ao invés de espalhar a definição do tabuleiro por várias partes do código,
 * centralizamos tudo aqui. Isso facilita a manutenção, leitura e futuras alterações.
 * ================================================================
 */
class Tabuleiro {

    /** Lista que guarda todos os espaços (40 casas) do jogo, na ordem. */
    private final List<Espaco> espacos = new ArrayList<>();

    /** Índice da casa "Prisão" (usado para mover jogadores para lá). */
    private int indicePrisao = -1;

    /**
     * Construtor padrão.
     * Ao ser criado, o tabuleiro chama o método {@link #montar()} para
     * construir a sequência de casas automaticamente.
     */
    Tabuleiro() {
        montar();
    }

    /**
     * ================================================================
     * 🏗️ Método montar()
     * ================================================================
     * Cria e adiciona todos os espaços do jogo em ordem (0 a 39).
     * Cada espaço é um objeto da classe {@link Espaco} ou de uma de suas
     * subclasses (por exemplo, {@link Propriedade}).
     *
     * Aqui são definidos:
     * - O nome da casa
     * - O tipo da casa (propriedade, sorte, revés, prisão etc.)
     * - Os valores de compra, aluguel e construção (fictícios por enquanto)
     *
     * Obs.: Os valores de aluguel variam conforme o número de casas ou hotel
     * construídos e são passados diretamente no construtor de {@link Propriedade}.
     * ================================================================
     */
    /* Contrutor de Propriedade é (nome, preço, aluguel sem casa, aluguel com uma casa, 
     * aluguel com duas casas, aluguel com três casas, aluguel com quatro casas, 
     * aluguel com hotel e custo para construir casa ou hotel*/
    private void montar() {

        // 0 - Partida (ponto inicial do jogo)
        espacos.add(new Espaco("Partida", Espaco.Tipo.PARTIDA) {});

        // 1 - Propriedade: Leblon
        espacos.add(new Propriedade("Leblon", 100, 6, 30, 90, 270, 400, 550, 50));

        // 2 - Casa de Sorte (sorteio de carta)
        espacos.add(new Espaco("Sorte", Espaco.Tipo.SORTE) {});

        // 3 - Av. Presidente Vargas
        espacos.add(new Propriedade("Av. Presidente Vargas", 60, 2, 10, 30, 90, 160, 250, 50));

        // 4 - Av. Nossa Senhora de Copacabana
        espacos.add(new Propriedade("Av. Nossa Senhora de Copacabana", 60, 4, 20, 60, 180, 320, 450, 50));

        // 5 - Companhia Ferroviária
        espacos.add(new Propriedade("Companhia Ferroviária", 200, 25, 50, 100, 200, 300, 400, 0));

        // 6 - Av. Brigadeiro Faria Lima
        espacos.add(new Propriedade("Av. Brigadeiro Faria Lima", 100, 6, 30, 90, 270, 400, 550, 50));

        // 7 - Casa de Revés (carta de penalidade)
        espacos.add(new Espaco("Revés", Espaco.Tipo.REVEZ) {});

        // 8 - Av. Rebouças
        espacos.add(new Propriedade("Av. Rebouças", 100, 6, 30, 90, 270, 400, 550, 50));

        // 9 - Av. 9 de Julho
        espacos.add(new Propriedade("Av. 9 de Julho", 120, 8, 40, 100, 300, 450, 600, 50));

        // 10 - Prisão (apenas visitante neste espaço)
        indicePrisao = espacos.size(); // salva o índice para uso posterior
        espacos.add(new Espaco("Prisão", Espaco.Tipo.PRISAO) {});

        // 11 - Av. Europa
        espacos.add(new Propriedade("Av. Europa", 140, 10, 50, 150, 450, 625, 750, 100));

        // 12 - Companhia de Água e Esgoto
        espacos.add(new Propriedade("Companhia de Água e Esgoto", 150, 15, 30, 90, 180, 250, 400, 0));

        // 13 - Rua Augusta
        espacos.add(new Propriedade("Rua Augusta", 140, 10, 50, 150, 450, 625, 750, 100));

        // 14 - Av. Pacaembu
        espacos.add(new Propriedade("Av. Pacaembu", 160, 12, 60, 180, 500, 700, 900, 100));

        // 15 - Companhia Ferroviária Central
        espacos.add(new Propriedade("Companhia Ferroviária Central", 200, 25, 50, 100, 200, 300, 400, 0));

        // 16 - Parada Inglesa
        espacos.add(new Propriedade("Parada Inglesa", 180, 14, 70, 200, 550, 750, 950, 100));

        // 17 - Sorte
        espacos.add(new Espaco("Sorte", Espaco.Tipo.SORTE) {});

        // 18 - Brooklin
        espacos.add(new Propriedade("Brooklin", 180, 14, 70, 200, 550, 750, 950, 100));

        // 19 - Morumbi
        espacos.add(new Propriedade("Morumbi", 200, 16, 80, 220, 600, 800, 1000, 100));

        // 20 - Estacionamento Livre (sem ação)
        espacos.add(new Espaco("Estacionamento Livre", Espaco.Tipo.NEUTRO) {});

        // 21 - Jardim Europa
        espacos.add(new Propriedade("Jardim Europa", 220, 18, 90, 250, 700, 875, 1050, 150));

        // 22 - Revés
        espacos.add(new Espaco("Revés", Espaco.Tipo.REVEZ) {});

        // 23 - Jardim Paulista
        espacos.add(new Propriedade("Jardim Paulista", 220, 18, 90, 250, 700, 875, 1050, 150));

        // 24 - Rua Oscar Freire
        espacos.add(new Propriedade("Rua Oscar Freire", 240, 20, 100, 300, 750, 925, 1100, 150));

        // 25 - Companhia Ferroviária Sul
        espacos.add(new Propriedade("Companhia Ferroviária Sul", 200, 25, 50, 100, 200, 300, 400, 0));

        // 26 - Pacaembu
        espacos.add(new Propriedade("Pacaembu", 260, 22, 110, 330, 800, 975, 1150, 150));

        // 27 - Paulista
        espacos.add(new Propriedade("Paulista", 260, 22, 110, 330, 800, 975, 1150, 150));

        // 28 - Companhia de Luz
        espacos.add(new Propriedade("Companhia de Luz", 150, 15, 30, 90, 180, 250, 400, 0));

        // 29 - Higienópolis
        espacos.add(new Propriedade("Higienópolis", 280, 24, 120, 360, 850, 1025, 1200, 150));

        // 30 - Vá para a Prisão
        espacos.add(new Espaco("Vá para a Prisão", Espaco.Tipo.VA_PARA_PRISAO) {});

        // 31 - Vila Mariana
        espacos.add(new Propriedade("Vila Mariana", 300, 26, 130, 390, 900, 1100, 1275, 200));

        // 32 - Consolação
        espacos.add(new Propriedade("Consolação", 300, 26, 130, 390, 900, 1100, 1275, 200));

        // 33 - Sorte
        espacos.add(new Espaco("Sorte", Espaco.Tipo.SORTE) {});

        // 34 - Pinheiros
        espacos.add(new Propriedade("Pinheiros", 320, 28, 150, 450, 1000, 1200, 1400, 200));

        // 35 - Companhia Ferroviária Norte
        espacos.add(new Propriedade("Companhia Ferroviária Norte", 200, 25, 50, 100, 200, 300, 400, 0));

        // 36 - Revés
        espacos.add(new Espaco("Revés", Espaco.Tipo.REVEZ) {});

        // 37 - Moema
        espacos.add(new Propriedade("Moema", 350, 35, 175, 500, 1100, 1300, 1500, 200));

        // 38 - Imposto de Renda
        espacos.add(new Espaco("Imposto de Renda", Espaco.Tipo.IMPOSTO) {});

        // 39 - Ibirapuera
        espacos.add(new Propriedade("Ibirapuera", 400, 50, 200, 600, 1400, 1700, 2000, 200));
    }

    // ================================================================
    // 📍 Métodos utilitários de acesso ao tabuleiro
    // ================================================================

    /** Retorna o número total de casas no tabuleiro. */
    public int tamanho() {
        return espacos.size();
    }

    /** Retorna o espaço (propriedade, sorte, revés, etc.) de um índice específico. */
    public Espaco getEspaco(int idx) {
        return espacos.get(idx);
    }

    /** Retorna o índice da casa "Prisão" (usado ao enviar um jogador para lá). */
    public int getIndicePrisao() {
        return indicePrisao;
    }
}
