package br.com.techchallenge.foodsys.comandos.endereco.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AtualizarEnderecoComandoDto {
    private String rua;
    private String cep;
    private String numero;
    @NotNull(message = "{usuario.id.obrigatorio}")
    private Long usuarioId;
}
