package br.com.techchallenge.foodsys.core.domain.usecases.login.dto;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.dtos.login.DetalhesUsuarioDto;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class DetalhesUsuarioDtoTest {

    private Usuario usuario;
    private DetalhesUsuarioDto detalhesUsuarioDto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senha123");
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuario.setAtivo(true);
        
        detalhesUsuarioDto = new DetalhesUsuarioDto(usuario);
    }

    @Test
    void deveRetornarAuthoritiesCorretas() {
        Collection<? extends GrantedAuthority> authorities = detalhesUsuarioDto.getAuthorities();
        
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("CLIENTE")));
    }

    @Test
    void deveRetornarPasswordDoUsuario() {
        String password = detalhesUsuarioDto.getPassword();
        
        assertEquals("senha123", password);
    }

    @Test
    void deveRetornarUsernameDoUsuario() {
        String username = detalhesUsuarioDto.getUsername();
        
        assertEquals("joao123", username);
    }

    @Test
    void deveRetornarTrueParaAccountNonExpired() {
        boolean isAccountNonExpired = detalhesUsuarioDto.isAccountNonExpired();
        
        assertTrue(isAccountNonExpired);
    }

    @Test
    void deveRetornarTrueParaAccountNonLocked() {
        boolean isAccountNonLocked = detalhesUsuarioDto.isAccountNonLocked();
        
        assertTrue(isAccountNonLocked);
    }

    @Test
    void deveRetornarTrueParaCredentialsNonExpired() {
        boolean isCredentialsNonExpired = detalhesUsuarioDto.isCredentialsNonExpired();
        
        assertTrue(isCredentialsNonExpired);
    }

    @Test
    void deveRetornarTrueParaEnabledQuandoUsuarioAtivo() {
        boolean isEnabled = detalhesUsuarioDto.isEnabled();
        
        assertTrue(isEnabled);
    }

    @Test
    void deveRetornarFalseParaEnabledQuandoUsuarioInativo() {
        usuario.setAtivo(false);
        DetalhesUsuarioDto dtoInativo = new DetalhesUsuarioDto(usuario);
        
        boolean isEnabled = dtoInativo.isEnabled();
        
        assertFalse(isEnabled);
    }

    @Test
    void deveRetornarUsuarioCorreto() {
        Usuario usuarioRetornado = detalhesUsuarioDto.usuario();
        
        assertEquals(usuario, usuarioRetornado);
    }

    @Test
    void deveRetornarAuthoritiesParaDiferentesTiposDeUsuario() {
        usuario.setTipo(TipoUsuario.ADMIN);
        DetalhesUsuarioDto dtoAdmin = new DetalhesUsuarioDto(usuario);
        Collection<? extends GrantedAuthority> authoritiesAdmin = dtoAdmin.getAuthorities();
        
        assertTrue(authoritiesAdmin.stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")));
        
        usuario.setTipo(TipoUsuario.CLIENTE);
        DetalhesUsuarioDto dtoCliente = new DetalhesUsuarioDto(usuario);
        Collection<? extends GrantedAuthority> authoritiesCliente = dtoCliente.getAuthorities();
        
        assertTrue(authoritiesCliente.stream().anyMatch(auth -> auth.getAuthority().equals("CLIENTE")));
    }

    @Test
    void deveRetornarDadosCorretosQuandoUsuarioAtualizado() {
        usuario.setLogin("novoLogin");
        usuario.setSenha("novaSenha");
        usuario.setTipo(TipoUsuario.ADMIN);
        
        DetalhesUsuarioDto novoDto = new DetalhesUsuarioDto(usuario);
        
        assertEquals("novoLogin", novoDto.getUsername());
        assertEquals("novaSenha", novoDto.getPassword());
        assertTrue(novoDto.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")));
    }
} 