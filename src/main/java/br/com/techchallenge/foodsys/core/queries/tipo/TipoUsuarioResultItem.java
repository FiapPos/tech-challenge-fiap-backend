package br.com.techchallenge.foodsys.core.queries.tipo;

import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoUsuarioResultItem {
    private TipoUsuario tipo;
    private int codigo;
}
