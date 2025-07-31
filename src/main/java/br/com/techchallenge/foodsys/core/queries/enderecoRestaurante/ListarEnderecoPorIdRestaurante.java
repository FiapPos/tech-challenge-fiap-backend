package br.com.techchallenge.foodsys.core.queries.enderecoRestaurante;

import java.util.List;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.core.queries.params.ListarEnderecosParams;
import br.com.techchallenge.foodsys.core.queries.resultadoItem.enderecoRestaurante.ListarEnderecoPorRestauranteResultadoItem;
import br.com.techchallenge.foodsys.infrastructure.services.ValidarRestauranteExistente;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListarEnderecoPorIdRestaurante {

    private final ValidarRestauranteExistente validarRestauranteExistente;
    private final EnderecoRepository enderecoRepository;

    public List<ListarEnderecoPorRestauranteResultadoItem> execute(ListarEnderecosParams params) {
        Long restauranteId = params.getRestauranteId();
        validarRestauranteExistente.execute(restauranteId);
        List<Endereco> enderecos = buscarEnderecos(restauranteId);
        return mapToResultadoItemList(enderecos);
    }

    private List<Endereco> buscarEnderecos(Long restauranteId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        return enderecoRepository.findByRestauranteId(restauranteId, sort);
    }

    private List<ListarEnderecoPorRestauranteResultadoItem> mapToResultadoItemList(List<Endereco> enderecos) {
        return enderecos.stream()
                .map(this::mapToResultadoItem)
                .toList();
    }

    private ListarEnderecoPorRestauranteResultadoItem mapToResultadoItem(Endereco endereco) {
        return ListarEnderecoPorRestauranteResultadoItem.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .cep(endereco.getCep())
                .numero(endereco.getNumero())
                .dataCriacao(endereco.getDataCriacao())
                .dataAtualizacao(endereco.getDataAtualizacao())
                .build();
    }
}