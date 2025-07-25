package br.com.techchallenge.foodsys.integration;

import br.com.techchallenge.foodsys.core.dtos.endereco.CriarEnderecoComandoDto;
import br.com.techchallenge.foodsys.comandos.login.dto.CredenciaisUsuarioDto;
import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.gateways.UsuarioRepository;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import com.fasterxml.jackson.core.JsonProcessingException;
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
class EnderecoIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;
    private String token;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        enderecoRepository.deleteAll();
        usuarioRepository.deleteAll();

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha(passwordEncoder.encode("senha123"));
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuario = usuarioRepository.save(usuario);

        CredenciaisUsuarioDto credentials = new CredenciaisUsuarioDto("joao123", "senha123");
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
    void deveCriarEnderecoComSucesso() throws Exception {
        CriarEnderecoComandoDto dto = new CriarEnderecoComandoDto();
        dto.setUsuarioId(usuario.getId());
        dto.setRua("Rua das Flores");
        dto.setNumero("123");
        dto.setCep("01234-567");

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .post("/enderecos")
        .then()
            .statusCode(201);
    }

    @Test
    void deveRetornarErroAoCriarEnderecoComUsuarioInexistente() throws Exception {
        CriarEnderecoComandoDto dto = new CriarEnderecoComandoDto();
        dto.setUsuarioId(999L);
        dto.setRua("Rua das Flores");
        dto.setNumero("123");
        dto.setCep("01234-567");

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .post("/enderecos")
        .then()
            .statusCode(403);
    }

    @Test
    void deveRetornarErroAoCriarEnderecoComDadosInvalidos() throws Exception {
        CriarEnderecoComandoDto dto = new CriarEnderecoComandoDto();
        dto.setUsuarioId(usuario.getId());
        dto.setRua("Rua das Flores");

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .post("/enderecos")
        .then()
            .statusCode(400);
    }

    @Test
    void deveAtualizarEnderecoComSucesso() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setUsuario(usuario);
        endereco.setRua("Rua das Flores");
        endereco.setNumero("123");
        endereco.setCep("01234-567");
        endereco = enderecoRepository.save(endereco);
        
        assert endereco.getId() != null : "Endereço deve ter um ID após ser salvo";
        assert enderecoRepository.findById(endereco.getId()).isPresent() : "Endereço deve existir no banco";

        AtualizarEnderecoUsuarioComandoDto dto = new AtualizarEnderecoUsuarioComandoDto();
        dto.setUsuarioId(usuario.getId());
        dto.setRua("Rua das Flores Atualizada");
        dto.setNumero("456");
        dto.setCep("04567-890");

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .put("/enderecos/" + endereco.getId())
        .then()
            .statusCode(200);
    }

    @Test
    void deveRetornarErroAoAtualizarEnderecoInexistente() throws Exception {
        AtualizarEnderecoUsuarioComandoDto dto = new AtualizarEnderecoUsuarioComandoDto();
        dto.setUsuarioId(usuario.getId());
        dto.setRua("Rua das Flores Atualizada");
        dto.setNumero("456");
        dto.setCep("04567-890");

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .put("/enderecos/999")
        .then()
            .statusCode(400);
    }

    @Test
    void deveRetornarErroAoAtualizarEnderecoDeOutroUsuario() throws Exception {
        Usuario outroUsuario = new Usuario();
        outroUsuario.setNome("Maria Silva");
        outroUsuario.setEmail("maria@email.com");
        outroUsuario.setLogin("maria123");
        outroUsuario.setSenha(passwordEncoder.encode("senha456"));
        outroUsuario.setTipo(TipoUsuario.CLIENTE);
        outroUsuario = usuarioRepository.save(outroUsuario);

        Endereco endereco = new Endereco();
        endereco.setUsuario(usuario);
        endereco.setRua("Rua das Flores");
        endereco.setNumero("123");
        endereco.setCep("01234-567");
        endereco = enderecoRepository.save(endereco);

        AtualizarEnderecoUsuarioComandoDto dto = new AtualizarEnderecoUsuarioComandoDto();
        dto.setUsuarioId(outroUsuario.getId());
        dto.setRua("Rua das Flores Atualizada");
        dto.setNumero("456");
        dto.setCep("04567-890");

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .put("/enderecos/" + endereco.getId())
        .then()
            .statusCode(403);
    }

    @Test
    void deveDeletarEnderecoComSucesso() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setUsuario(usuario);
        endereco.setRua("Rua das Flores");
        endereco.setNumero("123");
        endereco.setCep("01234-567");
        endereco = enderecoRepository.save(endereco);

        DeletarEnderecoUsuarioComandoDto dto = new DeletarEnderecoUsuarioComandoDto();
        dto.setEnderecoId(endereco.getId());
        dto.setUsuarioId(usuario.getId());

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .delete("/enderecos")
        .then()
            .statusCode(200);
    }

    @Test
    void deveRetornarErroAoDeletarEnderecoInexistente() throws Exception {
        DeletarEnderecoUsuarioComandoDto dto = new DeletarEnderecoUsuarioComandoDto();
        dto.setEnderecoId(999L);
        dto.setUsuarioId(usuario.getId());

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .delete("/enderecos")
        .then()
            .statusCode(400);
    }

    @Test
    void deveListarEnderecosPorUsuarioComSucesso()   {
        Endereco endereco1 = new Endereco();
        endereco1.setUsuario(usuario);
        endereco1.setRua("Rua das Flores");
        endereco1.setNumero("123");
        endereco1.setCep("01234-567");
        enderecoRepository.save(endereco1);

        Endereco endereco2 = new Endereco();
        endereco2.setUsuario(usuario);
        endereco2.setRua("Rua das Palmeiras");
        endereco2.setNumero("456");
        endereco2.setCep("04567-890");
        enderecoRepository.save(endereco2);

        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/enderecos/usuario/" + usuario.getId())
        .then()
            .statusCode(200)
            .body("$", hasSize(2))
            .body("[0].rua", equalTo("Rua das Flores"))
            .body("[1].rua", equalTo("Rua das Palmeiras"));
    }

    @Test
    void deveRetornarNoContentQuandoUsuarioNaoTemEnderecos()   {
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/enderecos/usuario/" + usuario.getId())
        .then()
            .statusCode(204);
    }

    @Test
    void deveRetornarErroAoListarEnderecosDeUsuarioInexistente()   {
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/enderecos/usuario/999")
        .then()
            .statusCode(403);
    }

    @Test
    void deveRetornarErroSemAutenticacao() throws Exception {
        CriarEnderecoUsuarioCommandDto dto = new CriarEnderecoUsuarioCommandDto();
        dto.setUsuarioId(usuario.getId());
        dto.setRua("Rua das Flores");
        dto.setNumero("123");
        dto.setCep("01234-567");

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .post("/enderecos")
        .then()
            .statusCode(401);
    }
} 