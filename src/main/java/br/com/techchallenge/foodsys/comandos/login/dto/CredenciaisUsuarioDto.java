package br.com.techchallenge.foodsys.comandos.login.dto;

import jakarta.validation.constraints.NotBlank;

public record CredenciaisUsuarioDto(@NotBlank String login, @NotBlank String senha) {}