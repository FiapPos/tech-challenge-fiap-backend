package br.com.techchallenge.foodsys.comandos.cardapio;
import br.com.techchallenge.foodsys.dominio.cardapio.Prato;
import br.com.techchallenge.foodsys.dominio.cardapio.PratoRepository;
import br.com.techchallenge.foodsys.dominio.foto.FotoPratoDocumento;
import br.com.techchallenge.foodsys.dominio.foto.FotoPratoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class FotoPratoHandler {

    private final FotoPratoRepository fotoPratoRepository;
    private final PratoRepository pratoRepository;

    public FotoPratoHandler(FotoPratoRepository fotoPratoRepository, PratoRepository pratoRepository) {
        this.fotoPratoRepository = fotoPratoRepository;
        this.pratoRepository = pratoRepository;
    }

    public void salvarFoto(Long restauranteId, Long pratoId, MultipartFile arquivo) throws IOException {

        Prato prato = pratoRepository.findById(pratoId)
                .orElseThrow(() -> new RuntimeException("Prato não encontrado"));

        FotoPratoDocumento foto = new FotoPratoDocumento();
        foto.setRestauranteId(restauranteId);
        foto.setPratoId(pratoId);
        foto.setNomeArquivo(arquivo.getOriginalFilename());
        foto.setTipoArquivo(arquivo.getContentType());
        foto.setDados(arquivo.getBytes());

        fotoPratoRepository.save(foto);

        String caminhoFoto = "/foto/prato/" + pratoId;
        prato.setCaminhoFoto(caminhoFoto);
        pratoRepository.save(prato);
    }
}