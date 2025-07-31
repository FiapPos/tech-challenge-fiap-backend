package br.com.techchallenge.foodsys.comandos.restaurante;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.techchallenge.foodsys.comandos.restaurante.dtos.AtualizarRestauranteComandoDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.restaurante.RestauranteRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioTipo;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.utils.ValidarProprietarioRestaurante;
import br.com.techchallenge.foodsys.utils.ValidarRestauranteExistente;

public class AtualizarRestauranteComandoTest {

    @Mock
    private RestauranteRepository restauranteRepository;
    @Mock
    private ValidarRestauranteExistente validarRestauranteExistente;
    @Mock
    private ValidarProprietarioRestaurante validarProprietarioRestaurante;
    @InjectMocks
    private AtualizarRestauranteComando atualizarRestauranteComando;
    @Mock
    private CompartilhadoService sharedService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Usuario criarUsuarioComTipo(TipoUsuario tipoUsuario) {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setTipo(tipoUsuario);
        usuarioTipo.setUsuario(usuario);

        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        usuarioTipos.add(usuarioTipo);
        usuario.setUsuarioTipos(usuarioTipos);

        return usuario;
    }

    @Test
    void deveAtualizarRestauranteComSucesso() {
        Long id = 1L;
        AtualizarRestauranteComandoDto dto = new AtualizarRestauranteComandoDto();
        dto.setNome("Restaurante Atualizado");
        dto.setHorarioAbertura("09:00");
        dto.setHorarioFechamento("23:00");
        dto.setTipoCozinha("Mexicana");
        LocalDateTime dataAtualizacao = LocalDateTime.now();

        Usuario usuario = criarUsuarioComTipo(TipoUsuario.ADMIN);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(id);
        restaurante.setUsuario(usuario);

        when(validarRestauranteExistente.execute(id)).thenReturn(restaurante);
        when(sharedService.getCurrentDateTime()).thenReturn(dataAtualizacao);
        when(restauranteRepository.save(any(Restaurante.class))).thenAnswer(i -> i.getArgument(0));

        Restaurante resultado = atualizarRestauranteComando.execute(id, dto, usuario);

        assertEquals(dto.getNome(), resultado.getNome());
        assertEquals(dto.getHorarioAbertura(), resultado.getHorarioAbertura());
        assertEquals(dto.getHorarioFechamento(), resultado.getHorarioFechamento());
        assertEquals(dto.getTipoCozinha(), resultado.getTipoCozinha());
        assertEquals(dataAtualizacao, resultado.getDataAtualizacao());
    }

