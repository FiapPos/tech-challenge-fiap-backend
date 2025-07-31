package br.com.techchallenge.foodsys.core.queries.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ListarRestaurantesParams {
    private Boolean ativo;
    private String tipoCozinha;
}
