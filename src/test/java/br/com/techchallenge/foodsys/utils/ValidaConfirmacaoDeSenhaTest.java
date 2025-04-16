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
        void deve_rejeitar_quando_senhas_nao_sao_iguais() {
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
        void nao_deve_rejeitar_quando_senhas_sao_iguais() {
            var dto = new AtualizaCredenciaisComandoDto("senha123", "senha123");
            Errors errors = new BeanPropertyBindingResult(dto, "dto");

            validator.validate(dto, errors);
            assertFalse(errors.hasErrors());
        }
    }

    @Nested
    class Supports {
        @Test
        void deve_retornar_true_para_AtualizaCredenciaisComandoDto() {
            assertTrue(validator.supports(AtualizaCredenciaisComandoDto.class));
        }

        @Test
        void deve_retornar_false_para_outra_classe() {
            assertFalse(validator.supports(String.class));
        }
    }
}