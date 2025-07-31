package br.com.techchallenge.foodsys.core.utils.usuario;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import br.com.techchallenge.foodsys.core.exceptions.BadRequestException;
import br.com.techchallenge.foodsys.core.exceptions.ForbiddenException;
import br.com.techchallenge.foodsys.core.gateways.RestauranteRepository;
import br.com.techchallenge.foodsys.core.utils.AutorizacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidadorPermissoesTest {

    @Mock
    private ValidarAcessoAdmin validarAcessoAdmin;

    @Mock
    private ValidarAcessoDonoRestaurante validarAcessoDonoRestaurante;

    @Mock
    private ValidarAcessoFuncionario validarAcessoFuncionario;

    @Mock
    private ValidarAcessoCliente validarAcessoCliente;

    @Mock
    private AutorizacaoService autorizacaoService;

    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private ValidadorPermissoes validadorPermissoes;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setUsuarioTipos(new HashSet<>());
    }

    @Test
    void deveValidarCriacaoUsuarioComSucesso() {
        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarCriacaoUsuario());

        // Assert
        verify(validarAcessoAdmin).validarOperacaoCompleta();
    }

    @Test
    void deveValidarListagemUsuariosComSucesso() {
        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarListagemUsuarios());

        // Assert
        verify(validarAcessoAdmin).validarOperacaoCompleta();
    }

    @Test
    void deveValidarDesativacaoUsuarioComSucesso() {
        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarDesativacaoUsuario());

        // Assert
        verify(validarAcessoAdmin).validarOperacaoCompleta();
    }

    @Test
    void deveValidarGerenciamentoRestauranteComAdmin() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoAdmin = new UsuarioTipo();
        tipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipos.add(tipoAdmin);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarGerenciamentoRestaurante());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarGerenciamentoRestauranteComDono() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarGerenciamentoRestaurante());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoParaGerenciamentoRestauranteSemPermissao() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        assertThrows(ForbiddenException.class, () -> 
            validadorPermissoes.validarGerenciamentoRestaurante());
        
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarGerenciamentoRestauranteComIdParaAdmin() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoAdmin = new UsuarioTipo();
        tipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipos.add(tipoAdmin);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarGerenciamentoRestaurante(1L));

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarGerenciamentoRestauranteComIdParaDono() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setUsuario(usuario);
        restaurante.setUsuarioId(1L);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarGerenciamentoRestaurante(1L));

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
        verify(restauranteRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoParaGerenciamentoRestauranteComIdSemPermissao() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        assertThrows(ForbiddenException.class, () -> 
            validadorPermissoes.validarGerenciamentoRestaurante(1L));
        
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarGerenciamentoCardapioComAdmin() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoAdmin = new UsuarioTipo();
        tipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipos.add(tipoAdmin);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarGerenciamentoCardapio());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarGerenciamentoCardapioComDono() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarGerenciamentoCardapio());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarGerenciamentoCardapioComFuncionario() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoFuncionario = new UsuarioTipo();
        tipoFuncionario.setTipo(TipoUsuario.FUNCIONARIO);
        usuarioTipos.add(tipoFuncionario);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarGerenciamentoCardapio());

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoParaGerenciamentoCardapioSemPermissao() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        assertThrows(ForbiddenException.class, () -> 
            validadorPermissoes.validarGerenciamentoCardapio());
        
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarGerenciamentoCardapioComIdParaAdmin() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoAdmin = new UsuarioTipo();
        tipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipos.add(tipoAdmin);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarGerenciamentoCardapio(1L));

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarGerenciamentoCardapioComIdParaDono() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setUsuario(usuario);
        restaurante.setUsuarioId(1L);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarGerenciamentoCardapio(1L));

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
        verify(restauranteRepository).findById(1L);
    }

    @Test
    void deveValidarGerenciamentoCardapioComIdParaFuncionario() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoFuncionario = new UsuarioTipo();
        tipoFuncionario.setTipo(TipoUsuario.FUNCIONARIO);
        usuarioTipos.add(tipoFuncionario);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarGerenciamentoCardapio(1L));

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoParaGerenciamentoCardapioComIdSemPermissao() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoCliente = new UsuarioTipo();
        tipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipos.add(tipoCliente);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        // Act & Assert
        assertThrows(ForbiddenException.class, () -> 
            validadorPermissoes.validarGerenciamentoCardapio(1L));
        
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> 
            validadorPermissoes.validarGerenciamentoRestaurante(1L));
        
        verify(autorizacaoService).getUsuarioLogado();
        verify(restauranteRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoProprietarioDoRestaurante() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setUsuarioId(2L); // Usuário diferente
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

        // Act & Assert
        assertThrows(ForbiddenException.class, () -> 
            validadorPermissoes.validarGerenciamentoRestaurante(1L));
        
        verify(autorizacaoService).getUsuarioLogado();
        verify(restauranteRepository).findById(1L);
    }

    @Test
    void deveValidarGerenciamentoRestauranteComIdParaDonoComRestauranteDiferente() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        Restaurante restaurante = new Restaurante();
        restaurante.setId(2L);
        restaurante.setUsuario(usuario);
        restaurante.setUsuarioId(1L);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        when(restauranteRepository.findById(2L)).thenReturn(Optional.of(restaurante));

        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarGerenciamentoRestaurante(2L));

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
        verify(restauranteRepository).findById(2L);
    }

    @Test
    void deveValidarGerenciamentoCardapioComIdParaDonoComRestauranteDiferente() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        Restaurante restaurante = new Restaurante();
        restaurante.setId(2L);
        restaurante.setUsuario(usuario);
        restaurante.setUsuarioId(1L);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        when(restauranteRepository.findById(2L)).thenReturn(Optional.of(restaurante));

        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarGerenciamentoCardapio(2L));

        // Assert
        verify(autorizacaoService).getUsuarioLogado();
        verify(restauranteRepository).findById(2L);
    }

    @Test
    void deveLancarExcecaoParaGerenciamentoCardapioComIdQuandoRestauranteNaoEncontrado() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> 
            validadorPermissoes.validarGerenciamentoCardapio(1L));
        
        verify(autorizacaoService).getUsuarioLogado();
        verify(restauranteRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoParaGerenciamentoCardapioComIdQuandoUsuarioNaoProprietario() {
        // Arrange
        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        UsuarioTipo tipoDono = new UsuarioTipo();
        tipoDono.setTipo(TipoUsuario.DONO_RESTAURANTE);
        usuarioTipos.add(tipoDono);
        usuario.setUsuarioTipos(usuarioTipos);
        
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setUsuarioId(2L); // Usuário diferente
        
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

        // Act & Assert
        assertThrows(ForbiddenException.class, () -> 
            validadorPermissoes.validarGerenciamentoCardapio(1L));
        
        verify(autorizacaoService).getUsuarioLogado();
        verify(restauranteRepository).findById(1L);
    }

    @Test
    void deveValidarVisualizacaoComSucesso() {
        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarVisualizacao());
        
        // Verify
        verify(autorizacaoService).getUsuarioLogado();
    }

    @Test
    void deveValidarGerenciamentoDadosPropriosComSucesso() {
        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarGerenciamentoDadosProprios(1L));
        
        // Verify
        verify(autorizacaoService).getUsuarioLogado();
        verify(autorizacaoService).validarAcessoUsuario(1L);
    }

    @Test
    void deveValidarGerenciamentoDadosPropriosComUsuarioIdDiferente() {
        // Act
        assertDoesNotThrow(() -> validadorPermissoes.validarGerenciamentoDadosProprios(5L));
        
        // Verify
        verify(autorizacaoService).getUsuarioLogado();
        verify(autorizacaoService).validarAcessoUsuario(5L);
    }
} 