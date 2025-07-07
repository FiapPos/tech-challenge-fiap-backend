package br.com.techchallenge.foodsys.comandos.cardapio;

import br.com.techchallenge.foodsys.comandos.cardapio.dtos.PratoResponseDTO;
import br.com.techchallenge.foodsys.dominio.cardapio.Prato;
import br.com.techchallenge.foodsys.dominio.cardapio.PratoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante; // importe o Restaurante
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AtualizarPratoHandlerTest {

    private PratoRepository pratoRepository;
    private AtualizarPratoHandler handler;

    @BeforeEach
    void setup() {
        pratoRepository = mock(PratoRepository.class);
        handler = new AtualizarPratoHandler(pratoRepository);
    }

    @Test
    void deveAtualizarPratoExistente() {
        Long restauranteId = 1L;
        Long pratoId = 10L;

        // Cria o restaurante e seta o id
        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        // Cria o prato e associa ao restaurante
        Prato pratoExistente = new Prato();
        pratoExistente.setId(pratoId);
        pratoExistente.setNome("Antigo Nome");
        pratoExistente.setDescricao("Antiga descrição");
        pratoExistente.setPreco(new BigDecimal("30.00"));
        pratoExistente.setDisponivelSomenteNoLocal(false);
        pratoExistente.setRestaurante(restaurante); // evita NullPointerException

        when(pratoRepository.findByIdAndRestauranteId(pratoId, restauranteId))
                .thenReturn(Optional.of(pratoExistente));

        AtualizarPratoComando comando = new AtualizarPratoComando(
                "Novo Nome",
                "Nova descrição",
                new BigDecimal("40.00"),
                true
        );

        when(pratoRepository.save(any(Prato.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PratoResponseDTO response = handler.executar(restauranteId, pratoId, comando);

        assertEquals("Novo Nome", pratoExistente.getNome());
        assertEquals("Nova descrição", pratoExistente.getDescricao());
        assertEquals(new BigDecimal("40.00"), pratoExistente.getPreco());
        assertTrue(pratoExistente.getDisponivelSomenteNoLocal());

        ArgumentCaptor<Prato> captor = ArgumentCaptor.forClass(Prato.class);
        verify(pratoRepository).save(captor.capture());
        Prato salvo = captor.getValue();
        assertEquals(pratoExistente, salvo);

        assertEquals(response.getNome(), "Novo Nome");
        assertEquals(response.getDescricao(), "Nova descrição");
        assertEquals(response.getPreco(), new BigDecimal("40.00"));
        assertTrue(response.getDisponivelSomenteNoLocal());
    }

    @Test
    void deveLancarExcecaoSePratoNaoEncontrado() {
        Long restauranteId = 1L;
        Long pratoId = 10L;

        when(pratoRepository.findByIdAndRestauranteId(pratoId, restauranteId)).thenReturn(Optional.empty());

        AtualizarPratoComando comando = new AtualizarPratoComando(
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
