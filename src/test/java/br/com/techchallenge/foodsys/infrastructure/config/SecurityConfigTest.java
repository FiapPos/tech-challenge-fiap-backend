package br.com.techchallenge.foodsys.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {
    @InjectMocks
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarPasswordEncoder() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder);
    }

    @Test
    void deveCriarAuthenticationEntryPoint() {
        AuthenticationEntryPoint entryPoint = securityConfig.authenticationEntryPoint();
        assertNotNull(entryPoint);
        assertInstanceOf(HttpStatusEntryPoint.class, entryPoint);
    }

    @Test
    void deveCriarAuthenticationManager() {
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        
        AuthenticationManager authenticationManager = securityConfig.authenticationManager(userDetailsService, passwordEncoder);
        
        assertNotNull(authenticationManager);
        assertInstanceOf(ProviderManager.class, authenticationManager);
    }
} 