# Plano de Teste de Usuário Final — Mercado Livre

**Disciplina:** Projeto, Implementação e Teste de Software — 2026
**Semana 14 — Teste de usuário final**
**Sistema sob teste:** site do Mercado Livre Brasil — <https://www.mercadolivre.com.br>
**Versão avaliada:** site de produção, acessado em 26/11/2026
**Responsáveis:** Samuel Fuentes Michels (RA 24011114-2), Felipe de Almeida Matrone (RA 24051005-2)

---

## 1. Objetivo do teste

Avaliar a **facilidade de uso, a eficiência e a clareza da interface** do Mercado Livre
durante as etapas de **busca, filtragem, comparação e simulação de compra** de um produto,
aplicando conceitos de IHC e de teste de usabilidade.

O teste é um **teste de usuário final** (também chamado de teste de usuário real): usuários
reais executam tarefas pré-definidas em um ambiente de produção, enquanto um avaliador
observa e registra o comportamento, sem interferir.

### 1.1 O que este teste **não** é

- Não é teste de aceitação do cliente — não há requisitos contratuais sendo verificados.
- Não é teste funcional — não se busca defeito de código, e sim dificuldade de uso.
- Não há acesso ao código-fonte: é um teste **caixa-preta**, pela interface.

## 2. Escopo

| Dentro do escopo | Fora do escopo |
| --- | --- |
| Busca por produto | Finalização de pagamento |
| Aplicação de filtros (Full, melhores avaliados) | Cadastro de conta nova |
| Leitura da página de detalhes do produto | Avaliação do app mobile |
| Adição ao carrinho | Pós-venda, rastreio, devolução |
| Início do fluxo de compra (checkout) | Desempenho de rede / carga |
| Retorno à tela inicial | Acessibilidade formal (WCAG) |

## 3. Cenário de uso apresentado ao participante

> Você está procurando um fone de ouvido sem fio para uso diário e quer comprar pelo
> Mercado Livre. Seu objetivo é encontrar um modelo com boa avaliação, comparar opções e
> simular a compra até o carrinho — **sem finalizar o pagamento**.

## 4. Perfil dos participantes

- **Quantidade:** 5 participantes (número suficiente para revelar cerca de 85% dos problemas
  de usabilidade, conforme Nielsen & Landauer, 1993).
- **Perfil:** pessoas que já compram on-line, com e sem experiência prévia no Mercado Livre.
- **Critério de exclusão:** quem participou da construção deste plano de teste.
- Os participantes **não** recebem treinamento prévio nem explicação da interface.

## 5. Tarefas a serem executadas

| # | Tarefa | Critério objetivo de conclusão |
| --- | --- | --- |
| T1 | Acessar o site <https://www.mercadolivre.com.br> | A página inicial é exibida |
| T2 | Pesquisar "fone de ouvido Bluetooth" | A lista de resultados do termo é exibida |
| T3a | Filtrar apenas produtos com **entrega Full** | O chip/filtro "Full" aparece aplicado |
| T3b | Ordenar pelos **melhores avaliados** | A ordenação exibida é a de melhor avaliação |
| T4 | Escolher um produto e abrir os detalhes | A página do anúncio é exibida |
| T5 | Adicionar o item ao carrinho | O carrinho passa a exibir 1 item |
| T6 | Iniciar a compra **sem concluir o pagamento** | A tela de checkout é exibida |
| T7 | Retornar à tela inicial | A home do Mercado Livre é exibida |

**Tempo máximo por tarefa:** 2 minutos. Esgotado o tempo, a tarefa é registrada como
**insucesso** e o avaliador libera o participante para a tarefa seguinte.

## 6. Métricas coletadas

| Métrica | Definição operacional | Como medir |
| --- | --- | --- |
| **Tempo de execução** | Segundos entre o avaliador dizer "pode começar" e o critério de conclusão da tarefa ser atingido | Cronômetro, por tarefa e total |
| **Taxa de sucesso** | Percentual de participantes que concluíram a tarefa **sem ajuda** | Contagem de S/N por tarefa |
| **Erros cometidos** | Clique em elemento que não leva ao objetivo, uso de filtro errado, perda do caminho (voltar/recomeçar), abandono de tela | Contagem por tarefa + descrição |
| **Satisfação (SUS)** | System Usability Scale, 10 itens, escore 0–100 | Questionário ao final (ver `04-questionario-sus.md`) |

