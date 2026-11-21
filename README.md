# Teste_Software

Repositório da disciplina **Projeto, Implementação e Teste de Software — 2026**.

**Integrantes**

- Samuel Fuentes Michels — RA 24011114-2
- Felipe de Almeida Matrone — RA 24051005-2

Repositório da disciplina: <https://github.com/JoaoChoma/teste_software_2026>

---

## Atividades da Avaliação Prática — Prova 01

| # | Atividade | Pasta | Estado |
| --- | --- | --- | --- |
| 1 | Artefatos de Plano de Teste e Casos de Teste | [`Trabalho/`](Trabalho) | Concluída |
| 2 | Teste funcional com Playwright | [`SEMANA04_teste_funcional_playwright/`](SEMANA04_teste_funcional_playwright) | Concluída — 60 testes |
| 3 | Teste estrutural / unitário com JUnit | [`SEMANA06_teste_estrutural_junit/`](SEMANA06_teste_estrutural_junit) | Concluída — 218 testes, 100% de cobertura |
| 4 | Exercícios sobre Grafo de Fluxo de Controle (GFC) | [`SEMANA07_GFC/`](SEMANA07_GFC) | Concluída |

---

### 1. Artefatos de Plano de Teste e Casos de Teste

Sistema escolhido: **Sistema de Reserva de Salas**.

- `Trabalho/Plano_de_Teste_Sistema_Reserva_Salas.docx` (e `.pdf`)
- `Trabalho/Casos_de_Teste_Sistema_Reserva_Salas.docx` (e `.pdf`)
- `Trabalho/Matriz_de_Rastreabilidade_Sistema_Reserva_Salas.docx`

---

### 2. Teste funcional com Playwright

Projeto de `aulas/SEMANA04/exemplo-playwright_win` com os testes das interfaces de
**frete** e **senha**, que eram o que faltava entregar.

Arquivos escritos por nós:

- [`tests/frete.spec.ts`](SEMANA04_teste_funcional_playwright/tests/frete.spec.ts) — 27 testes
- [`tests/senha.spec.ts`](SEMANA04_teste_funcional_playwright/tests/senha.spec.ts) — 22 testes

Os specs `idade.spec.ts` e `login.spec.ts` são os gabaritos originais da aula e foram mantidos.

Cobertura de cada tela, por classe de equivalência e valor-limite:

| Tela | Caminhos válidos | Classes inválidas | Valores-limite |
| --- | --- | --- | --- |
| `/frete` | CEP faixa 8 (R$ 15,00), demais CEPs (R$ 25,00), frete grátis, separador `,` e `.`, `trim` | CEP com 7/9 dígitos, com letra, com hífen, vazio; valor 0, negativo, com 3 casas, não numérico, vazio | `199,99` x `200,00` x `200,01`; `0,01`; CEP de 7/8/9 dígitos |
| `/senha` | 8 a 20 caracteres, com maiúscula, minúscula e número | 7 e 21 caracteres, sem maiúscula, sem minúscula, sem número, com espaço, vazia, confirmação divergente | `7` x `8` x `9` e `19` x `20` x `21` caracteres |

Também são verificados o atributo `role` (`status` x `alert`), a classe `success`, o
`reset` do formulário após o cadastro e a precedência da validação de formato sobre a
comparação com a confirmação.

**Como executar**

```bash
cd SEMANA04_teste_funcional_playwright && npm install && npx playwright install chromium && npm test
```

Resultado da última execução: **60 testes, 60 aprovados** (Chromium).

---

### 3. Teste estrutural / unitário com JUnit

Dois projetos Maven com JUnit 5 e JaCoCo.

#### 3.1 `boletim-simples` — 31 testes

Testes dos quatro métodos (`calcularMedia`, `verificarSituacao`, `contarAprovados`,
`calcularPontos`), com as fronteiras 4 e 7 da classificação, o laço com zero/uma/várias
iterações e as quatro combinações da tabela de decisão de `calcularPontos`.

```bash
cd SEMANA06_teste_estrutural_junit/boletim-simples && mvn clean test
```

Cobertura: **100%** de instruções, branches, linhas, métodos e classes.

#### 3.2 `central-pedidos` — 187 testes

Laboratório completo de teste estrutural: nove classes, sem alterar nenhuma regra de
produção. Inclui CFGs, cálculo de McCabe, base de caminhos independentes e matriz de
testes em [`RELATORIO.md`](SEMANA06_teste_estrutural_junit/central-pedidos/RELATORIO.md).

