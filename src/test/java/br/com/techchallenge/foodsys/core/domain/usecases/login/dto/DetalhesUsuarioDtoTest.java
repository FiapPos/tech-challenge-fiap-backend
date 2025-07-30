package br.com.techchallenge.foodsys.core.domain.usecases.login.dto;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import br.com.techchallenge.foodsys.core.dtos.login.DetalhesUsuarioDto;
import br.com.techchallenge.foodsys.core.enums.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

        UsuarioTipo usuarioTipo = new UsuarioTipo();
        usuarioTipo.setTipo(TipoUsuario.CLIENTE);
        usuarioTipo.setUsuario(usuario);

        Set<UsuarioTipo> usuarioTipos = new HashSet<>();
        usuarioTipos.add(usuarioTipo);
        usuario.setUsuarioTipos(usuarioTipos);

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
        UsuarioTipo usuarioTipoAdmin = new UsuarioTipo();
        usuarioTipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipoAdmin.setUsuario(usuario);

        Set<UsuarioTipo> usuarioTiposAdmin = new HashSet<>();
        usuarioTiposAdmin.add(usuarioTipoAdmin);
        usuario.setUsuarioTipos(usuarioTiposAdmin);

        DetalhesUsuarioDto dtoAdmin = new DetalhesUsuarioDto(usuario);
        Collection<? extends GrantedAuthority> authoritiesAdmin = dtoAdmin.getAuthorities();
        
        assertTrue(authoritiesAdmin.stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")));

        UsuarioTipo usuarioTipoCliente = new UsuarioTipo();
        usuarioTipoCliente.setTipo(TipoUsuario.CLIENTE);
        usuarioTipoCliente.setUsuario(usuario);

        Set<UsuarioTipo> usuarioTiposCliente = new HashSet<>();
        usuarioTiposCliente.add(usuarioTipoCliente);
        usuario.setUsuarioTipos(usuarioTiposCliente);

        DetalhesUsuarioDto dtoCliente = new DetalhesUsuarioDto(usuario);
        Collection<? extends GrantedAuthority> authoritiesCliente = dtoCliente.getAuthorities();
        
        assertTrue(authoritiesCliente.stream().anyMatch(auth -> auth.getAuthority().equals("CLIENTE")));
    }

    @Test
    void deveRetornarDadosCorretosQuandoUsuarioAtualizado() {
        usuario.setLogin("novoLogin");
        usuario.setSenha("novaSenha");

        UsuarioTipo usuarioTipoAdmin = new UsuarioTipo();
        usuarioTipoAdmin.setTipo(TipoUsuario.ADMIN);
        usuarioTipoAdmin.setUsuario(usuario);

        Set<UsuarioTipo> usuarioTiposAdmin = new HashSet<>();
        usuarioTiposAdmin.add(usuarioTipoAdmin);
        usuario.setUsuarioTipos(usuarioTiposAdmin);

        DetalhesUsuarioDto novoDto = new DetalhesUsuarioDto(usuario);
        
        assertEquals("novoLogin", novoDto.getUsername());
        assertEquals("novaSenha", novoDto.getPassword());
        assertTrue(novoDto.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")));
    }
}
