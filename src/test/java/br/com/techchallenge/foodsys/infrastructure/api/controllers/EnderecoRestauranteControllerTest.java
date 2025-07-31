package br.com.techchallenge.foodsys.infrastructure.api.controllers;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.usecases.enderecoRestaurante.AtualizarEnderecoRestauranteComando;
import br.com.techchallenge.foodsys.core.domain.usecases.enderecoRestaurante.CriarEnderecoRestauranteComando;
import br.com.techchallenge.foodsys.core.domain.usecases.enderecoRestaurante.DeletarEnderecoRestauranteComando;
import br.com.techchallenge.foodsys.core.dtos.enderecoRestaurante.AtualizarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.dtos.enderecoRestaurante.CriarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.dtos.enderecoRestaurante.DeletarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.core.queries.enderecoRestaurante.ListarEnderecoPorIdRestaurante;
import br.com.techchallenge.foodsys.core.queries.params.ListarEnderecosParams;
import br.com.techchallenge.foodsys.core.queries.resultadoItem.enderecoRestaurante.ListarEnderecoPorRestauranteResultadoItem;
import br.com.techchallenge.foodsys.infrastructure.services.AutorizacaoService;
import br.com.techchallenge.foodsys.core.utils.usuario.ValidadorPermissoes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EnderecoRestauranteControllerTest {
    @Mock
    private CriarEnderecoRestauranteComando criarEnderecoCommand;

    @Mock
    private AtualizarEnderecoRestauranteComando atualizarEnderecoComando;

    @Mock
    private DeletarEnderecoRestauranteComando deletarEnderecoComando;

    @Mock
    private ListarEnderecoPorIdRestaurante listarEnderecosQuery;
    @Mock
    private AutorizacaoService autorizacaoService;
    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private ValidadorPermissoes validadorPermissoes;
    @InjectMocks
    private EnderecoRestauranteController enderecoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarEnderecoComSucesso() {
        CriarEnderecoRestauranteComandoDto dto = new CriarEnderecoRestauranteComandoDto();
        dto.setRua("Rua Teste");
        dto.setCep("12345-678");
        dto.setNumero("100");
        dto.setRestauranteId(1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        Endereco endereco = new Endereco();
        endereco.setId(1L);

        when(criarEnderecoCommand.execute(usuario.getId(), dto)).thenReturn(endereco);

        ResponseEntity<Void> response = enderecoController.criar(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(autorizacaoService).getUsuarioLogado();
        verify(criarEnderecoCommand).execute(usuario.getId(), dto);
    }

    @Test
    void deveAtualizarEnderecoComSucesso() {
        Long enderecoId = 1L;
        AtualizarEnderecoRestauranteComandoDto dto = new AtualizarEnderecoRestauranteComandoDto();
        dto.setRua("Rua Atualizada");
        dto.setCep("98765-432");
        dto.setNumero("200");

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);
        endereco.setUsuario(usuario);

        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);
        when(enderecoRepository.findById(enderecoId)).thenReturn(Optional.of(endereco));
        when(atualizarEnderecoComando.execute(enderecoId, dto, usuario.getId())).thenReturn(endereco);

        ResponseEntity<Void> response = enderecoController.atualizar(enderecoId, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(autorizacaoService).getUsuarioLogado();
        verify(atualizarEnderecoComando).execute(enderecoId, dto, usuario.getId());
    }

    @Test
    void deveDeletarEnderecoComSucesso() {
        DeletarEnderecoRestauranteComandoDto dto = new DeletarEnderecoRestauranteComandoDto();
        dto.setEnderecoId(1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        Endereco endereco = new Endereco();
        endereco.setId(dto.getEnderecoId());
        endereco.setUsuario(usuario);

        when(enderecoRepository.findById(dto.getEnderecoId())).thenReturn(Optional.of(endereco));
        doNothing().when(deletarEnderecoComando).execute(usuario.getId(), dto);

        ResponseEntity<Void> response = enderecoController.deletar(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(autorizacaoService).getUsuarioLogado();
        verify(deletarEnderecoComando).execute(usuario.getId(), dto);
        verify(enderecoRepository).findById(dto.getEnderecoId());
    }

    @Test
    void deveListarEnderecosPorUsuarioComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        doNothing().when(validadorPermissoes).validarVisualizacao();
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        ListarEnderecoPorRestauranteResultadoItem item = ListarEnderecoPorRestauranteResultadoItem.builder()
                .id(1L)
                .rua("Rua Teste")
                .build();
        List<ListarEnderecoPorRestauranteResultadoItem> resultado = List.of(item);

        when(listarEnderecosQuery.execute(any())).thenReturn(resultado);

        ResponseEntity<List<ListarEnderecoPorRestauranteResultadoItem>> response = enderecoController
                .listarEnderecos(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resultado, response.getBody());
        verify(validadorPermissoes).validarVisualizacao();
        verify(autorizacaoService).getUsuarioLogado();
        verify(listarEnderecosQuery).execute(any());
    }

    @Test
    void deveRetornarNoContentQuandoListaVazia() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        doNothing().when(validadorPermissoes).validarVisualizacao();
        when(autorizacaoService.getUsuarioLogado()).thenReturn(usuario);

        ListarEnderecosParams params = new ListarEnderecosParams(usuario.getId(), null);
        when(listarEnderecosQuery.execute(params)).thenReturn(List.of());

        ResponseEntity<List<ListarEnderecoPorRestauranteResultadoItem>> response = enderecoController
                .listarEnderecos(null);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(validadorPermissoes).validarVisualizacao();
        verify(autorizacaoService).getUsuarioLogado();
        verify(listarEnderecosQuery).execute(any(ListarEnderecosParams.class));
    }

}
