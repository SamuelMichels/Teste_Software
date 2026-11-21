# SEMANA 11 — Testes de integração com Spring Boot, JUnit 5 e MockMvc

**Disciplina:** Projeto, Implementação e Teste de Software — 2026

**Integrantes:**
- Samuel Fuentes Michels — RA 24011114-2
- Felipe de Almeida Matrone — RA 24051005-2

O enunciado original da atividade está preservado em [ENUNCIADO.md](ENUNCIADO.md).

## Como executar

```bash
cd SEMANA11_teste_integracao_springboot/exercicios-integracao
mvn test
```

Requer JDK 17 (o projeto declara `java.version=17`). Nenhuma configuração extra: o banco é um H2 em memória criado e destruído pela própria suíte (`jdbc:h2:mem:produtos`, `ddl-auto=create-drop`).

## O que foi implementado

Somente os arquivos de teste. O código de produção (`src/main`) é **byte a byte idêntico** ao projeto-base do professor — conferido com `diff -r src/main <projeto-base>/src/main`, que não reportou diferença alguma. Nenhum mock substitui `ProdutoRepository` ou `ProdutoService`, conforme a restrição do enunciado.

| Caso | Arquivo | O que verifica |
|---|---|---|
| CT01 | `ProdutoServiceIntegrationTest` | Cadastra `Teclado`/`150.0`, exige ID gerado pelo banco e relê o registro pelo repositório conferindo nome e preço. |
| CT02 | `ProdutoServiceIntegrationTest` | Conta os registros, tenta cadastrar nome em branco, exige `IllegalArgumentException` e que a contagem não mude. |
| CT03 (desafio) | `ProdutoServiceIntegrationTest` | Cadastra `Mouse`/`80.0`, atualiza para `95.0` e relê do banco exigindo `95.0` — e o nome intacto. |
| CT04 | `ProdutoControllerIntegrationTest` | `POST /produtos` com `{"nome":"Mouse","preco":80.0}`: HTTP 200, `$.id` numérico, nome e preço no JSON. |
| CT05 | `ProdutoControllerIntegrationTest` | `POST` → extrai o ID com `ObjectMapper`/`JsonNode` → `GET /produtos/{id}`: HTTP 200 e JSON da consulta igual ao do cadastro. |

## Evidências de execução

### 1. Baseline — por que a suíte verde do projeto-base não valia nada

Antes de escrever qualquer asserção, a suíte original foi executada **com os cinco corpos de teste contendo apenas comentários `TODO`**:

```
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0 -- in br.edu.exemplo.ProdutoServiceIntegrationTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Cinco testes verdes sem uma única asserção. É o falso positivo que o próprio enunciado adverte: um método `@Test` sem asserção passa por não lançar exceção. **`BUILD SUCCESS` não é evidência de nada por si só** — é o conteúdo das asserções que decide se o resultado significa algo.

### 2. Suíte completa

```
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.375 s -- in br.edu.exemplo.ProdutoControllerIntegrationTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.821 s -- in br.edu.exemplo.ProdutoServiceIntegrationTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Executado em `Java 17.0.18`, Spring Boot 3.3.13, Hibernate ORM 6.5.3, H2 em memória.

### 3. Verificação por mutação — os testes realmente detectam defeito

Como o verde sozinho não prova nada (seção 1), três defeitos foram injetados **um por vez** em `ProdutoService`, a suíte foi executada e o código de produção restaurado em seguida:

| Mutante | Defeito injetado | Testes que mataram o mutante |
|---|---|---|
| M1 | Validação de nome em branco removida | CT02 — `Expected java.lang.IllegalArgumentException to be thrown, but nothing was thrown` |
| M2 | `atualizarPreco` não aplica o novo preço | CT03 — `expected: <95.0> but was: <80.0>` |
| M3 | `cadastrar` devolve o objeto sem chamar `repository.save` | CT01 (`expected: not <null>`), CT04 (`No value at JSON path "$.id"`), CT05 (`o POST deve devolver o ID gerado pelo banco`) |

Os três mutantes resultaram em `BUILD FAILURE`. Nenhum sobreviveu, e após a restauração a suíte voltou a `Tests run: 5, Failures: 0`.

## Discussão — Exercício 1