```bash
cd SEMANA06_teste_estrutural_junit/central-pedidos && mvn clean test
```

| Classe de teste | Testes |
| --- | --- |
| `PedidoTest` | 42 |
| `PoliticaDescontoTest` | 33 |
| `CalculadoraFreteTest` | 28 |
| `ItemPedidoTest` | 26 |
| `PedidoServiceTest` | 21 |
| `PagamentoServiceTest` | 17 |
| `AnaliseRiscoTest` | 16 |
| `ClienteTest` | 4 |

Cobertura JaCoCo: **637/637 instruções · 116/116 branches · 108/108 linhas · 21/21 métodos ·
9/9 classes — 100%**.

Resumo dos grafos (modelo e caminhos detalhados no `RELATORIO.md`):

| Método | N | E | V(G) |
| --- | --- | --- | --- |
| `PoliticaDesconto.calcular` | 21 | 31 | 12 |
| `CalculadoraFrete.calcular` | 19 | 28 | 11 |
| `AnaliseRisco.avaliar` | 13 | 19 | 8 |
| `PedidoService.fechar` | 18 | 24 | 8 |
| `PagamentoService.pagar` | 13 | 18 | 7 |

O relatório traz ainda a alteração proposital exigida pelo roteiro (`>=` trocado por `>`
em `PoliticaDesconto`), os dois testes que a detectaram e a confirmação de que a alteração
foi desfeita.

---

### 4. Exercícios sobre Grafo de Fluxo de Controle

[`SEMANA07_GFC/respostas_exercicios_gfc.md`](SEMANA07_GFC/respostas_exercicios_gfc.md)

Os dois exercícios resolvidos com blocos básicos, decisões, grafo (diagrama Mermaid e
versão em texto), contagem de nós e arestas, complexidade ciclomática pelas duas fórmulas,
base de caminhos independentes com dados de teste e resultados esperados, além das
questões para discussão.

| Exercício | N | E | V(G) = E − N + 2 | decisões + 1 |
| --- | --- | --- | --- | --- |
| 1 — `classificarPedido` | 8 | 10 | 4 | 4 |
| 2 — `contarAlertas` | 9 | 11 | 4 | 4 |

Os resultados esperados de cada caminho foram conferidos executando os dois métodos.

---

## Atividades da Avaliação Prática — Prova 02

| # | Atividade | Pasta | Estado |
| --- | --- | --- | --- |
| 5 | SEMANA 10 — TDD nos quatro projetos | [`SEMANA10_TDD/`](SEMANA10_TDD) | Concluída — 27 testes |
| 6 | SEMANA 11 — testes de integração com Spring Boot e MockMvc | [`SEMANA11_teste_integracao_springboot/`](SEMANA11_teste_integracao_springboot) | Concluída — 5 testes |
| 7 | SEMANA 14 — teste de usuário final | [`SEMANA14_teste_usuario/`](SEMANA14_teste_usuario) | Concluída — 8 tarefas, 6 achados |

---

### 5. TDD — Test Driven Development

Quatro projetos Maven independentes. Detalhamento completo em
[`SEMANA10_TDD/README.md`](SEMANA10_TDD/README.md).

| Projeto | Testes | O que faltava no projeto-base |
| --- | ---: | --- |
| `bubblesort-tdd` | 3 | `ordenar()` vazio |
| `conta-bancaria-tdd` | 6 | `depositar` / `sacar` vazios |
| `estoque-tdd` | 7 | `adicionarEstoque` / `retirarEstoque` vazios |
| `carrinho-compras-tdd` | 11 | classe e testes vazios — ciclo TDD completo |
| **Total** | **27** | |

```bash
cd SEMANA10_TDD/<projeto> && mvn test
```

Pontos registrados no relatório da pasta:

- **O RED expôs três falsos positivos** nos projetos-base: `deveManterVetorJaOrdenado`,
  `produtoDeveIniciarComEstoqueZero` e `naoDeveAdicionarQuantidadeNegativa` passavam
  mesmo com os métodos vazios, porque esperavam exatamente o estado inicial.
- **O carrinho foi construído requisito por requisito**, com o RED de cada um registrado
  — de erro de compilação (`cannot find symbol: method getTotal()`) a falha de asserção
  (`expected: <540.0> but was: <600.0>`).
