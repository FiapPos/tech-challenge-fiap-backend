package br.com.techchallenge.foodsys.core.domain.usecases.enderecoRestaurante;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.dtos.enderecoRestaurante.DeletarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.core.utils.ValidarEnderecoExistente;
import br.com.techchallenge.foodsys.core.utils.ValidarProprietarioEndereco;
import br.com.techchallenge.foodsys.core.utils.ValidarUsuarioExistente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletarEnderecoRestauranteComando {

    private final EnderecoRepository enderecoRepository;
    private final ValidarEnderecoExistente validarEnderecoExistente;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarProprietarioEndereco validarProprietarioEndereco;

    public void execute(Long usuarioId, DeletarEnderecoRestauranteComandoDto dto) {
        validarUsuarioExistente.execute(usuarioId);
        validarProprietarioEndereco.execute(dto.getEnderecoId(), usuarioId);
        Endereco endereco = validarEnderecoExistente.execute(dto.getEnderecoId());
        deletarEndereco(endereco);
    }

    private void deletarEndereco(Endereco endereco) {
        enderecoRepository.deleteById(endereco.getId());
    }
}