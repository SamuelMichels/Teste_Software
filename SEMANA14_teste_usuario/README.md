# SEMANA 14 — Teste de Usuário Final

Atividade da Avaliação Prática da Prova 02 — Projeto, Implementação e Teste de Software, 2026.

**Enunciado:** executar o teste de usuário para o problema proposto — avaliar facilidade de
uso, eficiência e clareza da interface do **Mercado Livre** nas etapas de busca, comparação e
simulação de compra, aplicando conceitos de IHC e de teste de usabilidade.

**Integrantes:** Samuel Fuentes Michels (RA 24011114-2) · Felipe de Almeida Matrone (RA 24051005-2)

## Conteúdo

| Arquivo | O que é |
| --- | --- |
| [`01-plano-de-teste.md`](01-plano-de-teste.md) | Plano completo: objetivo, escopo, cenário, tarefas, métricas, critérios de sucesso, papéis, procedimento, riscos e consentimento |
| [`02-roteiro-do-avaliador.md`](02-roteiro-do-avaliador.md) | Script palavra por palavra da sessão, regra de parada e checklist |
| [`03-planilha-observacao.csv`](03-planilha-observacao.csv) | Dados coletados: tempo, sucesso e erros por tarefa e por participante |
| [`04-questionario-sus.md`](04-questionario-sus.md) | Questionário SUS de 10 itens e como interpretar o escore |
| [`04-respostas-sus.csv`](04-respostas-sus.csv) | Respostas do SUS, uma linha por participante |
| [`05-relatorio.md`](05-relatorio.md) | **Relatório final**: resultados, achados de usabilidade, priorização e conclusão |
| [`sus/sus.py`](sus/sus.py) | Calculadora do escore SUS |
| [`sus/test_sus.py`](sus/test_sus.py) | Testes da calculadora (6 casos, escritos antes da implementação) |

## Resultados

| Critério | Meta | Resultado |
| --- | --- | --- |
| Participantes que adicionam ao carrinho | ≥ 90% | 100% (5/5) |
| Tempo médio total | ≤ 4 min | 3 min 12 s |
| Escore SUS médio | ≥ 68 | 69,0 — aceitável |

Seis achados de usabilidade, dois deles de severidade alta: **filtro "Full" não é encontrado**
e **ordenação por avaliação não é descoberta**. As duas tarefas de filtragem concentram 62%
dos erros e 44% do tempo total.

## Origem dos dados

A coluna `origem` de `03-planilha-observacao.csv` separa as duas procedências:

- **`piloto-real`** — execução-piloto do roteiro feita no navegador em 26/11/2026. Deu origem
  ao achado A1, com URL e trace-ids registrados.
- **`simulado`** — as sessões P1 a P5 são **dados simulados para fins didáticos**, usados para
  demonstrar a consolidação das métricas e a análise. Não são coleta com participantes reais.

Para aplicar o teste de verdade, basta substituir as linhas `simulado` pelos dados das sessões
e rodar o cálculo de novo — nada mais muda.

## Como reproduzir

Calcular os escores SUS a partir das respostas:

```
python sus/sus.py 04-respostas-sus.csv
```

Rodar os testes da calculadora:

```
cd sus && python test_sus.py
```
