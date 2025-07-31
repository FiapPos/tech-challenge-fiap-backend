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

class ValidarAcessoAdminTest {

    @Mock
    private AutorizacaoService autorizacaoService;

    @InjectMocks
    private ValidarAcessoAdmin validarAcessoAdmin;

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
    void deveValidarPermissaoAdminComSucesso() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoAdmin = new UsuarioTipo();
        tipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipos.add(tipoAdmin);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoAdmin.validarPermissaoAdmin());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEAdmin() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        assertThrows(ForbiddenException.class, () -> 
            validarAcessoAdmin.validarPermissaoAdmin());
        
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoTemTipos() {
        // Arrange
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        assertThrows(ForbiddenException.class, () -> 
            validarAcessoAdmin.validarPermissaoAdmin());
        
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarOperacaoCompletaComSucesso() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoAdmin = new UsuarioTipo();
        tipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipos.add(tipoAdmin);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoAdmin.validarOperacaoCompleta());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarOperacaoCompletaComMultiplosTiposIncluindoAdmin() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        
        UsuarioTipo tipoAdmin = new UsuarioTipo();
        tipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipos.add(tipoAdmin);
        
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validarAcessoAdmin.validarOperacaoCompleta());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }
} 