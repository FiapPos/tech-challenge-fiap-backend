package br.com.techchallenge.foodsys.comandos.usuario;

import br.com.techchallenge.foodsys.comandos.usuario.dtos.AtualizarUsuarioComandoDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioTipo;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.utils.ValidarEmailExistente;
import br.com.techchallenge.foodsys.utils.ValidarLoginExistente;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
    private CompartilhadoService compartilhadoService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AtualizarUsuarioComando atualizarUsuarioComando;

    private AtualizarUsuarioComandoDto dto;
    private final Long ID_USUARIO = 1L;
    private final LocalDateTime DATA_ATUAL = LocalDateTime.now();

    private Usuario criarUsuarioComTipo(TipoUsuario tipoUsuario) {
        Usuario usuario = new Usuario();
        usuario.setId(ID_USUARIO);
        usuario.setNome("Nome Original");
        usuario.setEmail("original@email.com");
        usuario.setLogin("login_original");
        usuario.setSenha("senha_codificada");
        usuario.setAtivo(true);

        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setTipo(tipoUsuario);
        usuarioTipo.setUsuario(usuario);

        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        usuarioTipos.add(usuarioTipo);
        usuario.setUsuarioTipos(usuarioTipos);

        return usuario;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Usuario usuarioExistente = criarUsuarioComTipo(TipoUsuario.CLIENTE);

        when(validarUsuarioExistente.execute(ID_USUARIO)).thenReturn(usuarioExistente);
        when(compartilhadoService.getCurrentDateTime()).thenReturn(DATA_ATUAL);
        when(passwordEncoder.encode(anyString())).thenReturn("nova_senha_codificada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void deveAtualizarNomeEmailLoginESenha() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setNome("Novo Nome");
        dto.setEmail("novo@email.com");
        dto.setLogin("novo_login");
        dto.setSenha("nova_senha");
        
        doNothing().when(validarEmailExistente).execute("novo@email.com");
        doNothing().when(validarLoginExistente).execute("novo_login");

        Usuario resultado = atualizarUsuarioComando.execute(ID_USUARIO, dto);

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
    void deveAtualizarSomenteNome() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setNome("Novo Nome");

        Usuario resultado = atualizarUsuarioComando.execute(ID_USUARIO, dto);

        assertEquals("Novo Nome", resultado.getNome());
        assertEquals("original@email.com", resultado.getEmail());
        assertEquals("login_original", resultado.getLogin());
        // Verificar se o usuário mantém o tipo CLIENTE
        assertTrue(resultado.getUsuarioTipos().stream()
                .anyMatch(ut -> ut.getTipo() == TipoUsuario.CLIENTE));

        verify(validarEmailExistente, never()).execute(anyString());
        verify(validarLoginExistente, never()).execute(anyString());
    }

    @Test
    void deveLancarExcecaoQuandoNenhumCampoPreenchido() {
        dto = new AtualizarUsuarioComandoDto();

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> atualizarUsuarioComando.execute(ID_USUARIO, dto)
        );

        assertEquals("atualizar.usuario.nenhum.campo", exception.getMessage());
        verify(validarUsuarioExistente, never()).execute(any());
    }

    @Test
    void deveValidarEmailUnicoQuandoEmailForDiferente() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setEmail("novo@email.com");
        
        doNothing().when(validarEmailExistente).execute("novo@email.com");

        atualizarUsuarioComando.execute(ID_USUARIO, dto);

        verify(validarEmailExistente).execute("novo@email.com");
    }

    @Test
    void deveValidarLoginUnicoQuandoLoginForDiferente() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setLogin("novo_login");
        
        doNothing().when(validarLoginExistente).execute("novo_login");

        atualizarUsuarioComando.execute(ID_USUARIO, dto);

        verify(validarLoginExistente).execute("novo_login");
    }

    @Test
    void deveAtualizarSenhaQuandoPreenchida() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setSenha("nova_senha");

        Usuario resultado = atualizarUsuarioComando.execute(ID_USUARIO, dto);

        assertEquals("nova_senha_codificada", resultado.getSenha());
        verify(passwordEncoder).encode("nova_senha");
    }

    @Test
    void naoDeveAtualizarSenhaQuandoVazia() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setSenha("");

        Usuario resultado = atualizarUsuarioComando.execute(ID_USUARIO, dto);

        assertEquals("senha_codificada", resultado.getSenha());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void naoDeveValidarEmailQuandoIgualAoAtual() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setNome("Novo Nome"); // Adicionar um campo para passar na validação
        dto.setEmail("original@email.com"); // Mesmo email do usuário

        atualizarUsuarioComando.execute(ID_USUARIO, dto);

        verify(validarEmailExistente, never()).execute(anyString());
    }

    @Test
    void naoDeveValidarLoginQuandoIgualAoAtual() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setNome("Novo Nome"); // Adicionar um campo para passar na validação
        dto.setLogin("login_original"); // Mesmo login do usuário

        atualizarUsuarioComando.execute(ID_USUARIO, dto);

        verify(validarLoginExistente, never()).execute(anyString());
    }

    @Test
    void naoDeveAtualizarSenhaQuandoNull() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setNome("Novo Nome");
        dto.setSenha(null);

        Usuario resultado = atualizarUsuarioComando.execute(ID_USUARIO, dto);

        assertEquals("senha_codificada", resultado.getSenha());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void deveAtualizarApenasEmail() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setEmail("novo@email.com");
        
        doNothing().when(validarEmailExistente).execute("novo@email.com");

        Usuario resultado = atualizarUsuarioComando.execute(ID_USUARIO, dto);

        assertEquals("Nome Original", resultado.getNome());
        assertEquals("novo@email.com", resultado.getEmail());
        assertEquals("login_original", resultado.getLogin());
        assertEquals("senha_codificada", resultado.getSenha());
    }

    @Test
    void deveAtualizarApenasLogin() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setLogin("novo_login");
        
        doNothing().when(validarLoginExistente).execute("novo_login");

        Usuario resultado = atualizarUsuarioComando.execute(ID_USUARIO, dto);

        assertEquals("Nome Original", resultado.getNome());
        assertEquals("original@email.com", resultado.getEmail());
        assertEquals("novo_login", resultado.getLogin());
        assertEquals("senha_codificada", resultado.getSenha());
    }

    @Test
    void deveAtualizarNomeELogin() {
        dto = new AtualizarUsuarioComandoDto();
        dto.setNome("Novo Nome");
        dto.setLogin("novo_login");
        
        doNothing().when(validarLoginExistente).execute("novo_login");

        Usuario resultado = atualizarUsuarioComando.execute(ID_USUARIO, dto);

        assertEquals("Novo Nome", resultado.getNome());
        assertEquals("original@email.com", resultado.getEmail());
        assertEquals("novo_login", resultado.getLogin());
        assertEquals("senha_codificada", resultado.getSenha());
    }
}