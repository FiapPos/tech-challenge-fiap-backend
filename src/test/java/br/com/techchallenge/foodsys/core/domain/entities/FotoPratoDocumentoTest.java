package br.com.techchallenge.foodsys.core.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FotoPratoDocumentoTest {

    private FotoPratoDocumento fotoPratoDocumento;

    @BeforeEach
    void setUp() {
        fotoPratoDocumento = new FotoPratoDocumento();
    }

    @Test
    void deveTestarGettersESettersCompletos() {
        // Arrange
        String id = "foto123";
        Long restauranteId = 1L;
        Long pratoId = 2L;
        String nomeArquivo = "foto_prato.jpg";
        String tipoArquivo = "image/jpeg";
        byte[] dados = "dados_da_imagem".getBytes();

        // Act
        fotoPratoDocumento.setId(id);
        fotoPratoDocumento.setRestauranteId(restauranteId);
        fotoPratoDocumento.setPratoId(pratoId);
        fotoPratoDocumento.setNomeArquivo(nomeArquivo);
        fotoPratoDocumento.setTipoArquivo(tipoArquivo);
        fotoPratoDocumento.setDados(dados);

        // Assert
        assertEquals(id, fotoPratoDocumento.getId());
        assertEquals(restauranteId, fotoPratoDocumento.getRestauranteId());
        assertEquals(pratoId, fotoPratoDocumento.getPratoId());
        assertEquals(nomeArquivo, fotoPratoDocumento.getNomeArquivo());
        assertEquals(tipoArquivo, fotoPratoDocumento.getTipoArquivo());
        assertArrayEquals(dados, fotoPratoDocumento.getDados());
    }

    @Test
    void deveTestarId() {
        // Arrange
        String id = "foto456";

        // Act
        fotoPratoDocumento.setId(id);

        // Assert
        assertEquals(id, fotoPratoDocumento.getId());
    }

    @Test
    void deveTestarRestauranteId() {
        // Arrange
        Long restauranteId = 10L;

        // Act
        fotoPratoDocumento.setRestauranteId(restauranteId);

        // Assert
        assertEquals(restauranteId, fotoPratoDocumento.getRestauranteId());
    }

    @Test
    void deveTestarPratoId() {
        // Arrange
        Long pratoId = 20L;

        // Act
        fotoPratoDocumento.setPratoId(pratoId);

        // Assert
        assertEquals(pratoId, fotoPratoDocumento.getPratoId());
    }

    @Test
    void deveTestarNomeArquivo() {
        // Arrange
        String nomeArquivo = "prato_italiano.png";

        // Act
        fotoPratoDocumento.setNomeArquivo(nomeArquivo);

        // Assert
        assertEquals(nomeArquivo, fotoPratoDocumento.getNomeArquivo());
    }

    @Test
    void deveTestarTipoArquivo() {
        // Arrange
        String tipoArquivo = "image/png";

        // Act
        fotoPratoDocumento.setTipoArquivo(tipoArquivo);

        // Assert
        assertEquals(tipoArquivo, fotoPratoDocumento.getTipoArquivo());
    }

    @Test
    void deveTestarDados() {
        // Arrange
        byte[] dados = "conteudo_da_imagem".getBytes();

        // Act
        fotoPratoDocumento.setDados(dados);

        // Assert
        assertArrayEquals(dados, fotoPratoDocumento.getDados());
    }

    @Test
    void deveTestarValoresNulos() {
        // Act
        fotoPratoDocumento.setId(null);
        fotoPratoDocumento.setRestauranteId(null);
        fotoPratoDocumento.setPratoId(null);
        fotoPratoDocumento.setNomeArquivo(null);
        fotoPratoDocumento.setTipoArquivo(null);
        fotoPratoDocumento.setDados(null);

        // Assert
        assertNull(fotoPratoDocumento.getId());
        assertNull(fotoPratoDocumento.getRestauranteId());
        assertNull(fotoPratoDocumento.getPratoId());
        assertNull(fotoPratoDocumento.getNomeArquivo());
        assertNull(fotoPratoDocumento.getTipoArquivo());
        assertNull(fotoPratoDocumento.getDados());
    }

    @Test
    void deveTestarValoresVazios() {
        // Arrange
        String idVazio = "";
        String nomeArquivoVazio = "";
        String tipoArquivoVazio = "";
        byte[] dadosVazios = new byte[0];

        // Act
        fotoPratoDocumento.setId(idVazio);
        fotoPratoDocumento.setNomeArquivo(nomeArquivoVazio);
        fotoPratoDocumento.setTipoArquivo(tipoArquivoVazio);
        fotoPratoDocumento.setDados(dadosVazios);

        // Assert
        assertEquals(idVazio, fotoPratoDocumento.getId());
        assertEquals(nomeArquivoVazio, fotoPratoDocumento.getNomeArquivo());
        assertEquals(tipoArquivoVazio, fotoPratoDocumento.getTipoArquivo());
        assertArrayEquals(dadosVazios, fotoPratoDocumento.getDados());
    }

    @Test
    void deveTestarValoresLongos() {
        // Arrange
        String idLongo = "foto_muito_longa_com_muitos_caracteres_para_testar_limites_123456789";
        String nomeArquivoLongo = "nome_arquivo_muito_longo_com_extensao_para_testar_limites.jpg";
        String tipoArquivoLongo = "application/octet-stream";
        byte[] dadosLongos = new byte[1000];

        // Act
        fotoPratoDocumento.setId(idLongo);
        fotoPratoDocumento.setNomeArquivo(nomeArquivoLongo);
        fotoPratoDocumento.setTipoArquivo(tipoArquivoLongo);
        fotoPratoDocumento.setDados(dadosLongos);

        // Assert
        assertEquals(idLongo, fotoPratoDocumento.getId());
        assertEquals(nomeArquivoLongo, fotoPratoDocumento.getNomeArquivo());
        assertEquals(tipoArquivoLongo, fotoPratoDocumento.getTipoArquivo());
        assertArrayEquals(dadosLongos, fotoPratoDocumento.getDados());
    }

    @Test
    void deveTestarValoresEspeciais() {
        // Arrange
        String idEspecial = "foto-123_456@789";
        String nomeArquivoEspecial = "foto com espaços e caracteres especiais!@#$%.jpg";
        String tipoArquivoEspecial = "image/svg+xml";
        byte[] dadosEspeciais = "dados com caracteres especiais: áéíóú çãõ".getBytes();

        // Act
        fotoPratoDocumento.setId(idEspecial);
        fotoPratoDocumento.setNomeArquivo(nomeArquivoEspecial);
        fotoPratoDocumento.setTipoArquivo(tipoArquivoEspecial);
        fotoPratoDocumento.setDados(dadosEspeciais);

        // Assert
        assertEquals(idEspecial, fotoPratoDocumento.getId());
        assertEquals(nomeArquivoEspecial, fotoPratoDocumento.getNomeArquivo());
        assertEquals(tipoArquivoEspecial, fotoPratoDocumento.getTipoArquivo());
        assertArrayEquals(dadosEspeciais, fotoPratoDocumento.getDados());
    }
} 