package br.com.techchallenge.foodsys.core.domain.entities.usuario;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void deveAtualizarSenhaENovaDataDeAtualizacao() {
        Usuario usuario = new Usuario();
        usuario.setSenha("senhaAntiga");
        usuario.setDataAtualizacao(LocalDateTime.of(2023, 1, 1, 0, 0));

        usuario.trocaSenha("novaSenha");

        assertEquals("novaSenha", usuario.getSenha());
        assertNotNull(usuario.getDataAtualizacao());
        assertTrue(usuario.getDataAtualizacao().isAfter(LocalDateTime.of(2023, 1, 1, 0, 0)));
    }
}