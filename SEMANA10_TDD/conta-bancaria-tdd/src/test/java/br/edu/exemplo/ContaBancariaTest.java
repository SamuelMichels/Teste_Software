package br.edu.exemplo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ContaBancariaTest {

    @Test
    void deveDepositarValorNaConta() {
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);
        assertEquals(100, conta.getSaldo(), 0.001);
    }

    @Test
    void deveAcumularDepositos() {
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);
        conta.depositar(50);
        assertEquals(150, conta.getSaldo(), 0.001);
    }

    @Test
    void deveSacarValorDaConta() {
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);
        conta.sacar(40);
        assertEquals(60, conta.getSaldo(), 0.001);
    }

    @Test
    void naoDevePermitirSaqueMaiorQueSaldo() {
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);
        conta.sacar(150);
        assertEquals(100, conta.getSaldo(), 0.001);
    }

    // Testes adicionais (nao fornecidos pelo enunciado): os quatro casos originais
    // nao especificam valores negativos, e sem eles um deposito de -50 reduziria o
    // saldo. Acrescentados para fechar essa lacuna, sem alterar os casos fornecidos.

    @Test
    void naoDevePermitirDepositoDeValorNegativo() {
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);
        conta.depositar(-50);
        assertEquals(100, conta.getSaldo(), 0.001);
    }

    @Test
    void naoDevePermitirSaqueDeValorNegativo() {
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);
        conta.sacar(-50);
        assertEquals(100, conta.getSaldo(), 0.001);
    }
}
