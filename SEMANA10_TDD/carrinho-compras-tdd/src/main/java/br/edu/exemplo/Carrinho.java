package br.edu.exemplo;

import java.util.ArrayList;
import java.util.List;

/**
 * Carrinho de compras construido por TDD, requisito por requisito.
 *
 * Decisao do requisito 5: preco negativo lanca IllegalArgumentException em vez de
 * ser ignorado em silencio. Ignorar deixaria o chamador convencido de que o produto
 * entrou no carrinho, com um total que nao corresponde ao que ele pediu; a excecao
 * recusa a operacao no momento em que o erro acontece. A justificativa completa esta
 * no README.md desta pasta.
 */
public class Carrinho {

    private static final double MINIMO_PARA_DESCONTO = 500.0;
    private static final double TAXA_DE_DESCONTO = 0.10;

    private final List<Produto> produtos = new ArrayList<>();

    public void adicionar(Produto produto) {
        if (produto.getPreco() < 0) {
            throw new IllegalArgumentException(
                    "Preco negativo nao e permitido: " + produto.getNome());
        }
        produtos.add(produto);
    }

    public void remover(Produto produto) {
        produtos.remove(produto);
    }

    public double getTotal() {
        double subtotal = subtotal();
        if (subtotal >= MINIMO_PARA_DESCONTO) {
            return subtotal - (subtotal * TAXA_DE_DESCONTO);
        }
        return subtotal;
    }

    private double subtotal() {
        double subtotal = 0;
        for (Produto produto : produtos) {
            subtotal += produto.getPreco();
        }
        return subtotal;
    }
}
