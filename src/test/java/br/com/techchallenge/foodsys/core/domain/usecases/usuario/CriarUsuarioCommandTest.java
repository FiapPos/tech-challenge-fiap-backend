package br.com.techchallenge.foodsys.core.domain.usecases.usuario;

import br.com.techchallenge.foodsys.core.dtos.usuario.CriarUsuarioCommandDto;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.gateways.UsuarioRepository;
import br.com.techchallenge.foodsys.core.shared.CompartilhadoService;
import br.com.techchallenge.foodsys.core.utils.ValidarEmailExistente;
import br.com.techchallenge.foodsys.core.utils.ValidarLoginExistente;
import br.com.techchallenge.foodsys.core.utils.usuario.CriarUsuarioBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
    @Mock
    private CriarUsuarioBase criarUsuarioBase;
    @Mock
    private AdicionarTipoUsuarioComando adicionarTipoUsuarioComando;
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
        usuarioDto.setTipos(Arrays.asList(TipoUsuario.CLIENTE));

        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setNome(usuarioDto.getNome());
        usuarioMock.setEmail(usuarioDto.getEmail());

        doNothing().when(validarEmailExistente).execute(usuarioDto.getEmail());
        doNothing().when(validarLoginExistente).execute(usuarioDto.getLogin());
        when(criarUsuarioBase.execute(usuarioDto)).thenReturn(usuarioMock);
        doNothing().when(adicionarTipoUsuarioComando).execute(any(Usuario.class), any(TipoUsuario.class));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = criarUsuarioCommand.execute(usuarioDto);

        verify(validarEmailExistente).execute(usuarioDto.getEmail());
        verify(validarLoginExistente).execute(usuarioDto.getLogin());
        verify(criarUsuarioBase).execute(usuarioDto);
        verify(adicionarTipoUsuarioComando).execute(usuarioMock, TipoUsuario.CLIENTE);
        verify(usuarioRepository).save(usuarioMock);

        assertEquals(usuarioMock, resultado);
    }

    @Test
    void deveValidarEmailEDuplicidadeDeLogin() {
        CriarUsuarioCommandDto usuarioDto = new CriarUsuarioCommandDto();
        usuarioDto.setNome("Usuário Teste");
        usuarioDto.setEmail("teste@exemplo.com");
        usuarioDto.setSenha("senha123");
        usuarioDto.setLogin("loginTeste");
        usuarioDto.setTipos(Arrays.asList(TipoUsuario.CLIENTE));

        doNothing().when(validarEmailExistente).execute(usuarioDto.getEmail());
        doNothing().when(validarLoginExistente).execute(usuarioDto.getLogin());
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCriptografada");
        when(sharedService.getCurrentDateTime()).thenReturn(LocalDateTime.now());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        criarUsuarioCommand.execute(usuarioDto);

        verify(validarEmailExistente).execute(usuarioDto.getEmail());
        verify(validarLoginExistente).execute(usuarioDto.getLogin());
    }

    @Test
    void deveCriarUsuarioComMultiplosTipos() {
        CriarUsuarioCommandDto usuarioDto = new CriarUsuarioCommandDto();
        usuarioDto.setNome("Usuário Admin");
        usuarioDto.setEmail("admin@exemplo.com");
        usuarioDto.setSenha("senha123");
        usuarioDto.setLogin("adminLogin");
        usuarioDto.setTipos(Arrays.asList(TipoUsuario.ADMIN, TipoUsuario.FUNCIONARIO));

        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(2L);
        usuarioMock.setNome(usuarioDto.getNome());
        usuarioMock.setEmail(usuarioDto.getEmail());

        doNothing().when(validarEmailExistente).execute(usuarioDto.getEmail());
        doNothing().when(validarLoginExistente).execute(usuarioDto.getLogin());
        when(criarUsuarioBase.execute(usuarioDto)).thenReturn(usuarioMock);
        doNothing().when(adicionarTipoUsuarioComando).execute(any(Usuario.class), any(TipoUsuario.class));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = criarUsuarioCommand.execute(usuarioDto);

        verify(validarEmailExistente).execute(usuarioDto.getEmail());
        verify(validarLoginExistente).execute(usuarioDto.getLogin());
        verify(criarUsuarioBase).execute(usuarioDto);
        verify(adicionarTipoUsuarioComando).execute(usuarioMock, TipoUsuario.ADMIN);
        verify(adicionarTipoUsuarioComando).execute(usuarioMock, TipoUsuario.FUNCIONARIO);
        verify(usuarioRepository).save(usuarioMock);

        assertEquals(usuarioMock, resultado);
    }
}
