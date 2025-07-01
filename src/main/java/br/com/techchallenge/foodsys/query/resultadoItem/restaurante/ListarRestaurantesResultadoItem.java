package br.com.techchallenge.foodsys.query.resultadoItem.restaurante;

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
    private String horarioFuncionamento;
    private String endereco;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
