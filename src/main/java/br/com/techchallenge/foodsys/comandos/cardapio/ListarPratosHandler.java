package br.com.techchallenge.foodsys.comandos.cardapio;
import br.com.techchallenge.foodsys.comandos.cardapio.dtos.PratoResponseDTO;
import br.com.techchallenge.foodsys.dominio.cardapio.PratoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarPratosHandler {

    private final PratoRepository pratoRepository;

    public ListarPratosHandler(PratoRepository pratoRepository) {
        this.pratoRepository = pratoRepository;
    }

    public List<PratoResponseDTO> executar() {
        return pratoRepository.findAll()
                .stream()
                .map(PratoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PratoResponseDTO> executarPorRestaurante(Long restauranteId) {
        return pratoRepository.findByRestauranteId(restauranteId)
                .stream()
                .map(PratoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
