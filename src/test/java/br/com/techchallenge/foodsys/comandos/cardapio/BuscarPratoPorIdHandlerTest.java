package br.com.techchallenge.foodsys.comandos.cardapio;

import br.com.techchallenge.foodsys.comandos.cardapio.dtos.PratoResponseDTO;
import br.com.techchallenge.foodsys.dominio.cardapio.Prato;
import br.com.techchallenge.foodsys.dominio.cardapio.PratoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.excpetion.PratoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BuscarPratoPorIdHandlerTest {

    private PratoRepository pratoRepository;
    private BuscarPratoPorIdHandler handler;

    @BeforeEach
    void setup() {
        pratoRepository = mock(PratoRepository.class);
        handler = new BuscarPratoPorIdHandler(pratoRepository);
    }

    @Test
    void deveRetornarPratoQuandoEncontrado() {
        Long restauranteId = 1L;
        Long pratoId = 2L;

        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        Prato prato = new Prato();
        prato.setId(pratoId);
        prato.setNome("Prato Teste");
        prato.setRestaurante(restaurante);  // <--- MUITO IMPORTANTE setar o restaurante!

        when(pratoRepository.findByIdAndRestauranteId(pratoId, restauranteId))
                .thenReturn(Optional.of(prato));

        PratoResponseDTO resposta = handler.executar(restauranteId, pratoId);

        assertNotNull(resposta);
        assertEquals(pratoId, resposta.getId());
        assertEquals("Prato Teste", resposta.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoPratoNaoEncontrado() {
        Long restauranteId = 1L;
        Long pratoId = 2L;

        when(pratoRepository.findByIdAndRestauranteId(pratoId, restauranteId))
                .thenReturn(Optional.empty());

        assertThrows(PratoNaoEncontradoException.class,
                () -> handler.executar(restauranteId, pratoId));
    }
}
