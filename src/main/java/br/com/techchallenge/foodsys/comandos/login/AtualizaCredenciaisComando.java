package br.com.techchallenge.foodsys.comandos.login;

import br.com.techchallenge.foodsys.comandos.login.dto.AtualizaCredenciaisComandoDto;
import br.com.techchallenge.foodsys.compartilhado.UsuarioLogado;
import br.com.techchallenge.foodsys.dominio.usuario.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class AtualizaCredenciaisComando {

    private final UsuarioLogado usuarioLogado;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    public AtualizaCredenciaisComando(UsuarioLogado usuarioLogado, PasswordEncoder passwordEncoder,
            UsuarioRepository usuarioRepository) {
        this.usuarioLogado = usuarioLogado;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    public void execute(AtualizaCredenciaisComandoDto atualizaCredenciaisComandoDto) {
        Assert.notNull(atualizaCredenciaisComandoDto.senha(), "A senha não pode ser nula");
        Assert.notNull(atualizaCredenciaisComandoDto.confirmacaoSenha(), "A confirmação da senha não pode ser nula");
        Assert.notNull(usuarioLogado.getUsuarioId(), "A pessoa usuária precisa estar logada para trocar a senha");

        usuarioRepository
                .findById(usuarioLogado.getUsuarioId())
                .ifPresent(
                        usuario -> usuario.trocaSenha(passwordEncoder.encode(atualizaCredenciaisComandoDto.senha())));
    }
}