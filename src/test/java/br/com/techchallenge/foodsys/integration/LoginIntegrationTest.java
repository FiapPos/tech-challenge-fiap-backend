package br.com.techchallenge.foodsys.integration;

import br.com.techchallenge.foodsys.comandos.login.dto.AtualizaCredenciaisComandoDto;
import br.com.techchallenge.foodsys.comandos.login.dto.CredenciaisUsuarioDto;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import static org.mockito.Mockito.*;
import br.com.techchallenge.foodsys.compartilhado.UsuarioLogado;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class LoginIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @MockBean
    private UsuarioLogado usuarioLogado;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        usuarioRepository.deleteAll();
    }

    @Test
    void deveFazerLoginComSucesso() throws Exception {
        // Criar usuário
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha(passwordEncoder.encode("senha123"));
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuarioRepository.save(usuario);

        // Fazer login
        CredenciaisUsuarioDto credentials = new CredenciaisUsuarioDto("joao123", "senha123");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void deveRetornarErroComCredenciaisInvalidas() throws Exception {
        CredenciaisUsuarioDto credentials = new CredenciaisUsuarioDto("usuario_inexistente", "senha_incorreta");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornarErroComSenhaIncorreta() throws Exception {
        // Criar usuário
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha(passwordEncoder.encode("senha123"));
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuarioRepository.save(usuario);

        // Tentar login com senha incorreta
        CredenciaisUsuarioDto credentials = new CredenciaisUsuarioDto("joao123", "senha_incorreta");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornarErroComDadosInvalidos() throws Exception {
        // Teste com dados incompletos - apenas login
        CredenciaisUsuarioDto credentials = new CredenciaisUsuarioDto("joao123", "");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAtualizarSenhaComSucesso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha(passwordEncoder.encode("senha123"));
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuarioRepository.save(usuario);
        when(usuarioLogado.getUsuarioId()).thenReturn(1L);
        AtualizaCredenciaisComandoDto dto = new AtualizaCredenciaisComandoDto("novaSenha123", "novaSenha123");
        mockMvc.perform(put("/login/atualiza-senha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
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
        when(usuarioLogado.getUsuarioId()).thenReturn(1L);
        AtualizaCredenciaisComandoDto dto = new AtualizaCredenciaisComandoDto("novaSenha123", "senhaDiferente");
        mockMvc.perform(put("/login/atualiza-senha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarErroAoAtualizarSenhaComUsuarioInexistente() throws Exception {
        when(usuarioLogado.getUsuarioId()).thenReturn(null);
        AtualizaCredenciaisComandoDto dto = new AtualizaCredenciaisComandoDto("novaSenha123", "novaSenha123");
        mockMvc.perform(put("/login/atualiza-senha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
} 