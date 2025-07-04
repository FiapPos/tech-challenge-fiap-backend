package br.com.techchallenge.foodsys.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.*;

class MessageSourceConfigTest {
    private final MessageSourceConfig messageSourceConfig = new MessageSourceConfig();

    @Test
    void deveCriarMessageSourceComConfiguracaoCorreta() {
        MessageSource messageSource = messageSourceConfig.messageSource();
        
        assertNotNull(messageSource);
        assertTrue(messageSource instanceof org.springframework.context.support.ResourceBundleMessageSource);
    }

    @Test
    void deveCriarValidatorComMessageSource() {
        LocalValidatorFactoryBean validator = messageSourceConfig.getValidator();
        
        assertNotNull(validator);
    }
} 