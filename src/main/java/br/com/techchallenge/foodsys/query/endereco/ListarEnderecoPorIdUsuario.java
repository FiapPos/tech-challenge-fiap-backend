package br.com.techchallenge.foodsys.query.endereco;

import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.query.resultadoItem.endereco.ListarEnderecoPorResultadoItem;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarEnderecoPorIdUsuario {

    private final EnderecoRepository enderecoRepository;
    private final ValidarUsuarioExistente validarUsuarioExistente;

    public List<ListarEnderecoPorResultadoItem> execute(Long usuarioId) {

        validarUsuarioExistente.execute(usuarioId);
        List<Endereco> enderecos = buscarEnderecos(usuarioId);
        return mapToResultadoItemList(enderecos);
    }

    private List<Endereco> buscarEnderecos(Long usuarioId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        return enderecoRepository.findByUsuarioId(usuarioId, sort);
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