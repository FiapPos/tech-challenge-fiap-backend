package br.com.techchallenge.foodsys.controller;

import br.com.techchallenge.foodsys.comandos.cardapio.*;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.ItemDoCardapioRequestDTO;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.ItemDoCardapioResponseDTO;
import br.com.techchallenge.foodsys.utils.usuario.ValidadorPermissoes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    @Mock
    private CriarItemDoCardapioHandler criarItemDoCardapioHandler;
    @Mock
    private ListarItemDoCardapioHandler listarItemDoCardapioHandler;
    @Mock
    private BuscarItemDoCardapioPorIdHandler buscarItemDoCardapioPorIdHandler;
    @Mock
    private AtualizarItemDoCardapioHandler atualizarItemDoCardapioHandler;
    @Mock
    private ExcluirItemDoCardapioHandler excluirItemDoCardapioHandler;
    @Mock
    private ValidadorPermissoes validadorPermissoes;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
    void deveCriarItemRetornando201() throws Exception {
        ItemDoCardapioRequestDTO request = new ItemDoCardapioRequestDTO();
        request.setNome("Pizza de Calabresa");
        request.setDescricao("Deliciosa pizza com calabresa artesanal");
        request.setPreco(BigDecimal.valueOf(45.90));
        request.setDisponivelSomenteNoLocal(false);

        ItemDoCardapioResponseDTO responseDTO = new ItemDoCardapioResponseDTO(
                1L, "Pizza de Calabresa", "Deliciosa pizza com calabresa artesanal",
                BigDecimal.valueOf(45.90), false, null, 1L);

        doNothing().when(validadorPermissoes).validarGerenciamentoCardapio(1L);
        when(criarItemDoCardapioHandler.executar(any(CriarItemDoCardapioComando.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/restaurantes/1/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Pizza de Calabresa"));

        verify(validadorPermissoes).validarGerenciamentoCardapio(1L);
        verify(criarItemDoCardapioHandler).executar(any(CriarItemDoCardapioComando.class));
    }

    @Test
    void deveListarItensPorRestaurante() throws Exception {
        ItemDoCardapioResponseDTO item = new ItemDoCardapioResponseDTO(
                1L, "Pizza de Calabresa", "Descrição",
                BigDecimal.valueOf(45.90), false, null, 1L);

        doNothing().when(validadorPermissoes).validarVisualizacao();
        when(listarItemDoCardapioHandler.executarPorRestaurante(1L)).thenReturn(List.of(item));

        mockMvc.perform(get("/restaurantes/1/itens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Pizza de Calabresa"));

        verify(validadorPermissoes).validarVisualizacao();
        verify(listarItemDoCardapioHandler).executarPorRestaurante(1L);
    }

    @Test
    void deveBuscarItemPorId() throws Exception {
        ItemDoCardapioResponseDTO item = new ItemDoCardapioResponseDTO(
                1L, "Pizza de Calabresa", "Descrição",
                BigDecimal.valueOf(45.90), false, null, 1L);

        doNothing().when(validadorPermissoes).validarVisualizacao();
        when(buscarItemDoCardapioPorIdHandler.executar(1L, 1L)).thenReturn(item);

        mockMvc.perform(get("/restaurantes/1/itens/1"))
                .andExpect(status().isOk());

        verify(validadorPermissoes).validarVisualizacao();
        verify(buscarItemDoCardapioPorIdHandler).executar(1L, 1L);
    }

    @Test
    void deveAtualizarItem() throws Exception {
        ItemDoCardapioRequestDTO request = new ItemDoCardapioRequestDTO();
        request.setNome("Pizza Atualizada");
        request.setDescricao("Descrição atualizada");
        request.setPreco(BigDecimal.valueOf(50.00));
        request.setDisponivelSomenteNoLocal(true);

        ItemDoCardapioResponseDTO responseDTO = new ItemDoCardapioResponseDTO(
                1L, "Pizza Atualizada", "Descrição atualizada",
                BigDecimal.valueOf(50.00), true, null, 1L);

        doNothing().when(validadorPermissoes).validarGerenciamentoCardapio(1L);
        when(atualizarItemDoCardapioHandler.executar(eq(1L), eq(1L), any(AtualizarItemDoCardapioComando.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/restaurantes/1/itens/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Pizza Atualizada"))
                .andExpect(jsonPath("$.preco").value(50.00))
                .andExpect(jsonPath("$.disponivelSomenteNoLocal").value(true));

        verify(validadorPermissoes).validarGerenciamentoCardapio(1L);
        verify(atualizarItemDoCardapioHandler).executar(eq(1L), eq(1L), any(AtualizarItemDoCardapioComando.class));
    }

    @Test
    void deveExcluirItemRetornando204() throws Exception {
        doNothing().when(validadorPermissoes).validarGerenciamentoCardapio(1L);
        doNothing().when(excluirItemDoCardapioHandler).executar(1L, 1L);

        mockMvc.perform(delete("/restaurantes/1/itens/1"))
                .andExpect(status().isNoContent());

        verify(validadorPermissoes).validarGerenciamentoCardapio(1L);
        verify(excluirItemDoCardapioHandler).executar(1L, 1L);
    }
}
