package br.com.techchallenge.foodsys.integration;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoCommandDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.AtualizarEnderecoComandoDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.DeletarEnderecoComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class EnderecoIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        enderecoRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Criar usuário para os testes com ID 1 para corresponder ao mock
        usuario = new Usuario();
        usuario.setId(1L); // Forçar ID 1 para corresponder ao mock
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senha123");
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuario = usuarioRepository.save(usuario);
    }

    @Test
    @WithMockUser(username = "joao123", roles = "CLIENTE")
    void deveCriarEnderecoComSucesso() throws Exception {
        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setUsuarioId(usuario.getId());
        dto.setRua("Rua das Flores");
        dto.setNumero("123");
        dto.setCep("01234-567");

        mockMvc.perform(post("/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "joao123", roles = "CLIENTE")
    void deveRetornarErroAoCriarEnderecoComUsuarioInexistente() throws Exception {
        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setUsuarioId(999L);
        dto.setRua("Rua das Flores");
        dto.setNumero("123");
        dto.setCep("01234-567");

        mockMvc.perform(post("/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "joao123", roles = "CLIENTE")
    void deveRetornarErroAoCriarEnderecoComDadosInvalidos() throws Exception {
        CriarEnderecoCommandDto dto = new CriarEnderecoCommandDto();
        dto.setUsuarioId(usuario.getId());
        dto.setRua("Rua das Flores");

        mockMvc.perform(post("/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "joao123", roles = "CLIENTE")
    void deveAtualizarEnderecoComSucesso() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setUsuario(usuario);
        endereco.setRua("Rua das Flores");
        endereco.setNumero("123");
        endereco.setCep("01234-567");
        endereco = enderecoRepository.save(endereco);
        
        assert endereco.getId() != null : "Endereço deve ter um ID após ser salvo";
        assert enderecoRepository.findById(endereco.getId()).isPresent() : "Endereço deve existir no banco";

        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setUsuarioId(usuario.getId());
        dto.setRua("Rua das Flores Atualizada");
        dto.setNumero("456");
        dto.setCep("04567-890");

        mockMvc.perform(put("/enderecos/" + endereco.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "joao123", roles = "CLIENTE")
    void deveRetornarErroAoAtualizarEnderecoInexistente() throws Exception {
        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setUsuarioId(usuario.getId());
        dto.setRua("Rua das Flores Atualizada");
        dto.setNumero("456");
        dto.setCep("04567-890"); // CEP diferente para evitar duplicação

        mockMvc.perform(put("/enderecos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "joao123", roles = "CLIENTE")
    void deveRetornarErroAoAtualizarEnderecoDeOutroUsuario() throws Exception {
        Usuario outroUsuario = new Usuario();
        outroUsuario.setNome("Maria Silva");
        outroUsuario.setEmail("maria@email.com");
        outroUsuario.setLogin("maria123");
        outroUsuario.setSenha("senha456");
        outroUsuario.setTipo(TipoUsuario.CLIENTE);
        outroUsuario = usuarioRepository.save(outroUsuario);

        Endereco endereco = new Endereco();
        endereco.setUsuario(usuario);
        endereco.setRua("Rua das Flores");
        endereco.setNumero("123");
        endereco.setCep("01234-567");
        endereco = enderecoRepository.save(endereco);

        AtualizarEnderecoComandoDto dto = new AtualizarEnderecoComandoDto();
        dto.setUsuarioId(outroUsuario.getId());
        dto.setRua("Rua das Flores Atualizada");
        dto.setNumero("456");
        dto.setCep("04567-890");

        mockMvc.perform(put("/enderecos/" + endereco.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "joao123", roles = "CLIENTE")
    void deveDeletarEnderecoComSucesso() throws Exception {
        // Criar endereço
        Endereco endereco = new Endereco();
        endereco.setUsuario(usuario);
        endereco.setRua("Rua das Flores");
        endereco.setNumero("123");
        endereco.setCep("01234-567");
        endereco = enderecoRepository.save(endereco);

        // Deletar endereço
        DeletarEnderecoComandoDto dto = new DeletarEnderecoComandoDto();
        dto.setEnderecoId(endereco.getId());
        dto.setUsuarioId(usuario.getId());

        mockMvc.perform(delete("/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "joao123", roles = "CLIENTE")
    void deveRetornarErroAoDeletarEnderecoInexistente() throws Exception {
        DeletarEnderecoComandoDto dto = new DeletarEnderecoComandoDto();
        dto.setEnderecoId(999L);
        dto.setUsuarioId(usuario.getId());

        mockMvc.perform(delete("/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "joao123", roles = "CLIENTE")
    void deveListarEnderecosPorUsuarioComSucesso() throws Exception {
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

        mockMvc.perform(get("/enderecos/usuario/" + usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].rua").value("Rua das Flores"))
                .andExpect(jsonPath("$[1].rua").value("Rua das Palmeiras"));
    }

    @Test
    @WithMockUser(username = "joao123", roles = "CLIENTE")
    void deveRetornarNoContentQuandoUsuarioNaoTemEnderecos() throws Exception {
        mockMvc.perform(get("/enderecos/usuario/" + usuario.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "joao123", roles = "CLIENTE")
    void deveRetornarErroAoListarEnderecosDeUsuarioInexistente() throws Exception {
        mockMvc.perform(get("/enderecos/usuario/999"))
                .andExpect(status().isForbidden());
    }
} 