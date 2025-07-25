package br.com.techchallenge.foodsys.comandos.enderecoRestaurante;

import br.com.techchallenge.foodsys.comandos.enderecoRestaurante.dto.DeletarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.utils.ValidarEnderecoExistente;
import br.com.techchallenge.foodsys.utils.ValidarProprietarioEndereco;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;
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