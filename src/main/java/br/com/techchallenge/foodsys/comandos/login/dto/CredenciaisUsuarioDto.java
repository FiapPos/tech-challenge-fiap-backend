package br.com.techchallenge.foodsys.comandos.login.dto;

import jakarta.validation.constraints.NotBlank;

public record CredenciaisUsuarioDto(@NotBlank(message = "{login.nao.nulo}") String login, @NotBlank(message = "{senha.nao.nula}") String senha) {}