package br.com.techchallenge.foodsys.core.domain.usecases.enderecoRestaurante;

import br.com.techchallenge.foodsys.core.dtos.enderecoRestaurante.CriarEnderecoRestauranteComandoDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.core.utils.restaurante.ValidarRestauranteExistente;
import br.com.techchallenge.foodsys.utils.ValidarCepDoUsuario;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarEnderecoRestauranteComando {

    private final EnderecoRepository enderecoRepository;
    private final CompartilhadoService sharedService;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarCepDoUsuario validarCepDoUsuario;
    private final ValidarRestauranteExistente validarRestauranteExistente;

    public Endereco execute(Long usuarioId, CriarEnderecoRestauranteComandoDto criarEnderecoCommandDto) {
        Usuario usuario = validarUsuarioExistente.execute(usuarioId);
        Restaurante restaurante = validarRestauranteExistente.execute(criarEnderecoCommandDto.getRestauranteId());
        validarCepDoUsuario.validarCepDuplicado(usuario.getId(), criarEnderecoCommandDto.getCep());
        Endereco endereco = mapToEntity(criarEnderecoCommandDto, usuario, restaurante);
        return enderecoRepository.save(endereco);
    }

    private Endereco mapToEntity(CriarEnderecoRestauranteComandoDto dto, Usuario usuario, Restaurante restaurante) {
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