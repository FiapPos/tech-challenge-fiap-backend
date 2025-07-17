package br.com.techchallenge.foodsys.comandos.enderecoRestaurante;

import br.com.techchallenge.foodsys.comandos.enderecoRestaurante.dtos.DeletarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.ValidarDadosDelecaoEnderecoRestaurante;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeletarEnderecoRestauranteComandoTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ValidarDadosDelecaoEnderecoRestaurante validarDadosDelecao;

    @InjectMocks
    private DeletarEnderecoRestauranteComando deletarEnderecoComando;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveDeletarEnderecoRestauranteComSucesso() {
        Long enderecoId = 1L;
        Long usuarioId = 2L;
        Long restauranteId = 3L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        DeletarEnderecoRestauranteComandoDto dto = new DeletarEnderecoRestauranteComandoDto();
        dto.setEnderecoId(enderecoId);
        dto.setRestauranteId(restauranteId);

        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);

        when(validarDadosDelecao.validarDelecao(enderecoId, usuarioId, restauranteId)).thenReturn(endereco);
        doNothing().when(enderecoRepository).delete(endereco);

        deletarEnderecoComando.execute(dto, usuario);

        verify(validarDadosDelecao).validarDelecao(enderecoId, usuarioId, restauranteId);
        verify(enderecoRepository).deleteById(enderecoId);
    }

    @Test
    void deveLancarExcecaoQuandoDadosInvalidos() {
        Long enderecoId = 1L;
        Long usuarioId = 2L;
        Long restauranteId = null;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        DeletarEnderecoRestauranteComandoDto dto = new DeletarEnderecoRestauranteComandoDto();
        dto.setEnderecoId(enderecoId);
        dto.setRestauranteId(restauranteId);

        RuntimeException exceptionEsperada = new RuntimeException("Dados inválidos");
        when(validarDadosDelecao.validarDelecao(enderecoId, usuarioId, restauranteId))
                .thenThrow(exceptionEsperada);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deletarEnderecoComando.execute(dto, usuario);
        });

        assertEquals("Dados inválidos", exception.getMessage());
        verify(validarDadosDelecao).validarDelecao(enderecoId, usuarioId, restauranteId);
        verify(enderecoRepository, never()).delete(any(Endereco.class));
    }
}