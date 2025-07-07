package br.com.techchallenge.foodsys.comandos.cardapio;

import br.com.techchallenge.foodsys.dominio.cardapio.Prato;
import br.com.techchallenge.foodsys.dominio.cardapio.PratoRepository;
import br.com.techchallenge.foodsys.dominio.foto.FotoPratoDocumento;
import br.com.techchallenge.foodsys.dominio.foto.FotoPratoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FotoPratoHandlerTest {

    private FotoPratoRepository fotoPratoRepository;
    private PratoRepository pratoRepository;
    private FotoPratoHandler handler;

    @BeforeEach
    void setup() {
        fotoPratoRepository = mock(FotoPratoRepository.class);
        pratoRepository = mock(PratoRepository.class);
        handler = new FotoPratoHandler(fotoPratoRepository, pratoRepository);
    }

    @Test
    void deveLancarExceptionQuandoPratoNaoEncontrado() {
        Long restauranteId = 1L;
        Long pratoId = 2L;

        MultipartFile arquivo = mock(MultipartFile.class);

        when(pratoRepository.findById(pratoId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> handler.salvarFoto(restauranteId, pratoId, arquivo));

        verify(fotoPratoRepository, never()).save(any(FotoPratoDocumento.class));
    }

    @Test
    void deveSalvarFotoQuandoPratoExistir() throws IOException {
        Long restauranteId = 1L;
        Long pratoId = 2L;

        MultipartFile arquivo = mock(MultipartFile.class);
        when(arquivo.getOriginalFilename()).thenReturn("foto.jpg");
        when(arquivo.getContentType()).thenReturn("image/jpeg");
        when(arquivo.getBytes()).thenReturn(new byte[]{1, 2, 3});

        Prato prato = new Prato();
        prato.setId(pratoId);

        when(pratoRepository.findById(pratoId)).thenReturn(Optional.of(prato));

        handler.salvarFoto(restauranteId, pratoId, arquivo);

        verify(fotoPratoRepository, times(1)).save(any(FotoPratoDocumento.class));
        verify(pratoRepository, times(1)).save(prato);
    }
}
