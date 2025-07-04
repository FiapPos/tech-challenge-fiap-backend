package br.com.techchallenge.foodsys.comandos.cardapio;

import br.com.techchallenge.foodsys.dominio.cardapio.PratoRepository;
import br.com.techchallenge.foodsys.excpetion.PratoNaoEncontradoException;
import org.springframework.stereotype.Service;

@Service
public class ExcluirPratoHandler {

    private final PratoRepository pratoRepository;

    public ExcluirPratoHandler(PratoRepository pratoRepository) {
        this.pratoRepository = pratoRepository;
    }

    public void executar(Long restauranteId, Long pratoId) {
        boolean exists = pratoRepository.existsByIdAndRestauranteId(pratoId, restauranteId);
        if (exists) {
            pratoRepository.deleteById(pratoId);
        } else {
            throw new PratoNaoEncontradoException(pratoId);
        }
    }
}
