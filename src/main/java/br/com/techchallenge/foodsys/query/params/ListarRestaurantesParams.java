package br.com.techchallenge.foodsys.query.params;

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
