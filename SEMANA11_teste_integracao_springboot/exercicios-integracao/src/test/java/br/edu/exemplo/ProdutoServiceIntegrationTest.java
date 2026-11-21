package br.edu.exemplo;

import br.edu.exemplo.model.Produto;
import br.edu.exemplo.repository.ProdutoRepository;
import br.edu.exemplo.service.ProdutoService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Exercicio 1 - integracao entre ProdutoService, ProdutoRepository e o banco H2.
 *
 * Nenhuma dependencia e substituida por mock: o service recebe o repository real
 * e o repository grava no H2 em memoria configurado em application.properties.
 *
 * Sobre o uso de entityManager.flush() + clear(): dentro de uma mesma transacao o
 * Hibernate mantem um cache de primeiro nivel (persistence context). Sem limpa-lo,
 * repository.findById() poderia devolver a mesma instancia que ainda esta em memoria,
 * sem gerar SELECT algum - a consulta pareceria provar persistencia sem prova-la.
 * O flush() forca o INSERT/UPDATE e o clear() desanexa as entidades, obrigando a
 * leitura seguinte a buscar de fato no banco.
 */
@SpringBootTest
@Transactional
class ProdutoServiceIntegrationTest {

    @Autowired
    ProdutoService service;

    @Autowired
    ProdutoRepository repository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("CT01 - cadastro valido recebe ID e e encontrado no repositorio")
    void deveCadastrarERecuperarProduto() {
        Produto salvo = service.cadastrar("Teclado", 150.0);

        assertNotNull(salvo.getId(), "o produto deve receber o ID gerado pelo banco");

        entityManager.flush();
        entityManager.clear();

        Produto recuperado = repository.findById(salvo.getId())
                .orElseThrow(() -> new AssertionError("produto nao encontrado no repositorio"));

        assertEquals("Teclado", recuperado.getNome());
        assertEquals(150.0, recuperado.getPreco(), 0.001);
    }

    @Test
    @DisplayName("CT02 - nome em branco lanca IllegalArgumentException e nao persiste")
    void naoDevePersistirProdutoComNomeEmBranco() {
        long registrosAntes = repository.count();

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar("   ", 80.0),
                "nome em branco deveria ser rejeitado pela regra do service");

        assertTrue(erro.getMessage().toLowerCase().contains("nome"),
                "a mensagem deve apontar o campo recusado, veio: " + erro.getMessage());

        entityManager.flush();

        assertEquals(registrosAntes, repository.count(),
                "a tentativa invalida nao deve ter gerado registro no banco");
    }

    @Test
    @DisplayName("CT03 (desafio) - atualizacao de preco e persistida no banco")
    void deveAtualizarPrecoPersistido() {
        Long id = service.cadastrar("Mouse", 80.0).getId();

        entityManager.flush();
        entityManager.clear();

        service.atualizarPreco(id, 95.0);

        entityManager.flush();
        entityManager.clear();

        Produto recuperado = repository.findById(id)
                .orElseThrow(() -> new AssertionError("produto nao encontrado apos a atualizacao"));

        assertEquals(95.0, recuperado.getPreco(), 0.001);
        assertEquals("Mouse", recuperado.getNome(), "a atualizacao nao deve alterar o nome");
        assertTrue(repository.existsById(id));
    }
}
