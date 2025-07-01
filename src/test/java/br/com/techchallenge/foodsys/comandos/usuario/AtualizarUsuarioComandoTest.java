package br.com.techchallenge.foodsys.comandos.usuario;

import br.com.techchallenge.foodsys.comandos.usuario.dtos.AtualizarUsuarioComandoDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.utils.ValidarEmailExistente;
import br.com.techchallenge.foodsys.utils.ValidarLoginExistente;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarUsuarioComandoTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ValidarEmailExistente validarEmailExistente;

    @Mock
    private ValidarLoginExistente validarLoginExistente;

    @Mock
    private ValidarUsuarioExistente validarUsuarioExistente;

    @Mock
    private CompartilhadoService sharedService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AtualizarUsuarioComando comando;

    private Usuario usuarioExistente;
    private AtualizarUsuarioComandoDto dto;
    private final Long ID_USUARIO = 1L;
    private final LocalDateTime DATA_ATUAL = LocalDateTime.now();

    @BeforeEach
    void configurar() {
        usuarioExistente = new Usuario();
        usuarioExistente.setId(ID_USUARIO);
        usuarioExistente.setNome("Nome Original");
        usuarioExistente.setEmail("original@email.com");
        usuarioExistente.setLogin("login_original");
        usuarioExistente.setSenha("senha_codificada");
        usuarioExistente.setTipo(TipoUsuario.CLIENTE);
        usuarioExistente.setAtivo(true);

        lenient().when(validarUsuarioExistente.execute(ID_USUARIO)).thenReturn(usuarioExistente);
        lenient().when(sharedService.getCurrentDateTime()).thenReturn(DATA_ATUAL);
        lenient().when(passwordEncoder.encode(anyString())).thenReturn("nova_senha_codificada");
        lenient().when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void deveAtualizarTodosOsCamposComSucesso() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setNome("Novo Nome");
        dto.setEmail("novo@email.com");
        dto.setLogin("novo_login");
        dto.setSenha("nova_senha");


        Usuario resultado = comando.execute(ID_USUARIO, dto);

        assertNotNull(resultado);
        assertEquals("Novo Nome", resultado.getNome());
        assertEquals("novo@email.com", resultado.getEmail());
        assertEquals("novo_login", resultado.getLogin());
        assertEquals("nova_senha_codificada", resultado.getSenha());
        assertEquals(DATA_ATUAL, resultado.getDataAtualizacao());

        verify(validarEmailExistente).execute("novo@email.com");
        verify(validarLoginExistente).execute("novo_login");
    }

    @Test
    void deveAtualizarApenasCamposPreenchidos() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setNome("Novo Nome");

        Usuario resultado = comando.execute(ID_USUARIO, dto);

        assertEquals("Novo Nome", resultado.getNome());
        assertEquals("original@email.com", resultado.getEmail());
        assertEquals("login_original", resultado.getLogin());
        assertEquals(TipoUsuario.CLIENTE, resultado.getTipo());

        verify(validarEmailExistente, never()).execute(anyString());
        verify(validarLoginExistente, never()).execute(anyString());
    }

    @Test
    void deveLancarExcecaoQuandoNenhumCampoPreenchido() {
        dto = new AtualizarUsuarioComandoDto();

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> comando.execute(ID_USUARIO, dto)
        );

        assertEquals("atualizar.usuario.nenhum.campo", exception.getMessage());
        verify(validarUsuarioExistente, never()).execute(anyLong());
    }

    @Test
    void deveValidarEmailQuandoForAlterado() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setEmail("novo@email.com");

        comando.execute(ID_USUARIO, dto);

        verify(validarEmailExistente).execute("novo@email.com");
    }

    @Test
    void naoDeveValidarEmailQuandoIgual() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setEmail("original@email.com");

        comando.execute(ID_USUARIO, dto);

        verify(validarEmailExistente, never()).execute(anyString());
    }

    @Test
    void deveValidarLoginQuandoForAlterado() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setLogin("novo_login");

        comando.execute(ID_USUARIO, dto);

        verify(validarLoginExistente).execute("novo_login");
    }

    @Test
    void deveAtualizarSenhaQuandoPreenchida() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setSenha("nova_senha");

        Usuario resultado = comando.execute(ID_USUARIO, dto);

        assertEquals("nova_senha_codificada", resultado.getSenha());
        verify(passwordEncoder).encode("nova_senha");
    }

    @Test
    void naoDeveAtualizarSenhaQuandoVazia() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setSenha("");

        Usuario resultado = comando.execute(ID_USUARIO, dto);

        assertEquals("senha_codificada", resultado.getSenha());
        verify(passwordEncoder, never()).encode(anyString());
    }
}