package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.AtualizarEnderecoComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidarDadosAtualizacaoEnderecoTest {
    @Mock
    private ValidarCepDuplicado validarCepDuplicado;
    @Mock
    private ValidarEnderecoExistente validarEnderecoExistente;
    @Mock
    private ValidarProprietarioEndereco validarProprietarioEndereco;
    @Mock
    private ValidarDadosAtualizacaoEndereco validarDadosAtualizacaoEndereco;

    @BeforeEach
    void setUp() {

        validarDadosAtualizacaoEndereco = new ValidarDadosAtualizacaoEndereco(
                validarCepDuplicado,
                validarEnderecoExistente,
                validarProprietarioEndereco);
    }

    @Test
    void deveLancarExcecaoQuandoNenhumCampoPreenchido() {
        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setRua(null);
        dto.setCep(null);
        dto.setNumero(null);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarDadosAtualizacaoEndereco.validarAtualizacao(1L, dto, usuario));
        assertEquals("atualizar.endereco.nenhum.campo", exception.getMessage());
        verifyNoInteractions(validarEnderecoExistente, validarProprietarioEndereco, validarCepDuplicado);
    }

    @Test
    void naoDeveLancarExcecaoQuandoPeloMenosUmCampoPreenchido() {
        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setRua("Nova Rua");
        dto.setCep(null);
        dto.setNumero(null);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Endereco endereco = new Endereco();
        endereco.setId(1L);

        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);

        Endereco resultado = validarDadosAtualizacaoEndereco.validarAtualizacao(1L, dto, usuario);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(validarEnderecoExistente).execute(1L);
        verify(validarProprietarioEndereco).validarProprietarioEndereco(endereco, 1L, null);
        verify(validarCepDuplicado).validarCep(1L, null, null);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEProprietario() {
        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setRua("Rua");
        dto.setCep(null);
        dto.setNumero(null);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Endereco endereco = new Endereco();
        endereco.setId(1L);

        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        doThrow(new BadRequestException("proprietario.invalido"))
                .when(validarProprietarioEndereco).validarProprietarioEndereco(endereco, 1L, null);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarDadosAtualizacaoEndereco.validarAtualizacao(1L, dto, usuario));
        assertEquals("proprietario.invalido", exception.getMessage());
        verify(validarEnderecoExistente).execute(1L);
        verify(validarProprietarioEndereco).validarProprietarioEndereco(endereco, 1L, null);
        verify(validarCepDuplicado, never()).validarCep(anyLong(), any(), any());
    }

    @Test
    void deveLancarExcecaoQuandoCepDuplicado() {
        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setRua("Rua");
        dto.setCep("12345-000");
        dto.setNumero(null);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Endereco endereco = new Endereco();
        endereco.setId(1L);

        when(validarEnderecoExistente.execute(1L)).thenReturn(endereco);
        doNothing().when(validarProprietarioEndereco).validarProprietarioEndereco(endereco, 1L, null);
        doThrow(new BadRequestException("cep.duplicado"))
                .when(validarCepDuplicado).validarCep(1L, null, "12345-000");

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validarDadosAtualizacaoEndereco.validarAtualizacao(1L, dto, usuario));
        assertEquals("cep.duplicado", exception.getMessage());
        verify(validarEnderecoExistente).execute(1L);
        verify(validarProprietarioEndereco).validarProprietarioEndereco(endereco, 1L, null);
        verify(validarCepDuplicado).validarCep(1L, null, "12345-000");
    }
}