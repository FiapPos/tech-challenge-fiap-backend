package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.cardapio.*;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.PratoRequestDTO;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.PratoResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PratoControllerTest {

    private MockMvc mockMvc;
    private CriarPratoHandler criarPratoHandler;
    private ListarPratosHandler listarPratosHandler;
    private BuscarPratoPorIdHandler buscarPratoPorIdHandler;
    private AtualizarPratoHandler atualizarPratoHandler;
    private ExcluirPratoHandler excluirPratoHandler;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        criarPratoHandler = mock(CriarPratoHandler.class);
        listarPratosHandler = mock(ListarPratosHandler.class);
        buscarPratoPorIdHandler = mock(BuscarPratoPorIdHandler.class);
        atualizarPratoHandler = mock(AtualizarPratoHandler.class);
        excluirPratoHandler = mock(ExcluirPratoHandler.class);

        PratoController controller = new PratoController(
                criarPratoHandler,
                listarPratosHandler,
                buscarPratoPorIdHandler,
                atualizarPratoHandler,
                excluirPratoHandler
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void deveCriarPratoRetornando201() throws Exception {
        PratoRequestDTO request = new PratoRequestDTO();
        request.setNome("Pizza de Calabresa");
        request.setDescricao("Deliciosa pizza com calabresa artesanal");
        request.setPreco(BigDecimal.valueOf(45.90));
        request.setDisponivelSomenteNoLocal(false);

        PratoResponseDTO responseDTO = new PratoResponseDTO(1L, "Pizza de Calabresa", "Deliciosa pizza com calabresa artesanal", BigDecimal.valueOf(45.90), false, null, 1L);

        when(criarPratoHandler.executar(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/restaurante/1/pratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Pizza de Calabresa"));
    }

    @Test
    void deveListarPratosPorRestaurante() throws Exception {
        PratoResponseDTO prato = new PratoResponseDTO(1L, "Pizza de Calabresa", "Descrição", BigDecimal.valueOf(45.90), false, null, 1L);

        when(listarPratosHandler.executarPorRestaurante(1L)).thenReturn(List.of(prato));

        mockMvc.perform(get("/restaurante/1/pratos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Pizza de Calabresa"));
    }

    @Test
    void deveBuscarPratoPorId() throws Exception {
        PratoResponseDTO prato = new PratoResponseDTO(1L, "Pizza de Calabresa", "Descrição", BigDecimal.valueOf(45.90), false, null, 1L);

        when(buscarPratoPorIdHandler.executar(1L, 1L)).thenReturn(prato);

        mockMvc.perform(get("/restaurante/1/pratos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Pizza de Calabresa"));
    }

    @Test
    void deveAtualizarPrato() throws Exception {
        PratoRequestDTO request = new PratoRequestDTO();
        request.setNome("Pizza Atualizada");
        request.setDescricao("Descrição atualizada");
        request.setPreco(BigDecimal.valueOf(50.00));
        request.setDisponivelSomenteNoLocal(true);

        PratoResponseDTO responseDTO = new PratoResponseDTO(1L, "Pizza Atualizada", "Descrição atualizada", BigDecimal.valueOf(50.00), true, null, 1L);

        when(atualizarPratoHandler.executar(eq(1L), eq(1L), any())).thenReturn(responseDTO);

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
        doNothing().when(excluirPratoHandler).executar(1L, 1L);

        mockMvc.perform(delete("/restaurante/1/pratos/1"))
                .andExpect(status().isNoContent());
    }
}
