package br.com.techchallenge.foodsys.comandos.restaurante;

import org.springframework.stereotype.Service;

import br.com.techchallenge.foodsys.comandos.restaurante.dtos.AtualizarRestauranteComandoDto;
import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.restaurante.RestauranteRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.utils.ValidarProprietarioRestaurante;
import br.com.techchallenge.foodsys.utils.ValidarRestauranteExistente;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AtualizarRestauranteComando {

    private final RestauranteRepository restauranteRepository;
    private final ValidarRestauranteExistente validarRestauranteExistente;
    private final ValidarProprietarioRestaurante validarProprietarioRestaurante;
    private final CompartilhadoService sharedService;

    public Restaurante execute(Long id, AtualizarRestauranteComandoDto dto) {
        validarDto(dto);
        Restaurante restaurante = validarRestauranteExistente.execute(id);
        validarProprietarioRestaurante.validarProprietario(restaurante, dto.getUsuarioDonoId());
        atualizarCampos(restaurante, dto);
        return restauranteRepository.save(restaurante);
    }

    private void validarDto(AtualizarRestauranteComandoDto dto) {
        if (!isPeloMenosUmCampoPreenchido(dto)) {
            throw new BadRequestException("atualizar.restaurante.nenhum.campo");
        }
    }

    private boolean isPeloMenosUmCampoPreenchido(AtualizarRestauranteComandoDto dto) {
        return dto.getNome() != null || dto.getEndereco() != null || dto.getHorarioFuncionamento() != null ||
                dto.getTipoCozinha() != null;
    }

    private void atualizarCampos(Restaurante restaurante, AtualizarRestauranteComandoDto dto) {
        atualizarNome(restaurante, dto.getNome());
        atualizarEndereco(restaurante, dto.getEndereco());
        atualizarTipoCozinha(restaurante, dto.getTipoCozinha());
        atualizarHorarioFuncionamento(restaurante, dto.getHorarioFuncionamento());
        restaurante.setDataAtualizacao(sharedService.getCurrentDateTime());
    }

    public void atualizarNome(Restaurante restaurante, String nome) {
        if (nome != null) {
            restaurante.setNome(nome);
        }
    }

    public void atualizarEndereco(Restaurante restaurante, String endereco) {
        if (endereco != null) {
            restaurante.setEndereco(endereco);
        }
    }

    public void atualizarTipoCozinha(Restaurante restaurante, String tipoCozinha) {
        if (tipoCozinha != null) {
            restaurante.setTipoCozinha(tipoCozinha);
        }
    }

    public void atualizarHorarioFuncionamento(Restaurante restaurante, String horarioFuncionamento) {
        if (horarioFuncionamento != null) {
            restaurante.setHorarioFuncionamento(horarioFuncionamento);
        }
    }
}
