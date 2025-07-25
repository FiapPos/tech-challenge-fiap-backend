package br.com.techchallenge.foodsys.core.queries.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListarUsuariosParams {
    private Boolean ativo;
}
