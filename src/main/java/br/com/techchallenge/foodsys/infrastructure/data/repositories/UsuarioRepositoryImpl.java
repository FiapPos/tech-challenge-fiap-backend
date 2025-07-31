package br.com.techchallenge.foodsys.infrastructure.data.repositories;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.core.gateways.UsuarioRepository;
import br.com.techchallenge.foodsys.infrastructure.data.entities.UsuarioEntity;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryImpl(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = UsuarioEntity.fromDomain(usuario);
        UsuarioEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return jpaRepository.findById(id)
                .map(UsuarioEntity::toDomain);
    }

    @Override
    public List<Usuario> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(UsuarioEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByLogin(String login) {
        return jpaRepository.existsByLogin(login);
    }

    @Override
    public List<Usuario> findByAtivo(boolean ativo) {
        return jpaRepository.findByAtivo(ativo, Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(UsuarioEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Usuario> findByLogin(String login) {
        return jpaRepository.findByLogin(login)
                .map(UsuarioEntity::toDomain);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
} 