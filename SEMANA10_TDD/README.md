# SEMANA 10 — TDD (Test Driven Development)

**Disciplina:** Projeto, Implementação e Teste de Software — 2026

**Integrantes:**
- Samuel Fuentes Michels — RA 24011114-2
- Felipe de Almeida Matrone — RA 24051005-2

Quatro projetos Maven independentes, cada um com JUnit 5. Em três deles os testes já vinham escritos e faltava a implementação; no quarto (`carrinho-compras-tdd`) faltavam os dois, e o ciclo TDD foi percorrido requisito por requisito.

| Projeto | Testes | Situação inicial | Resultado |
|---|---:|---|---|
| [bubblesort-tdd](bubblesort-tdd) | 3 | Testes dados, `ordenar()` vazio | 3/3 verdes |
| [conta-bancaria-tdd](conta-bancaria-tdd) | 6 | Testes dados, `depositar`/`sacar` vazios | 6/6 verdes |
| [estoque-tdd](estoque-tdd) | 7 | Testes dados, `adicionarEstoque`/`retirarEstoque` vazios | 7/7 verdes |
| [carrinho-compras-tdd](carrinho-compras-tdd) | 11 | Classe e testes vazios | 11/11 verdes |
| **Total** | **27** | | **27/27 verdes** |

## Como executar

```bash
cd SEMANA10_TDD/<projeto>
mvn test
```

Requer JDK 17 ou superior. Cada projeto tem seu próprio `pom.xml`; não há agregador na raiz da pasta.

## O que o RED revelou antes de qualquer implementação

A suíte foi executada nos três projetos **antes** de escrever uma linha de implementação:

```
bubblesort-tdd      Tests run: 3, Failures: 2   <- 1 teste passou mesmo sem implementação
conta-bancaria-tdd  Tests run: 4, Failures: 4
estoque-tdd         Tests run: 6, Failures: 4   <- 2 testes passaram mesmo sem implementação
```

Os três testes que já passavam no RED merecem atenção, porque são falsos positivos:

- `deveManterVetorJaOrdenado` — o vetor `{1,2,3,4,5}` já está ordenado, então um `ordenar()` que não faz nada satisfaz a asserção.
- `produtoDeveIniciarComEstoqueZero` — verifica o estado do construtor, que já estava correto.
- `naoDeveAdicionarQuantidadeNegativa` — espera que o estoque permaneça `0`; um método vazio também deixa em `0`.

Isso mostra que **contar testes verdes não mede nada**: o que mede é se o teste falha quando o código está errado. Os dois primeiros casos continuariam verdes com qualquer implementação quebrada de `ordenar()`; o terceiro só ganha valor junto com os demais casos de `adicionarEstoque`.

## Implementações

### bubblesort-tdd

`ordenar()` com bubble sort clássico: varreduras sucessivas trocando vizinhos fora de ordem, encurtando o alcance a cada passada (`numeros.length - 1 - i`), já que o maior elemento fica posicionado ao fim de cada varredura. Os três casos dados cobrem vetor desordenado, vetor já ordenado e vetor com repetições — este último confirma que a troca usa `>` e não `>=`, o que evita trocas inúteis entre iguais.

### conta-bancaria-tdd

```java
public void depositar(double valor) {
    if (valor <= 0) return;
    saldo += valor;
}

public void sacar(double valor) {
    if (valor <= 0 || valor > saldo) return;
    saldo -= valor;
}
```

O caso `naoDevePermitirSaqueMaiorQueSaldo` espera saldo `100` depois de `sacar(150)` — ou seja, o enunciado pede que a operação inválida seja **ignorada em silêncio**, não que lance exceção. A implementação segue isso.

**Dois testes foram acrescentados** (`naoDevePermitirDepositoDeValorNegativo`, `naoDevePermitirSaqueDeValorNegativo`): os quatro casos fornecidos não dizem nada sobre valores negativos, e sem o guarda `valor <= 0` um `depositar(-50)` reduziria o saldo e um `sacar(-50)` o aumentaria. Os casos originais não foram alterados.

### estoque-tdd

Mesmo padrão: operação inválida é ignorada, conforme os requisitos 5 e 6 do enunciado (retirar mais que o estoque mantém a quantidade; adicionar negativo mantém em zero). **Um teste foi acrescentado** (`naoDeveRetirarQuantidadeNegativa`), pela mesma lacuna: o enunciado proíbe adicionar quantidade negativa mas nada diz sobre retirar, e `retirarEstoque(-5)` aumentaria o estoque.

