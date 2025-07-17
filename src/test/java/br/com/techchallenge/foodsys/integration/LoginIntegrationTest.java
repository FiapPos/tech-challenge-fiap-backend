package br.com.techchallenge.foodsys.integration;

import br.com.techchallenge.foodsys.comandos.login.dto.CredenciaisUsuarioDto;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LoginIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        usuarioRepository.deleteAll();
    }

    @Test
    void deveFazerLoginComSucesso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha(passwordEncoder.encode("senha123"));
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuarioRepository.save(usuario);

        CredenciaisUsuarioDto credentials = new CredenciaisUsuarioDto("joao123", "senha123");

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(credentials))
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .body("token", notNullValue());
    }

    @Test
    void deveRetornarErroComCredenciaisInvalidas() throws Exception {
        CredenciaisUsuarioDto credentials = new CredenciaisUsuarioDto("usuario_inexistente", "senha_incorreta");

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(credentials))
        .when()
            .post("/login")
        .then()
            .statusCode(401);
    }

    @Test
    void deveRetornarErroComSenhaIncorreta() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha(passwordEncoder.encode("senha123"));
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuarioRepository.save(usuario);

        CredenciaisUsuarioDto credentials = new CredenciaisUsuarioDto("joao123", "senha_incorreta");

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(credentials))
        .when()
            .post("/login")
        .then()
            .statusCode(401);
    }

    @Test
    void deveRetornarErroComDadosInvalidos() throws Exception {
        CredenciaisUsuarioDto credentials = new CredenciaisUsuarioDto("joao123", "");

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(credentials))
        .when()
            .post("/login")
        .then()
            .statusCode(400);
    }

    @Test
    void deveAtualizarSenhaComSucesso() throws Exception {
        // Criar usuário
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha(passwordEncoder.encode("senha123"));
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuarioRepository.save(usuario);

        CredenciaisUsuarioDto credentials = new CredenciaisUsuarioDto("joao123", "senha123");
        String token = given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(credentials))
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .extract()
            .path("token");

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body("{\"senha\":\"novaSenha123\",\"confirmacaoSenha\":\"novaSenha123\"}")
        .when()
            .put("/login/atualiza-senha")
        .then()
            .statusCode(200);
    }

    @Test
    void deveRetornarErroAoAtualizarSenhaComConfirmacaoDiferente() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha(passwordEncoder.encode("senha123"));
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuarioRepository.save(usuario);

        CredenciaisUsuarioDto credentials = new CredenciaisUsuarioDto("joao123", "senha123");
        String token = given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(credentials))
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .extract()
            .path("token");

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body("{\"senha\":\"novaSenha123\",\"confirmacaoSenha\":\"senhaDiferente\"}")
        .when()
            .put("/login/atualiza-senha")
        .then()
            .statusCode(400);
    }

    @Test
    void deveRetornarErroAoAtualizarSenhaSemToken()   {
        given()
            .contentType(ContentType.JSON)
            .body("{\"senha\":\"novaSenha123\",\"confirmacaoSenha\":\"novaSenha123\"}")
        .when()
            .put("/login/atualiza-senha")
        .then()
            .statusCode(401);
    }
} 