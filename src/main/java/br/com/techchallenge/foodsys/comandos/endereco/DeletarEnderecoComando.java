package br.com.techchallenge.foodsys.comandos.endereco;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.DeletarEnderecoComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.utils.ValidarEnderecoExistente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletarEnderecoComando {

    private final EnderecoRepository enderecoRepository;
    private final ValidarEnderecoExistente validarEnderecoExistente;

    public void execute(DeletarEnderecoComandoDto dto) {

        Endereco endereco = validarEnderecoExistente.execute(dto.getEnderecoId());
        validarProprietarioEndereco(endereco, dto.getUsuarioId(), dto.getRestauranteId());
        deletarEndereco(endereco);
    }

    private void validarProprietarioEndereco(Endereco endereco, Long usuarioId, Long restauranteId) {

        if (restauranteId != null) {

            if (!endereco.getRestaurante().getId().equals(restauranteId)) {
                throw new BadRequestException("endereco.nao.pertence.ao.restaurante");
            }
        } else {
            if (!endereco.getUsuario().getId().equals(usuarioId)) {
                throw new BadRequestException("endereco.nao.pertence.ao.usuario");
            }
        }
    }

    private void deletarEndereco(Endereco endereco) {
        enderecoRepository.deleteById(endereco.getId());
    }
}