### carrinho-compras-tdd — o ciclo TDD completo

Aqui nada existia. Cada requisito de [REQUISITOS.md](carrinho-compras-tdd/REQUISITOS.md) foi percorrido na ordem: escrever o teste → executar e observar a falha → implementar o mínimo → executar e confirmar o verde.

| Requisito | RED observado | Implementação mínima |
|---|---|---|
| R1 — carrinho vazio | `cannot find symbol: method getTotal()` (erro de compilação) | `getTotal()` devolvendo `0` |
| R2 — adicionar um produto | `cannot find symbol: method adicionar(Produto)` | `List<Produto>` + soma dos preços |
| R3 — vários produtos | **passou sem alteração** | nenhuma — ver nota abaixo |
| R4 — remover produto | `cannot find symbol: method remover(Produto)` | `produtos.remove(produto)` |
| R5 — preço negativo | `Expected java.lang.IllegalArgumentException to be thrown, but nothing was thrown` | guarda em `adicionar` |
| R6 — desconto de 10% | `expected: <540.0> but was: <600.0>` | condição em `getTotal()` |

**Nota sobre R3:** o teste de vários produtos passou de primeira, sem nenhuma implementação nova. Isso não é um defeito do processo: a implementação mínima de R2 já somava uma lista, e somar dois itens é o mesmo código que somar um. O ciclo TDD foi seguido corretamente — escrever o teste, executar, observar — e a observação foi que o requisito já estava satisfeito. O teste permanece na suíte porque protege esse comportamento contra regressões futuras, mesmo não tendo provocado código novo.

**Nota sobre as fronteiras de R6:** dos quatro testes de desconto, apenas três falharam no RED. O de `499,99` passou de imediato, porque espera justamente a *ausência* de desconto, que era o comportamento vigente. Os três que falharam são os que cobrem `500,00`, `500,01` e `600,00`.

#### Decisão do Requisito 5 — exceção, não silêncio

O enunciado deixa a escolha aberta ("Discuta com o professor se o sistema deve ignorar a operação ou lançar uma exceção"). A decisão foi **lançar `IllegalArgumentException`**, por três razões:

1. **O chamador precisa saber.** Ignorar em silêncio devolve o controle ao código que chamou `adicionar()` com a impressão de que o produto entrou. O total fica diferente do que o usuário pediu e nada aponta o motivo. A exceção falha no ponto exato do erro, com o nome do produto na mensagem.
2. **Preço negativo não é entrada de usuário, é defeito.** Um estoque que tenta retirar mais do que tem é uma situação normal de operação — faz sentido absorvê-la. Um produto cadastrado com preço negativo é inconsistência de dados: tratá-la como rotina é esconder um defeito que vai reaparecer na fatura.
3. **Simetria com o resto do sistema.** `ProdutoService` da SEMANA11 recusa preço não positivo exatamente assim, com `IllegalArgumentException`. Divergir aqui criaria duas convenções para o mesmo tipo de erro.

Note o contraste deliberado com `conta-bancaria-tdd` e `estoque-tdd`, onde a operação inválida é ignorada: ali a decisão não era nossa, os testes fornecidos a fixaram. Onde a escolha era nossa, ela foi outra — e o teste `produtoRecusadoNaoEntraNoCarrinho` garante que a recusa não deixa resíduo no carrinho.

## Resultado final

```
bubblesort-tdd      Tests run: 3,  Failures: 0, Errors: 0, Skipped: 0   BUILD SUCCESS
conta-bancaria-tdd  Tests run: 6,  Failures: 0, Errors: 0, Skipped: 0   BUILD SUCCESS
estoque-tdd         Tests run: 7,  Failures: 0, Errors: 0, Skipped: 0   BUILD SUCCESS
carrinho-compras-tdd Tests run: 11, Failures: 0, Errors: 0, Skipped: 0  BUILD SUCCESS
```

Nenhum caso de teste fornecido pelo enunciado foi alterado para fazer a implementação passar, conforme a restrição dos `REQUISITOS.md`. Os três testes acrescentados são adições, identificadas por comentário no próprio arquivo.
