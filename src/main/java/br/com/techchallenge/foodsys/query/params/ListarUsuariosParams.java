package br.com.techchallenge.foodsys.query.params;

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
