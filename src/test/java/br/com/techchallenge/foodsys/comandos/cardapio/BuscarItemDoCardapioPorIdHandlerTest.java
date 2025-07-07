package br.com.techchallenge.foodsys.comandos.cardapio;

import br.com.techchallenge.foodsys.comandos.cardapio.dtos.ItemDoCardapioResponseDTO;
import br.com.techchallenge.foodsys.dominio.cardapio.ItemDoCardapio;
import br.com.techchallenge.foodsys.dominio.cardapio.ItemDoCardapioRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.excpetion.PratoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BuscarItemDoCardapioPorIdHandlerTest {

    private ItemDoCardapioRepository itemDoCardapioRepository;
    private BuscarItemDoCardapioPorIdHandler handler;

    @BeforeEach
    void setup() {
        itemDoCardapioRepository = mock(ItemDoCardapioRepository.class);
        handler = new BuscarItemDoCardapioPorIdHandler(itemDoCardapioRepository);
    }

    @Test
    void deveRetornarPratoQuandoEncontrado() {
        Long restauranteId = 1L;
        Long pratoId = 2L;

        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        ItemDoCardapio itemDoCardapio = new ItemDoCardapio();
        itemDoCardapio.setId(pratoId);
        itemDoCardapio.setNome("Prato Teste");
        itemDoCardapio.setRestaurante(restaurante);  // <--- MUITO IMPORTANTE setar o restaurante!

        when(itemDoCardapioRepository.findByIdAndRestauranteId(pratoId, restauranteId))
                .thenReturn(Optional.of(itemDoCardapio));

        ItemDoCardapioResponseDTO resposta = handler.executar(restauranteId, pratoId);

        assertNotNull(resposta);
        assertEquals(pratoId, resposta.getId());
        assertEquals("Prato Teste", resposta.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoPratoNaoEncontrado() {
        Long restauranteId = 1L;
        Long pratoId = 2L;

        when(itemDoCardapioRepository.findByIdAndRestauranteId(pratoId, restauranteId))
                .thenReturn(Optional.empty());

        assertThrows(PratoNaoEncontradoException.class,
                () -> handler.executar(restauranteId, pratoId));
    }
}
