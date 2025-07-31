package br.com.techchallenge.foodsys.infrastructure.data.repositories;

import br.com.techchallenge.foodsys.core.domain.entities.Endereco;
import br.com.techchallenge.foodsys.core.gateways.EnderecoRepository;
import br.com.techchallenge.foodsys.infrastructure.data.entities.EnderecoEntity;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EnderecoRepositoryImpl implements EnderecoRepository {

    private final EnderecoJpaRepository jpaRepository;

    public EnderecoRepositoryImpl(EnderecoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Endereco save(Endereco endereco) {
        EnderecoEntity entity = EnderecoEntity.fromDomain(endereco);
        EnderecoEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Endereco> findById(Long id) {
        return jpaRepository.findById(id)
                .map(EnderecoEntity::toDomain);
    }

    @Override
    public List<Endereco> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(EnderecoEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Endereco> findByUsuarioId(Long usuarioId, Sort sort) {
        return jpaRepository.findByUsuarioId(usuarioId, sort)
                .stream()
                .map(EnderecoEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Endereco> findByRestauranteId(Long restauranteId, Sort sort) {
        return jpaRepository.findByRestauranteId(restauranteId, sort)
                .stream()
                .map(EnderecoEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUsuarioIdAndCep(Long usuarioId, String cep) {
        return jpaRepository.existsByUsuarioIdAndCep(usuarioId, cep);
    }

    @Override
    public boolean existsByRestauranteIdAndCep(Long restauranteId, String cep) {
        return jpaRepository.existsByRestauranteIdAndCep(restauranteId, cep);
    }

    @Override
    public boolean existsByUsuarioIdAndRestauranteId(Long usuarioId, Long restauranteId) {
        return jpaRepository.existsByUsuarioIdAndRestauranteId(usuarioId, restauranteId);
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