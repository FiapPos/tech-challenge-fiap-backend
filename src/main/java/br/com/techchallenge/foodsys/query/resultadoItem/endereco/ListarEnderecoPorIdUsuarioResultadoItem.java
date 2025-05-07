package br.com.techchallenge.foodsys.query.resultadoItem.endereco;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ListarEnderecoPorIdUsuarioResultadoItem {
    private Long id;
    private String rua;
    private String cep;
    private String numero;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