    @Test
    void deveLancarExcecaoQuandoNenhumCampoForPreenchido() {
        Long id = 1L;
        AtualizarRestauranteComandoDto dto = new AtualizarRestauranteComandoDto();
        Usuario usuario = criarUsuarioComTipo(TipoUsuario.ADMIN);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(id);
        restaurante.setUsuario(usuario);

        when(validarRestauranteExistente.execute(id)).thenReturn(restaurante);

        assertThrows(BadRequestException.class, () -> atualizarRestauranteComando.execute(1L, dto, usuario));
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteNaoExistir() {
        Long id = 1L;
        AtualizarRestauranteComandoDto dto = new AtualizarRestauranteComandoDto();
        dto.setNome("Restaurante Atualizado");
        dto.setHorarioAbertura("09:00");
        dto.setHorarioFechamento("23:00");
        dto.setTipoCozinha("Mexicana");

        Usuario usuario = criarUsuarioComTipo(TipoUsuario.ADMIN);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(id);
        restaurante.setUsuario(usuario);

        doThrow(new BadRequestException("restaurante.nao.encontrado")).when(validarRestauranteExistente)
                .execute(id);

        BadRequestException exception = assertThrows(BadRequestException.class,
            () -> atualizarRestauranteComando.execute(id, dto, usuario));

        assertEquals("restaurante.nao.encontrado", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForProprietario() {
        Long id = 1L;
        AtualizarRestauranteComandoDto dto = new AtualizarRestauranteComandoDto();
        dto.setNome("Restaurante Teste");
        dto.setHorarioAbertura("08:00");
        dto.setHorarioFechamento("22:00");
        dto.setTipoCozinha("Italiana");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Cliente Teste");

        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setTipo(TipoUsuario.CLIENTE);
        usuarioTipo.setUsuario(usuario);

        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        usuarioTipos.add(usuarioTipo);
        usuario.setUsuarioTipos(usuarioTipos);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(id);
        restaurante.setUsuario(usuario);

        when(validarRestauranteExistente.execute(id)).thenReturn(restaurante);
        doThrow(new BadRequestException("restaurante.nao.pertence.ao.usuario")).when(validarProprietarioRestaurante)
                .validarProprietario(restaurante, usuario.getId());

        BadRequestException exception = assertThrows(BadRequestException.class,
            () -> atualizarRestauranteComando.execute(id, dto, usuario));

        assertEquals("restaurante.nao.pertence.ao.usuario", exception.getMessage());
    }

    @Test
    void deveAtualizarRestauranteQuandoApenasNomeForPreenchido() {
        Long id = 1L;
        AtualizarRestauranteComandoDto dto = new AtualizarRestauranteComandoDto();
        dto.setNome("Novo Nome Restaurante");

        Usuario usuario = criarUsuarioComTipo(TipoUsuario.ADMIN);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(id);
        restaurante.setUsuario(usuario);

        when(validarRestauranteExistente.execute(id)).thenReturn(restaurante);
        when(restauranteRepository.save(any(Restaurante.class))).thenAnswer(i -> i.getArgument(0));

        Restaurante resultado = atualizarRestauranteComando.execute(id, dto, usuario);

        assertEquals(dto.getNome(), resultado.getNome());
        verify(restauranteRepository).save(any(Restaurante.class));
    }

    @Test
    void deveAtualizarRestauranteQuandoApenasHorarioAberturaForPreenchido() {
        Long id = 1L;
        AtualizarRestauranteComandoDto dto = new AtualizarRestauranteComandoDto();
        dto.setHorarioAbertura("10:00");

        Usuario usuario = criarUsuarioComTipo(TipoUsuario.ADMIN);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(id);
        restaurante.setUsuario(usuario);

        when(validarRestauranteExistente.execute(id)).thenReturn(restaurante);
        when(restauranteRepository.save(any(Restaurante.class))).thenAnswer(i -> i.getArgument(0));

        Restaurante resultado = atualizarRestauranteComando.execute(id, dto, usuario);

        assertEquals(dto.getHorarioAbertura(), resultado.getHorarioAbertura());
        verify(restauranteRepository).save(any(Restaurante.class));
    }

    @Test
    void deveAtualizarRestauranteQuandoApenasHorarioFechamentoForPreenchido() {
        Long id = 1L;
        AtualizarRestauranteComandoDto dto = new AtualizarRestauranteComandoDto();
        dto.setHorarioFechamento("22:00");

        Usuario usuario = criarUsuarioComTipo(TipoUsuario.ADMIN);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(id);
        restaurante.setUsuario(usuario);

        when(validarRestauranteExistente.execute(id)).thenReturn(restaurante);
        when(restauranteRepository.save(any(Restaurante.class))).thenAnswer(i -> i.getArgument(0));

        Restaurante resultado = atualizarRestauranteComando.execute(id, dto, usuario);

        assertEquals(dto.getHorarioFechamento(), resultado.getHorarioFechamento());
        verify(restauranteRepository).save(any(Restaurante.class));
    }

    @Test
    void deveAtualizarRestauranteQuandoApenasTipoCozinhaForPreenchido() {
        Long id = 1L;
        AtualizarRestauranteComandoDto dto = new AtualizarRestauranteComandoDto();
        dto.setTipoCozinha("Brasileira");

        Usuario usuario = criarUsuarioComTipo(TipoUsuario.ADMIN);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(id);
        restaurante.setUsuario(usuario);

        when(validarRestauranteExistente.execute(id)).thenReturn(restaurante);
        when(restauranteRepository.save(any(Restaurante.class))).thenAnswer(i -> i.getArgument(0));

        Restaurante resultado = atualizarRestauranteComando.execute(id, dto, usuario);

        assertEquals(dto.getTipoCozinha(), resultado.getTipoCozinha());
        verify(restauranteRepository).save(any(Restaurante.class));
    }

}
