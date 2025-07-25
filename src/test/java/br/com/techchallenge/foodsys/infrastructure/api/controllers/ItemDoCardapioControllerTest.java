package br.com.techchallenge.foodsys.infrastructure.api.controllers;

import br.com.techchallenge.foodsys.core.domain.usecases.cardapio.*;
import br.com.techchallenge.foodsys.core.dtos.cardapio.ItemDoCardapioRequestDTO;
import br.com.techchallenge.foodsys.core.dtos.cardapio.ItemDoCardapioResponseDTO;
import br.com.techchallenge.foodsys.core.utils.usuario.ValidadorPermissoes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ItemDoCardapioControllerTest {

    private MockMvc mockMvc;
    private CriarItemDoCardapioHandler criarItemDoCardapioHandler;
    private ListarItemDoCardapioHandler listarItemDoCardapioHandler;
    private BuscarItemDoCardapioPorIdHandler buscarItemDoCardapioPorIdHandler;
    private AtualizarItemDoCardapioHandler atualizarItemDoCardapioHandler;
    private ExcluirItemDoCardapioHandler excluirItemDoCardapioHandler;
    private ValidadorPermissoes validadorPermissoes;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        criarItemDoCardapioHandler = mock(CriarItemDoCardapioHandler.class);
        listarItemDoCardapioHandler = mock(ListarItemDoCardapioHandler.class);
        buscarItemDoCardapioPorIdHandler = mock(BuscarItemDoCardapioPorIdHandler.class);
        atualizarItemDoCardapioHandler = mock(AtualizarItemDoCardapioHandler.class);
        excluirItemDoCardapioHandler = mock(ExcluirItemDoCardapioHandler.class);
        validadorPermissoes = mock(ValidadorPermissoes.class);

        ItemDoCardapioController controller = new ItemDoCardapioController(
                criarItemDoCardapioHandler,
                listarItemDoCardapioHandler,
                buscarItemDoCardapioPorIdHandler,
                atualizarItemDoCardapioHandler,
                excluirItemDoCardapioHandler,
                validadorPermissoes
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void deveCriarPratoRetornando201() throws Exception {
        ItemDoCardapioRequestDTO request = new ItemDoCardapioRequestDTO();
        request.setNome("Pizza de Calabresa");
        request.setDescricao("Deliciosa pizza com calabresa artesanal");
        request.setPreco(BigDecimal.valueOf(45.90));
        request.setDisponivelSomenteNoLocal(false);

        ItemDoCardapioResponseDTO responseDTO = new ItemDoCardapioResponseDTO(1L, "Pizza de Calabresa", "Deliciosa pizza com calabresa artesanal", BigDecimal.valueOf(45.90), false, null, 1L);

        when(criarItemDoCardapioHandler.executar(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/restaurante/1/pratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Pizza de Calabresa"));
    }

    @Test
    void deveListarPratosPorRestaurante() throws Exception {
        ItemDoCardapioResponseDTO prato = new ItemDoCardapioResponseDTO(1L, "Pizza de Calabresa", "Descrição", BigDecimal.valueOf(45.90), false, null, 1L);

        when(listarItemDoCardapioHandler.executarPorRestaurante(1L)).thenReturn(List.of(prato));

        mockMvc.perform(get("/restaurante/1/pratos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Pizza de Calabresa"));
    }

    @Test
    void deveBuscarPratoPorId() throws Exception {
        ItemDoCardapioResponseDTO prato = new ItemDoCardapioResponseDTO(1L, "Pizza de Calabresa", "Descrição", BigDecimal.valueOf(45.90), false, null, 1L);

        when(buscarItemDoCardapioPorIdHandler.executar(1L, 1L)).thenReturn(prato);

        mockMvc.perform(get("/restaurante/1/pratos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Pizza de Calabresa"));
    }

    @Test
    void deveAtualizarPrato() throws Exception {
        ItemDoCardapioRequestDTO request = new ItemDoCardapioRequestDTO();
        request.setNome("Pizza Atualizada");
        request.setDescricao("Descrição atualizada");
        request.setPreco(BigDecimal.valueOf(50.00));
        request.setDisponivelSomenteNoLocal(true);

        ItemDoCardapioResponseDTO responseDTO = new ItemDoCardapioResponseDTO(1L, "Pizza Atualizada", "Descrição atualizada", BigDecimal.valueOf(50.00), true, null, 1L);

        when(atualizarItemDoCardapioHandler.executar(eq(1L), eq(1L), any())).thenReturn(responseDTO);

        mockMvc.perform(put("/restaurante/1/pratos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Pizza Atualizada"))
                .andExpect(jsonPath("$.preco").value(50.00))
                .andExpect(jsonPath("$.disponivelSomenteNoLocal").value(true));
    }

    @Test
    void deveExcluirPratoRetornando204() throws Exception {
        doNothing().when(excluirItemDoCardapioHandler).executar(1L, 1L);

        mockMvc.perform(delete("/restaurante/1/pratos/1"))
                .andExpect(status().isNoContent());
    }
}
