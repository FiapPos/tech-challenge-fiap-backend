package br.com.techchallenge.foodsys.core.dtos.usuario;

import br.com.techchallenge.foodsys.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class AtualizarUsuarioComandoDto {
    private String nome;

    @Email(message = "Email deve ser válido")
    private String email;

    private String senha;

    private String login;


}