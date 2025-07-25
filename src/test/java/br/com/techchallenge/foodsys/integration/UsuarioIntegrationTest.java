package br.com.techchallenge.foodsys.integration;

import br.com.techchallenge.foodsys.core.dtos.usuario.CriarUsuarioCommandDto;
import br.com.techchallenge.foodsys.core.dtos.usuario.AtualizarUsuarioComandoDto;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.gateways.UsuarioRepository;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UsuarioIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        enderecoRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void deveCriarUsuarioComSucesso() throws Exception {
        CriarUsuarioCommandDto dto = new CriarUsuarioCommandDto();
        dto.setNome("João Silva");
        dto.setEmail("joao@email.com");
        dto.setLogin("joao123");
        dto.setSenha("senha123");
        dto.setTipo(TipoUsuario.CLIENTE);

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .post("/usuarios")
        .then()
            .statusCode(201);
    }

    @Test
    void deveRetornarErroAoCriarUsuarioComEmailDuplicado() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senha123");
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuarioRepository.save(usuario);

        CriarUsuarioCommandDto dto = new CriarUsuarioCommandDto();
        dto.setNome("Maria Silva");
        dto.setEmail("joao@email.com");
        dto.setLogin("maria123");
        dto.setSenha("senha456");
        dto.setTipo(TipoUsuario.CLIENTE);

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .post("/usuarios")
        .then()
            .statusCode(400);
    }

    @Test
    void deveRetornarErroAoCriarUsuarioComLoginDuplicado() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senha123");
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuarioRepository.save(usuario);

        CriarUsuarioCommandDto dto = new CriarUsuarioCommandDto();
        dto.setNome("Maria Silva");
        dto.setEmail("maria@email.com");
        dto.setLogin("joao123");
        dto.setSenha("senha456");
        dto.setTipo(TipoUsuario.CLIENTE);

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .post("/usuarios")
        .then()
            .statusCode(400);
    }

    @Test
    void deveListarUsuariosComSucesso()   {
        Usuario usuario1 = new Usuario();
        usuario1.setNome("João Silva");
        usuario1.setEmail("joao@email.com");
        usuario1.setLogin("joao123");
        usuario1.setSenha("senha123");
        usuario1.setTipo(TipoUsuario.CLIENTE);
        usuarioRepository.save(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setNome("Maria Silva");
        usuario2.setEmail("maria@email.com");
        usuario2.setLogin("maria123");
        usuario2.setSenha("senha456");
        usuario2.setTipo(TipoUsuario.ADMIN);
        usuarioRepository.save(usuario2);

        given()
        .when()
            .get("/usuarios")
        .then()
            .statusCode(200)
            .body("$", hasSize(2))
            .body("[0].nome", equalTo("João Silva"))
            .body("[1].nome", equalTo("Maria Silva"));
    }

    @Test
    void deveRetornarNoContentQuandoNaoHouverUsuarios()   {
        given()
        .when()
            .get("/usuarios")
        .then()
            .statusCode(204);
    }

    @Test
    void deveAtualizarUsuarioComSucesso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senha123");
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuario = usuarioRepository.save(usuario);

        AtualizarUsuarioComandoDto dto = new AtualizarUsuarioComandoDto();
        dto.setNome("João Silva Atualizado");
        dto.setEmail("joao.atualizado@email.com");

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .put("/usuarios/" + usuario.getId())
        .then()
            .statusCode(200);
    }

    @Test
    void deveRetornarErroAoAtualizarUsuarioInexistente() throws Exception {
        AtualizarUsuarioComandoDto dto = new AtualizarUsuarioComandoDto();
        dto.setNome("João Silva Atualizado");
        dto.setEmail("joao.atualizado@email.com");

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .put("/usuarios/999")
        .then()
            .statusCode(400);
    }

    @Test
    void deveDesativarUsuarioComSucesso()   {
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senha123");
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuario = usuarioRepository.save(usuario);

        given()
        .when()
            .delete("/usuarios/" + usuario.getId())
        .then()
            .statusCode(200);
    }

    @Test
    void deveRetornarErroAoDesativarUsuarioInexistente()   {
        given()
        .when()
            .delete("/usuarios/999")
        .then()
            .statusCode(400);
    }

    @Test
    void deveRetornarErroAoCriarUsuarioComDadosInvalidos() throws Exception {
        CriarUsuarioCommandDto dto = new CriarUsuarioCommandDto();
        dto.setNome("João");

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .post("/usuarios")
        .then()
            .statusCode(400);
    }
} 