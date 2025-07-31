package br.com.techchallenge.foodsys.infrastructure.data.entities;

import br.com.techchallenge.foodsys.core.domain.entities.FotoPratoDocumento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FotoPratoDocumentoEntityTest {

    private FotoPratoDocumentoEntity fotoEntity;
    private FotoPratoDocumento foto;
    private byte[] dadosTeste;

    @BeforeEach
    void setUp() {
        fotoEntity = new FotoPratoDocumentoEntity();
        foto = new FotoPratoDocumento();
        dadosTeste = "dados de teste".getBytes();

        // Configurar dados básicos
        fotoEntity.setId("foto123");
        fotoEntity.setRestauranteId(1L);
        fotoEntity.setPratoId(1L);
        fotoEntity.setNomeArquivo("pizza-margherita.jpg");
        fotoEntity.setTipoArquivo("image/jpeg");
        fotoEntity.setDados(dadosTeste);

        foto.setId("foto123");
        foto.setRestauranteId(1L);
        foto.setPratoId(1L);
        foto.setNomeArquivo("pizza-margherita.jpg");
        foto.setTipoArquivo("image/jpeg");
        foto.setDados(dadosTeste);
    }

    @Test
    void deveConverterParaDomainComSucesso() {
        // When
        FotoPratoDocumento resultado = fotoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(fotoEntity.getId(), resultado.getId());
        assertEquals(fotoEntity.getRestauranteId(), resultado.getRestauranteId());
        assertEquals(fotoEntity.getPratoId(), resultado.getPratoId());
        assertEquals(fotoEntity.getNomeArquivo(), resultado.getNomeArquivo());
        assertEquals(fotoEntity.getTipoArquivo(), resultado.getTipoArquivo());
        assertArrayEquals(fotoEntity.getDados(), resultado.getDados());
    }

    @Test
    void deveConverterDeDomainComSucesso() {
        // When
        FotoPratoDocumentoEntity resultado = FotoPratoDocumentoEntity.fromDomain(foto);

        // Then
        assertNotNull(resultado);
        assertEquals(foto.getId(), resultado.getId());
        assertEquals(foto.getRestauranteId(), resultado.getRestauranteId());
        assertEquals(foto.getPratoId(), resultado.getPratoId());
        assertEquals(foto.getNomeArquivo(), resultado.getNomeArquivo());
        assertEquals(foto.getTipoArquivo(), resultado.getTipoArquivo());
        assertArrayEquals(foto.getDados(), resultado.getDados());
    }

    @Test
    void deveManterValoresPadrao() {
        // Given
        FotoPratoDocumentoEntity entity = new FotoPratoDocumentoEntity();

        // Then
        assertNull(entity.getId());
        assertNull(entity.getRestauranteId());
        assertNull(entity.getPratoId());
        assertNull(entity.getNomeArquivo());
        assertNull(entity.getTipoArquivo());
        assertNull(entity.getDados());
    }

    @Test
    void deveConverterComDadosNull() {
        // Given
        fotoEntity.setDados(null);

        // When
        FotoPratoDocumento resultado = fotoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals(fotoEntity.getId(), resultado.getId());
        assertNull(resultado.getDados());
    }

    @Test
    void deveConverterComDadosVazios() {
        // Given
        byte[] dadosVazios = new byte[0];
        fotoEntity.setDados(dadosVazios);

        // When
        FotoPratoDocumento resultado = fotoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertArrayEquals(dadosVazios, resultado.getDados());
    }

    @Test
    void deveConverterComDadosGrandes() {
        // Given
        byte[] dadosGrandes = new byte[1024 * 1024]; // 1MB
        for (int i = 0; i < dadosGrandes.length; i++) {
            dadosGrandes[i] = (byte) (i % 256);
        }
        fotoEntity.setDados(dadosGrandes);

        // When
        FotoPratoDocumento resultado = fotoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertArrayEquals(dadosGrandes, resultado.getDados());
    }

    @Test
    void deveConverterComDiferentesTiposDeArquivo() {
        // Given
        fotoEntity.setTipoArquivo("image/png");
        fotoEntity.setNomeArquivo("hamburguer.png");

        // When
        FotoPratoDocumento resultado = fotoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals("image/png", resultado.getTipoArquivo());
        assertEquals("hamburguer.png", resultado.getNomeArquivo());
    }

    @Test
    void deveConverterComIdsDiferentes() {
        // Given
        fotoEntity.setId("foto456");
        fotoEntity.setRestauranteId(2L);
        fotoEntity.setPratoId(3L);

        // When
        FotoPratoDocumento resultado = fotoEntity.toDomain();

        // Then
        assertNotNull(resultado);
        assertEquals("foto456", resultado.getId());
        assertEquals(2L, resultado.getRestauranteId());
        assertEquals(3L, resultado.getPratoId());
    }

    @Test
    void deveTestarGettersESetters() {
        // Given
        FotoPratoDocumentoEntity entity = new FotoPratoDocumentoEntity();
        byte[] dados = "teste".getBytes();

        // When
        entity.setId("teste123");
        entity.setRestauranteId(5L);
        entity.setPratoId(10L);
        entity.setNomeArquivo("teste.jpg");
        entity.setTipoArquivo("image/jpeg");
        entity.setDados(dados);

        // Then
        assertEquals("teste123", entity.getId());
        assertEquals(5L, entity.getRestauranteId());
        assertEquals(10L, entity.getPratoId());
        assertEquals("teste.jpg", entity.getNomeArquivo());
        assertEquals("image/jpeg", entity.getTipoArquivo());
        assertArrayEquals(dados, entity.getDados());
    }

    @Test
    void deveConverterDeDomainComDadosNull() {
        // Given
        foto.setDados(null);

        // When
        FotoPratoDocumentoEntity resultado = FotoPratoDocumentoEntity.fromDomain(foto);

        // Then
        assertNotNull(resultado);
        assertEquals(foto.getId(), resultado.getId());
        assertNull(resultado.getDados());
    }

    @Test
    void deveConverterDeDomainComDadosVazios() {
        // Given
        byte[] dadosVazios = new byte[0];
        foto.setDados(dadosVazios);

        // When
        FotoPratoDocumentoEntity resultado = FotoPratoDocumentoEntity.fromDomain(foto);

        // Then
        assertNotNull(resultado);
        assertArrayEquals(dadosVazios, resultado.getDados());
    }

    @Test
    void deveConverterDeDomainComDadosGrandes() {
        // Given
        byte[] dadosGrandes = new byte[1024 * 1024]; // 1MB
        for (int i = 0; i < dadosGrandes.length; i++) {
            dadosGrandes[i] = (byte) (i % 256);
        }
        foto.setDados(dadosGrandes);

        // When
        FotoPratoDocumentoEntity resultado = FotoPratoDocumentoEntity.fromDomain(foto);

        // Then
        assertNotNull(resultado);
        assertArrayEquals(dadosGrandes, resultado.getDados());
    }

    @Test
    void deveConverterDeDomainComDiferentesTiposDeArquivo() {
        // Given
        foto.setTipoArquivo("image/gif");
        foto.setNomeArquivo("sobremesa.gif");

        // When
        FotoPratoDocumentoEntity resultado = FotoPratoDocumentoEntity.fromDomain(foto);

        // Then
        assertNotNull(resultado);
        assertEquals("image/gif", resultado.getTipoArquivo());
        assertEquals("sobremesa.gif", resultado.getNomeArquivo());
    }

    @Test
    void deveConverterDeDomainComIdsDiferentes() {
        // Given
        foto.setId("foto789");
        foto.setRestauranteId(4L);
        foto.setPratoId(6L);

        // When
        FotoPratoDocumentoEntity resultado = FotoPratoDocumentoEntity.fromDomain(foto);

        // Then
        assertNotNull(resultado);
        assertEquals("foto789", resultado.getId());
        assertEquals(4L, resultado.getRestauranteId());
        assertEquals(6L, resultado.getPratoId());
    }
} 