package br.com.techchallenge.foodsys.comandos.endereco;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.DeletarEnderecoComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.utils.ValidarEnderecoExistente;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeletarEnderecoComandoTest {
    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private ValidarEnderecoExistente validarEnderecoExistente;
    @Mock
    private ValidarUsuarioExistente validarUsuarioExistente;
    @InjectMocks
    private DeletarEnderecoComando deletarEnderecoComando;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveDeletarEnderecoComSucesso() {
        DeletarEnderecoComandoDto dto = new DeletarEnderecoComandoDto();
        dto.setEnderecoId(1L);
        dto.setUsuarioId(2L);
        Usuario usuario = mock(Usuario.class);
        Endereco endereco = mock(Endereco.class);
        
        when(usuario.getId()).thenReturn(2L);
        when(endereco.getId()).thenReturn(1L);
        when(endereco.getUsuario()).thenReturn(usuario);
        when(validarUsuarioExistente.execute(dto.getUsuarioId())).thenReturn(usuario);
        when(validarEnderecoExistente.execute(dto.getEnderecoId())).thenReturn(endereco);
        doNothing().when(enderecoRepository).deleteById(1L);

        assertDoesNotThrow(() -> deletarEnderecoComando.execute(dto));
        verify(enderecoRepository).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoNaoPertenceAoUsuario() {
        DeletarEnderecoComandoDto dto = new DeletarEnderecoComandoDto();
        dto.setEnderecoId(1L);
        dto.setUsuarioId(2L);
        Usuario usuario = mock(Usuario.class);
        when(usuario.getId()).thenReturn(3L);
        Endereco endereco = mock(Endereco.class);
        when(endereco.getId()).thenReturn(1L);
        when(endereco.getUsuario()).thenReturn(usuario);
        when(validarUsuarioExistente.execute(dto.getUsuarioId())).thenReturn(usuario);
        when(validarEnderecoExistente.execute(dto.getEnderecoId())).thenReturn(endereco);

        assertThrows(BadRequestException.class, () -> deletarEnderecoComando.execute(dto));
    }
} 