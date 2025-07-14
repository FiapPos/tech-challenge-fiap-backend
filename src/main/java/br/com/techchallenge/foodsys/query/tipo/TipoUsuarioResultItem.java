package br.com.techchallenge.foodsys.query.tipo;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
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
