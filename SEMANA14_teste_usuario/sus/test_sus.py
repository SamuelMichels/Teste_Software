"""Testes da calculadora de escore SUS (System Usability Scale)."""
from sus import escore_sus

def test_respostas_mais_positivas_possiveis_dao_100():
    # Itens ímpares (positivos) = 5, pares (negativos) = 1
    assert escore_sus([5, 1, 5, 1, 5, 1, 5, 1, 5, 1]) == 100.0

def test_respostas_mais_negativas_possiveis_dao_0():
    assert escore_sus([1, 5, 1, 5, 1, 5, 1, 5, 1, 5]) == 0.0

def test_tudo_neutro_da_50():
    assert escore_sus([3] * 10) == 50.0

def test_caso_real_do_artigo_de_brooke():
    # 4,2,5,2,4,2,4,2,4,2 -> ímpares: 3+4+3+3+3=16 ; pares: 3+3+3+3+3=15 ; (31)*2.5
    assert escore_sus([4, 2, 5, 2, 4, 2, 4, 2, 4, 2]) == 77.5

def test_rejeita_quantidade_errada_de_respostas():
    try:
        escore_sus([5] * 9)
    except ValueError:
        return
    raise AssertionError("deveria ter recusado 9 respostas")

def test_rejeita_resposta_fora_da_escala():
    try:
        escore_sus([6] + [3] * 9)
    except ValueError:
        return
    raise AssertionError("deveria ter recusado resposta 6")

if __name__ == "__main__":
    for nome, fn in sorted(globals().items()):
        if nome.startswith("test_"):
            fn()
            print("ok:", nome)
    print("todos os testes passaram")
