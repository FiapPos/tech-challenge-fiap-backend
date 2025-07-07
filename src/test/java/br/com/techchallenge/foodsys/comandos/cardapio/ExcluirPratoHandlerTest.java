package br.com.techchallenge.foodsys.comandos.cardapio;
import br.com.techchallenge.foodsys.dominio.cardapio.PratoRepository;
import br.com.techchallenge.foodsys.excpetion.PratoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExcluirPratoHandlerTest {

    private PratoRepository pratoRepository;
    private ExcluirPratoHandler excluirPratoHandler;

    @BeforeEach
    void setup() {
        pratoRepository = mock(PratoRepository.class);
        excluirPratoHandler = new ExcluirPratoHandler(pratoRepository);
    }

    @Test
    void deveExcluirPratoQuandoExistir() {
        Long restauranteId = 1L;
        Long pratoId = 2L;

        when(pratoRepository.existsByIdAndRestauranteId(pratoId, restauranteId)).thenReturn(true);
        doNothing().when(pratoRepository).deleteById(pratoId);

        excluirPratoHandler.executar(restauranteId, pratoId);

        verify(pratoRepository).existsByIdAndRestauranteId(pratoId, restauranteId);
        verify(pratoRepository).deleteById(pratoId);
    }

    @Test
    void deveLancarExcecaoQuandoPratoNaoExistir() {
        Long restauranteId = 1L;
        Long pratoId = 99L;

        when(pratoRepository.existsByIdAndRestauranteId(pratoId, restauranteId)).thenReturn(false);

        assertThrows(PratoNaoEncontradoException.class, () -> {
            excluirPratoHandler.executar(restauranteId, pratoId);
        });

        verify(pratoRepository).existsByIdAndRestauranteId(pratoId, restauranteId);
        verify(pratoRepository, never()).deleteById(any());
    }
}
