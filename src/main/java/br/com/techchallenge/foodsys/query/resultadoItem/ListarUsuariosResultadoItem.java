package br.com.techchallenge.foodsys.query.resultadoItem;

import br.com.techchallenge.foodsys.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListarUsuariosResultadoItem {
    private Long id;
    private String nome;
    private String email;
    private String login;
    private TipoUsuario tipo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}