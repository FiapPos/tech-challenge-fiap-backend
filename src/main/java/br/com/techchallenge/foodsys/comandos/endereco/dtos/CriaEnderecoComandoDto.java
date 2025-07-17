package br.com.techchallenge.foodsys.comandos.endereco.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public abstract class CriaEnderecoComandoDto {
    @NotBlank(message = "{rua.obrigatoria}")
    private String rua;

    @NotBlank(message = "{cep.obrigatorio}")
    private String cep;

    @NotBlank(message = "{numero.obrigatorio}")
    private String numero;
}
