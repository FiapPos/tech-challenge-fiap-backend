package br.com.techchallenge.foodsys.infrastructure.data.entities;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoEntityTest {

    private EnderecoEntity enderecoEntity;
    private Endereco endereco;
    private UsuarioEntity usuarioEntity;
    private Usuario usuario;
    private RestauranteEntity restauranteEntity;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        enderecoEntity = new EnderecoEntity();
        endereco = new Endereco();
        usuarioEntity = new UsuarioEntity();
        usuario = new Usuario();
        restauranteEntity = new RestauranteEntity();
        restaurante = new Restaurante();

        // Configurar dados básicos do endereço
        enderecoEntity.setId(1L);
        enderecoEntity.setRua("Rua das Flores");
        enderecoEntity.setCep("12345-678");
        enderecoEntity.setNumero("123");
        enderecoEntity.setUsuarioId(1L);
        enderecoEntity.setDataCriacao(LocalDateTime.now());
        enderecoEntity.setDataAtualizacao(LocalDateTime.now());

        endereco.setId(1L);
        endereco.setRua("Rua das Flores");
        endereco.setCep("12345-678");
        endereco.setNumero("123");
        endereco.setUsuarioId(1L);
        endereco.setDataCriacao(LocalDateTime.now());
        endereco.setDataAtualizacao(LocalDateTime.now());

        // Configurar usuário
        usuarioEntity.setId(1L);
        usuarioEntity.setNome("João Silva");
        usuarioEntity.setEmail("joao@email.com");
        usuarioEntity.setLogin("joao123");
        usuarioEntity.setSenha("senha123");
        usuarioEntity.setAtivo(true);

        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senha123");
        usuario.setAtivo(true);

        // Configurar restaurante
        restauranteEntity.setId(1L);
        restauranteEntity.setNome("Restaurante Teste");
        restauranteEntity.setTipoCozinha("Italiana");
        restauranteEntity.setAtivo(true);

        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setAtivo(true);
    }

    @Test
    void deveConverterParaDomainComSucesso() {
        // Given
        enderecoEntity.setUsuario(usuarioEntity);
        enderecoEntity.setRestaurante(restauranteEntity);

        // When
        Endereco resultado = enderecoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(enderecoEntity.getId(), resultado.getId());
        assertEquals(enderecoEntity.getRua(), resultado.getRua());
        assertEquals(enderecoEntity.getCep(), resultado.getCep());
        assertEquals(enderecoEntity.getNumero(), resultado.getNumero());
        assertEquals(enderecoEntity.getUsuarioId(), resultado.getUsuarioId());
        assertEquals(enderecoEntity.getDataCriacao(), resultado.getDataCriacao());
        assertEquals(enderecoEntity.getDataAtualizacao(), resultado.getDataAtualizacao());
        assertNotNull(resultado.getUsuario());
        assertNotNull(resultado.getRestaurante());
        assertEquals(usuarioEntity.getId(), resultado.getUsuario().getId());
        assertEquals(restauranteEntity.getId(), resultado.getRestaurante().getId());
    }

    @Test
    void deveConverterParaDomainSemUsuario() {
        // Given
        enderecoEntity.setUsuario(null);
        enderecoEntity.setRestaurante(restauranteEntity);

        // When
        Endereco resultado = enderecoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(enderecoEntity.getId(), resultado.getId());
        assertEquals(enderecoEntity.getRua(), resultado.getRua());
        assertNull(resultado.getUsuario());
        assertNotNull(resultado.getRestaurante());
    }

    @Test
    void deveConverterParaDomainSemRestaurante() {
        // Given
        enderecoEntity.setUsuario(usuarioEntity);
        enderecoEntity.setRestaurante(null);

        // When
        Endereco resultado = enderecoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(enderecoEntity.getId(), resultado.getId());
        assertEquals(enderecoEntity.getRua(), resultado.getRua());
        assertNotNull(resultado.getUsuario());
        assertNull(resultado.getRestaurante());
    }

    @Test
    void deveConverterParaDomainSemRelacionamentos() {
        // Given
        enderecoEntity.setUsuario(null);
        enderecoEntity.setRestaurante(null);

        // When
        Endereco resultado = enderecoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(enderecoEntity.getId(), resultado.getId());
        assertEquals(enderecoEntity.getRua(), resultado.getRua());
        assertNull(resultado.getUsuario());
        assertNull(resultado.getRestaurante());
    }

    @Test
    void deveConverterDeDomainComSucesso() {
        // Given
        endereco.setUsuario(usuario);
        endereco.setRestaurante(restaurante);

        // When
        EnderecoEntity resultado = EnderecoEntity.fromDomain(endereco);

        // Then
        assertNotNull(resultado);
        assertEquals(endereco.getId(), resultado.getId());
        assertEquals(endereco.getRua(), resultado.getRua());
        assertEquals(endereco.getCep(), resultado.getCep());
        assertEquals(endereco.getNumero(), resultado.getNumero());
        assertEquals(endereco.getUsuarioId(), resultado.getUsuarioId());
        assertEquals(endereco.getDataCriacao(), resultado.getDataCriacao());
        assertEquals(endereco.getDataAtualizacao(), resultado.getDataAtualizacao());
        assertNotNull(resultado.getUsuario());
        assertNotNull(resultado.getRestaurante());
        assertEquals(usuario.getId(), resultado.getUsuario().getId());
        assertEquals(restaurante.getId(), resultado.getRestaurante().getId());
    }

    @Test
    void deveConverterDeDomainSemUsuario() {
        // Given
        endereco.setUsuario(null);
        endereco.setRestaurante(restaurante);

        // When
        EnderecoEntity resultado = EnderecoEntity.fromDomain(endereco);

        // Then
        assertNotNull(resultado);
        assertEquals(endereco.getId(), resultado.getId());
        assertEquals(endereco.getRua(), resultado.getRua());
        assertNull(resultado.getUsuario());
        assertNotNull(resultado.getRestaurante());
    }

    @Test
    void deveConverterDeDomainSemRestaurante() {
        // Given
        endereco.setUsuario(usuario);
        endereco.setRestaurante(null);

        // When
        EnderecoEntity resultado = EnderecoEntity.fromDomain(endereco);

        // Then
        assertNotNull(resultado);
        assertEquals(endereco.getId(), resultado.getId());
        assertEquals(endereco.getRua(), resultado.getRua());
        assertNotNull(resultado.getUsuario());
        assertNull(resultado.getRestaurante());
    }

    @Test
    void deveConverterDeDomainSemRelacionamentos() {
        // Given
        endereco.setUsuario(null);
        endereco.setRestaurante(null);

        // When
        EnderecoEntity resultado = EnderecoEntity.fromDomain(endereco);

        // Then
        assertNotNull(resultado);
        assertEquals(endereco.getId(), resultado.getId());
        assertEquals(endereco.getRua(), resultado.getRua());
        assertNull(resultado.getUsuario());
        assertNull(resultado.getRestaurante());
    }

    @Test
    void deveManterValoresPadrao() {
        // Given
        EnderecoEntity entity = new EnderecoEntity();

        // Then
        assertNull(entity.getId());
        assertNull(entity.getRua());
        assertNull(entity.getCep());
        assertNull(entity.getNumero());
        assertNull(entity.getUsuarioId());
        assertNull(entity.getUsuario());
        assertNull(entity.getRestaurante());
    }

    @Test
    void deveConverterEnderecoCompleto() {
        // Given
        enderecoEntity.setId(1L);
        enderecoEntity.setRua("Avenida Paulista");
        enderecoEntity.setCep("01310-100");
        enderecoEntity.setNumero("1000");
        enderecoEntity.setUsuarioId(1L);
        enderecoEntity.setUsuario(usuarioEntity);
        enderecoEntity.setRestaurante(restauranteEntity);
        enderecoEntity.setDataCriacao(LocalDateTime.of(2023, 1, 1, 10, 0));
        enderecoEntity.setDataAtualizacao(LocalDateTime.of(2023, 1, 2, 15, 30));

        // When
        Endereco resultado = enderecoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Avenida Paulista", resultado.getRua());
        assertEquals("01310-100", resultado.getCep());
        assertEquals("1000", resultado.getNumero());
        assertEquals(1L, resultado.getUsuarioId());
        assertNotNull(resultado.getUsuario());
        assertNotNull(resultado.getRestaurante());
        assertEquals(LocalDateTime.of(2023, 1, 1, 10, 0), resultado.getDataCriacao());
        assertEquals(LocalDateTime.of(2023, 1, 2, 15, 30), resultado.getDataAtualizacao());
    }
} 