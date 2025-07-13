package br.com.techchallenge.foodsys.comandos.endereco;

import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.comandos.endereco.dtos.CriarEnderecoCommandDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.endereco.Endereco;
import br.com.techchallenge.foodsys.dominio.endereco.EnderecoRepository;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.utils.ValidarCepDuplicado;
import br.com.techchallenge.foodsys.utils.ValidarProprietarioRestaurante;
import br.com.techchallenge.foodsys.utils.ValidarRestauranteExistente;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;
import br.com.techchallenge.foodsys.utils.ValidarEnderecoExistente;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CriarEnderecoCommand {

    private final EnderecoRepository enderecoRepository;
    private final CompartilhadoService sharedService;
    private final ValidarCepDuplicado validarCepDuplicado;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarEnderecoExistente validarEnderecoExistente;
    private final ValidarRestauranteExistente validarRestauranteExistente;
    private final ValidarProprietarioRestaurante validarProprietarioRestaurante;

    public Endereco execute(CriarEnderecoCommandDto criarEnderecoCommandDto, Usuario usuario) {
        validarUsuarioExistente.execute(usuario.getId());
        Restaurante restaurante = null;

        if (criarEnderecoCommandDto.getRestauranteId() != null) {
            validarEnderecoExistente.validarEnderecoRestauranteExistente(criarEnderecoCommandDto.getRestauranteId(),
                    usuario.getId());
            restaurante = validarRestauranteExistente.execute(criarEnderecoCommandDto.getRestauranteId());
            validarProprietarioRestaurante.validarProprietario(restaurante, usuario.getId());
        }

        validarCepDuplicado.validarCep(usuario.getId(),
                criarEnderecoCommandDto.getRestauranteId(), criarEnderecoCommandDto.getCep());

        Endereco endereco = mapToEntity(criarEnderecoCommandDto, usuario, restaurante);
        return enderecoRepository.save(endereco);
    }

    private Endereco mapToEntity(CriarEnderecoCommandDto dto, Usuario usuario, Restaurante restaurante) {
        Endereco endereco = new Endereco();
        endereco.setRua(dto.getRua());
        endereco.setCep(dto.getCep());
        endereco.setNumero(dto.getNumero());
        endereco.setUsuario(usuario);
        endereco.setRestaurante(restaurante);
        endereco.setDataCriacao(sharedService.getCurrentDateTime());
        return endereco;
    }

}