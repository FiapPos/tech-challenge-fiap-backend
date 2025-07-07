package br.com.techchallenge.foodsys.comandos.cardapio;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.PratoResponseDTO;
import br.com.techchallenge.foodsys.dominio.cardapio.Prato;
import br.com.techchallenge.foodsys.dominio.cardapio.PratoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.restaurante.RestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CriarPratoHandlerTest {

    private PratoRepository pratoRepository;
    private RestauranteRepository restauranteRepository;
    private CriarPratoHandler handler;

    @BeforeEach
    void setup() {
        pratoRepository = mock(PratoRepository.class);
        restauranteRepository = mock(RestauranteRepository.class);
        handler = new CriarPratoHandler(pratoRepository, restauranteRepository);
    }

    @Test
    void deveCriarPratoComRestauranteExistente() {
        Long restauranteId = 1L;

        CriarPratoComando comando = new CriarPratoComando(
                "Prato Teste",
                "Descrição Teste",
                new BigDecimal("50.00"),
                false,
                restauranteId
        );

        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));

        when(pratoRepository.save(any(Prato.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PratoResponseDTO response = handler.executar(comando);

        assertEquals(comando.getNome(), response.getNome());
        assertEquals(comando.getDescricao(), response.getDescricao());
        assertEquals(comando.getPreco(), response.getPreco());
        assertEquals(comando.getDisponivelSomenteNoLocal(), response.getDisponivelSomenteNoLocal());
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteNaoExistir() {
        Long restauranteId = 1L;

        CriarPratoComando comando = new CriarPratoComando(
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
