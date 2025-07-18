package br.com.techchallenge.foodsys.comandos.login;

import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AutenticaJwtComandoTest {
    @InjectMocks
    private AutenticaJwtComando autenticaJwtComando;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(autenticaJwtComando, "SECRET", "testSecret");
        ReflectionTestUtils.setField(autenticaJwtComando, "EXPIRATION_TIME", 3600000L);
    }

    @Test
    void deveCriarTokenComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setLogin("usuario1");

        String token = autenticaJwtComando.createToken(usuario);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void deveExtrairLoginDoToken() {
        String login = "usuario1";
        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        String token = autenticaJwtComando.createToken(usuario);

        String loginExtraido = autenticaJwtComando.getLogin(token);
        assertEquals(login, loginExtraido);
    }

    @Test
    void deveCriarTokenComDiferentesUsuarios() {
        Usuario usuario1 = new Usuario();
        usuario1.setLogin("usuario1");
        Usuario usuario2 = new Usuario();
        usuario2.setLogin("usuario2");

        String token1 = autenticaJwtComando.createToken(usuario1);
        String token2 = autenticaJwtComando.createToken(usuario2);

        assertNotEquals(token1, token2);
        assertEquals("usuario1", autenticaJwtComando.getLogin(token1));
        assertEquals("usuario2", autenticaJwtComando.getLogin(token2));
    }
} 