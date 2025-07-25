package br.com.techchallenge.foodsys.core.queries.resultadoItem.restaurante;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ListarRestaurantesResultadoItem {
    private Long id;
    private String nome;
    private Long usuarioDonoId;
    private String tipoCozinha;
    private String horarioAbertura;
    private String horarioFechamento;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private LocalDateTime dataDesativacao;
}
