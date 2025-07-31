package br.com.techchallenge.foodsys.infrastructure.data.entities;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioEntityTest {

    private UsuarioEntity usuarioEntity;
    private Usuario usuario;
    private UsuarioTipoEntity usuarioTipoEntity;
    private UsuarioTipo usuarioTipo;

    @BeforeEach
    void setUp() {
        usuarioEntity = new UsuarioEntity();
        usuario = new Usuario();
        usuarioTipoEntity = new UsuarioTipoEntity();
        usuarioTipo = new UsuarioTipo();

        // Configurar dados básicos do usuário
        usuarioEntity.setId(1L);
        usuarioEntity.setNome("João Silva");
        usuarioEntity.setEmail("joao@email.com");
        usuarioEntity.setSenha("senha123");
        usuarioEntity.setLogin("joao123");
        usuarioEntity.setAtivo(true);
        usuarioEntity.setDataCriacao(LocalDateTime.now());
        usuarioEntity.setDataAtualizacao(LocalDateTime.now());

        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("senha123");
        usuario.setLogin("joao123");
        usuario.setAtivo(true);
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setDataAtualizacao(LocalDateTime.now());

        // Configurar tipo de usuário
        usuarioTipoEntity.setId(1L);
        usuarioTipoEntity.setTipo(TipoUsuario.CLIENTE);
        usuarioTipoEntity.setUsuario(usuarioEntity);

        usuarioTipo.setId(1L);
        usuarioTipo.setTipo(TipoUsuario.CLIENTE);
        usuarioTipo.setUsuario(usuario);
    }

    @Test
    void deveConverterParaDomainComSucesso() {
        // Given
        Set<UsuarioTipoEntity> tipos = new HashSet<>();
        tipos.add(usuarioTipoEntity);
        usuarioEntity.setUsuarioTipos(tipos);

        // When
        Usuario resultado = usuarioEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(usuarioEntity.getId(), resultado.getId());
        assertEquals(usuarioEntity.getNome(), resultado.getNome());
        assertEquals(usuarioEntity.getEmail(), resultado.getEmail());
        assertEquals(usuarioEntity.getSenha(), resultado.getSenha());
        assertEquals(usuarioEntity.getLogin(), resultado.getLogin());
        assertEquals(usuarioEntity.isAtivo(), resultado.isAtivo());
        assertEquals(usuarioEntity.getDataCriacao(), resultado.getDataCriacao());
        assertEquals(usuarioEntity.getDataAtualizacao(), resultado.getDataAtualizacao());
        assertEquals(usuarioEntity.getDataDesativacao(), resultado.getDataDesativacao());
        assertNotNull(resultado.getUsuarioTipos());
        assertEquals(1, resultado.getUsuarioTipos().size());
    }

    @Test
    void deveConverterParaDomainSemTipos() {
        // Given
        usuarioEntity.setUsuarioTipos(new HashSet<>());

        // When
        Usuario resultado = usuarioEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(usuarioEntity.getId(), resultado.getId());
        assertEquals(usuarioEntity.getNome(), resultado.getNome());
        assertNotNull(resultado.getUsuarioTipos());
        assertTrue(resultado.getUsuarioTipos().isEmpty());
    }

    @Test
    void deveConverterDeDomainComSucesso() {
        // Given
        Set<UsuarioTipo> tipos = new HashSet<>();
        tipos.add(usuarioTipo);
        usuario.setUsuarioTipos(tipos);

        // When
        UsuarioEntity resultado = UsuarioEntity.fromDomain(usuario);

        // Then
        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());
        assertEquals(usuario.getNome(), resultado.getNome());
        assertEquals(usuario.getEmail(), resultado.getEmail());
        assertEquals(usuario.getSenha(), resultado.getSenha());
        assertEquals(usuario.getLogin(), resultado.getLogin());
        assertEquals(usuario.isAtivo(), resultado.isAtivo());
        assertEquals(usuario.getDataCriacao(), resultado.getDataCriacao());
        assertEquals(usuario.getDataAtualizacao(), resultado.getDataAtualizacao());
        assertEquals(usuario.getDataDesativacao(), resultado.getDataDesativacao());
        assertNotNull(resultado.getUsuarioTipos());
        assertEquals(1, resultado.getUsuarioTipos().size());
    }

    @Test
    void deveConverterDeDomainSemTipos() {
        // Given
        usuario.setUsuarioTipos(new HashSet<>());

        // When
        UsuarioEntity resultado = UsuarioEntity.fromDomain(usuario);

        // Then
        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());
        assertEquals(usuario.getNome(), resultado.getNome());
        assertNotNull(resultado.getUsuarioTipos());
        assertTrue(resultado.getUsuarioTipos().isEmpty());
    }

    @Test
    void deveManterValoresPadrao() {
        // Given
        UsuarioEntity entity = new UsuarioEntity();

        // Then
        assertTrue(entity.isAtivo()); // Valor padrão deve ser true
        assertNotNull(entity.getUsuarioTipos()); // Deve inicializar HashSet
        assertTrue(entity.getUsuarioTipos().isEmpty());
        assertNull(entity.getId());
        assertNull(entity.getNome());
        assertNull(entity.getEmail());
    }

    @Test
    void deveConverterUsuarioDesativado() {
        // Given
        usuarioEntity.setAtivo(false);
        usuarioEntity.setDataDesativacao(LocalDateTime.now());

        // When
        Usuario resultado = usuarioEntity.toDomain();

        // Then
        assertFalse(resultado.isAtivo());
        assertNotNull(resultado.getDataDesativacao());
    }

    @Test
    void deveConverterComMultiplosTipos() {
        // Given
        UsuarioTipoEntity tipo1 = new UsuarioTipoEntity();
        tipo1.setId(1L);
        tipo1.setTipo(TipoUsuario.CLIENTE);
        tipo1.setUsuario(usuarioEntity);

        UsuarioTipoEntity tipo2 = new UsuarioTipoEntity();
        tipo2.setId(2L);
        tipo2.setTipo(TipoUsuario.ADMIN);
        tipo2.setUsuario(usuarioEntity);

        Set<UsuarioTipoEntity> tipos = new HashSet<>();
        tipos.add(tipo1);
        tipos.add(tipo2);
        usuarioEntity.setUsuarioTipos(tipos);

        // When
        Usuario resultado = usuarioEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.getUsuarioTipos().size());
        assertTrue(resultado.getUsuarioTipos().stream()
            .anyMatch(tipo -> tipo.getTipo() == TipoUsuario.CLIENTE));
        assertTrue(resultado.getUsuarioTipos().stream()
            .anyMatch(tipo -> tipo.getTipo() == TipoUsuario.ADMIN));
    }

    @Test
    void deveConverterDeDomainComMultiplosTipos() {
        // Given
        UsuarioTipo tipo1 = new UsuarioTipo();
        tipo1.setId(1L);
        tipo1.setTipo(TipoUsuario.CLIENTE);
        tipo1.setUsuario(usuario);

        UsuarioTipo tipo2 = new UsuarioTipo();
        tipo2.setId(2L);
        tipo2.setTipo(TipoUsuario.ADMIN);
        tipo2.setUsuario(usuario);

        Set<UsuarioTipo> tipos = new HashSet<>();
        tipos.add(tipo1);
        tipos.add(tipo2);
        usuario.setUsuarioTipos(tipos);

        // When
        UsuarioEntity resultado = UsuarioEntity.fromDomain(usuario);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.getUsuarioTipos().size());
        assertTrue(resultado.getUsuarioTipos().stream()
            .anyMatch(tipo -> tipo.getTipo() == TipoUsuario.CLIENTE));
        assertTrue(resultado.getUsuarioTipos().stream()
            .anyMatch(tipo -> tipo.getTipo() == TipoUsuario.ADMIN));
    }
} 