package br.com.techchallenge.foodsys.comandos.cardapio;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.PratoResponseDTO;
import br.com.techchallenge.foodsys.dominio.cardapio.PratoRepository;
import br.com.techchallenge.foodsys.excpetion.PratoNaoEncontradoException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class BuscarPratoPorIdHandler {

    private final PratoRepository pratoRepository;

    public BuscarPratoPorIdHandler(PratoRepository pratoRepository) {
        this.pratoRepository = pratoRepository;
    }

    public PratoResponseDTO executar(Long id) {
        return pratoRepository.findById(id)
                .map(PratoResponseDTO::fromEntity)
                .orElseThrow(() -> new PratoNaoEncontradoException(id));
    }
}
