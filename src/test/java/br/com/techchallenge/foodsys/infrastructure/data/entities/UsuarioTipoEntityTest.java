package br.com.techchallenge.foodsys.infrastructure.data.entities;

import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTipoEntityTest {

    private UsuarioTipoEntity usuarioTipoEntity;
    private UsuarioTipo usuarioTipo;

    @BeforeEach
    void setUp() {
        usuarioTipoEntity = new UsuarioTipoEntity();
        usuarioTipo = new UsuarioTipo();

        // Configurar dados básicos
        usuarioTipoEntity.setId(1L);
        usuarioTipoEntity.setTipo(TipoUsuario.CLIENTE);

        usuarioTipo.setId(1L);
        usuarioTipo.setTipo(TipoUsuario.CLIENTE);
    }

    @Test
    void deveConverterParaDomainComSucesso() {
        // When
        UsuarioTipo resultado = usuarioTipoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(usuarioTipoEntity.getId(), resultado.getId());
        assertEquals(usuarioTipoEntity.getTipo(), resultado.getTipo());
        assertNull(resultado.getUsuario()); // Não deve definir usuario para evitar loop
    }

    @Test
    void deveConverterDeDomainComSucesso() {
        // When
        UsuarioTipoEntity resultado = UsuarioTipoEntity.fromDomain(usuarioTipo);

        // Then
        assertNotNull(resultado);
        assertEquals(usuarioTipo.getId(), resultado.getId());
        assertEquals(usuarioTipo.getTipo(), resultado.getTipo());
        assertNull(resultado.getUsuario()); // Não deve definir usuario para evitar loop
    }

    @Test
    void deveManterValoresPadrao() {
        // Given
        UsuarioTipoEntity entity = new UsuarioTipoEntity();

        // Then
        assertNull(entity.getId());
        assertNull(entity.getTipo());
        assertNull(entity.getUsuario());
    }

    @Test
    void deveConverterComDiferentesTipos() {
        // Given
        usuarioTipoEntity.setTipo(TipoUsuario.ADMIN);

        // When
        UsuarioTipo resultado = usuarioTipoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(TipoUsuario.ADMIN, resultado.getTipo());
    }

    @Test
    void deveConverterComTodosOsTipos() {
        TipoUsuario[] tipos = {TipoUsuario.ADMIN, TipoUsuario.CLIENTE, TipoUsuario.FUNCIONARIO, TipoUsuario.DONO_RESTAURANTE};

        for (TipoUsuario tipo : tipos) {
            // Given
            usuarioTipoEntity.setTipo(tipo);

            // When
            UsuarioTipo resultado = usuarioTipoEntity.toDomain();

            // Then
            assertNotNull(resultado);
            assertEquals(tipo, resultado.getTipo());
        }
    }

    @Test
    void deveConverterDeDomainComDiferentesTipos() {
        TipoUsuario[] tipos = {TipoUsuario.ADMIN, TipoUsuario.CLIENTE, TipoUsuario.FUNCIONARIO, TipoUsuario.DONO_RESTAURANTE};

        for (TipoUsuario tipo : tipos) {
            // Given
            usuarioTipo.setTipo(tipo);

            // When
            UsuarioTipoEntity resultado = UsuarioTipoEntity.fromDomain(usuarioTipo);

            // Then
            assertNotNull(resultado);
            assertEquals(tipo, resultado.getTipo());
        }
    }

    @Test
    void deveConverterComIdNull() {
        // Given
        usuarioTipoEntity.setId(null);

        // When
        UsuarioTipo resultado = usuarioTipoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertNull(resultado.getId());
        assertEquals(usuarioTipoEntity.getTipo(), resultado.getTipo());
    }

    @Test
    void deveConverterDeDomainComIdNull() {
        // Given
        usuarioTipo.setId(null);

        // When
        UsuarioTipoEntity resultado = UsuarioTipoEntity.fromDomain(usuarioTipo);

        // Then
        assertNotNull(resultado);
        assertNull(resultado.getId());
        assertEquals(usuarioTipo.getTipo(), resultado.getTipo());
    }

    @Test
    void deveConverterComTipoNull() {
        // Given
        usuarioTipoEntity.setTipo(null);

        // When
        UsuarioTipo resultado = usuarioTipoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(usuarioTipoEntity.getId(), resultado.getId());
        assertNull(resultado.getTipo());
    }

    @Test
    void deveConverterDeDomainComTipoNull() {
        // Given
        usuarioTipo.setTipo(null);

        // When
        UsuarioTipoEntity resultado = UsuarioTipoEntity.fromDomain(usuarioTipo);

        // Then
        assertNotNull(resultado);
        assertEquals(usuarioTipo.getId(), resultado.getId());
        assertNull(resultado.getTipo());
    }

    @Test
    void deveTestarGettersESetters() {
        // Given
        UsuarioTipoEntity entity = new UsuarioTipoEntity();
        UsuarioEntity usuarioEntity = new UsuarioEntity();

        // When
        entity.setId(5L);
        entity.setTipo(TipoUsuario.FUNCIONARIO);
        entity.setUsuario(usuarioEntity);

        // Then
        assertEquals(5L, entity.getId());
        assertEquals(TipoUsuario.FUNCIONARIO, entity.getTipo());
        assertEquals(usuarioEntity, entity.getUsuario());
    }

    @Test
    void deveConverterComUsuarioEntity() {
        // Given
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(1L);
        usuarioEntity.setNome("João Silva");
        usuarioTipoEntity.setUsuario(usuarioEntity);

        // When
        UsuarioTipo resultado = usuarioTipoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(usuarioTipoEntity.getId(), resultado.getId());
        assertEquals(usuarioTipoEntity.getTipo(), resultado.getTipo());
        // Usuario não deve ser definido para evitar loop
        assertNull(resultado.getUsuario());
    }

    @Test
    void deveConverterDeDomainComUsuario() {
        // Given
        UsuarioTipo usuarioTipoComUsuario = new UsuarioTipo();
        usuarioTipoComUsuario.setId(2L);
        usuarioTipoComUsuario.setTipo(TipoUsuario.DONO_RESTAURANTE);
        // Não definir usuario para evitar loop

        // When
        UsuarioTipoEntity resultado = UsuarioTipoEntity.fromDomain(usuarioTipoComUsuario);

        // Then
        assertNotNull(resultado);
        assertEquals(usuarioTipoComUsuario.getId(), resultado.getId());
        assertEquals(usuarioTipoComUsuario.getTipo(), resultado.getTipo());
        // Usuario não deve ser definido para evitar loop
        assertNull(resultado.getUsuario());
    }

    @Test
    void deveConverterComIdsDiferentes() {
        // Given
        usuarioTipoEntity.setId(999L);

        // When
        UsuarioTipo resultado = usuarioTipoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(999L, resultado.getId());
    }

    @Test
    void deveConverterDeDomainComIdsDiferentes() {
        // Given
        usuarioTipo.setId(888L);

        // When
        UsuarioTipoEntity resultado = UsuarioTipoEntity.fromDomain(usuarioTipo);

        // Then
        assertNotNull(resultado);
        assertEquals(888L, resultado.getId());
    }
} 