package br.com.techchallenge.foodsys.comandos.cardapio;

import br.com.techchallenge.foodsys.comandos.cardapio.dtos.PratoResponseDTO;
import br.com.techchallenge.foodsys.dominio.cardapio.PratoRepository;
import br.com.techchallenge.foodsys.excpetion.PratoNaoEncontradoException;
import org.springframework.stereotype.Service;

@Service
public class BuscarPratoPorIdHandler {

    private final PratoRepository pratoRepository;

    public BuscarPratoPorIdHandler(PratoRepository pratoRepository) {
        this.pratoRepository = pratoRepository;
    }

    public PratoResponseDTO executar(Long restauranteId, Long pratoId) {
        return pratoRepository.findByIdAndRestauranteId(pratoId, restauranteId)
                .map(PratoResponseDTO::fromEntity)
                .orElseThrow(() -> new PratoNaoEncontradoException(pratoId));
    }
}
