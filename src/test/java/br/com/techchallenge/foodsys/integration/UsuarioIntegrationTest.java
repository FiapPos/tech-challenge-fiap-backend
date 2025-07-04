package br.com.techchallenge.foodsys.integration;

import br.com.techchallenge.foodsys.comandos.usuario.dtos.CriarUsuarioCommandDto;
import br.com.techchallenge.foodsys.comandos.usuario.dtos.AtualizarUsuarioComandoDto;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class UsuarioIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        // Deletar endereços primeiro para evitar violação de chave estrangeira
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

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveRetornarErroAoCriarUsuarioComEmailDuplicado() throws Exception {
        // Criar primeiro usuário
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senha123");
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuarioRepository.save(usuario);

        // Tentar criar segundo usuário com mesmo email
        CriarUsuarioCommandDto dto = new CriarUsuarioCommandDto();
        dto.setNome("Maria Silva");
        dto.setEmail("joao@email.com");
        dto.setLogin("maria123");
        dto.setSenha("senha456");
        dto.setTipo(TipoUsuario.CLIENTE);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarErroAoCriarUsuarioComLoginDuplicado() throws Exception {
        // Criar primeiro usuário
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senha123");
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuarioRepository.save(usuario);

        // Tentar criar segundo usuário com mesmo login
        CriarUsuarioCommandDto dto = new CriarUsuarioCommandDto();
        dto.setNome("Maria Silva");
        dto.setEmail("maria@email.com");
        dto.setLogin("joao123");
        dto.setSenha("senha456");
        dto.setTipo(TipoUsuario.CLIENTE);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveListarUsuariosComSucesso() throws Exception {
        // Criar usuários de teste
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

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[1].nome").value("Maria Silva"));
    }

    @Test
    void deveRetornarNoContentQuandoNaoHouverUsuarios() throws Exception {
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveAtualizarUsuarioComSucesso() throws Exception {
        // Criar usuário
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senha123");
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuario = usuarioRepository.save(usuario);

        // Atualizar usuário
        AtualizarUsuarioComandoDto dto = new AtualizarUsuarioComandoDto();
        dto.setNome("João Silva Atualizado");
        dto.setEmail("joao.atualizado@email.com");

        mockMvc.perform(put("/usuarios/" + usuario.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornarErroAoAtualizarUsuarioInexistente() throws Exception {
        AtualizarUsuarioComandoDto dto = new AtualizarUsuarioComandoDto();
        dto.setNome("João Silva Atualizado");
        dto.setEmail("joao.atualizado@email.com");

        mockMvc.perform(put("/usuarios/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveDesativarUsuarioComSucesso() throws Exception {
        // Criar usuário
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senha123");
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuario = usuarioRepository.save(usuario);

        mockMvc.perform(delete("/usuarios/" + usuario.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornarErroAoDesativarUsuarioInexistente() throws Exception {
        mockMvc.perform(delete("/usuarios/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarErroAoCriarUsuarioComDadosInvalidos() throws Exception {
        CriarUsuarioCommandDto dto = new CriarUsuarioCommandDto();
        // Dados incompletos
        dto.setNome("João");
        // email e outros campos obrigatórios não preenchidos

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
} 