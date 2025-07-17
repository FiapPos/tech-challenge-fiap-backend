package br.com.techchallenge.foodsys.comandos.login;

import br.com.techchallenge.foodsys.dominio.usuario.Usuario;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticaUsuarioComando {

    private final UsuarioRepository usuarioRepository;

    public AutenticaUsuarioComando(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario getByLogin(String login) {
        return usuarioRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}