### Por que `repository.findById()` é uma evidência diferente de olhar o objeto devolvido pelo serviço?

O objeto devolvido por `service.cadastrar(...)` é o mesmo que o teste acabou de mandar criar: ele tem nome e preço corretos porque foi construído assim em memória, independentemente de o banco ter recebido o registro. Conferir esse objeto testa a aritmética do próprio teste. Já `repository.findById()` atravessa a fronteira da persistência: o registro só é encontrado se a entidade foi mapeada, o `INSERT` foi aceito pelo H2 e o ID foi gerado. O mutante M3 mostra isso na prática — ele mantém o objeto em memória perfeito e quebra apenas a gravação, e é a leitura pelo repositório que o denuncia.

**Com uma ressalva que importa:** dentro de uma mesma transação o Hibernate mantém um cache de primeiro nível (*persistence context*). Se o teste chamar `findById()` logo após o `save()` sem limpar esse contexto, o Hibernate pode devolver **a mesma instância que já está em memória, sem emitir SELECT algum** — a consulta pareceria provar persistência sem prová-la. Por isso CT01 e CT03 chamam `entityManager.flush()` (força o `INSERT`/`UPDATE` a ir ao banco) e `entityManager.clear()` (desanexa as entidades) antes de reler. Só assim a leitura seguinte vai de fato ao H2.

### O que `@Transactional` faz ao término de cada teste?

Em um teste do Spring, `@Transactional` abre uma transação antes do método e faz **rollback** ao final, não commit. Consequência prática: cada um dos cinco testes começa com o banco no mesmo estado em que o anterior o encontrou. Nada do que CT01 grava sobrevive para CT02, e por isso CT02 pode comparar `repository.count()` antes e depois sem depender da ordem de execução dos testes. É esse isolamento que torna a suíte repetível — rodar duas vezes seguidas dá o mesmo resultado — e que dispensa limpeza manual de tabelas entre testes. Vale notar que o `flush()` usado em CT01/CT03 envia os comandos ao banco mas **não** faz commit: o rollback ao final do teste desfaz tudo igualmente.

## Discussão — Exercício 2

### Quais componentes são efetivamente exercitados?

Toda a pilha, do HTTP ao banco: `TestDispatcherServlet` (roteamento da URL e do verbo) → desserialização do corpo JSON para o record `ProdutoRequest` pelo Jackson → `ProdutoController` → `ProdutoService` com suas validações → `ProdutoRepository`/Hibernate → H2 → serialização da entidade `Produto` de volta para JSON. CT05 ainda exercita o `@PathVariable` e a conversão de tipo do ID na URL. Os logs confirmam que o contexto real é carregado: `Initializing Spring TestDispatcherServlet` e `Found 1 JPA repository interface`.

### Por que o MockMvc não precisa de um servidor HTTP real?

Porque o `MockMvc` invoca o `DispatcherServlet` diretamente, passando objetos `MockHttpServletRequest`/`MockHttpServletResponse` em vez de trafegar bytes por um socket. A camada de servlet é exercitada de verdade; apenas o transporte TCP é dispensado. Os ganhos são concretos: nenhuma porta é aberta, não há espera por *startup* do Tomcat nem instabilidade por porta ocupada, e — decisivo aqui — a requisição corre **na mesma thread e na mesma transação do teste**, o que permite que o rollback do `@Transactional` alcance também o que foi gravado via HTTP. Com um servidor real (`WebEnvironment.RANDOM_PORT`), a requisição rodaria em outra thread, em transação própria, e o rollback do teste não a desfaria.

### Qual a diferença em relação a um teste unitário do controller com dependências simuladas?

Um teste unitário com `@MockBean ProdutoService` verifica que o controller chamou o método certo com os argumentos certos e devolveu o que o mock mandou. Ele é rápido e preciso quanto ao contrato do controller, mas é cego justamente para as falhas de integração: mapeamento JPA incorreto, coluna faltando, JSON que não desserializa no record, rota mal escrita, ID que não é gerado. O mock responde conforme programado, mesmo que o banco real recusasse a operação. O mutante M3 ilustra o limite: com `ProdutoService` mockado, CT04 continuaria verde, porque o mock devolveria um produto com ID sem precisar de banco nenhum. É exatamente por isso que o enunciado proíbe mocks nesta atividade.
