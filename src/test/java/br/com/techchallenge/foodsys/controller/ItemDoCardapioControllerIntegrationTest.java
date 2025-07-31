package br.com.techchallenge.foodsys.controller;
import br.com.techchallenge.foodsys.comandos.login.dto.DetalhesUsuarioDto;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.restaurante.RestauranteRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioTipo;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private Usuario usuarioLogado;

    @BeforeEach
    void setup() {
        Usuario dono = new Usuario();
        dono.setNome("Dono Teste");
        dono.setEmail("dono@teste.com");
        dono.setLogin("dono.teste");
        dono.setSenha("senha123");

        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setUsuario(dono);
        usuarioTipo.setTipo(TipoUsuario.ADMIN);
        dono.setUsuarioTipos(Set.of(usuarioTipo));
        usuarioLogado = usuarioRepository.save(dono);

        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Teste");
        restaurante.setTipoCozinha("Brasileira");
        restaurante.setUsuario(usuarioLogado);

        restauranteSalvo = restauranteRepository.save(restaurante);
    }

    private void configurarAutenticacao() {
        DetalhesUsuarioDto userDetails = new DetalhesUsuarioDto(usuarioLogado);
        org.springframework.security.authentication.UsernamePasswordAuthenticationToken authentication = 
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("Deve Criar um Item de Cardápio com Sucesso")
    void deveCriarItemDeCardapioComSucesso() throws Exception {
        configurarAutenticacao();
        
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
    void deveRetornarErroAoCriarItemComDadosInvalidos() throws Exception {
        configurarAutenticacao();
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("nome", null);
        requestBody.put("descricao", "Descrição válida.");
        requestBody.put("preco", new BigDecimal("20.00"));
        requestBody.put("disponivelSomenteNoLocal", false);

        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        mockMvc.perform(post("/restaurantes/{restauranteId}/itens", restauranteSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve Buscar um Item de Cardápio por ID com Sucesso")
    void deveBuscarItemPorIdComSucesso() throws Exception {
        configurarAutenticacao();
        
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
    void deveRetornarNotFoundAoBuscarItemInexistente() throws Exception {
        configurarAutenticacao();
        
        long idInexistente = 999L;
        mockMvc.perform(get("/restaurantes/{restauranteId}/itens/{itemId}", restauranteSalvo.getId(), idInexistente)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
