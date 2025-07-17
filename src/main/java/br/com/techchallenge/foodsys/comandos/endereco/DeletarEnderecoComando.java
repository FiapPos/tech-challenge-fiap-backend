package br.com.techchallenge.foodsys.comandos.endereco;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.DeletarEnderecoComandoDto;
import br.com.techchallenge.foodsys.comandos.endereco.dtos.DeletarEnderecoUsuarioComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.utils.ValidarEnderecoExistente;
import br.com.techchallenge.foodsys.utils.ValidarProprietarioEndereco;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletarEnderecoComando {

    private final EnderecoRepository enderecoRepository;
    private final ValidarEnderecoExistente validarEnderecoExistente;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarProprietarioEndereco validarProprietarioEndereco;

    public void execute(Long usuarioId, DeletarEnderecoComandoDto dto) {
        validarUsuarioExistente.execute(usuarioId);
        validarProprietarioEndereco.execute(dto.getEnderecoId(), usuarioId);
        Endereco endereco = validarEnderecoExistente.execute(dto.getEnderecoId());
        deletarEndereco(endereco);
    }

    private void deletarEndereco(Endereco endereco) {
        enderecoRepository.deleteById(endereco.getId());
    }
}