package br.com.techchallenge.foodsys.core.utils.usuario;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.exceptions.ForbiddenException;
import br.com.techchallenge.foodsys.core.utils.AutorizacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidarAcessoClienteTest {

    @Mock
    private AutorizacaoService autorizacaoService;

    @InjectMocks
    private ValidarAcessoCliente validarAcessoCliente;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setUsuarioTipos(new HashSet<>());
    }

    @Test
    void deveValidarPermissaoClienteComSucesso() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoCliente.validarPermissaoCliente());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoECliente() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoAdmin = new UsuarioTipo();
        tipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipos.add(tipoAdmin);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoCliente.validarPermissaoCliente());
        
        assertEquals("acesso.restrito.cliente", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoTemTipos() {
        // Arrange
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoCliente.validarPermissaoCliente());
        
        assertEquals("acesso.restrito.cliente", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarVisualizacaoComSucesso() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoCliente.validarVisualizacao());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoParaVisualizacaoQuandoUsuarioNaoECliente() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoCliente.validarVisualizacao());
        
        assertEquals("acesso.restrito.cliente", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarGerenciamentoPropriosEnderecosComSucesso() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        doNothing().when(autorizacaoService).validarAcessoUsuario(1L);

        // Act
        assertDoesNotThrow(() -> validarAcessoCliente.validarGerenciamentoPropriosEnderecos(1L));

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
        verify(autorizacaoService).validarAcessoUsuario(1L);
    }

    @Test
    void deveLancarExcecaoParaGerenciamentoPropriosEnderecosQuandoUsuarioNaoECliente() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoFuncionario = new UsuarioTipo();
        tipoFuncionario.setTipo(TipoUsuario.FUNCIONARIO);
        usuarioTipos.add(tipoFuncionario);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoCliente.validarGerenciamentoPropriosEnderecos(1L));
        
        assertEquals("acesso.restrito.cliente", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
        verify(autorizacaoService, never()).validarAcessoUsuario(any());
    }

    @Test
    void deveValidarGerenciamentoPropriosEnderecosComUsuarioIdDiferente() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        doNothing().when(autorizacaoService).validarAcessoUsuario(5L);

        // Act
        assertDoesNotThrow(() -> validarAcessoCliente.validarGerenciamentoPropriosEnderecos(5L));

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
        verify(autorizacaoService).validarAcessoUsuario(5L);
    }

    @Test
    void deveValidarAtualizacaoPropriosDadosComSucesso() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        doNothing().when(autorizacaoService).validarAcessoUsuario(1L);

        // Act
        assertDoesNotThrow(() -> validarAcessoCliente.validarAtualizacaoPropriosDados(1L));

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
        verify(autorizacaoService).validarAcessoUsuario(1L);
    }

    @Test
    void deveLancarExcecaoParaAtualizacaoPropriosDadosQuandoUsuarioNaoECliente() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoAdmin = new UsuarioTipo();
        tipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipos.add(tipoAdmin);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoCliente.validarAtualizacaoPropriosDados(1L));
        
        assertEquals("acesso.restrito.cliente", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
        verify(autorizacaoService, never()).validarAcessoUsuario(any());
    }

    @Test
    void deveValidarAtualizacaoPropriosDadosComUsuarioIdDiferente() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        doNothing().when(autorizacaoService).validarAcessoUsuario(10L);

        // Act
        assertDoesNotThrow(() -> validarAcessoCliente.validarAtualizacaoPropriosDados(10L));

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
        verify(autorizacaoService).validarAcessoUsuario(10L);
    }

    @Test
    void deveValidarPermissaoClienteComMultiplosTiposIncluindoCliente() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        UsuarioTipo tipoAdmin = new UsuarioTipo();
        tipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipos.add(tipoCliente);
        usuarioTipos.add(tipoAdmin);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoCliente.validarPermissaoCliente());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveChamarAutorizacaoServiceComUsuarioLogado() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        validarAcessoCliente.validarPermissaoCliente();

        // Verify
        verify(autorizacaoService).getUsuarioLogado();
    }
} 