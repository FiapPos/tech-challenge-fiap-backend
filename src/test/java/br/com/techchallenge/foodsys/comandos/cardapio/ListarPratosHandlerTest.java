package br.com.techchallenge.foodsys.comandos.cardapio;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.PratoResponseDTO;
import br.com.techchallenge.foodsys.dominio.cardapio.Prato;
import br.com.techchallenge.foodsys.dominio.cardapio.PratoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListarPratosHandlerTest {

    private PratoRepository pratoRepository;
    private ListarPratosHandler listarPratosHandler;

    @BeforeEach
    void setUp() {
        pratoRepository = mock(PratoRepository.class);
        listarPratosHandler = new ListarPratosHandler(pratoRepository);
    }

    @Test
    void deveRetornarTodosOsPratos() {
        Long restauranteId = 1L;
        Prato prato1 = criarPrato(1L, "Pizza de Calabresa", restauranteId);
        Prato prato2 = criarPrato(2L, "Pizza Margherita", restauranteId);

        when(pratoRepository.findAll()).thenReturn(List.of(prato1, prato2));

        List<PratoResponseDTO> resultado = listarPratosHandler.executar();

        assertEquals(2, resultado.size());
        assertEquals("Pizza de Calabresa", resultado.get(0).getNome());
        assertEquals("Pizza Margherita", resultado.get(1).getNome());
    }

    @Test
    void deveRetornarPratosPorRestaurante() {
        Long restauranteId = 1L;
        Prato prato1 = criarPrato(1L, "Pizza de Calabresa", restauranteId);
        Prato prato2 = criarPrato(2L, "Pizza Margherita", restauranteId);

        when(pratoRepository.findByRestauranteId(restauranteId)).thenReturn(List.of(prato1, prato2));

        List<PratoResponseDTO> resultado = listarPratosHandler.executarPorRestaurante(restauranteId);

        assertEquals(2, resultado.size());
        assertEquals("Pizza de Calabresa", resultado.get(0).getNome());
        assertEquals("Pizza Margherita", resultado.get(1).getNome());
    }

    private Prato criarPrato(Long id, String nome, Long restauranteId) {
        Prato prato = new Prato();
        prato.setId(id);
        prato.setNome(nome);
        prato.setDescricao("Descrição");
        prato.setPreco(BigDecimal.valueOf(40.0));
        prato.setDisponivelSomenteNoLocal(false);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);
        prato.setRestaurante(restaurante);

        return prato;
    }
}
