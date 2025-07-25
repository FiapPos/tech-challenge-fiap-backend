package br.com.techchallenge.foodsys.comandos.restaurante;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import br.com.techchallenge.foodsys.core.domain.usecases.restaurante.CriarRestauranteCommand;
import br.com.techchallenge.foodsys.core.utils.restaurante.ValidarRestauranteExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.techchallenge.foodsys.core.dtos.restaurante.CriarRestauranteCommandDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.gateways.RestauranteRepository;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioDono;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;

public class CriarRestauranteCommandTest {

    @Mock
    private RestauranteRepository restauranteRepository;
    @Mock
    private ValidarUsuarioExistente validarUsuarioExistente;
    @Mock
    private ValidarRestauranteExistente validarRestauranteExistente;
    @Mock
    private ValidarUsuarioDono validarUsuarioDono;
    @InjectMocks
    private CriarRestauranteCommand criarRestauranteCommand;
    @Mock
    private CompartilhadoService sharedService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarRestauranteComSucesso() {

        Restaurante restaurante = new Restaurante();
        CriarRestauranteCommandDto dto = new CriarRestauranteCommandDto();
        dto.setNome("Restaurante Teste");
        dto.setHorarioAbertura("08:00");
        dto.setHorarioFechamento("22:00");
        dto.setTipoCozinha("Italiana");
        LocalDateTime dataCriacao = LocalDateTime.now();

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Proprietário Teste");
        usuario.setTipo(TipoUsuario.ADMIN);

        when(validarUsuarioExistente.execute(usuario.getId())).thenReturn(usuario);
        assertDoesNotThrow(() -> validarUsuarioDono.validarUsuarioDono(usuario));
        assertDoesNotThrow(() -> validarRestauranteExistente.validarNomeRestauranteDuplicado(dto.getNome()));

        when(sharedService.getCurrentDateTime()).thenReturn(dataCriacao);
        when(restauranteRepository.save(any(Restaurante.class))).thenAnswer(i -> i.getArgument(0));

        restaurante = criarRestauranteCommand.execute(dto, usuario);

        assertEquals(dto.getNome(), restaurante.getNome());
        assertEquals(dto.getHorarioAbertura(), restaurante.getHorarioAbertura());
        assertEquals(dto.getHorarioFechamento(), restaurante.getHorarioFechamento());
        assertEquals(dto.getTipoCozinha(), restaurante.getTipoCozinha());
        assertEquals(usuario.getId(), restaurante.getUsuarioDonoId());
        assertEquals(dataCriacao, restaurante.getDataCriacao());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {

        CriarRestauranteCommandDto dto = new CriarRestauranteCommandDto();
        dto.setNome("Restaurante Teste");
        dto.setHorarioAbertura("08:00");
        dto.setHorarioFechamento("22:00");
        dto.setTipoCozinha("Italiana");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Inexistente");

        doThrow(new BadRequestException("usuario.nao.encontrado")).when(validarUsuarioExistente)
                .execute(usuario.getId());

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            criarRestauranteCommand.execute(dto, usuario);
        });

        assertEquals("usuario.nao.encontrado", ex.getMessage());
        verify(restauranteRepository, never()).save(any(Restaurante.class));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForDono() {

        CriarRestauranteCommandDto dto = new CriarRestauranteCommandDto();
        dto.setNome("Restaurante Teste");
        dto.setHorarioAbertura("08:00");
        dto.setHorarioFechamento("22:00");
        dto.setTipoCozinha("Italiana");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Cliente Teste");
        usuario.setTipo(TipoUsuario.CLIENTE); // Tipo diferente de ADMIN

        doThrow(new BadRequestException("usuario.nao.e.dono")).when(validarUsuarioDono).validarUsuarioDono(usuario);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            criarRestauranteCommand.execute(dto, usuario);
        });

        assertEquals("usuario.nao.e.dono", ex.getMessage());
        verify(restauranteRepository, never()).save(any(Restaurante.class));
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteJaExistir() {

        CriarRestauranteCommandDto dto = new CriarRestauranteCommandDto();
        dto.setNome("Restaurante Duplicado");
        dto.setHorarioAbertura("08:00");
        dto.setHorarioFechamento("22:00");
        dto.setTipoCozinha("Italiana");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Proprietário Teste");

        doThrow(new BadRequestException("restaurante.duplicado")).when(validarRestauranteExistente)
                .validarNomeRestauranteDuplicado(dto.getNome());

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            criarRestauranteCommand.execute(dto, usuario);
        });

        assertEquals("restaurante.duplicado", ex.getMessage());
        verify(restauranteRepository, never()).save(any(Restaurante.class));
    }
}
