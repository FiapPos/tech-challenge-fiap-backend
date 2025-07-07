package br.com.techchallenge.foodsys.controller;
import br.com.techchallenge.foodsys.comandos.cardapio.FotoItemDoCardapioHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.io.IOException;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FotoItemDoCardapioControllerTest {

    private MockMvc mockMvc;
    private FotoItemDoCardapioHandler fotoItemDoCardapioHandler;

    @BeforeEach
    void setUp() {
        fotoItemDoCardapioHandler = Mockito.mock(FotoItemDoCardapioHandler.class);
        FotoItemDoCardapioController controller = new FotoItemDoCardapioController(fotoItemDoCardapioHandler);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void deveRetornar201QuandoUploadForSucesso() throws Exception {
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo",
                "foto.png",
                MediaType.IMAGE_PNG_VALUE,
                "conteudo".getBytes()
        );

        doNothing().when(fotoItemDoCardapioHandler).salvarFoto(anyLong(), anyLong(), any());

        mockMvc.perform(multipart("/restaurantes/1/pratos/1/foto")
                        .file(arquivo))
                .andExpect(status().isCreated())
                .andExpect(content().string("Foto salva com sucesso!"));
    }

    @Test
    void deveRetornar500QuandoIOException() throws Exception {
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo",
                "foto.png",
                MediaType.IMAGE_PNG_VALUE,
                "conteudo".getBytes()
        );

        doThrow(IOException.class).when(fotoItemDoCardapioHandler).salvarFoto(anyLong(), anyLong(), any());

        mockMvc.perform(multipart("/restaurantes/1/pratos/1/foto")
                        .file(arquivo))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Erro ao salvar a foto."));
    }

    @Test
    void deveRetornar404QuandoRuntimeException() throws Exception {
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo",
                "foto.png",
                MediaType.IMAGE_PNG_VALUE,
                "conteudo".getBytes()
        );

        doThrow(new RuntimeException("Prato não encontrado")).when(fotoItemDoCardapioHandler).salvarFoto(anyLong(), anyLong(), any());

        mockMvc.perform(multipart("/restaurantes/1/pratos/1/foto")
                        .file(arquivo))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Prato não encontrado"));
    }
}
