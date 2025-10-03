package controller;

import model.ModelFacade;
import model.ModelFacade.PropriedadeSnapshot;
import java.util.List;

/**
 * ================================================================
 * Classe ControladorAcoes
 * ================================================================
 * Esta classe faz parte da camada "Controller" do padrão MVC.
 * 
 * Sua função é servir como um intermediário entre a camada de
 * controle principal do jogo (ControladorJogo) e a camada de
 * "Model" (lógica do jogo) representada por ModelFacade.
 *
 * Em outras palavras: ela traduz ações que vêm da interface do
 * usuário (ou do fluxo do jogo) em chamadas para o modelo,
 * sem expor a lógica interna do sistema diretamente.
 * 
 * Cada método abaixo representa uma ação que o jogador pode
 * realizar durante o jogo.
 * ================================================================
 */
public class ControladorAcoes {

    // Fachada do modelo. É por meio dela que o controlador acessa as regras do jogo.
    private final ModelFacade facade;

    /**
     * Construtor: recebe a fachada do modelo como dependência.
     * Assim, essa classe não conhece detalhes internos do jogo,
     * apenas chama métodos públicos definidos pela façade.
     */
    public ControladorAcoes(ModelFacade facade) {
        this.facade = facade;
    }

    /**
     * ================================================================
     * processarCasaAtual()
     * ================================================================
     * Responsável por aplicar as regras da casa em que o jogador
     * parou após jogar os dados.
     * 
     * Exemplo de comportamentos:
     * - Se for uma propriedade de outro jogador → paga aluguel.
     * - Se for "Vá para a prisão" → jogador vai preso.
     * - Se for neutra → nada acontece.
     */
    public void processarCasaAtual() {
        facade.processarCasaAtualDaVez();
    }

    /**
     * ================================================================
     * tentarComprarAtual()
     * ================================================================
     * Tenta comprar a propriedade onde o jogador parou, caso ela
     * esteja disponível para compra e ele tenha saldo suficiente.
     * 
     * Retorno:
     * - true  → compra realizada com sucesso.
     * - false → não foi possível comprar (sem dinheiro ou já tem dono).
     */
    public boolean tentarComprarAtual() {
        return facade.comprarPropriedadeAtualDaVez();
    }

    /**
     * ================================================================
     * construirNaAtual()
     * ================================================================
     * Tenta construir uma casa ou hotel na propriedade em que o
     * jogador se encontra. Só é possível construir se:
     * - A propriedade for dele.
     * - Houver saldo suficiente.
     * - Ainda houver espaço para construir (máx. 4 casas + 1 hotel).
     * 
     * Retorno:
     * - true  → construção realizada.
     * - false → não foi possível construir.
     */
    public boolean construirNaAtual() {
        return facade.construirNaPropriedadeAtualDaVez();
    }

    /**
     * ================================================================
     * venderPropriedadePorNome(String nomePropriedade)
     * ================================================================
     * Vende uma propriedade do jogador atual de volta ao banco.
     * 
     * O nome da propriedade vem da interface (escolha do usuário).
     * A venda devolve 90% do valor investido ao jogador e a
     * propriedade volta a estar disponível no tabuleiro.
     * 
     * Retorno:
     * - true  → venda concluída.
     * - false → venda não realizada (nome incorreto ou não é dono).
     */
    public boolean venderPropriedadePorNome(String nomePropriedade) {
        return facade.venderPropriedadePorNome(nomePropriedade);
    }

    /**
     * ================================================================
     * listarPropriedadesJogadorDaVez()
     * ================================================================
     * Retorna uma lista com informações (snapshots) de todas as
     * propriedades que pertencem ao jogador da vez.
     * 
     * IMPORTANTE:
     * - Essa lista não contém objetos "Propriedade" reais.
     * - Ela retorna apenas cópias imutáveis com dados necessários
     *   para exibição na interface.
     * 
     * Uso típico:
     * - Mostrar as propriedades para que o jogador escolha qual vender.
     * - Exibir status (quantas casas, tem hotel etc.).
     */
    public List<PropriedadeSnapshot> listarPropriedadesJogadorDaVez() {
        return facade.getPropriedadesDoJogadorDaVez();
    }
}