### 6.1 O que conta como erro

Conta como erro: clicar em banner/publicidade achando que é resultado de busca; aplicar
filtro de categoria em vez do filtro pedido; usar o botão "voltar" do navegador por ter se
perdido; procurar o carrinho em lugar errado; confundir "Comprar agora" com "Adicionar ao
carrinho".

Não conta como erro: rolar a página, ler descrição de produto, comparar dois anúncios —
isso é o comportamento esperado da tarefa.

## 7. Critérios de sucesso do teste

| Critério | Meta | Resultado |
| --- | --- | --- |
| Participantes que adicionam o produto ao carrinho (T5) | ≥ 90% | *(preencher)* |
| Tempo médio para concluir todas as tarefas | ≤ 4 minutos | *(preencher)* |
| Escore SUS médio | ≥ 68 (média de mercado) | *(preencher)* |

## 8. Papéis

| Papel | Quem | Responsabilidade |
| --- | --- | --- |
| **Usuário final** | Participante convidado | Executa as tarefas como comprador, pensando em voz alta |
| **Avaliador** | Colega da dupla | Lê o roteiro, cronometra, registra tempo, erros e comentários — **não ajuda** |
| **Instrutor** | Prof. João Choma | Orienta e supervisiona a aplicação do teste |

## 9. Ferramentas

- Navegador web (Chrome, Firefox ou Edge) em janela anônima, para não haver histórico ou
  recomendações personalizadas influenciando o resultado.
- Cronômetro (celular).
- Planilha de observação: `03-planilha-observacao.csv`.
- Questionário SUS: `04-questionario-sus.md`; cálculo do escore com `sus/sus.py`.

## 10. Procedimento

**Antes da sessão**
1. Abrir o navegador em janela anônima, sem conta conectada.
2. Conferir que o cronômetro está zerado e a planilha em branco.
3. Ler o termo de consentimento (seção 12) e obter o aceite verbal.

**Durante a sessão**
4. Ler o cenário (seção 3) em voz alta, sem explicar a interface.
5. Ler uma tarefa por vez; iniciar o cronômetro ao dizer "pode começar".
6. Registrar tempo, sucesso e erros de cada tarefa **enquanto** o participante age.
7. Não responder a perguntas sobre como usar o site. Resposta padrão:
   *"Faça o que você faria se estivesse sozinho."*

**Depois da sessão**
8. Aplicar o questionário SUS.
9. Fazer duas perguntas abertas: *o que mais atrapalhou?* e *o que você mudaria?*
10. Consolidar os dados na planilha e calcular o escore SUS.

## 11. Riscos e ameaças à validade

| Risco | Impacto | Mitigação |
| --- | --- | --- |
| Site de produção muda durante o período de teste (promoções, Black Friday) | Resultados não comparáveis entre participantes | Rodar todas as sessões no mesmo dia e registrar a data |
| Participante já conhece bem o Mercado Livre | Tempos artificialmente baixos | Registrar a experiência prévia e analisar os grupos separados |
| Avaliador ajuda sem perceber | Taxa de sucesso inflada | Resposta padrão da seção 10, item 7 |
| Barreira de verificação de conta no site | Tarefa bloqueada | Registrar como insucesso e descrever (ver `05-relatorio.md`, achado A1) |
| Amostra pequena (5 pessoas) | Resultado não é estatisticamente generalizável | Tratar como estudo qualitativo: o objetivo é achar problemas, não estimar proporções |

## 12. Ética e consentimento

- A participação é voluntária e pode ser interrompida a qualquer momento.
- **Nenhum pagamento é concluído** e **nenhum dado real de cartão, CPF ou endereço é
  digitado** em qualquer etapa.
- O que é avaliado é o site, nunca o participante: não existe resposta errada.
- Os dados coletados são anonimizados (P1, P2, …) e usados apenas para a disciplina.
