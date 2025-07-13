package br.com.techchallenge.foodsys.comandos.endereco;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.DeletarEnderecoComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.ValidarEnderecoExistente;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;
import br.com.techchallenge.foodsys.utils.ValidarProprietarioEndereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

class DeletarEnderecoComandoTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ValidarProprietarioEndereco validarProprietarioEndereco;

    @Mock
    private ValidarUsuarioExistente validarUsuarioExistente;

    @Mock
    private ValidarEnderecoExistente validarEnderecoExistente;

    @InjectMocks
    private DeletarEnderecoComando deletarEnderecoComando;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveDeletarEnderecoUsuarioComSucesso() {
        Long enderecoId = 1L;
        Long usuarioId = 2L;
        Long restauranteId = null;

        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        DeletarEnderecoComandoDto dto = new DeletarEnderecoComandoDto();
        dto.setEnderecoId(enderecoId);
        dto.setRestauranteId(restauranteId);

        when(validarUsuarioExistente.execute(usuarioId)).thenReturn(usuario);
        when(validarEnderecoExistente.execute(enderecoId)).thenReturn(endereco);
        doNothing().when(validarProprietarioEndereco).validarProprietarioEndereco(endereco, usuarioId, restauranteId);
        doNothing().when(enderecoRepository).delete(endereco);

        deletarEnderecoComando.execute(dto, usuario);

        verify(validarUsuarioExistente).execute(usuarioId);
        verify(validarEnderecoExistente).execute(enderecoId);
        verify(validarProprietarioEndereco).validarProprietarioEndereco(endereco, usuarioId, restauranteId);
        verify(enderecoRepository).deleteById(enderecoId);
    }

    @Test
    void deveDeletarEnderecoRestauranteComSucesso() {
        Long enderecoId = 1L;
        Long usuarioId = 2L;
        Long restauranteId = 3L;

        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        DeletarEnderecoComandoDto dto = new DeletarEnderecoComandoDto();
        dto.setEnderecoId(enderecoId);
        dto.setRestauranteId(restauranteId);

        when(validarUsuarioExistente.execute(usuarioId)).thenReturn(usuario);
        when(validarEnderecoExistente.execute(enderecoId)).thenReturn(endereco);
        doNothing().when(validarProprietarioEndereco).validarProprietarioEndereco(endereco, usuarioId, restauranteId);
        doNothing().when(enderecoRepository).delete(endereco);

        deletarEnderecoComando.execute(dto, usuario);

        verify(validarUsuarioExistente).execute(usuarioId);
        verify(validarEnderecoExistente).execute(enderecoId);
        verify(validarProprietarioEndereco).validarProprietarioEndereco(endereco, usuarioId, restauranteId);
        verify(enderecoRepository).deleteById(enderecoId);
    }
}