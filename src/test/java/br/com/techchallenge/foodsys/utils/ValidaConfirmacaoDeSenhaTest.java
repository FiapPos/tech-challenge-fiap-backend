package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.comandos.login.dto.AtualizaCredenciaisComandoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidaConfirmacaoDeSenhaTest {

    private MessageSource messageSource;
    private ValidaConfirmacaoDeSenha validator;

    @BeforeEach
    void setUp() {
        messageSource = mock(MessageSource.class);
        validator = new ValidaConfirmacaoDeSenha(messageSource);
    }

    @Nested
    class Validate {
        @Test
        void deveRejeitarQuandoSenhasNaoSaoIguais() {
            var dto = new AtualizaCredenciaisComandoDto("12345678", "87654321");
            Errors errors = new BeanPropertyBindingResult(dto, "dto");

            when(messageSource.getMessage(eq("senha.e.confirmacao.nao.sao.iguais"), any(), any(Locale.class)))
                    .thenReturn("A senha e a confirmação da senha devem ser iguais.");

            validator.validate(dto, errors);
            assertTrue(errors.hasErrors());
            assertEquals(2, errors.getErrorCount());
            assertNotNull(errors.getFieldError("senha"));
            assertNotNull(errors.getFieldError("confirmacaoSenha"));
        }

        @Test
        void naoDeveRejeitarQuandoSenhasSaoIguais() {
            var dto = new AtualizaCredenciaisComandoDto("senha123", "senha123");
            Errors errors = new BeanPropertyBindingResult(dto, "dto");

            validator.validate(dto, errors);
            assertFalse(errors.hasErrors());
        }
    }

    @Nested
    class Supports {
        @Test
        void deveRetornarTrueParaAtualizaCredenciaisComandoDto() {
            assertTrue(validator.supports(AtualizaCredenciaisComandoDto.class));
        }

        @Test
        void deveRetornarFalseParaOutraClasse() {
            assertFalse(validator.supports(String.class));
        }
    }
}