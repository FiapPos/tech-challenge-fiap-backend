package br.com.techchallenge.foodsys.query.restaurante;

import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.query.params.ListarRestaurantesParams;
import br.com.techchallenge.foodsys.query.resultadoItem.restaurante.ListarRestaurantesResultadoItem;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.utils.ValidarParametroConsultaRestaurante;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ListarRestaurantesQuery {

    private final ValidarParametroConsultaRestaurante validarParametroConsultaRestaurante;

    public List<ListarRestaurantesResultadoItem> execute(ListarRestaurantesParams params) {
        List<Restaurante> restaurantes;

        restaurantes = validarParametroConsultaRestaurante.validarParametrosConsultaRestaurante(params);

        if (restaurantes.isEmpty()) {
            throw new BadRequestException("nenhum.restaurante.encontrado");
        }

        return mapToResultadoItemList(restaurantes);
    }

    private List<ListarRestaurantesResultadoItem> mapToResultadoItemList(List<Restaurante> restaurantes) {
        return restaurantes.stream()
                .map(this::mapToResultadoItem)
                .toList();
    }

    private ListarRestaurantesResultadoItem mapToResultadoItem(Restaurante restaurante) {
        return ListarRestaurantesResultadoItem.builder()
                .id(restaurante.getId())
                .nome(restaurante.getNome())
                .usuarioDonoId(restaurante.getUsuario().getId())
                .tipoCozinha(restaurante.getTipoCozinha())
                .horarioAbertura(restaurante.getHorarioAbertura())
                .horarioFechamento(restaurante.getHorarioFechamento())
                .ativo(restaurante.isAtivo())
                .dataCriacao(restaurante.getDataCriacao())
                .dataAtualizacao(restaurante.getDataAtualizacao())
                .dataDesativacao(restaurante.getDataDesativacao())
                .build();
    }
}
