package br.com.techchallenge.foodsys.controller;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.restaurante.RestauranteRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test" )
@Transactional
class ItemDoCardapioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Restaurante restauranteSalvo;

    @BeforeEach
    void setup() {
        Usuario dono = new Usuario();
        dono.setNome("Dono Teste");
        dono.setEmail("dono@teste.com");
        dono.setLogin("dono.teste");
        dono.setSenha("senha123");
        dono.setTipo(TipoUsuario.ADMIN);
        Usuario donoSalvo = usuarioRepository.save(dono);

        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Teste");
        restaurante.setTipoCozinha("Brasileira");
        restaurante.setUsuario(donoSalvo);

        restauranteSalvo = restauranteRepository.save(restaurante);
    }

    @Test
    @DisplayName("Deve Criar um Item de Cardápio com Sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveCriarItemDeCardapioComSucesso() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("nome", "Pizza Margherita");
        requestBody.put("descricao", "Molho de tomate, mussarela e manjericão.");
        requestBody.put("preco", new BigDecimal("45.50"));
        requestBody.put("disponivelSomenteNoLocal", false);

        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        mockMvc.perform(post("/restaurantes/{restauranteId}/itens", restauranteSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Pizza Margherita"))
                .andExpect(jsonPath("$.preco").value(45.50));
    }

    @Test
    @DisplayName("Deve Retornar Erro 400 ao Tentar Criar Item com Dados Inválidos")
    @WithMockUser(roles = "ADMIN")
    void deveRetornarErroAoCriarItemComDadosInvalidos() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("nome", null);
        requestBody.put("descricao", "Descrição válida.");
        requestBody.put("preco", new BigDecimal("20.00"));
        requestBody.put("disponivelSomenteNoLocal", false);

        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        mockMvc.perform(post("/restaurantes/{restauranteId}/itens", restauranteSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve Buscar um Item de Cardápio por ID com Sucesso")
    @WithMockUser
    void deveBuscarItemPorIdComSucesso() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("nome", "Cheeseburger");
        requestBody.put("descricao", "Pão, carne e queijo.");
        requestBody.put("preco", new BigDecimal("30.00"));
        requestBody.put("disponivelSomenteNoLocal", true);

        String responseString = mockMvc.perform(post("/restaurantes/{restauranteId}/itens", restauranteSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long itemIdCriado = objectMapper.readTree(responseString).get("id").asLong();

        mockMvc.perform(get("/restaurantes/{restauranteId}/itens/{itemId}", restauranteSalvo.getId(), itemIdCriado)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemIdCriado))
                .andExpect(jsonPath("$.nome").value("Cheeseburger"));
    }

    @Test
    @DisplayName("Deve Retornar Erro 404 ao Buscar Item com ID Inexistente")
    @WithMockUser
    void deveRetornarNotFoundAoBuscarItemInexistente() throws Exception {
        long idInexistente = 999L;
        mockMvc.perform(get("/restaurantes/{restauranteId}/itens/{itemId}", restauranteSalvo.getId(), idInexistente)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
