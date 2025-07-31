package br.com.techchallenge.foodsys.core.queries.resultadoItem.enderecoRestaurante;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ListarEnderecoPorRestauranteResultadoItem {
    private Long id;
    private String rua;
    private String cep;
    private String numero;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
