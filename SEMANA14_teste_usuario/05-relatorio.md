# Relatório do Teste de Usuário Final — Mercado Livre

**Semana 14 — Projeto, Implementação e Teste de Software — 2026**
**Autores:** Samuel Fuentes Michels (RA 24011114-2), Felipe de Almeida Matrone (RA 24051005-2)
**Data da aplicação:** 26/11/2026
**Sistema sob teste:** <https://www.mercadolivre.com.br> (produção)

> ## ⚠️ Origem dos dados
>
> Este relatório combina duas origens, identificadas na coluna `origem` de
> `03-planilha-observacao.csv`:
>
> - **`piloto-real`** — execução-piloto instrumentada do roteiro, feita no navegador em
>   26/11/2026. Produziu o achado **A1**, com evidência registrada (URL e trace-id).
> - **`simulado`** — as sessões **P1 a P5 são dados simulados para fins didáticos**, usados
>   para demonstrar a consolidação das métricas e a análise. **Não** são coleta de campo com
>   participantes reais. Para uso real, basta substituir essas linhas pelos dados das sessões
>   e recalcular.

---

## 1. Resumo executivo

Foram avaliadas 8 tarefas do fluxo de busca → filtragem → comparação → carrinho → checkout.
Os três critérios de sucesso definidos no plano foram atendidos, mas **a etapa de filtragem
concentra 62% de todos os erros observados** e é o principal gargalo de usabilidade do fluxo.

| Critério | Meta | Resultado | Situação |
| --- | --- | --- | --- |
| Participantes que adicionam ao carrinho (T5) | ≥ 90% | 100% (5/5) | ✅ Atendido |
| Tempo médio total | ≤ 4 min | 3 min 12 s | ✅ Atendido |
| Escore SUS médio | ≥ 68 | 69,0 | ⚠️ Atendido no limite |

## 2. Resultados por tarefa

| Tarefa | Tempo médio | Taxa de sucesso | Erros |
| --- | --- | --- | --- |
| T1 — Acessar site | 7,0 s | 100% (5/5) | 0 |
| T2 — Buscar produto | 14,8 s | 100% (5/5) | 1 |
| **T3a — Filtrar entrega Full** | **41,4 s** | **80% (4/5)** | **8** |
| **T3b — Ordenar por melhores avaliados** | **43,4 s** | **60% (3/5)** | **8** |
| T4 — Abrir detalhes | 25,4 s | 100% (5/5) | 1 |
| T5 — Adicionar ao carrinho | 18,4 s | 100% (5/5) | 3 |
| T6 — Iniciar a compra | 30,6 s | 100% (5/5) | 4 |
| T7 — Voltar à tela inicial | 11,0 s | 100% (5/5) | 1 |
| **Total** | **3 min 12 s** | — | **26** |

### Tempo total por participante

| Participante | Tempo | Observação |
| --- | --- | --- |
| P1 | 3 min 11 s | Desistiu de T3b |
| P2 | 2 min 54 s | Concluiu todas as tarefas |
| P3 | 4 min 06 s | Único acima da meta de 4 min |
| P4 | 2 min 00 s | Experiência prévia no site |
| P5 | 3 min 49 s | Desistiu de T3b |

As duas tarefas de filtro custam **84,8 s**, ou **44% do tempo total**, embora sejam 2 das 8
tarefas. Esse é o resultado mais relevante do teste.

## 3. Satisfação — System Usability Scale (SUS)

Calculado por `sus/sus.py` a partir de `04-respostas-sus.csv`:

| Participante | Escore | Classificação (Bangor et al., 2009) |
| --- | --- | --- |
| P1 | 70,0 | aceitável |
| P2 | 82,5 | bom |
| P3 | 47,5 | ruim |
| P4 | 100,0 | excelente |
| P5 | 45,0 | ruim |
| **Média** | **69,0** | **aceitável** |

A média fica logo acima da referência de mercado (68), mas a **dispersão é alta**: os dois
participantes que não concluíram T3b deram os escores mais baixos (45,0 e 47,5). A
insatisfação acompanha exatamente a tarefa com menor taxa de sucesso.

Para reproduzir o cálculo:

```
python sus/sus.py 04-respostas-sus.csv
```

## 4. Achados de usabilidade

Cada achado é classificado por **severidade** (1 = cosmético, 2 = menor, 3 = maior,
4 = catastrófico) e pela heurística de **Nielsen** violada.

### A1 — Busca de visitante interrompida por verificação de conta · severidade 4 · *(piloto-real)*

Ao submeter a busca sem estar autenticado, o site redirecionou para
`/gz/account-verification` com a mensagem *"Olá! Para continuar, acesse sua conta"*,
oferecendo apenas "Sou novo" e "Já tenho conta". O acesso direto à URL da listagem
(`lista.mercadolivre.com.br/fone-de-ouvido-bluetooth`) sofreu o mesmo redirecionamento.
Trace-ids registrados: `88073487-65da-4f3b-96db-1e149443a24f` e
`ebb348ad-ed94-4887-a19e-9f01ae262f44`.

- **Heurística violada:** *liberdade e controle do usuário* — não há saída para quem só quer
  pesquisar; e *flexibilidade e eficiência de uso* — a função primária do site fica atrás de
  um portão.
- **Impacto:** bloqueia 6 das 8 tarefas do roteiro. Um visitante que só quer comparar preços
  é empurrado para o cadastro antes de ver qualquer resultado.
