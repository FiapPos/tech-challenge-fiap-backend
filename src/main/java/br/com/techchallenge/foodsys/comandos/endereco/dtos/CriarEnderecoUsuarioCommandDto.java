package br.com.techchallenge.foodsys.comandos.endereco.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CriarEnderecoUsuarioCommandDto extends CriaEnderecoComandoDto {

    @NotNull(message = "{usuario.id.obrigatorio}")
    private Long usuarioId;
}