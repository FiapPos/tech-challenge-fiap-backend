package br.com.techchallenge.foodsys.core.domain.usecases.restaurante;

import br.com.techchallenge.foodsys.comandos.restaurante.dtos.AtualizarRestauranteComandoDto;
import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.utils.ValidarProprietarioRestaurante;
import br.com.techchallenge.foodsys.utils.ValidarRestauranteExistente;
import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.gateways.RestauranteRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AtualizarRestauranteComando {

    private final RestauranteRepository restauranteRepository;
    private final ValidarRestauranteExistente validarRestauranteExistente;
    private final ValidarProprietarioRestaurante validarProprietarioRestaurante;
    private final CompartilhadoService sharedService;

    public Restaurante execute(Long id, AtualizarRestauranteComandoDto dto, Usuario usuario) {
        validarDto(dto);
        Restaurante restaurante = validarRestauranteExistente.execute(id);
        validarProprietarioRestaurante.validarProprietario(restaurante, usuario.getId());
        atualizarCampos(restaurante, dto);
        return restauranteRepository.save(restaurante);
    }

    private void validarDto(AtualizarRestauranteComandoDto dto) {
        if (!isPeloMenosUmCampoPreenchido(dto)) {
            throw new BadRequestException("atualizar.restaurante.nenhum.campo");
        }
    }

    private boolean isPeloMenosUmCampoPreenchido(AtualizarRestauranteComandoDto dto) {
        return dto.getNome() != null || dto.getHorarioAbertura() != null || dto.getHorarioFechamento() != null ||
                dto.getTipoCozinha() != null;
    }

    private void atualizarCampos(Restaurante restaurante, AtualizarRestauranteComandoDto dto) {
        atualizarNome(restaurante, dto.getNome());
        atualizarHorarioAbertura(restaurante, dto.getHorarioAbertura());
        atualizarHorarioFechamento(restaurante, dto.getHorarioFechamento());
        atualizarTipoCozinha(restaurante, dto.getTipoCozinha());
        restaurante.setDataAtualizacao(sharedService.getCurrentDateTime());
    }

    public void atualizarNome(Restaurante restaurante, String nome) {
        if (nome != null) {
            restaurante.setNome(nome);
        }
    }

    public void atualizarHorarioFechamento(Restaurante restaurante, String horarioFechamento) {
        if (horarioFechamento != null) {
            restaurante.setHorarioFechamento(horarioFechamento);
        }
    }

    public void atualizarHorarioAbertura(Restaurante restaurante, String horarioAbertura) {
        if (horarioAbertura != null) {
            restaurante.setHorarioAbertura(horarioAbertura);
        }
    }

    public void atualizarTipoCozinha(Restaurante restaurante, String tipoCozinha) {
        if (tipoCozinha != null) {
            restaurante.setTipoCozinha(tipoCozinha);
        }
    }

}
