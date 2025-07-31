package br.com.techchallenge.foodsys.comandos.usuario;

import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DesativarUsuarioComandoTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ValidarUsuarioExistente validarUsuarioExistente;
    @Mock
    private CompartilhadoService sharedService;
    @InjectMocks
    private DesativarUsuarioComando desativarUsuarioComando;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveDesativarUsuarioComSucesso() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setAtivo(true);
        LocalDateTime dataDesativacao = LocalDateTime.now();
        when(validarUsuarioExistente.execute(id)).thenReturn(usuario);
        when(sharedService.getCurrentDateTime()).thenReturn(dataDesativacao);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario desativado = desativarUsuarioComando.execute(id);
        assertFalse(desativado.isAtivo());
        assertEquals(dataDesativacao, desativado.getDataDesativacao());
    }
} 