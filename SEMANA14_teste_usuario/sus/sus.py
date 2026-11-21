"""Escore SUS (System Usability Scale) — Brooke, 1996.

10 itens em escala Likert de 1 a 5. Itens ímpares são afirmações positivas
e contribuem com (resposta - 1); itens pares são negativas e contribuem com
(5 - resposta). A soma vai de 0 a 40 e é multiplicada por 2,5 para dar 0..100.
"""


def escore_sus(respostas):
    if len(respostas) != 10:
        raise ValueError("o SUS tem exatamente 10 itens")
    if any(r not in (1, 2, 3, 4, 5) for r in respostas):
        raise ValueError("cada resposta deve estar entre 1 e 5")
    soma = sum(
        r - 1 if i % 2 == 0 else 5 - r
        for i, r in enumerate(respostas)
    )
    return soma * 2.5


def classificacao(escore):
    """Faixas de Bangor et al. (2009), usadas para interpretar o escore."""
    if escore >= 85:
        return "excelente"
    if escore >= 72:
        return "bom"
    if escore >= 52:
        return "aceitável (OK)"
    if escore >= 39:
        return "ruim"
    return "inaceitável"


if __name__ == "__main__":
    import csv
    import sys

    if len(sys.argv) != 2:
        print("uso: python sus.py <respostas-sus.csv>")
        raise SystemExit(2)

    with open(sys.argv[1], encoding="utf-8") as f:
        linhas = [l for l in csv.DictReader(f) if l.get("participante")]

    escores = []
    for linha in linhas:
        respostas = [int(linha[f"q{n}"]) for n in range(1, 11)]
        e = escore_sus(respostas)
        escores.append(e)
        print(f"{linha['participante']}: {e:.1f} ({classificacao(e)})")

    if escores:
        media = sum(escores) / len(escores)
        print(f"\nmédia ({len(escores)} participantes): {media:.1f} ({classificacao(media)})")
