package br.com.techchallenge.foodsys.core.utils.usuario.tipo;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VerificarTipoUsuarioExistenteTest {

    private VerificarTipoUsuarioExistente verificarTipoUsuarioExistente;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        verificarTipoUsuarioExistente = new VerificarTipoUsuarioExistente();
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setUsuarioTipos(new HashSet<>());
    }

    @Test
    void deveVerificarTipoNaoExistenteComSucesso() {
        // Act
        assertDoesNotThrow(() -> verificarTipoUsuarioExistente.execute(usuario, TipoUsuario.CLIENTE));
    }

    @Test
    void deveLancarExcecaoQuandoTipoJaExiste() {
        // Arrange
        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setTipo(TipoUsuario.CLIENTE);
        usuario.getUsuarioTipos().add(usuarioTipo);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> 
            verificarTipoUsuarioExistente.execute(usuario, TipoUsuario.CLIENTE));
    }

    @Test
    void deveVerificarTipoDiferenteComSucesso() {
        // Arrange
        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setTipo(TipoUsuario.CLIENTE);
        usuario.getUsuarioTipos().add(usuarioTipo);

        // Act
        assertDoesNotThrow(() -> verificarTipoUsuarioExistente.execute(usuario, TipoUsuario.DONO_RESTAURANTE));
    }

    @Test
    void deveVerificarComUsuarioSemTipos() {
        // Act
        assertDoesNotThrow(() -> verificarTipoUsuarioExistente.execute(usuario, TipoUsuario.ADMIN));
    }

    @Test
    void deveVerificarComMultiplosTipos() {
        // Arrange
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuario.getUsuarioTipos().add(tipoCliente);
        
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuario.getUsuarioTipos().add(tipoDono);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> 
            verificarTipoUsuarioExistente.execute(usuario, TipoUsuario.CLIENTE));
        
        assertThrows(BadRequestException.class, () -> 
            verificarTipoUsuarioExistente.execute(usuario, TipoUsuario.DONO_RESTAURANTE));
        
        assertDoesNotThrow(() -> verificarTipoUsuarioExistente.execute(usuario, TipoUsuario.FUNCIONARIO));
    }
} 