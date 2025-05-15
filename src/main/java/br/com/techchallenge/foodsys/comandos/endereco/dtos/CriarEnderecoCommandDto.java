package br.com.techchallenge.foodsys.comandos.endereco.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CriarEnderecoCommandDto {
    @NotBlank(message = "{rua.obrigatoria}")
    private String rua;

    @NotBlank(message = "{cep.obrigatorio}")
    private String cep;

    @NotBlank(message = "{numero.obrigatorio}")
    private String numero;

    @NotNull(message = "{usuario.id.obrigatorio}")
    private Long usuarioId;
}