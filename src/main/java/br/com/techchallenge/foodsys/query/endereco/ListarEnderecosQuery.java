package br.com.techchallenge.foodsys.query.endereco;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.query.params.ListarEnderecosParams;
import br.com.techchallenge.foodsys.query.resultadoItem.endereco.ListarEnderecoPorResultadoItem;
import br.com.techchallenge.foodsys.utils.ValidarRestauranteExistente;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;
import lombok.RequiredArgsConstructor;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;

@Service
@RequiredArgsConstructor
public class ListarEnderecosQuery {

    private final EnderecoRepository enderecoRepository;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarRestauranteExistente validarRestauranteExistente;

    public List<ListarEnderecoPorResultadoItem> execute(ListarEnderecosParams params) {
        List<Endereco> enderecos;

        Long usuarioId = params.getUsuarioId();
        Long restauranteId = params.getRestauranteId();

        if (restauranteId != null) {
            validarRestauranteExistente.execute(restauranteId);
            enderecos = enderecoRepository.findByRestauranteId(restauranteId);
        } else if (usuarioId != null) {
            validarUsuarioExistente.execute(usuarioId);
            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            enderecos = enderecoRepository.findByUsuarioId(usuarioId, sort);
        } else {
            throw new BadRequestException("usuario.id.ou.restaurante.id.obrigatorio");
        }
        return mapToResultadoItemList(enderecos);
    }

    private List<ListarEnderecoPorResultadoItem> mapToResultadoItemList(List<Endereco> enderecos) {
        return enderecos.stream()
                .map(this::mapToResultadoItem)
                .toList();
    }

    private ListarEnderecoPorResultadoItem mapToResultadoItem(Endereco endereco) {
        return ListarEnderecoPorResultadoItem.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .cep(endereco.getCep())
                .numero(endereco.getNumero())
                .dataCriacao(endereco.getDataCriacao())
                .dataAtualizacao(endereco.getDataAtualizacao())
                .build();
    }
}