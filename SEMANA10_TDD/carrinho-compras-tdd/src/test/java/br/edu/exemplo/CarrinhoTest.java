package br.edu.exemplo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Testes escritos por TDD, um requisito de REQUISITOS.md por vez:
 * escrever o teste, observar a falha (RED), implementar o minimo em Carrinho,
 * confirmar o verde (GREEN) e so entao avancar para o requisito seguinte.
 */
class CarrinhoTest {

    @Test
    @DisplayName("R1 - carrinho recem-criado tem total zero")
    void carrinhoVazioDeveTerTotalZero() {
        Carrinho carrinho = new Carrinho();

        assertEquals(0.0, carrinho.getTotal(), 0.001);
    }

    @Test
    @DisplayName("R2 - adicionar um produto soma seu preco ao total")
    void deveAdicionarUmProduto() {
        Carrinho carrinho = new Carrinho();

        carrinho.adicionar(new Produto("Teclado", 100.0));

        assertEquals(100.0, carrinho.getTotal(), 0.001);
    }

    @Test
    @DisplayName("R3 - total e a soma dos precos de varios produtos")
    void deveSomarVariosProdutos() {
        Carrinho carrinho = new Carrinho();

        carrinho.adicionar(new Produto("Teclado", 100.0));
        carrinho.adicionar(new Produto("Mouse", 50.0));

        assertEquals(150.0, carrinho.getTotal(), 0.001);
    }

    @Test
    @DisplayName("R4 - remover um produto desconta seu preco do total")
    void deveRemoverProduto() {
        Carrinho carrinho = new Carrinho();
        Produto teclado = new Produto("Teclado", 100.0);
        Produto mouse = new Produto("Mouse", 50.0);
        carrinho.adicionar(teclado);
        carrinho.adicionar(mouse);

        carrinho.remover(mouse);

        assertEquals(100.0, carrinho.getTotal(), 0.001);
    }

    @Test
    @DisplayName("R4 - remover produto que nao esta no carrinho nao altera o total")
    void removerProdutoAusenteNaoAlteraOTotal() {
        Carrinho carrinho = new Carrinho();
        carrinho.adicionar(new Produto("Teclado", 100.0));

        carrinho.remover(new Produto("Monitor", 900.0));

        assertEquals(100.0, carrinho.getTotal(), 0.001);
    }

    @Test
    @DisplayName("R5 - produto com preco negativo e recusado com excecao")
    void naoDeveAdicionarProdutoComPrecoNegativo() {
        Carrinho carrinho = new Carrinho();

        assertThrows(IllegalArgumentException.class,
                () -> carrinho.adicionar(new Produto("Teclado", -100.0)));
    }

    @Test
    @DisplayName("R5 - a tentativa recusada nao deixa residuo no carrinho")
    void produtoRecusadoNaoEntraNoCarrinho() {
        Carrinho carrinho = new Carrinho();
        carrinho.adicionar(new Produto("Mouse", 50.0));

        assertThrows(IllegalArgumentException.class,
                () -> carrinho.adicionar(new Produto("Teclado", -100.0)));

        assertEquals(50.0, carrinho.getTotal(), 0.001);
    }

    @Test
    @DisplayName("R6 - total de 600,00 recebe 10% de desconto e fecha em 540,00")
    void deveAplicarDescontoDeDezPorCento() {
        Carrinho carrinho = new Carrinho();

        carrinho.adicionar(new Produto("Monitor", 600.0));

        assertEquals(540.0, carrinho.getTotal(), 0.001);
    }

    @Test
    @DisplayName("R6 - fronteira 499,99: abaixo do minimo, sem desconto")
    void naoDeveAplicarDescontoAbaixoDaFronteira() {
        Carrinho carrinho = new Carrinho();

        carrinho.adicionar(new Produto("Monitor", 499.99));

        assertEquals(499.99, carrinho.getTotal(), 0.001);
    }

    @Test
    @DisplayName("R6 - fronteira 500,00: o minimo ja recebe desconto")
    void deveAplicarDescontoExatamenteNaFronteira() {
        Carrinho carrinho = new Carrinho();

        carrinho.adicionar(new Produto("Monitor", 500.00));

        assertEquals(450.00, carrinho.getTotal(), 0.001);
    }

    @Test
    @DisplayName("R6 - fronteira 500,01: acima do minimo, com desconto")
    void deveAplicarDescontoAcimaDaFronteira() {
        Carrinho carrinho = new Carrinho();

        carrinho.adicionar(new Produto("Monitor", 500.01));

        assertEquals(450.009, carrinho.getTotal(), 0.001);
    }
}
