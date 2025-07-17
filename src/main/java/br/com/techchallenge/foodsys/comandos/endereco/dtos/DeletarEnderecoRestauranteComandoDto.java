package br.com.techchallenge.foodsys.comandos.endereco.dtos;

import br.com.techchallenge.foodsys.comandos.endereco.DeletarEnderecoComando;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeletarEnderecoRestauranteComandoDto extends DeletarEnderecoComandoDto {
    private Long restauranteId;
}
