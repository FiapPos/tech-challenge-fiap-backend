package br.com.techchallenge.foodsys.comandos.endereco;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.DeletarEnderecoComandoDto;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.ValidarEnderecoExistente;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;
import br.com.techchallenge.foodsys.utils.ValidarProprietarioEndereco;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletarEnderecoComando {

    private final EnderecoRepository enderecoRepository;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarEnderecoExistente validarEnderecoExistente;
    private final ValidarProprietarioEndereco validarProprietarioEndereco;

    public void execute(DeletarEnderecoComandoDto dto, Usuario usuario) {
        validarUsuarioExistente.execute(usuario.getId());
        Endereco endereco = validarEnderecoExistente.execute(dto.getEnderecoId());
        validarProprietarioEndereco.validarProprietarioEndereco(endereco, usuario.getId(), dto.getRestauranteId());
        deletarEndereco(endereco);
    }

    private void deletarEndereco(Endereco endereco) {
        enderecoRepository.deleteById(endereco.getId());
    }
}