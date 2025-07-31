package br.com.techchallenge.foodsys.core.domain.entities;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Restaurante {

    private Long id;
    private String nome;
    private String tipoCozinha;
    private String horarioAbertura;
    private String horarioFechamento;
    private boolean ativo = true;
    private Long usuarioId;
    private Usuario usuario;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private LocalDateTime dataDesativacao;

    public boolean isAtivo() {
        return ativo;
    }
}