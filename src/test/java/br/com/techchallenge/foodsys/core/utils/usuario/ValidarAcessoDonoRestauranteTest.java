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

class ValidarAcessoDonoRestauranteTest {

    @Mock
    private AutorizacaoService autorizacaoService;

    @InjectMocks
    private ValidarAcessoDonoRestaurante validarAcessoDonoRestaurante;

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
    void deveValidarPermissaoDonoRestauranteComSucesso() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoDonoRestaurante.validarPermissaoDonoRestaurante());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEDonoRestaurante() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoDonoRestaurante.validarPermissaoDonoRestaurante());
        
        assertEquals("acesso.restrito.dono.restaurante", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoTemTipos() {
        // Arrange
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoDonoRestaurante.validarPermissaoDonoRestaurante());
        
        assertEquals("acesso.restrito.dono.restaurante", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarGerenciamentoRestauranteComSucesso() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoDonoRestaurante.validarGerenciamentoRestaurante());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoParaGerenciamentoRestauranteQuandoUsuarioNaoEDono() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoFuncionario = new UsuarioTipo();
        tipoFuncionario.setTipo(TipoUsuario.FUNCIONARIO);
        usuarioTipos.add(tipoFuncionario);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoDonoRestaurante.validarGerenciamentoRestaurante());
        
        assertEquals("acesso.restrito.dono.restaurante", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarGerenciamentoCardapioComSucesso() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoDonoRestaurante.validarGerenciamentoCardapio());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoParaGerenciamentoCardapioQuandoUsuarioNaoEDono() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoAdmin = new UsuarioTipo();
        tipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipos.add(tipoAdmin);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoDonoRestaurante.validarGerenciamentoCardapio());
        
        assertEquals("acesso.restrito.dono.restaurante", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarUploadFotoComSucesso() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoDonoRestaurante.validarUploadFoto());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoParaUploadFotoQuandoUsuarioNaoEDono() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
            () -> validarAcessoDonoRestaurante.validarUploadFoto());
        
        assertEquals("acesso.restrito.dono.restaurante", exception.getMessage());
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarPermissaoDonoRestauranteComMultiplosTiposIncluindoDono() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        UsuarioTipo tipoFuncionario = new UsuarioTipo();
        tipoFuncionario.setTipo(TipoUsuario.FUNCIONARIO);
        usuarioTipos.add(tipoDono);
        usuarioTipos.add(tipoFuncionario);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoDonoRestaurante.validarPermissaoDonoRestaurante());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveChamarAutorizacaoServiceComUsuarioLogado() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        validarAcessoDonoRestaurante.validarPermissaoDonoRestaurante();

        // Verify
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarGerenciamentoRestauranteComMultiplosTiposIncluindoDono() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        UsuarioTipo tipoAdmin = new UsuarioTipo();
        tipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipos.add(tipoDono);
        usuarioTipos.add(tipoAdmin);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoDonoRestaurante.validarGerenciamentoRestaurante());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarGerenciamentoCardapioComMultiplosTiposIncluindoDono() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoDono);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoDonoRestaurante.validarGerenciamentoCardapio());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarUploadFotoComMultiplosTiposIncluindoDono() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        UsuarioTipo tipoFuncionario = new UsuarioTipo();
        tipoFuncionario.setTipo(TipoUsuario.FUNCIONARIO);
        usuarioTipos.add(tipoDono);
        usuarioTipos.add(tipoFuncionario);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoDonoRestaurante.validarUploadFoto());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }
} 