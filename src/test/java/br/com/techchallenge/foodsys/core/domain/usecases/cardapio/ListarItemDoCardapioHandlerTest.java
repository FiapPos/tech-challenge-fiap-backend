package br.com.techchallenge.foodsys.core.domain.usecases.cardapio;
import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.dtos.cardapio.ItemDoCardapioResponseDTO;
import br.com.techchallenge.foodsys.core.domain.entities.ItemDoCardapio;
import br.com.techchallenge.foodsys.core.gateways.ItemDoCardapioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListarItemDoCardapioHandlerTest {

    private ItemDoCardapioRepository itemDoCardapioRepository;
    private ListarItemDoCardapioHandler listarItemDoCardapioHandler;

    @BeforeEach
    void setUp() {
        itemDoCardapioRepository = mock(ItemDoCardapioRepository.class);
        listarItemDoCardapioHandler = new ListarItemDoCardapioHandler(itemDoCardapioRepository);
    }

    @Test
    void deveRetornarTodosOsPratos() {
        Long restauranteId = 1L;
        ItemDoCardapio itemDoCardapio1 = criarPrato(1L, "Pizza de Calabresa", restauranteId);
        ItemDoCardapio itemDoCardapio2 = criarPrato(2L, "Pizza Margherita", restauranteId);

        when(itemDoCardapioRepository.findAll()).thenReturn(List.of(itemDoCardapio1, itemDoCardapio2));

        List<ItemDoCardapioResponseDTO> resultado = listarItemDoCardapioHandler.executar();

        assertEquals(2, resultado.size());
        assertEquals("Pizza de Calabresa", resultado.get(0).getNome());
        assertEquals("Pizza Margherita", resultado.get(1).getNome());
    }

    @Test
    void deveRetornarPratosPorRestaurante() {
        Long restauranteId = 1L;
        ItemDoCardapio itemDoCardapio1 = criarPrato(1L, "Pizza de Calabresa", restauranteId);
        ItemDoCardapio itemDoCardapio2 = criarPrato(2L, "Pizza Margherita", restauranteId);

        when(itemDoCardapioRepository.findByRestauranteId(restauranteId)).thenReturn(List.of(itemDoCardapio1, itemDoCardapio2));

        List<ItemDoCardapioResponseDTO> resultado = listarItemDoCardapioHandler.executarPorRestaurante(restauranteId);

        assertEquals(2, resultado.size());
        assertEquals("Pizza de Calabresa", resultado.get(0).getNome());
        assertEquals("Pizza Margherita", resultado.get(1).getNome());
    }

    private ItemDoCardapio criarPrato(Long id, String nome, Long restauranteId) {
        ItemDoCardapio itemDoCardapio = new ItemDoCardapio();
        itemDoCardapio.setId(id);
        itemDoCardapio.setNome(nome);
        itemDoCardapio.setDescricao("Descrição");
        itemDoCardapio.setPreco(BigDecimal.valueOf(40.0));
        itemDoCardapio.setDisponivelSomenteNoLocal(false);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);
        itemDoCardapio.setRestaurante(restaurante);

        return itemDoCardapio;
    }
}
