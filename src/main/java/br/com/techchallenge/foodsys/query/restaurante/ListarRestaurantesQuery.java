package br.com.techchallenge.foodsys.query.restaurante;

import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.restaurante.RestauranteRepository;
import br.com.techchallenge.foodsys.query.params.ListarRestaurantesParams;
import br.com.techchallenge.foodsys.query.resultadoItem.restaurante.ListarRestaurantesResultadoItem;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ListarRestaurantesQuery {

    private final RestauranteRepository restauranteRepository;

    public List<ListarRestaurantesResultadoItem> execute(ListarRestaurantesParams params) {
        List<Restaurante> restaurantes;

        boolean temParmAtivo = params.getAtivo() != null;
        boolean temParmTipoCozinha = params.getTipoCozinha() != null && !params.getTipoCozinha().isEmpty();

        if (temParmAtivo && temParmTipoCozinha) {

            restaurantes = restauranteRepository.findByAtivoAndTipoCozinha(
                    params.getAtivo(), params.getTipoCozinha());
        } else if (temParmTipoCozinha) {

            restaurantes = restauranteRepository.findByTipoCozinha(params.getTipoCozinha());
        } else if (temParmAtivo) {

            restaurantes = restauranteRepository.findByAtivo(params.getAtivo(), Sort.by(Sort.Direction.ASC, "id"));
        } else {

            restaurantes = buscarRestaurantes(params);
        }

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
                .build();
    }

    private List<Restaurante> buscarRestaurantes(ListarRestaurantesParams params) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        if (params.getAtivo() == null) {
            return restauranteRepository.findAll(sort);
        }
        return restauranteRepository.findByAtivo(params.getAtivo(), sort);
    }

}
