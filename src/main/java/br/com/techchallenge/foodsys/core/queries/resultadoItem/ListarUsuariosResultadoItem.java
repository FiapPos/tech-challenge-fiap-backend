package br.com.techchallenge.foodsys.core.queries.resultadoItem;

import br.com.techchallenge.foodsys.core.queries.tipo.TipoUsuarioResultItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListarUsuariosResultadoItem {
    private Long id;
    private String nome;
    private String email;
    private String login;
    private List<TipoUsuarioResultItem> tipo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}