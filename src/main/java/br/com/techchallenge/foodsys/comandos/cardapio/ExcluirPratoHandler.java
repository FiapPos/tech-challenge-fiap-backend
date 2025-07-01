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

    public void executar(Long id) {
        if (pratoRepository.existsById(id)) {
            pratoRepository.deleteById(id);
        } else {
            throw new PratoNaoEncontradoException(id);
        }
    }
}

