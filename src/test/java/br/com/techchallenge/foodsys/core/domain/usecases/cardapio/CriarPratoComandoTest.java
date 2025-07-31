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
        // Arrange
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
        when(itemDoCardapioRepository.existsByNomeAndRestauranteId(comando.getNome(), restauranteId)).thenReturn(false);
        when(itemDoCardapioRepository.save(any(ItemDoCardapio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ItemDoCardapioResponseDTO response = handler.executar(comando);

        // Assert
        assertEquals(comando.getNome(), response.getNome());
        assertEquals(comando.getDescricao(), response.getDescricao());
        assertEquals(comando.getPreco(), response.getPreco());
        assertEquals(comando.getDisponivelSomenteNoLocal(), response.getDisponivelSomenteNoLocal());
        
        // Verify
        verify(restauranteRepository).findById(restauranteId);
        verify(itemDoCardapioRepository).existsByNomeAndRestauranteId(comando.getNome(), restauranteId);
        verify(itemDoCardapioRepository).save(any(ItemDoCardapio.class));
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteNaoExistir() {
        // Arrange
        Long restauranteId = 1L;

        CriarItemDoCardapioComando comando = new CriarItemDoCardapioComando(
                "Prato Teste",
                "Descrição Teste",
                new BigDecimal("50.00"),
                false,
                restauranteId
        );

        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> handler.executar(comando));

        assertTrue(exception.getMessage().contains("Restaurante com ID " + restauranteId + " não encontrado."));
        
        // Verify
        verify(restauranteRepository).findById(restauranteId);
        verify(itemDoCardapioRepository, never()).existsByNomeAndRestauranteId(any(), any());
        verify(itemDoCardapioRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoNomeJaExiste() {
        // Arrange
        Long restauranteId = 1L;
        String nomePrato = "Prato Duplicado";

        CriarItemDoCardapioComando comando = new CriarItemDoCardapioComando(
                nomePrato,
                "Descrição Teste",
                new BigDecimal("50.00"),
                false,
                restauranteId
        );

        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
        when(itemDoCardapioRepository.existsByNomeAndRestauranteId(nomePrato, restauranteId)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> handler.executar(comando));

        assertTrue(exception.getMessage().contains("Já existe um item com esse nome no cardápio do restaurante."));
        
        // Verify
        verify(restauranteRepository).findById(restauranteId);
        verify(itemDoCardapioRepository).existsByNomeAndRestauranteId(nomePrato, restauranteId);
        verify(itemDoCardapioRepository, never()).save(any());
    }

    @Test
    void deveCriarPratoComDisponivelSomenteNoLocalTrue() {
        // Arrange
        Long restauranteId = 1L;

        CriarItemDoCardapioComando comando = new CriarItemDoCardapioComando(
                "Prato Local",
                "Descrição Local",
                new BigDecimal("25.00"),
                true,
                restauranteId
        );

        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
        when(itemDoCardapioRepository.existsByNomeAndRestauranteId(comando.getNome(), restauranteId)).thenReturn(false);
        when(itemDoCardapioRepository.save(any(ItemDoCardapio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ItemDoCardapioResponseDTO response = handler.executar(comando);

        // Assert
        assertTrue(response.getDisponivelSomenteNoLocal());
        assertEquals("Prato Local", response.getNome());
        assertEquals("Descrição Local", response.getDescricao());
        assertEquals(new BigDecimal("25.00"), response.getPreco());
    }

    @Test
    void deveCriarPratoComPrecoAlto() {
        // Arrange
        Long restauranteId = 1L;

        CriarItemDoCardapioComando comando = new CriarItemDoCardapioComando(
                "Prato Caro",
                "Descrição Cara",
                new BigDecimal("150.50"),
                false,
                restauranteId
        );

        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
        when(itemDoCardapioRepository.existsByNomeAndRestauranteId(comando.getNome(), restauranteId)).thenReturn(false);
        when(itemDoCardapioRepository.save(any(ItemDoCardapio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ItemDoCardapioResponseDTO response = handler.executar(comando);

        // Assert
        assertEquals(new BigDecimal("150.50"), response.getPreco());
        assertEquals("Prato Caro", response.getNome());
        assertFalse(response.getDisponivelSomenteNoLocal());
    }

    @Test
    void deveVerificarSeItemDoCardapioFoiSalvoCorretamente() {
        // Arrange
        Long restauranteId = 1L;

        CriarItemDoCardapioComando comando = new CriarItemDoCardapioComando(
                "Prato Verificação",
                "Descrição Verificação",
                new BigDecimal("75.00"),
                false,
                restauranteId
        );

        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
        when(itemDoCardapioRepository.existsByNomeAndRestauranteId(comando.getNome(), restauranteId)).thenReturn(false);
        when(itemDoCardapioRepository.save(any(ItemDoCardapio.class))).thenAnswer(invocation -> {
            ItemDoCardapio item = invocation.getArgument(0);
            item.setId(1L); // Simula ID gerado pelo banco
            return item;
        });

        // Act
        ItemDoCardapioResponseDTO response = handler.executar(comando);

        // Assert
        assertNotNull(response);
        assertEquals("Prato Verificação", response.getNome());
        
        // Verify que o save foi chamado com os dados corretos
        verify(itemDoCardapioRepository).save(argThat(item -> 
            item.getNome().equals("Prato Verificação") &&
            item.getDescricao().equals("Descrição Verificação") &&
            item.getPreco().equals(new BigDecimal("75.00")) &&
            !item.getDisponivelSomenteNoLocal() &&
            item.getRestaurante().equals(restaurante)
        ));
    }
}
