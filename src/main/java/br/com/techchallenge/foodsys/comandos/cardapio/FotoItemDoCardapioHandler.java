package br.com.techchallenge.foodsys.comandos.cardapio;
import br.com.techchallenge.foodsys.dominio.cardapio.ItemDoCardapio;
import br.com.techchallenge.foodsys.dominio.cardapio.ItemDoCardapioRepository;
import br.com.techchallenge.foodsys.dominio.foto.FotoPratoDocumento;
import br.com.techchallenge.foodsys.dominio.foto.FotoPratoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class FotoItemDoCardapioHandler {

    private final FotoPratoRepository fotoPratoRepository;
    private final ItemDoCardapioRepository itemDoCardapioRepository;

    public FotoItemDoCardapioHandler(FotoPratoRepository fotoPratoRepository, ItemDoCardapioRepository itemDoCardapioRepository) {
        this.fotoPratoRepository = fotoPratoRepository;
        this.itemDoCardapioRepository = itemDoCardapioRepository;
    }

    public void salvarFoto(Long restauranteId, Long pratoId, MultipartFile arquivo) throws IOException {

        ItemDoCardapio itemDoCardapio = itemDoCardapioRepository.findById(pratoId)
                .orElseThrow(() -> new RuntimeException("Prato não encontrado"));

        FotoPratoDocumento foto = new FotoPratoDocumento();
        foto.setRestauranteId(restauranteId);
        foto.setPratoId(pratoId);
        foto.setNomeArquivo(arquivo.getOriginalFilename());
        foto.setTipoArquivo(arquivo.getContentType());
        foto.setDados(arquivo.getBytes());

        fotoPratoRepository.save(foto);

        String caminhoFoto = "/foto/prato/" + pratoId;
        itemDoCardapio.setCaminhoFoto(caminhoFoto);
        itemDoCardapioRepository.save(itemDoCardapio);
    }
}