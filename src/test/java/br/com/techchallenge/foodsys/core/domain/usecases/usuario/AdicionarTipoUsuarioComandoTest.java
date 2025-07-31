package br.com.techchallenge.foodsys.core.domain.usecases.usuario;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.utils.usuario.tipo.VerificarTipoUsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdicionarTipoUsuarioComandoTest {

    @Mock
    private VerificarTipoUsuarioExistente verificarTipoExistente;

    @InjectMocks
    private AdicionarTipoUsuarioComando adicionarTipoUsuarioComando;

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
    void deveAdicionarTipoUsuarioComSucesso() {
        TipoUsuario tipo = TipoUsuario.CLIENTE;
        doNothing().when(verificarTipoExistente).execute(usuario, tipo);

        adicionarTipoUsuarioComando.execute(usuario, tipo);

        verify(verificarTipoExistente).execute(usuario, tipo);
        assertEquals(1, usuario.getUsuarioTipos().size());
        
        UsuarioTipo usuarioTipo = usuario.getUsuarioTipos().iterator().next();
        assertEquals(usuario, usuarioTipo.getUsuario());
        assertEquals(tipo, usuarioTipo.getTipo());
    }

    @Test
    void deveAdicionarMultiplosTipos() {
        TipoUsuario tipoCliente = TipoUsuario.CLIENTE;
        TipoUsuario tipoDono = TipoUsuario.DONO_RESTAURANTE;
        
        doNothing().when(verificarTipoExistente).execute(usuario, tipoCliente);
        doNothing().when(verificarTipoExistente).execute(usuario, tipoDono);

        adicionarTipoUsuarioComando.execute(usuario, tipoCliente);
        adicionarTipoUsuarioComando.execute(usuario, tipoDono);

        assertEquals(2, usuario.getUsuarioTipos().size());
        
        Set<TipoUsuario> tipos = new HashSet<>();
        for (UsuarioTipo usuarioTipo : usuario.getUsuarioTipos()) {
            tipos.add(usuarioTipo.getTipo());
        }
        
        assertTrue(tipos.contains(TipoUsuario.CLIENTE));
        assertTrue(tipos.contains(TipoUsuario.DONO_RESTAURANTE));
    }

    @Test
    void deveVerificarTipoExistenteAntesDeAdicionar() {
        TipoUsuario tipo = TipoUsuario.FUNCIONARIO;
        doNothing().when(verificarTipoExistente).execute(usuario, tipo);

        adicionarTipoUsuarioComando.execute(usuario, tipo);

        verify(verificarTipoExistente).execute(usuario, tipo);
    }

    @Test
    void deveAdicionarTipoAdmin() {
        TipoUsuario tipo = TipoUsuario.ADMIN;
        doNothing().when(verificarTipoExistente).execute(usuario, tipo);

        adicionarTipoUsuarioComando.execute(usuario, tipo);

        verify(verificarTipoExistente).execute(usuario, tipo);
        assertEquals(1, usuario.getUsuarioTipos().size());
        
        UsuarioTipo usuarioTipo = usuario.getUsuarioTipos().iterator().next();
        assertEquals(TipoUsuario.ADMIN, usuarioTipo.getTipo());
    }
} 