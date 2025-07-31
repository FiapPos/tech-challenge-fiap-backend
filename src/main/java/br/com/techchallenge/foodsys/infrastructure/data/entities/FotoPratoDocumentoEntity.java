package br.com.techchallenge.foodsys.infrastructure.data.entities;

import br.com.techchallenge.foodsys.core.domain.entities.FotoPratoDocumento;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "fotos_prato")
public class FotoPratoDocumentoEntity {

    @Id
    private String id;

    private Long restauranteId;
    private Long pratoId;
    private String nomeArquivo;
    private String tipoArquivo;
    private byte[] dados;

    public FotoPratoDocumento toDomain() {
        FotoPratoDocumento documento = new FotoPratoDocumento();
        documento.setId(this.id);
        documento.setRestauranteId(this.restauranteId);
        documento.setPratoId(this.pratoId);
        documento.setNomeArquivo(this.nomeArquivo);
        documento.setTipoArquivo(this.tipoArquivo);
        documento.setDados(this.dados);
        return documento;
    }

    public static FotoPratoDocumentoEntity fromDomain(FotoPratoDocumento documento) {
        FotoPratoDocumentoEntity entity = new FotoPratoDocumentoEntity();
        entity.setId(documento.getId());
        entity.setRestauranteId(documento.getRestauranteId());
        entity.setPratoId(documento.getPratoId());
        entity.setNomeArquivo(documento.getNomeArquivo());
        entity.setTipoArquivo(documento.getTipoArquivo());
        entity.setDados(documento.getDados());
        return entity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public Long getPratoId() {
        return pratoId;
    }

    public void setPratoId(Long pratoId) {
        this.pratoId = pratoId;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }

    public byte[] getDados() {
        return dados;
    }

    public void setDados(byte[] dados) {
        this.dados = dados;
    }
} 