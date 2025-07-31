package br.com.techchallenge.foodsys.infrastructure.data.repositories;

import br.com.techchallenge.foodsys.core.domain.entities.UsuarioTipo;
import br.com.techchallenge.foodsys.core.gateways.UsuarioTipoRepository;
import br.com.techchallenge.foodsys.infrastructure.data.entities.UsuarioTipoEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UsuarioTipoRepositoryImpl implements UsuarioTipoRepository {

    private final UsuarioTipoJpaRepository jpaRepository;

    public UsuarioTipoRepositoryImpl(UsuarioTipoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UsuarioTipo save(UsuarioTipo usuarioTipo) {
        UsuarioTipoEntity entity = UsuarioTipoEntity.fromDomain(usuarioTipo);
        UsuarioTipoEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<UsuarioTipo> findById(Long id) {
        return jpaRepository.findById(id)
                .map(UsuarioTipoEntity::toDomain);
    }

    @Override
    public List<UsuarioTipo> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(UsuarioTipoEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
} 