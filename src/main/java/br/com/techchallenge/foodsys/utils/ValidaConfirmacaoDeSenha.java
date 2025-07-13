package br.com.techchallenge.foodsys.utils;

import br.com.techchallenge.foodsys.comandos.login.dto.AtualizaCredenciaisComandoDto;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ValidaConfirmacaoDeSenha implements Validator {

    private final MessageSource messageSource;

    public ValidaConfirmacaoDeSenha(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AtualizaCredenciaisComandoDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!(target instanceof AtualizaCredenciaisComandoDto dto))
            return;

        if (!dto.senha().equals(dto.confirmacaoSenha())) {
            String message = messageSource.getMessage("senha.e.confirmacao.nao.sao.iguais", null,
                    LocaleContextHolder.getLocale());

            errors.rejectValue("senha", null, message);
            errors.rejectValue("confirmacaoSenha", null, message);
        }
    }
}