package br.com.techchallenge.foodsys.comandos.endereco.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeletarEnderecoUsuarioComandoDto extends DeletarEnderecoComandoDto {
    private Long usuarioId;
}
