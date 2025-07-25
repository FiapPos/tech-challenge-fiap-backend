package br.com.techchallenge.foodsys.core.dtos.login;

import br.com.techchallenge.foodsys.enums.TipoUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CredenciaisUsuarioDto(
        @NotBlank(message = "{login.nao.nulo}") String login,
        @NotBlank(message = "{senha.nao.nula}") String senha,
        @NotNull(message = "{tipo.usuario.nao.nulo}") TipoUsuario tipoUsuario
) {
}