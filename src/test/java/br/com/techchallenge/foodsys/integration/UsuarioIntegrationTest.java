package br.com.techchallenge.foodsys.integration;

import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import br.com.techchallenge.foodsys.core.dtos.login.CredenciaisUsuarioDto;
import br.com.techchallenge.foodsys.core.dtos.usuario.CriarUsuarioCommandDto;
import br.com.techchallenge.foodsys.core.dtos.usuario.AtualizarUsuarioComandoDto;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Usuario usuarioAdmin;
    private String token;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        enderecoRepository.deleteAll();
        usuarioRepository.deleteAll();

        usuarioAdmin = new Usuario();
        usuarioAdmin.setNome("Admin User");
        usuarioAdmin.setEmail("admin@email.com");
        usuarioAdmin.setLogin("admin123");
        usuarioAdmin.setSenha(passwordEncoder.encode("senha123"));

        UsuarioTipo usuarioTipoAdmin = new UsuarioTipo();
        usuarioTipoAdmin.setUsuario(usuarioAdmin);
        usuarioTipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioAdmin.getUsuarioTipos().add(usuarioTipoAdmin);

        usuarioAdmin = usuarioRepository.save(usuarioAdmin);

        CredenciaisUsuarioDto credentials = new CredenciaisUsuarioDto("admin123", "senha123", TipoUsuario.ADMIN);
        token = given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(credentials))
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @Test
    void deveCriarUsuarioComSucesso() throws Exception {
        CriarUsuarioCommandDto dto = new CriarUsuarioCommandDto();
        dto.setNome("João Silva");
        dto.setEmail("joao@email.com");
        dto.setLogin("joao123");
        dto.setSenha("senha123");
        dto.setTipos(List.of(TipoUsuario.CLIENTE));

        given()
            .header("Authorization", "Bearer " + token)
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

        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setUsuario(usuario);
        usuarioTipo.setTipo(TipoUsuario.CLIENTE);
        usuario.getUsuarioTipos().add(usuarioTipo);

        usuarioRepository.save(usuario);

        CriarUsuarioCommandDto dto = new CriarUsuarioCommandDto();
        dto.setNome("Maria Silva");
        dto.setEmail("joao@email.com");
        dto.setLogin("maria123");
        dto.setSenha("senha456");
        dto.setTipos(List.of(TipoUsuario.CLIENTE));

        given()
            .header("Authorization", "Bearer " + token)
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

        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setUsuario(usuario);
        usuarioTipo.setTipo(TipoUsuario.CLIENTE);
        usuario.getUsuarioTipos().add(usuarioTipo);

        usuarioRepository.save(usuario);

        CriarUsuarioCommandDto dto = new CriarUsuarioCommandDto();
        dto.setNome("Maria Silva");
        dto.setEmail("maria@email.com");
        dto.setLogin("joao123");
        dto.setSenha("senha456");
        dto.setTipos(List.of(TipoUsuario.CLIENTE));

        given()
            .header("Authorization", "Bearer " + token)
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

        UsuarioTipo usuarioTipo1 = new UsuarioTipo();
        usuarioTipo1.setUsuario(usuario1);
        usuarioTipo1.setTipo(TipoUsuario.CLIENTE);
        usuario1.getUsuarioTipos().add(usuarioTipo1);

        usuarioRepository.save(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setNome("Maria Silva");
        usuario2.setEmail("maria@email.com");
        usuario2.setLogin("maria123");
        usuario2.setSenha("senha456");

        UsuarioTipo usuarioTipo2 = new UsuarioTipo();
        usuarioTipo2.setUsuario(usuario2);
        usuarioTipo2.setTipo(TipoUsuario.CLIENTE);
        usuario2.getUsuarioTipos().add(usuarioTipo2);

        usuarioRepository.save(usuario2);

        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/usuarios")
        .then()
            .statusCode(200)
            .body("$", hasSize(greaterThanOrEqualTo(2)))
            .body("nome", hasItems("João Silva", "Maria Silva"));
    }

    @Test
    void deveRetornarNoContentQuandoNaoHouverUsuarios() throws Exception {

        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/usuarios?ativo=false")
        .then()
            .statusCode(204);
    }

    @Test
    void deveAtualizarUsuarioComSucesso() throws Exception {
        AtualizarUsuarioComandoDto dto = new AtualizarUsuarioComandoDto();
        dto.setNome("Admin User Atualizado");
        dto.setEmail("admin.atualizado@email.com");

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .put("/usuarios/" + usuarioAdmin.getId()) // Usar o ID do usuário admin logado
        .then()
            .statusCode(200);
    }

    @Test
    void deveRetornarErroAoAtualizarUsuarioInexistente() throws Exception {
        AtualizarUsuarioComandoDto dto = new AtualizarUsuarioComandoDto();
        dto.setNome("João Silva Atualizado");
        dto.setEmail("joao.atualizado@email.com");

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .put("/usuarios/999")
        .then()
            .statusCode(403);
    }

    @Test
    void deveDesativarUsuarioComSucesso()   {
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senha123");

        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setUsuario(usuario);
        usuarioTipo.setTipo(TipoUsuario.CLIENTE);
        usuario.getUsuarioTipos().add(usuarioTipo);

        usuario = usuarioRepository.save(usuario);

        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .delete("/usuarios/" + usuario.getId())
        .then()
            .statusCode(200);
    }

    @Test
    void deveRetornarErroAoDesativarUsuarioInexistente()   {
        given()
            .header("Authorization", "Bearer " + token)
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
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .post("/usuarios")
        .then()
            .statusCode(400);
    }
}
