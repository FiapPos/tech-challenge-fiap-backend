package br.com.techchallenge.foodsys.core.queries.tipo;

import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ListarPorTipoUsuarioTest {

    private ListarPorTipoUsuario listarPorTipoUsuario;

    @BeforeEach
    void setUp() {
        listarPorTipoUsuario = new ListarPorTipoUsuario();
    }

    @Test
    void deveListarTiposUsuarioComSucesso() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        
        UsuarioTipo tipoFuncionario = new UsuarioTipo();
        tipoFuncionario.setTipo(TipoUsuario.FUNCIONARIO);
        usuarioTipos.add(tipoFuncionario);

        // Act
        List<TipoUsuarioResultItem> resultado = listarPorTipoUsuario.execute(usuarioTipos);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        
        assertTrue(resultado.stream().anyMatch(item -> 
            item.getTipo() == TipoUsuario.CLIENTE && item.getCodigo() == TipoUsuario.CLIENTE.getCodigo()));
        
        assertTrue(resultado.stream().anyMatch(item -> 
            item.getTipo() == TipoUsuario.DONO_RESTAURANTE && item.getCodigo() == TipoUsuario.DONO_RESTAURANTE.getCodigo()));
        
        assertTrue(resultado.stream().anyMatch(item -> 
            item.getTipo() == TipoUsuario.FUNCIONARIO && item.getCodigo() == TipoUsuario.FUNCIONARIO.getCodigo()));
    }

    @Test
    void deveRetornarListaVaziaQuandoNenhumTipo() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();

        // Act
        List<TipoUsuarioResultItem> resultado = listarPorTipoUsuario.execute(usuarioTipos);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveListarApenasUmTipo() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);

        // Act
        List<TipoUsuarioResultItem> resultado = listarPorTipoUsuario.execute(usuarioTipos);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        
        TipoUsuarioResultItem item = resultado.get(0);
        assertEquals(TipoUsuario.CLIENTE, item.getTipo());
        assertEquals(TipoUsuario.CLIENTE.getCodigo(), item.getCodigo());
    }

    @Test
    void deveMapearCorretamenteCodigoDoTipo() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        
        UsuarioTipo tipoAdmin = new UsuarioTipo();
        tipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipos.add(tipoAdmin);

        // Act
        List<TipoUsuarioResultItem> resultado = listarPorTipoUsuario.execute(usuarioTipos);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        
        TipoUsuarioResultItem item = resultado.get(0);
        assertEquals(TipoUsuario.ADMIN, item.getTipo());
        assertEquals(TipoUsuario.ADMIN.getCodigo(), item.getCodigo());
    }
} 