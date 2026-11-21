package br.edu.exemplo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Exercicio 2 - integracao da camada HTTP: Controller -> Service -> Repository -> H2.
 *
 * MockMvc executa o DispatcherServlet de verdade (roteamento, desserializacao do
 * corpo JSON, conversao da resposta) sem subir um servidor Tomcat em uma porta.
 * Nenhum componente e substituido por mock: a requisicao atravessa toda a pilha
 * e termina no banco H2 em memoria.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProdutoControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("CT04 - POST /produtos responde 200 com o recurso criado")
    void deveCadastrarProdutoPelaApi() throws Exception {
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Mouse\",\"preco\":80.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Mouse"))
                .andExpect(jsonPath("$.preco").value(80.0));
    }

    @Test
    @DisplayName("CT05 - GET /produtos/{id} devolve o produto cadastrado pelo POST")
    void deveBuscarProdutoPelaApi() throws Exception {
        MvcResult cadastro = mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Monitor\",\"preco\":1200.0}"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode criado = objectMapper.readTree(cadastro.getResponse().getContentAsString());
        long id = criado.get("id").asLong();
        assertTrue(id > 0, "o POST deve devolver o ID gerado pelo banco");

        MvcResult consulta = mockMvc.perform(get("/produtos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Monitor"))
                .andExpect(jsonPath("$.preco").value(1200.0))
                .andReturn();

        JsonNode consultado = objectMapper.readTree(consulta.getResponse().getContentAsString());
        assertEquals(criado, consultado,
                "o JSON da consulta deve corresponder ao do cadastro, inclusive o ID");
    }
}
