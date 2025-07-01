package br.com.techchallenge.foodsys.comandos.usuario.dtos;

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