- **Requisito 5 do carrinho** (preço negativo), deixado em aberto pelo enunciado, foi
  resolvido com `IllegalArgumentException` em vez de silêncio; a justificativa está no
  README da pasta.
- **Fronteiras do desconto** testadas em `499,99` / `500,00` / `500,01`.
- Três testes foram **acrescentados** para cobrir valores negativos que os casos
  fornecidos não especificavam. Nenhum caso fornecido foi alterado.

---

### 6. Testes de integração com Spring Boot, JUnit 5 e MockMvc

Atividade avaliada em 10,0 pontos. Relatório completo, com as duas discussões do
enunciado respondidas, em
[`exercicios-integracao/README.md`](SEMANA11_teste_integracao_springboot/exercicios-integracao/README.md).

```bash
cd SEMANA11_teste_integracao_springboot/exercicios-integracao && mvn test
```

| Caso | Camadas exercitadas | Verificação |
| --- | --- | --- |
| CT01 | Service → Repository → H2 | cadastro de `Teclado`/150,00 recebe ID e é relido do banco |
| CT02 | Service → Repository → H2 | nome em branco lança `IllegalArgumentException` e não persiste |
| CT03 | Service → Repository → H2 | atualização de preço para 95,00 chega ao banco |
| CT04 | HTTP → Controller → Service → Repository → H2 | `POST /produtos` devolve 200 e o recurso criado |
| CT05 | HTTP → Controller → Service → Repository → H2 | `POST` seguido de `GET /produtos/{id}` devolve o mesmo JSON |

Resultado: **5 testes, 5 aprovados** (Java 17.0.18, Spring Boot 3.3.13, H2 em memória).
`src/main` permaneceu idêntico ao projeto-base — conferido com `diff -r`. Nenhum mock
substitui `ProdutoRepository` ou `ProdutoService`, conforme a restrição do enunciado.

Dois resultados que valem destaque:

- **A suíte do projeto-base ficava verde vazia.** Com os cinco corpos de teste contendo
  apenas comentários `TODO`, a execução reportava `Tests run: 5, Failures: 0` e
  `BUILD SUCCESS`. Um `@Test` sem asserção passa por não lançar exceção.
- **Verificação por mutação.** Como o verde sozinho não prova nada, três defeitos foram
  injetados um por vez em `ProdutoService` (validação removida, `atualizarPreco` sem
  efeito, `cadastrar` sem `save`). Os três foram detectados e o código foi restaurado.

---

### 7. Teste de usuário final

Teste de usabilidade do **Mercado Livre** nas etapas de busca, filtragem, comparação e
simulação de compra até o carrinho. Pacote completo em
[`SEMANA14_teste_usuario/`](SEMANA14_teste_usuario): plano de teste, roteiro do avaliador,
planilha de observação, questionário SUS e relatório final.

| Critério de sucesso | Meta | Resultado |
| --- | --- | --- |
| Participantes que adicionam ao carrinho | ≥ 90% | 100% (5/5) |
| Tempo médio total | ≤ 4 min | 3 min 12 s |
| Escore SUS médio | ≥ 68 | 69,0 — aceitável |

Seis achados de usabilidade classificados por severidade e heurística de Nielsen. As duas
tarefas de filtragem (filtro Full e ordenação por avaliação) concentram **62% dos erros e 44%
do tempo**, e são as únicas com insucesso — o fluxo de compra em si teve 100% de sucesso.

O achado de maior severidade foi observado na execução-piloto real: a busca feita sem conta
conectada foi interrompida por uma tela de verificação (`/gz/account-verification`), com os
trace-ids registrados no relatório.

O escore SUS é calculado por [`sus/sus.py`](SEMANA14_teste_usuario/sus/sus.py), escrito por
TDD e coberto por 6 testes:

```bash
cd SEMANA14_teste_usuario && python sus/sus.py 04-respostas-sus.csv
cd SEMANA14_teste_usuario/sus && python test_sus.py
```

As sessões P1–P5 da planilha são **dados simulados para fins didáticos**, identificados na
coluna `origem` do CSV e declarados no cabeçalho do relatório; a execução-piloto é marcada
como `piloto-real`.

---

## Aula 02

`aula02/` — primeiro exercício de teste automatizado da disciplina (`CalculadoraFrete` com JUnit).
