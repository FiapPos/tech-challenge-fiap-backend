package br.com.techchallenge.foodsys.core.domain.entities;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Endereco {

    private Long id;
    private String rua;
    private String cep;
    private String numero;
    private Long usuarioId;
    private Usuario usuario;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Restaurante restaurante;
}