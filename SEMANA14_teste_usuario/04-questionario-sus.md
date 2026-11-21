# Questionário SUS — System Usability Scale

Aplicado **ao final** da sessão, depois de todas as tarefas. O participante responde sozinho,
sem comentários do avaliador, e deve marcar uma resposta para **todas** as afirmações — se
estiver em dúvida, marca o ponto central (3).

**Escala:** 1 = discordo totalmente · 2 · 3 · 4 · 5 = concordo totalmente

---

Pensando no site do Mercado Livre que você acabou de usar:

| # | Afirmação | 1 | 2 | 3 | 4 | 5 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | Eu usaria esse site com frequência. | ☐ | ☐ | ☐ | ☐ | ☐ |
| 2 | Achei o site desnecessariamente complexo. | ☐ | ☐ | ☐ | ☐ | ☐ |
| 3 | Achei o site fácil de usar. | ☐ | ☐ | ☐ | ☐ | ☐ |
| 4 | Precisaria de ajuda de alguém para conseguir usar esse site. | ☐ | ☐ | ☐ | ☐ | ☐ |
| 5 | As funções do site estão bem integradas entre si. | ☐ | ☐ | ☐ | ☐ | ☐ |
| 6 | Achei o site inconsistente — as coisas funcionam de jeitos diferentes. | ☐ | ☐ | ☐ | ☐ | ☐ |
| 7 | A maioria das pessoas aprenderia a usar esse site rapidamente. | ☐ | ☐ | ☐ | ☐ | ☐ |
| 8 | Achei o site trabalhoso de usar. | ☐ | ☐ | ☐ | ☐ | ☐ |
| 9 | Me senti confiante usando o site. | ☐ | ☐ | ☐ | ☐ | ☐ |
| 10 | Precisei aprender muita coisa antes de conseguir usar o site. | ☐ | ☐ | ☐ | ☐ | ☐ |

As afirmações **ímpares são positivas** e as **pares são negativas** — essa alternância é
proposital: evita que o participante marque tudo na mesma coluna sem ler.

### Perguntas abertas (anotar a resposta literal)

1. O que mais te atrapalhou nesse site?
2. Se você pudesse mudar uma coisa, o que seria?

---

## Como calcular o escore

Para cada participante:

1. **Itens ímpares** (1, 3, 5, 7, 9): contribuição = `resposta − 1`
2. **Itens pares** (2, 4, 6, 8, 10): contribuição = `5 − resposta`
3. Some as 10 contribuições → valor de 0 a 40
4. Multiplique por **2,5** → escore final de **0 a 100**

O escore SUS **não é uma porcentagem**: 70 não quer dizer "70% de satisfação". É uma posição
numa escala comparativa.

| Faixa | Interpretação (Bangor et al., 2009) |
| --- | --- |
| 85–100 | Excelente |
| 72–84 | Bom |
| 52–71 | Aceitável (OK) |
| 39–51 | Ruim |
| 0–38 | Inaceitável |

A **média de mercado é 68** — abaixo disso, o sistema está pior que a maioria dos avaliados.

### Cálculo automatizado

Transfira as respostas para `04-respostas-sus.csv` (uma linha por participante, colunas
`q1` a `q10`) e rode:

```
python sus/sus.py 04-respostas-sus.csv
```

O script imprime o escore e a classificação de cada participante e a média do grupo. Ele é
coberto por testes em `sus/test_sus.py`:

```
cd sus && python test_sus.py
```
