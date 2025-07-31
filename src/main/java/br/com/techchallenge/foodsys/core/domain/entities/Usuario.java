package br.com.techchallenge.foodsys.core.domain.entities;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class Usuario {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String login;
    private Set<UsuarioTipo> usuarioTipos = new HashSet<>();
    private boolean ativo = true;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private LocalDateTime dataDesativacao;

    public void trocaSenha(String novaSenha) {
        setSenha(novaSenha);
        this.dataAtualizacao = LocalDateTime.now();
    }

    public boolean isAtivo() {
        return ativo;
    }
}