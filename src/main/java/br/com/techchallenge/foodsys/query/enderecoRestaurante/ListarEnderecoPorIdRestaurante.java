package br.com.techchallenge.foodsys.query.enderecoRestaurante;

import java.util.List;

import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.utils.ValidarRestauranteExistente;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.query.params.ListarEnderecosParams;
import br.com.techchallenge.foodsys.query.resultadoItem.enderecoRestaurante.ListarEnderecoPorRestauranteResultadoItem;
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