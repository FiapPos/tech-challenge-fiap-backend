package br.com.techchallenge.foodsys.infrastructure.data.entities;

import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RestauranteEntityTest {

    private RestauranteEntity restauranteEntity;
    private Restaurante restaurante;
    private UsuarioEntity usuarioEntity;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        restauranteEntity = new RestauranteEntity();
        restaurante = new Restaurante();
        usuarioEntity = new UsuarioEntity();
        usuario = new Usuario();

        // Configurar dados básicos
        restauranteEntity.setId(1L);
        restauranteEntity.setNome("Restaurante Teste");
        restauranteEntity.setTipoCozinha("Italiana");
        restauranteEntity.setHorarioAbertura("08:00");
        restauranteEntity.setHorarioFechamento("22:00");
        restauranteEntity.setAtivo(true);
        restauranteEntity.setUsuarioId(1L);
        restauranteEntity.setDataCriacao(LocalDateTime.now());
        restauranteEntity.setDataAtualizacao(LocalDateTime.now());

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
    }

    @Test
    void deveConverterParaDomainComSucesso() {
        // Given
        restauranteEntity.setUsuario(usuarioEntity);

        // When
        Restaurante resultado = restauranteEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(restauranteEntity.getId(), resultado.getId());
        assertEquals(restauranteEntity.getNome(), resultado.getNome());
        assertEquals(restauranteEntity.getTipoCozinha(), resultado.getTipoCozinha());
        assertEquals(restauranteEntity.getHorarioAbertura(), resultado.getHorarioAbertura());
        assertEquals(restauranteEntity.getHorarioFechamento(), resultado.getHorarioFechamento());
        assertEquals(restauranteEntity.isAtivo(), resultado.isAtivo());
        assertEquals(restauranteEntity.getUsuarioId(), resultado.getUsuarioId());
        assertEquals(restauranteEntity.getDataCriacao(), resultado.getDataCriacao());
        assertEquals(restauranteEntity.getDataAtualizacao(), resultado.getDataAtualizacao());
        assertEquals(restauranteEntity.getDataDesativacao(), resultado.getDataDesativacao());
        assertNotNull(resultado.getUsuario());
        assertEquals(usuarioEntity.getId(), resultado.getUsuario().getId());
    }

    @Test
    void deveConverterParaDomainSemUsuario() {
        // Given
        restauranteEntity.setUsuario(null);

        // When
        Restaurante resultado = restauranteEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(restauranteEntity.getId(), resultado.getId());
        assertEquals(restauranteEntity.getNome(), resultado.getNome());
        assertNull(resultado.getUsuario());
    }

    @Test
    void deveConverterDeDomainComSucesso() {
        // Given
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setHorarioAbertura("08:00");
        restaurante.setHorarioFechamento("22:00");
        restaurante.setAtivo(true);
        restaurante.setUsuarioId(1L);
        restaurante.setUsuario(usuario);
        restaurante.setDataCriacao(LocalDateTime.now());
        restaurante.setDataAtualizacao(LocalDateTime.now());

        // When
        RestauranteEntity resultado = RestauranteEntity.fromDomain(restaurante);

        // Then
        assertNotNull(resultado);
        assertEquals(restaurante.getId(), resultado.getId());
        assertEquals(restaurante.getNome(), resultado.getNome());
        assertEquals(restaurante.getTipoCozinha(), resultado.getTipoCozinha());
        assertEquals(restaurante.getHorarioAbertura(), resultado.getHorarioAbertura());
        assertEquals(restaurante.getHorarioFechamento(), resultado.getHorarioFechamento());
        assertEquals(restaurante.isAtivo(), resultado.isAtivo());
        assertEquals(restaurante.getUsuarioId(), resultado.getUsuarioId());
        assertEquals(restaurante.getDataCriacao(), resultado.getDataCriacao());
        assertEquals(restaurante.getDataAtualizacao(), resultado.getDataAtualizacao());
        assertEquals(restaurante.getDataDesativacao(), resultado.getDataDesativacao());
        assertNotNull(resultado.getUsuario());
        assertEquals(usuario.getId(), resultado.getUsuario().getId());
    }

    @Test
    void deveConverterDeDomainSemUsuario() {
        // Given
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setHorarioAbertura("08:00");
        restaurante.setHorarioFechamento("22:00");
        restaurante.setAtivo(true);
        restaurante.setUsuarioId(1L);
        restaurante.setUsuario(null);

        // When
        RestauranteEntity resultado = RestauranteEntity.fromDomain(restaurante);

        // Then
        assertNotNull(resultado);
        assertEquals(restaurante.getId(), resultado.getId());
        assertEquals(restaurante.getNome(), resultado.getNome());
        assertNull(resultado.getUsuario());
    }

    @Test
    void deveManterValoresPadrao() {
        // Given
        RestauranteEntity entity = new RestauranteEntity();

        // Then
        assertTrue(entity.isAtivo()); // Valor padrão deve ser true
        assertNull(entity.getId());
        assertNull(entity.getNome());
        assertNull(entity.getTipoCozinha());
    }

    @Test
    void deveConverterRestauranteDesativado() {
        // Given
        restauranteEntity.setAtivo(false);
        restauranteEntity.setDataDesativacao(LocalDateTime.now());

        // When
        Restaurante resultado = restauranteEntity.toDomain();

        // Then
        assertFalse(resultado.isAtivo());
        assertNotNull(resultado.getDataDesativacao());
    }
} 