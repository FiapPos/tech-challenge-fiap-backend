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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        CriarUsuarioCommandDto usuarioDto = new CriarUsuarioCommandDto();
        usuarioDto.setNome("Usuário Teste");
        usuarioDto.setEmail("teste@exemplo.com");
        usuarioDto.setSenha("senha123");
        usuarioDto.setLogin("loginTeste");
        usuarioDto.setTipo(TipoUsuario.CLIENTE);

        doNothing().when(validarEmailExistente).execute(usuarioDto.getEmail());
        doNothing().when(validarLoginExistente).execute(usuarioDto.getLogin());
        when(passwordEncoder.encode("senha123")).thenReturn("senhaCriptografada");
        LocalDateTime dataCriacao = LocalDateTime.now();
        when(sharedService.getCurrentDateTime()).thenReturn(dataCriacao);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario usuario = criarUsuarioCommand.execute(usuarioDto);
        assertEquals(usuarioDto.getNome(), usuario.getNome());
        assertEquals(usuarioDto.getEmail(), usuario.getEmail());
        assertEquals("senhaCriptografada", usuario.getSenha());
        assertEquals(usuarioDto.getLogin(), usuario.getLogin());
        assertEquals(usuarioDto.getTipo(), usuario.getTipo());
        assertEquals(dataCriacao, usuario.getDataCriacao());
    }

    @Test
    void deveValidarEmailEDuplicidadeDeLogin() {
        CriarUsuarioCommandDto usuarioDto = new CriarUsuarioCommandDto();
        usuarioDto.setNome("Usuário Teste");
        usuarioDto.setEmail("teste@exemplo.com");
        usuarioDto.setSenha("senha123");
        usuarioDto.setLogin("loginTeste");
        usuarioDto.setTipo(TipoUsuario.CLIENTE);

        doNothing().when(validarEmailExistente).execute(usuarioDto.getEmail());
        doNothing().when(validarLoginExistente).execute(usuarioDto.getLogin());
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCriptografada");
        when(sharedService.getCurrentDateTime()).thenReturn(LocalDateTime.now());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        criarUsuarioCommand.execute(usuarioDto);
        verify(validarEmailExistente).execute(usuarioDto.getEmail());
        verify(validarLoginExistente).execute(usuarioDto.getLogin());
    }
} 