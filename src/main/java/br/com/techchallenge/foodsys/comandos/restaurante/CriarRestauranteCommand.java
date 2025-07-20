package br.com.techchallenge.foodsys.comandos.restaurante;

import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.comandos.restaurante.dtos.CriarRestauranteCommandDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.restaurante.RestauranteRepository;
import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioExistente;
import br.com.techchallenge.foodsys.utils.ValidarRestauranteExistente;
import br.com.techchallenge.foodsys.utils.ValidarUsuarioDono;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CriarRestauranteCommand {

    private final RestauranteRepository restauranteRepository;
    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarRestauranteExistente validarRestauranteExistente;
    private final ValidarUsuarioDono validarUsuarioDono;
    private final CompartilhadoService sharedService;

    public Restaurante execute(CriarRestauranteCommandDto criarRestauranteCommandDto, Usuario usuario) {

        validarUsuarioExistente.execute(usuario.getId());
        validarUsuarioDono.validarUsuarioDono(usuario);
        validarRestauranteExistente.validarNomeRestauranteDuplicado(criarRestauranteCommandDto.getNome());

        Restaurante restaurante = mapToEntity(criarRestauranteCommandDto, usuario);
        return restauranteRepository.save(restaurante);
    }

    private Restaurante mapToEntity(CriarRestauranteCommandDto dto, Usuario usuario) {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.getNome());
        restaurante.setTipoCozinha(dto.getTipoCozinha());
        restaurante.setHorarioAbertura(dto.getHorarioAbertura());
        restaurante.setHorarioFechamento(dto.getHorarioFechamento());
        restaurante.setUsuario(usuario);
        restaurante.setAtivo(true);
        restaurante.setUsuarioId(usuario.getId());
        restaurante.setDataCriacao(sharedService.getCurrentDateTime());
        return restaurante;
    }

}
