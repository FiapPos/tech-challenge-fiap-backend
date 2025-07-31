package br.com.techchallenge.foodsys.core.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TipoUsuarioTest {

    @Test
    void deveTestarTodosOsValoresDoEnum() {
        // Assert
        assertEquals(4, TipoUsuario.values().length);
        assertEquals(TipoUsuario.ADMIN, TipoUsuario.values()[0]);
        assertEquals(TipoUsuario.CLIENTE, TipoUsuario.values()[1]);
        assertEquals(TipoUsuario.FUNCIONARIO, TipoUsuario.values()[2]);
        assertEquals(TipoUsuario.DONO_RESTAURANTE, TipoUsuario.values()[3]);
    }

    @Test
    void deveTestarGetCodigoParaTodosOsTipos() {
        // Assert
        assertEquals(0, TipoUsuario.ADMIN.getCodigo());
        assertEquals(1, TipoUsuario.CLIENTE.getCodigo());
        assertEquals(2, TipoUsuario.FUNCIONARIO.getCodigo());
        assertEquals(3, TipoUsuario.DONO_RESTAURANTE.getCodigo());
    }

    @Test
    void deveTestarFromCodigoComCodigoValido() {
        // Act & Assert
        assertEquals(TipoUsuario.ADMIN, TipoUsuario.fromCodigo(0));
        assertEquals(TipoUsuario.CLIENTE, TipoUsuario.fromCodigo(1));
        assertEquals(TipoUsuario.FUNCIONARIO, TipoUsuario.fromCodigo(2));
        assertEquals(TipoUsuario.DONO_RESTAURANTE, TipoUsuario.fromCodigo(3));
    }

    @Test
    void deveLancarExcecaoQuandoFromCodigoComCodigoInvalido() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> TipoUsuario.fromCodigo(-1));
        assertThrows(IllegalArgumentException.class, () -> TipoUsuario.fromCodigo(4));
        assertThrows(IllegalArgumentException.class, () -> TipoUsuario.fromCodigo(999));
    }

    @Test
    void deveTestarFromCodigoComCodigoNegativo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> TipoUsuario.fromCodigo(-1));
        assertTrue(exception.getMessage().contains("Código inválido: -1"));
    }

    @Test
    void deveTestarFromCodigoComCodigoMaiorQueMaximo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> TipoUsuario.fromCodigo(10));
        assertTrue(exception.getMessage().contains("Código inválido: 10"));
    }

    @Test
    void deveTestarFromCodigoComCodigoZero() {
        // Act
        TipoUsuario resultado = TipoUsuario.fromCodigo(0);
        
        // Assert
        assertEquals(TipoUsuario.ADMIN, resultado);
        assertEquals(0, resultado.getCodigo());
    }

    @Test
    void deveTestarFromCodigoComCodigoUm() {
        // Act
        TipoUsuario resultado = TipoUsuario.fromCodigo(1);
        
        // Assert
        assertEquals(TipoUsuario.CLIENTE, resultado);
        assertEquals(1, resultado.getCodigo());
    }

    @Test
    void deveTestarFromCodigoComCodigoDois() {
        // Act
        TipoUsuario resultado = TipoUsuario.fromCodigo(2);
        
        // Assert
        assertEquals(TipoUsuario.FUNCIONARIO, resultado);
        assertEquals(2, resultado.getCodigo());
    }

    @Test
    void deveTestarFromCodigoComCodigoTres() {
        // Act
        TipoUsuario resultado = TipoUsuario.fromCodigo(3);
        
        // Assert
        assertEquals(TipoUsuario.DONO_RESTAURANTE, resultado);
        assertEquals(3, resultado.getCodigo());
    }

    @Test
    void deveTestarConstrutorEGetCodigo() {
        // Act & Assert
        TipoUsuario admin = TipoUsuario.ADMIN;
        assertEquals(0, admin.getCodigo());
        
        TipoUsuario cliente = TipoUsuario.CLIENTE;
        assertEquals(1, cliente.getCodigo());
        
        TipoUsuario funcionario = TipoUsuario.FUNCIONARIO;
        assertEquals(2, funcionario.getCodigo());
        
        TipoUsuario dono = TipoUsuario.DONO_RESTAURANTE;
        assertEquals(3, dono.getCodigo());
    }

    @Test
    void deveTestarValoresDoEnum() {
        // Act
        TipoUsuario[] valores = TipoUsuario.values();
        
        // Assert
        assertEquals(4, valores.length);
        assertArrayEquals(new TipoUsuario[]{
            TipoUsuario.ADMIN,
            TipoUsuario.CLIENTE,
            TipoUsuario.FUNCIONARIO,
            TipoUsuario.DONO_RESTAURANTE
        }, valores);
    }

    @Test
    void deveTestarValueOf() {
        // Act & Assert
        assertEquals(TipoUsuario.ADMIN, TipoUsuario.valueOf("ADMIN"));
        assertEquals(TipoUsuario.CLIENTE, TipoUsuario.valueOf("CLIENTE"));
        assertEquals(TipoUsuario.FUNCIONARIO, TipoUsuario.valueOf("FUNCIONARIO"));
        assertEquals(TipoUsuario.DONO_RESTAURANTE, TipoUsuario.valueOf("DONO_RESTAURANTE"));
    }

    @Test
    void deveLancarExcecaoQuandoValueOfComNomeInvalido() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> TipoUsuario.valueOf("INVALIDO"));
        assertThrows(IllegalArgumentException.class, () -> TipoUsuario.valueOf(""));
        assertThrows(NullPointerException.class, () -> TipoUsuario.valueOf(null));
    }

    @Test
    void deveTestarOrdinal() {
        // Act & Assert
        assertEquals(0, TipoUsuario.ADMIN.ordinal());
        assertEquals(1, TipoUsuario.CLIENTE.ordinal());
        assertEquals(2, TipoUsuario.FUNCIONARIO.ordinal());
        assertEquals(3, TipoUsuario.DONO_RESTAURANTE.ordinal());
    }

    @Test
    void deveTestarName() {
        // Act & Assert
        assertEquals("ADMIN", TipoUsuario.ADMIN.name());
        assertEquals("CLIENTE", TipoUsuario.CLIENTE.name());
        assertEquals("FUNCIONARIO", TipoUsuario.FUNCIONARIO.name());
        assertEquals("DONO_RESTAURANTE", TipoUsuario.DONO_RESTAURANTE.name());
    }

    @Test
    void deveTestarToString() {
        // Act & Assert
        assertEquals("ADMIN", TipoUsuario.ADMIN.toString());
        assertEquals("CLIENTE", TipoUsuario.CLIENTE.toString());
        assertEquals("FUNCIONARIO", TipoUsuario.FUNCIONARIO.toString());
        assertEquals("DONO_RESTAURANTE", TipoUsuario.DONO_RESTAURANTE.toString());
    }

    @Test
    void deveTestarComparacaoEntreTipos() {
        // Act & Assert
        assertTrue(TipoUsuario.ADMIN.getCodigo() < TipoUsuario.CLIENTE.getCodigo());
        assertTrue(TipoUsuario.CLIENTE.getCodigo() < TipoUsuario.FUNCIONARIO.getCodigo());
        assertTrue(TipoUsuario.FUNCIONARIO.getCodigo() < TipoUsuario.DONO_RESTAURANTE.getCodigo());
        
        assertFalse(TipoUsuario.ADMIN.getCodigo() > TipoUsuario.CLIENTE.getCodigo());
        assertFalse(TipoUsuario.CLIENTE.getCodigo() > TipoUsuario.FUNCIONARIO.getCodigo());
        assertFalse(TipoUsuario.FUNCIONARIO.getCodigo() > TipoUsuario.DONO_RESTAURANTE.getCodigo());
    }

    @Test
    void deveTestarIgualdadeEntreTipos() {
        // Act & Assert
        assertEquals(TipoUsuario.ADMIN, TipoUsuario.ADMIN);
        assertEquals(TipoUsuario.CLIENTE, TipoUsuario.CLIENTE);
        assertEquals(TipoUsuario.FUNCIONARIO, TipoUsuario.FUNCIONARIO);
        assertEquals(TipoUsuario.DONO_RESTAURANTE, TipoUsuario.DONO_RESTAURANTE);
        
        assertNotEquals(TipoUsuario.ADMIN, TipoUsuario.CLIENTE);
        assertNotEquals(TipoUsuario.CLIENTE, TipoUsuario.FUNCIONARIO);
        assertNotEquals(TipoUsuario.FUNCIONARIO, TipoUsuario.DONO_RESTAURANTE);
    }
} 