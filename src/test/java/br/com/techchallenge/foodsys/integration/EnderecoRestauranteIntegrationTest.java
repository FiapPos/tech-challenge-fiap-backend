package br.com.techchallenge.foodsys.integration;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.AtualizarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.DeletarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.comandos.login.dto.CredenciaisUsuarioDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.restaurante.RestauranteRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
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

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class EnderecoRestauranteIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

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
        restauranteRepository.deleteAll();
        enderecoRepository.deleteAll();
        usuarioRepository.deleteAll();

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha(passwordEncoder.encode("senha123"));
        usuario.setTipo(TipoUsuario.ADMIN);
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
    void deveCriarEnderecoDoRestauranteComSucesso() throws Exception {

        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Teste");
        restaurante.setUsuario(usuario);
        restaurante.setTipoCozinha("BRASILEIRA");
        restaurante.setHorarioAbertura("08:00");
        restaurante.setHorarioFechamento("22:00");
        restaurante.setAtivo(true);
        restaurante.setDataCriacao(LocalDateTime.now());

        restaurante = restauranteRepository.save(restaurante);

        CriarEnderecoRestauranteComandoDto dto = new CriarEnderecoRestauranteComandoDto();

        dto.setRua("Rua do Restaurante");
        dto.setNumero("789");
        dto.setCep("99999-999");
        dto.setRestauranteId(restaurante.getId());

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(dto))
                .when()
                .post("/enderecoRestaurante")
                .then()
                .statusCode(201);
    }

    @Test
    void deveRetornarErroAoCriarEnderecoComDadosInvalidos() throws Exception {
        CriarEnderecoRestauranteComandoDto dto = new CriarEnderecoRestauranteComandoDto();

        dto.setRua("Rua das Flores");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(dto))
                .when()
                .post("/enderecoRestaurante")
                .then()
                .statusCode(400);
    }

    @Test
    void deveAtualizarEnderecoRestauranteComSucesso() throws Exception {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Atualizar");
        restaurante.setUsuario(usuario);
        restaurante.setTipoCozinha("ITALIANA");
        restaurante.setHorarioAbertura("09:00");
        restaurante.setHorarioFechamento("23:00");
        restaurante.setAtivo(true);
        restaurante.setDataCriacao(LocalDateTime.now());
        restaurante = restauranteRepository.save(restaurante);

        Endereco endereco = new Endereco();
        endereco.setUsuario(usuario);
        endereco.setRestaurante(restaurante);
        endereco.setRua("Rua Original");
        endereco.setNumero("100");
        endereco.setCep("11111-111");
        endereco = enderecoRepository.save(endereco);

        AtualizarEnderecoRestauranteComandoDto dto = new AtualizarEnderecoRestauranteComandoDto();
        dto.setRua("Rua Atualizada");
        dto.setNumero("200");
        dto.setCep("22222-222");
        dto.setRestauranteId(restaurante.getId());

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(dto))
                .when()
                .put("/enderecoRestaurante/" + endereco.getId())
                .then()
                .statusCode(200);
    }

    @Test
    void deveRetornarErroAoAtualizarEnderecoInexistente() throws Exception {
        AtualizarEnderecoRestauranteComandoDto dto = new AtualizarEnderecoRestauranteComandoDto();

        dto.setRua("Rua das Flores Atualizada");
        dto.setNumero("456");
        dto.setCep("04567-890");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(dto))
                .when()
                .put("/enderecoRestaurante/999")
                .then()
                .statusCode(400);
    }

    @Test
    void deveRetornarErroAoAtualizarEnderecoDeRestauranteDeOutroUsuario() throws Exception {
        Usuario outroUsuario = new Usuario();
        outroUsuario.setNome("Maria Silva");
        outroUsuario.setEmail("maria@email.com");
        outroUsuario.setLogin("maria123");
        outroUsuario.setSenha(passwordEncoder.encode("senha456"));
        outroUsuario.setTipo(TipoUsuario.CLIENTE);
        outroUsuario = usuarioRepository.save(outroUsuario);

        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante de Outro Usuário");
        restaurante.setUsuario(outroUsuario);
        restaurante.setTipoCozinha("JAPONESA");
        restaurante.setHorarioAbertura("10:00");
        restaurante.setHorarioFechamento("22:00");
        restaurante.setAtivo(true);
        restaurante.setDataCriacao(LocalDateTime.now());
        restaurante = restauranteRepository.save(restaurante);

        Endereco endereco = new Endereco();
        endereco.setUsuario(outroUsuario);
        endereco.setRestaurante(restaurante);
        endereco.setRua("Rua das Flores");
        endereco.setNumero("123");
        endereco.setCep("01234-567");
        endereco = enderecoRepository.save(endereco);

        AtualizarEnderecoRestauranteComandoDto dto = new AtualizarEnderecoRestauranteComandoDto();
        dto.setRua("Rua das Flores Atualizada");
        dto.setNumero("456");
        dto.setCep("04567-890");
        dto.setRestauranteId(restaurante.getId());

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(dto))
                .when()
                .put("/enderecoRestaurante/" + endereco.getId())
                .then()
                .statusCode(400);
    }

    @Test
    void deveDeletarEnderecoRestauranteComSucesso() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setUsuario(usuario);
        endereco.setRua("Rua das Flores");
        endereco.setNumero("123");
        endereco.setCep("01234-567");
        endereco = enderecoRepository.save(endereco);

        DeletarEnderecoRestauranteComandoDto dto = new DeletarEnderecoRestauranteComandoDto();
        dto.setEnderecoId(endereco.getId());

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(dto))
                .when()
                .delete("/enderecoRestaurante")
                .then()
                .statusCode(200);
    }

    @Test
    void deveRetornarErroAoDeletarEnderecoInexistente() throws Exception {
        DeletarEnderecoRestauranteComandoDto dto = new DeletarEnderecoRestauranteComandoDto();
        dto.setEnderecoId(999L);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(dto))
                .when()
                .delete("/enderecoRestaurante")
                .then()
                .statusCode(400);
    }


    @Test
    void deveListarEnderecosPorRestauranteComSucesso()   {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Listagem");
        restaurante.setUsuario(usuario);
        restaurante.setTipoCozinha("MEXICANA");
        restaurante.setHorarioAbertura("10:00");
        restaurante.setHorarioFechamento("22:00");
        restaurante.setAtivo(true);
        restaurante.setDataCriacao(LocalDateTime.now());
        restaurante = restauranteRepository.save(restaurante);

        Endereco endereco = new Endereco();
        endereco.setUsuario(usuario);
        endereco.setRestaurante(restaurante);
        endereco.setRua("Rua do Restaurante");
        endereco.setNumero("101");
        endereco.setCep("11111-111");
        enderecoRepository.save(endereco);


        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/enderecoRestaurante/restaurante/" + restaurante.getId())
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].rua", equalTo("Rua do Restaurante"))
                .body("[0].numero", equalTo("101"))
                .body("[0].cep", equalTo("11111-111"));
    }

    @Test
    void deveRetornarErroSemAutenticacao() throws Exception {
        CriarEnderecoRestauranteComandoDto dto = new CriarEnderecoRestauranteComandoDto();

        dto.setRua("Rua das Flores");
        dto.setNumero("123");
        dto.setCep("01234-567");

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(dto))
                .when()
                .post("/enderecoRestaurante")
                .then()
                .statusCode(401);
    }
}