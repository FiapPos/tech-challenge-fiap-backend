package br.com.techchallenge.foodsys.query.tipo;

import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import br.com.techchallenge.foodsys.enums.TipoUsuario;
import br.com.techchallenge.foodsys.query.tipo.TipoUsuarioResultItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ListarPorTipoUsuario {

    public List<TipoUsuarioResultItem> execute(Set<UsuarioTipo> usuarioTipos) {
        return usuarioTipos.stream()
                .map(usuarioTipo -> {
                    TipoUsuario tipo = usuarioTipo.getTipo();
                    return TipoUsuarioResultItem.builder()
                            .tipo(tipo)
                            .codigo(tipo.getCodigo())
                            .build();
                })
                .toList();
    }
}
