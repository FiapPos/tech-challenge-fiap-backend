package br.com.techchallenge.foodsys.core.domain.usecases.cardapio;
import br.com.techchallenge.foodsys.core.exceptions.ItemDoCardapioNaoEncontradoException;
import br.com.techchallenge.foodsys.core.gateways.ItemDoCardapioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExcluirItemDoCardapioHandlerTest {

    private ItemDoCardapioRepository itemDoCardapioRepository;
    private ExcluirItemDoCardapioHandler excluirItemDoCardapioHandler;

    @BeforeEach
    void setup() {
        itemDoCardapioRepository = mock(ItemDoCardapioRepository.class);
        excluirItemDoCardapioHandler = new ExcluirItemDoCardapioHandler(itemDoCardapioRepository);
    }

    @Test
    void deveExcluirPratoQuandoExistir() {
        Long restauranteId = 1L;
        Long pratoId = 2L;

        when(itemDoCardapioRepository.existsByIdAndRestauranteId(pratoId, restauranteId)).thenReturn(true);
        doNothing().when(itemDoCardapioRepository).deleteById(pratoId);

        excluirItemDoCardapioHandler.executar(restauranteId, pratoId);

        verify(itemDoCardapioRepository).existsByIdAndRestauranteId(pratoId, restauranteId);
        verify(itemDoCardapioRepository).deleteById(pratoId);
    }

    @Test
    void deveLancarExcecaoQuandoPratoNaoExistir() {
        Long restauranteId = 1L;
        Long pratoId = 99L;

        when(itemDoCardapioRepository.existsByIdAndRestauranteId(pratoId, restauranteId)).thenReturn(false);

        assertThrows(ItemDoCardapioNaoEncontradoException.class, () -> {
            excluirItemDoCardapioHandler.executar(restauranteId, pratoId);
        });

        verify(itemDoCardapioRepository).existsByIdAndRestauranteId(pratoId, restauranteId);
        verify(itemDoCardapioRepository, never()).deleteById(any());
    }
}