- **Ressalva de interpretação:** esse redirecionamento é provavelmente um mecanismo
  antiautomação, e não uma regra de negócio aplicada a todo visitante — usuários humanos
  costumam pesquisar sem conta. O achado é registrado como **comportamento observado**, com
  essa ressalva explícita, e não como afirmação sobre a política do site.
- **Recomendação:** permitir a listagem de resultados em modo visitante, pedindo conta apenas
  no checkout; se a verificação for necessária, explicar o motivo e oferecer "continuar sem
  conta".

### A2 — Filtro "Full" não é encontrado · severidade 3 · *(simulado)*

4 de 5 participantes procuraram o filtro fora da coluna lateral; dois clicaram em banners de
frete achando que eram o filtro. Um aplicou "Frete grátis" no lugar de "Full" e **não
percebeu o erro** — concluiu a tarefa com o filtro errado aplicado.

- **Heurística violada:** *correspondência entre o sistema e o mundo real* — "Full" é
  vocabulário interno da empresa, não do comprador; e *reconhecer em vez de lembrar*.
- **Recomendação:** rotular como "Entrega rápida (Full)" com explicação ao passar o mouse, e
  promover o filtro para um chip no topo da lista, junto dos resultados.

### A3 — Ordenação por avaliação não é descoberta · severidade 3 · *(simulado)*

Tarefa com a pior taxa de sucesso (60%). Dois participantes desistiram. Um tentou ordenar
clicando nas estrelas de um anúncio — comportamento que revela o modelo mental esperado:
*a avaliação é o controle*.

- **Heurística violada:** *visibilidade do estado do sistema* — o seletor "Mais relevantes"
  não comunica que ali também se ordena por avaliação.
- **Recomendação:** expor "Melhor avaliados" como opção visível ao lado do seletor, e tornar
  clicável a faixa de estrelas no card do produto.

### A4 — "Comprar agora" confundido com "Adicionar ao carrinho" · severidade 2 · *(simulado)*

3 de 5 participantes clicaram primeiro em "Comprar agora" e voltaram pelo botão do navegador.

- **Heurística violada:** *prevenção de erros* — os dois botões têm peso visual semelhante e
  consequências muito diferentes (um vai direto ao checkout).
- **Recomendação:** diferenciar a hierarquia visual e permitir voltar do checkout ao anúncio
  sem usar o botão do navegador.

### A5 — Carrinho procurado no lugar errado · severidade 2 · *(simulado)*

2 participantes procuraram o carrinho no menu lateral antes de achar o ícone no topo direito.

- **Heurística violada:** *consistência e padrões*.
- **Recomendação:** feedback visível ao adicionar — animação apontando o ícone, ou painel
  lateral com o item recém-adicionado.

### A6 — Anúncio patrocinado confundido com resultado orgânico · severidade 2 · *(simulado)*

Um participante abriu um patrocinado achando que era o primeiro resultado da ordenação que
havia acabado de aplicar.

- **Heurística violada:** *visibilidade do estado do sistema* — a ordenação aplicada não vale
  para os patrocinados, e isso não é comunicado.
- **Recomendação:** marcação mais evidente e separação visual dos patrocinados.

## 5. Priorização

| Achado | Severidade | Frequência | Prioridade |
| --- | --- | --- | --- |
| A1 — verificação de conta na busca | 4 | 1/1 no piloto | **1** |
| A3 — ordenação não descoberta | 3 | 4/5 | **2** |
| A2 — filtro Full não encontrado | 3 | 4/5 | **3** |
| A4 — Comprar agora vs. carrinho | 2 | 3/5 | 4 |
| A5 — localização do carrinho | 2 | 2/5 | 5 |
| A6 — patrocinado vs. orgânico | 2 | 1/5 | 6 |

## 6. Conclusão

O fluxo **principal de compra funciona**: 100% dos participantes chegaram ao carrinho e ao
checkout, dentro da meta de tempo. O problema não está em comprar — está em **decidir o que
comprar**. As duas tarefas de refinamento (filtrar e ordenar) respondem por 62% dos erros e
44% do tempo, e são as únicas com insucesso.

Isso aponta uma interface otimizada para a conversão de quem já sabe o que quer, e pouco
eficiente para quem está comparando. Como o cenário do teste era justamente comparar opções,
há um desalinhamento entre o que a interface facilita e o que o usuário veio fazer.

A recomendação de maior retorno é tornar **ordenação e filtros descobríveis no topo da lista
de resultados** (A2 e A3): atacam as duas tarefas de pior desempenho e explicam os escores
SUS mais baixos.

## 7. Ameaças à validade

- **Amostra pequena e simulada.** P1–P5 são dados didáticos; nenhuma conclusão deste
  relatório deve ser tratada como evidência empírica sobre usuários reais. O que é entregável
  aqui é a estrutura de análise, não a medição.
- **Site de produção em período de Black Friday**, com banners promocionais que não estão
  presentes o ano todo — eles próprios causaram erros em A2.
- **O achado A1 é real, mas provavelmente de origem antiautomação**, conforme a ressalva na
  própria seção.

## 8. Referências

- BROOKE, J. *SUS: A quick and dirty usability scale*. 1996.
- BANGOR, A.; KORTUM, P.; MILLER, J. *Determining what individual SUS scores mean*. 2009.
- NIELSEN, J. *10 Usability Heuristics for User Interface Design*. 1994.
- NIELSEN, J.; LANDAUER, T. K. *A mathematical model of the finding of usability problems*. 1993.
