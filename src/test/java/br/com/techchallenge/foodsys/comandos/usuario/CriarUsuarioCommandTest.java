package br.com.techchallenge.foodsys.comandos.usuario;

import br.com.techchallenge.foodsys.comandos.usuario.dtos.CriarUsuarioCommandDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import br.com.techchallenge.foodsys.utils.ValidarEmailExistente;
import br.com.techchallenge.foodsys.utils.ValidarLoginExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CriarUsuarioCommandTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private CompartilhadoService sharedService;
    @Mock
    private ValidarEmailExistente validarEmailExistente;
    @Mock
    private ValidarLoginExistente validarLoginExistente;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private CriarUsuarioCommand criarUsuarioCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarUsuarioComCamposCorretos() {
        CriarUsuarioCommandDto dto = new CriarUsuarioCommandDto();
        dto.setNome("Usuário Teste");
        dto.setEmail("teste@exemplo.com");
        dto.setSenha("senha123");
        dto.setLogin("loginTeste");
        dto.setTipo(TipoUsuario.CLIENTE);
        doNothing().when(validarEmailExistente).execute(dto.getEmail());
        doNothing().when(validarLoginExistente).execute(dto.getLogin());
        when(passwordEncoder.encode("senha123")).thenReturn("senhaCriptografada");
        LocalDateTime dataCriacao = LocalDateTime.now();
        when(sharedService.getCurrentDateTime()).thenReturn(dataCriacao);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario usuario = criarUsuarioCommand.execute(dto);
        assertEquals(dto.getNome(), usuario.getNome());
        assertEquals(dto.getEmail(), usuario.getEmail());
        assertEquals("senhaCriptografada", usuario.getSenha());
        assertEquals(dto.getLogin(), usuario.getLogin());
        assertEquals(dto.getTipo(), usuario.getTipo());
        assertEquals(dataCriacao, usuario.getDataCriacao());
    }

    @Test
    void deveValidarEmailEDuplicidadeDeLogin() {
        CriarUsuarioCommandDto dto = new CriarUsuarioCommandDto();
        dto.setNome("Usuário Teste");
        dto.setEmail("teste@exemplo.com");
        dto.setSenha("senha123");
        dto.setLogin("loginTeste");
        dto.setTipo(TipoUsuario.CLIENTE);
        doNothing().when(validarEmailExistente).execute(dto.getEmail());
        doNothing().when(validarLoginExistente).execute(dto.getLogin());
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCriptografada");
        when(sharedService.getCurrentDateTime()).thenReturn(LocalDateTime.now());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        criarUsuarioCommand.execute(dto);
        verify(validarEmailExistente).execute(dto.getEmail());
        verify(validarLoginExistente).execute(dto.getLogin());
    }
} 