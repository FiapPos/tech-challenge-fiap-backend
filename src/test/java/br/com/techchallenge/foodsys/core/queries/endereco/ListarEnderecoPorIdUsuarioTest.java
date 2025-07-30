package br.com.techchallenge.foodsys.core.queries.endereco;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.core.queries.resultadoItem.endereco.ListarEnderecoPorIdUsuarioResultadoItem;
import br.com.techchallenge.foodsys.core.utils.ValidarUsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListarEnderecoPorIdUsuarioTest {
    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private ValidarUsuarioExistente validarUsuarioExistente;
    @InjectMocks
    private ListarEnderecoPorIdUsuario listarEnderecoPorIdUsuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarEnderecosPorUsuarioId() {
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        Endereco endereco = criarEndereco(10L, usuarioId);

        when(enderecoRepository.findByUsuarioId(eq(usuarioId), any(Sort.class))).thenReturn(List.of(endereco));
        when(validarUsuarioExistente.execute(usuarioId)).thenReturn(usuario);

        List<ListarEnderecoPorIdUsuarioResultadoItem> resultado = listarEnderecoPorIdUsuario.execute(usuarioId);

        assertEquals(1, resultado.size());
        ListarEnderecoPorIdUsuarioResultadoItem item = resultado.getFirst();
        assertEquals(endereco.getId(), item.getId());
        assertEquals(endereco.getRua(), item.getRua());
        assertEquals(endereco.getCep(), item.getCep());
        assertEquals(endereco.getNumero(), item.getNumero());
        assertEquals(endereco.getDataCriacao(), item.getDataCriacao());
        assertEquals(endereco.getDataAtualizacao(), item.getDataAtualizacao());
    }

    private Endereco criarEndereco(Long id, Long usuarioId) {
        Endereco endereco = new Endereco();
        endereco.setId(id);
        endereco.setRua("Rua Teste");
        endereco.setCep("12345-678");
        endereco.setNumero("100");
        endereco.setUsuarioId(usuarioId);
        endereco.setDataCriacao(LocalDateTime.now());
        endereco.setDataAtualizacao(LocalDateTime.now());
        return endereco;
    }
} 