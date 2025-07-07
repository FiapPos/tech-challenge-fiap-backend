package br.com.techchallenge.foodsys.comandos.cardapio;

import br.com.techchallenge.foodsys.comandos.cardapio.dtos.ItemDoCardapioResponseDTO;
import br.com.techchallenge.foodsys.dominio.cardapio.ItemDoCardapio;
import br.com.techchallenge.foodsys.dominio.cardapio.ItemDoCardapioRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante; // importe o Restaurante
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AtualizarItemDoCardapioHandlerTest {

    private ItemDoCardapioRepository itemDoCardapioRepository;
    private AtualizarItemDoCardapioHandler handler;

    @BeforeEach
    void setup() {
        itemDoCardapioRepository = mock(ItemDoCardapioRepository.class);
        handler = new AtualizarItemDoCardapioHandler(itemDoCardapioRepository);
    }

    @Test
    void deveAtualizarPratoExistente() {
        Long restauranteId = 1L;
        Long pratoId = 10L;

        // Cria o restaurante e seta o id
        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        // Cria o prato e associa ao restaurante
        ItemDoCardapio itemDoCardapioExistente = new ItemDoCardapio();
        itemDoCardapioExistente.setId(pratoId);
        itemDoCardapioExistente.setNome("Antigo Nome");
        itemDoCardapioExistente.setDescricao("Antiga descrição");
        itemDoCardapioExistente.setPreco(new BigDecimal("30.00"));
        itemDoCardapioExistente.setDisponivelSomenteNoLocal(false);
        itemDoCardapioExistente.setRestaurante(restaurante); // evita NullPointerException

        when(itemDoCardapioRepository.findByIdAndRestauranteId(pratoId, restauranteId))
                .thenReturn(Optional.of(itemDoCardapioExistente));

        AtualizarItemDoCardapioComando comando = new AtualizarItemDoCardapioComando(
                "Novo Nome",
                "Nova descrição",
                new BigDecimal("40.00"),
                true
        );

        when(itemDoCardapioRepository.save(any(ItemDoCardapio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemDoCardapioResponseDTO response = handler.executar(restauranteId, pratoId, comando);

        assertEquals("Novo Nome", itemDoCardapioExistente.getNome());
        assertEquals("Nova descrição", itemDoCardapioExistente.getDescricao());
        assertEquals(new BigDecimal("40.00"), itemDoCardapioExistente.getPreco());
        assertTrue(itemDoCardapioExistente.getDisponivelSomenteNoLocal());

        ArgumentCaptor<ItemDoCardapio> captor = ArgumentCaptor.forClass(ItemDoCardapio.class);
        verify(itemDoCardapioRepository).save(captor.capture());
        ItemDoCardapio salvo = captor.getValue();
        assertEquals(itemDoCardapioExistente, salvo);

        assertEquals(response.getNome(), "Novo Nome");
        assertEquals(response.getDescricao(), "Nova descrição");
        assertEquals(response.getPreco(), new BigDecimal("40.00"));
        assertTrue(response.getDisponivelSomenteNoLocal());
    }

    @Test
    void deveLancarExcecaoSePratoNaoEncontrado() {
        Long restauranteId = 1L;
        Long pratoId = 10L;

        when(itemDoCardapioRepository.findByIdAndRestauranteId(pratoId, restauranteId)).thenReturn(Optional.empty());

        AtualizarItemDoCardapioComando comando = new AtualizarItemDoCardapioComando(
                "Nome",
                "Descrição",
                new BigDecimal("40.00"),
                true
        );

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                handler.executar(restauranteId, pratoId, comando));

        assertEquals("Prato não encontrado para o restaurante informado", exception.getReason());
        assertEquals(404, exception.getStatusCode().value());
    }
}
