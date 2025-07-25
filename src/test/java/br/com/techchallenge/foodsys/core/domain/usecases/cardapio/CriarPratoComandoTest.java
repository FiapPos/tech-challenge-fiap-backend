package br.com.techchallenge.foodsys.core.domain.usecases.cardapio;
import br.com.techchallenge.foodsys.core.domain.entities.ItemDoCardapio;
import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.dtos.cardapio.ItemDoCardapioResponseDTO;
import br.com.techchallenge.foodsys.core.gateways.ItemDoCardapioRepository;
import br.com.techchallenge.foodsys.core.gateways.RestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CriarItemDoCardapioHandlerTest {

    private ItemDoCardapioRepository itemDoCardapioRepository;
    private RestauranteRepository restauranteRepository;
    private CriarItemDoCardapioHandler handler;

    @BeforeEach
    void setup() {
        itemDoCardapioRepository = mock(ItemDoCardapioRepository.class);
        restauranteRepository = mock(RestauranteRepository.class);
        handler = new CriarItemDoCardapioHandler(itemDoCardapioRepository, restauranteRepository);
    }

    @Test
    void deveCriarPratoComRestauranteExistente() {
        Long restauranteId = 1L;

        CriarItemDoCardapioComando comando = new CriarItemDoCardapioComando(
                "Prato Teste",
                "Descrição Teste",
                new BigDecimal("50.00"),
                false,
                restauranteId
        );

        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));

        when(itemDoCardapioRepository.save(any(ItemDoCardapio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemDoCardapioResponseDTO response = handler.executar(comando);

        assertEquals(comando.getNome(), response.getNome());
        assertEquals(comando.getDescricao(), response.getDescricao());
        assertEquals(comando.getPreco(), response.getPreco());
        assertEquals(comando.getDisponivelSomenteNoLocal(), response.getDisponivelSomenteNoLocal());
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteNaoExistir() {
        Long restauranteId = 1L;

        CriarItemDoCardapioComando comando = new CriarItemDoCardapioComando(
                "Prato Teste",
                "Descrição Teste",
                new BigDecimal("50.00"),
                false,
                restauranteId
        );

        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> handler.executar(comando));

        assertTrue(exception.getMessage().contains("Restaurante com ID " + restauranteId + " não encontrado."));
    }
}
