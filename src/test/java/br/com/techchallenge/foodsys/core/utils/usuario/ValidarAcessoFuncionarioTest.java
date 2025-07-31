package br.com.techchallenge.foodsys.core.utils.usuario;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.exceptions.ForbiddenException;
import br.com.techchallenge.foodsys.infrastructure.services.AutorizacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidarAcessoFuncionarioTest {

    @Mock
    private AutorizacaoService autorizacaoService;

    @InjectMocks
    private ValidarAcessoFuncionario validarAcessoFuncionario;

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
    void deveValidarPermissaoFuncionarioComSucesso() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoFuncionario = new UsuarioTipo();
        tipoFuncionario.setTipo(TipoUsuario.FUNCIONARIO);
        usuarioTipos.add(tipoFuncionario);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoFuncionario.validarPermissaoFuncionario());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEFuncionario() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoFuncionario.validarPermissaoFuncionario());
        
        assertEquals("acesso.restrito.funcionario", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoTemTipos() {
        // Arrange
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoFuncionario.validarPermissaoFuncionario());
        
        assertEquals("acesso.restrito.funcionario", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarGerenciamentoCardapioComSucesso() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoFuncionario = new UsuarioTipo();
        tipoFuncionario.setTipo(TipoUsuario.FUNCIONARIO);
        usuarioTipos.add(tipoFuncionario);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoFuncionario.validarGerenciamentoCardapio());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoParaGerenciamentoCardapioQuandoUsuarioNaoEFuncionario() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoFuncionario.validarGerenciamentoCardapio());
        
        assertEquals("acesso.restrito.funcionario", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarUploadFotoComSucesso() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoFuncionario = new UsuarioTipo();
        tipoFuncionario.setTipo(TipoUsuario.FUNCIONARIO);
        usuarioTipos.add(tipoFuncionario);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoFuncionario.validarUploadFoto());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoParaUploadFotoQuandoUsuarioNaoEFuncionario() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoAdmin = new UsuarioTipo();
        tipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipos.add(tipoAdmin);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoFuncionario.validarUploadFoto());
        
        assertEquals("acesso.restrito.funcionario", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarVisualizacaoRestauranteComSucesso() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoFuncionario = new UsuarioTipo();
        tipoFuncionario.setTipo(TipoUsuario.FUNCIONARIO);
        usuarioTipos.add(tipoFuncionario);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoFuncionario.validarVisualizacaoRestaurante());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoParaVisualizacaoRestauranteQuandoUsuarioNaoEFuncionario() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoFuncionario.validarVisualizacaoRestaurante());
        
        assertEquals("acesso.restrito.funcionario", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarPermissaoFuncionarioComMultiplosTiposIncluindoFuncionario() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoFuncionario = new UsuarioTipo();
        tipoFuncionario.setTipo(TipoUsuario.FUNCIONARIO);
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoFuncionario);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoFuncionario.validarPermissaoFuncionario());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveChamarAutorizacaoServiceComUsuarioLogado() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoFuncionario = new UsuarioTipo();
        tipoFuncionario.setTipo(TipoUsuario.FUNCIONARIO);
        usuarioTipos.add(tipoFuncionario);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        validarAcessoFuncionario.validarPermissaoFuncionario();

        // Verify
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarGerenciamentoCardapioComMultiplosTiposIncluindoFuncionario() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoFuncionario = new UsuarioTipo();
        tipoFuncionario.setTipo(TipoUsuario.FUNCIONARIO);
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoFuncionario);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoFuncionario.validarGerenciamentoCardapio());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarUploadFotoComMultiplosTiposIncluindoFuncionario() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoFuncionario = new UsuarioTipo();
        tipoFuncionario.setTipo(TipoUsuario.FUNCIONARIO);
        UsuarioTipo tipoAdmin = new UsuarioTipo();
        tipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipos.add(tipoFuncionario);
        usuarioTipos.add(tipoAdmin);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoFuncionario.validarUploadFoto());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarVisualizacaoRestauranteComMultiplosTiposIncluindoFuncionario() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoFuncionario = new UsuarioTipo();
        tipoFuncionario.setTipo(TipoUsuario.FUNCIONARIO);
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoFuncionario);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoFuncionario.validarVisualizacaoRestaurante());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoParaGerenciamentoCardapioComUsuarioSemTipos() {
        // Arrange
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoFuncionario.validarGerenciamentoCardapio());
        
        assertEquals("acesso.restrito.funcionario", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoParaUploadFotoComUsuarioSemTipos() {
        // Arrange
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoFuncionario.validarUploadFoto());
        
        assertEquals("acesso.restrito.funcionario", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoParaVisualizacaoRestauranteComUsuarioSemTipos() {
        // Arrange
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoFuncionario.validarVisualizacaoRestaurante());
        
        assertEquals("acesso.restrito.funcionario", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
    }
} 